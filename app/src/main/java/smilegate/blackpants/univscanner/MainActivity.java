package smilegate.blackpants.univscanner;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.Gson;
import com.ncapdevi.fragnav.FragNavController;
import com.ncapdevi.fragnav.FragNavSwitchController;
import com.ncapdevi.fragnav.FragNavTransactionOptions;
import com.ncapdevi.fragnav.tabhistory.FragNavTabHistoryController;
import com.pixplicity.easyprefs.library.Prefs;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smilegate.blackpants.univscanner.data.model.LoginInfo;
import smilegate.blackpants.univscanner.data.model.MyBottomBarTab;
import smilegate.blackpants.univscanner.data.remote.ApiUtils;
import smilegate.blackpants.univscanner.data.remote.UserApiService;
import smilegate.blackpants.univscanner.notification.NotificationFragment;
import smilegate.blackpants.univscanner.profile.ProfileFragment;
import smilegate.blackpants.univscanner.search.SearchFragment;
import smilegate.blackpants.univscanner.utils.BaseFragment;
import smilegate.blackpants.univscanner.utils.ServiceState;

public class MainActivity extends AppCompatActivity implements BaseFragment.FragmentNavigation, FragNavController.TransactionListener, FragNavController.RootFragmentListener {
    private static final String TAG = "MainActivity";
    private static final int ACTIVITY_NUM = 0;

    private FirebaseAuth mAuth;
    private FirebaseAuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private GoogleSignInClient mGoogleSignInClient;
    private UserApiService mUserApiService;
    private Context mContext = MainActivity.this;
    private MenuItem mPrevMenuItem;
    public static MyBottomBarTab sMyBottomBarTab;
    public static BottomBarTab mBottomBarTab;
    private boolean isUnivAuth;
    public static boolean isNotification;
    private final int INDEX_HOME = FragNavController.TAB1;
    private final int INDEX_NOTIFICATION = FragNavController.TAB2;
    private final int INDEX_PROFILE = FragNavController.TAB3;
    public static FragNavController mNavController;

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
        setUpFirebaseAuth();

        mUserApiService = ApiUtils.getUserApiService();
        boolean initial = savedInstanceState == null;
        if (initial) {
            bottomBar.selectTabAtPosition(INDEX_HOME);
        }
        mNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.container)
                .transactionListener(this)
                .rootFragmentListener(this, 5)
                .popStrategy(FragNavTabHistoryController.UNIQUE_TAB_HISTORY)
                .switchController(new FragNavSwitchController() {
                    @Override
                    public void switchTab(int index, FragNavTransactionOptions transactionOptions) {
                        bottomBar.selectTabAtPosition(index);
                    }
                })
                .build();


        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.bb_menu_home:
                        mNavController.switchTab(INDEX_HOME);
                        break;
                    case R.id.bb_menu_notification:
                        mNavController.switchTab(INDEX_NOTIFICATION);
                        break;
                    case R.id.bb_menu_profile:
                        mNavController.switchTab(INDEX_PROFILE);
                        break;
                }
            }
        }, initial);

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                mNavController.clearStack();
            }
        });

        mBottomBarTab = bottomBar.getTabWithId(R.id.bb_menu_notification);
        int badgeCount = Prefs.getInt("badgeCount", 0);
        mBottomBarTab.setBadgeCount(badgeCount);
        startService(new Intent(getBaseContext(), ServiceState.class));

        isNotification = getIntent().getBooleanExtra("notificationFragment",false);
        if(MainActivity.isNotification) {
            MainActivity.isNotification = false;
            //MainActivity.mNavController.switchTab(INDEX_NOTIFICATION);
            bottomBar.selectTabAtPosition(INDEX_NOTIFICATION);
        }
        //signOut();
    }

    public void showErrorMessage() {
        Toast.makeText(this, "Error loading posts", Toast.LENGTH_SHORT).show();
    }

    public void checkCurrentUser(FirebaseUser user) {
        Log.d(TAG, "checkCurrentUser : checking if user is logged in");

        if (user == null) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void setUpFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth : setting up firebae auth");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuthStateListener();
    }

    class FirebaseAuthStateListener implements FirebaseAuth.AuthStateListener {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            Log.d(TAG, "onAuthStateChanged()");
            final FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            Prefs.putString("userToken", task.getResult().getToken());
                            Log.d(TAG, "onAuthStateChanged : userToken  : " + Prefs.getString("userToken", null));
                            getFromServer(user.getUid());
                            Log.d(TAG, "onAuthStateChanged : singed_in" + user.getUid());
                        } else {
                            Log.d(TAG, "onAuthStateChanged : get token fail");
                            getFromServer(user.getUid());
                            Log.d(TAG, "onAuthStateChanged : singed_in" + user.getUid());
                        }
                    }
                });

            } else {
                Log.d(TAG, "onAuthStateChanged : singed_out");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        //checkCurrentUser(mAuth.getCurrentUser());
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        if (!mNavController.popFragment()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mNavController != null) {
            mNavController.onSaveInstanceState(outState);
        }
    }

    @Override
    public void pushFragment(Fragment fragment) {
        if (mNavController != null) {
            mNavController.pushFragment(fragment);
        }
    }

    @Override
    public void onTabTransaction(Fragment fragment, int index) {
        // If we have a backstack, show the back button
        /*if (getSupportActionBar() != null && mNavController != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(!mNavController.isRootFragment());
        }*/
    }


    @Override
    public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType) {
        //do fragmentty stuff. Maybe change title, I'm not going to tell you how to live your life
        // If we have a backstack, show the back button
       /* if (getSupportActionBar() != null && mNavController != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(!mNavController.isRootFragment());
        }*/
    }

    @Override
    public Fragment getRootFragment(int index) {
        switch (index) {
            case INDEX_HOME:
                return SearchFragment.newInstance(0);
            case INDEX_NOTIFICATION:
                return NotificationFragment.newInstance(0);
            case INDEX_PROFILE:
                return ProfileFragment.newInstance(0);
        }
        throw new IllegalStateException("Need to send an index that we know");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mNavController.popFragment();
                break;
        }
        return true;
    }

    public void getFromServer(final String uid) {
        mUserApiService.getLoginInfo(uid).enqueue(new Callback<LoginInfo>() {
            @Override
            public void onResponse(Call<LoginInfo> call, Response<LoginInfo> response) {
                Gson gson = new Gson();
                isUnivAuth = Prefs.getBoolean("isUnivAuth", false);
                if (response.body() != null) {
                    LoginInfo loginInfo = new LoginInfo(response.body().getName(), response.body().getUniversity(), response.body().isRegistered());
                    isUnivAuth = loginInfo.isRegistered();
                    //isUnivAuth = Prefs.getBoolean("isUnivAuth",false);
                    String json = gson.toJson(loginInfo);
                    Log.d(TAG, "Json : " + json);
                    Prefs.putString("userInfo", json);
                    if (!isUnivAuth) {
                        Intent intent = new Intent(MainActivity.this, UniversityAuthActivity.class);
                        intent.putExtra("uid", uid);
                        intent.putExtra("university", loginInfo.getUniversity());
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Log.d(TAG, "Failed : response.body() is null");
                }

            }

            @Override
            public void onFailure(Call<LoginInfo> call, Throwable t) {

            }
        });
    }

    public void signOut() {
        String loginRoute = Prefs.getString("loginRoute", "email");

        mAuth.signOut();
        switch (loginRoute) {
            case "google":
                // Google sign out
                mGoogleSignInClient.signOut().addOnCompleteListener(this,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //updateUI(null);
                                goLoginActivity();
                            }
                        });
                break;
            case "facebook":
                LoginManager.getInstance().logOut();
                goLoginActivity();
                break;
            case "email":
                goLoginActivity();
                break;
            default:
                break;
        }


    }


    public void goLoginActivity() {

        Prefs.putString("loginRoute", null);
        //Intent intent = new Intent(getContext(), LoginActivity.class);
        //startActivity(intent);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.finish();
    }

}
