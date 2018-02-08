package smilegate.blackpants.univscanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

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
import smilegate.blackpants.univscanner.data.model.University;
import smilegate.blackpants.univscanner.data.remote.ApiUtils;
import smilegate.blackpants.univscanner.data.remote.UniversityApiService;
import smilegate.blackpants.univscanner.data.remote.UserApiService;
import smilegate.blackpants.univscanner.notification.NotificationFragment;
import smilegate.blackpants.univscanner.profile.ProfileFragment;
import smilegate.blackpants.univscanner.search.SearchFragment;
import smilegate.blackpants.univscanner.utils.BaseFragment;

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
    private MyBottomBarTab mMyBottomBarTab;
    private BottomBarTab mBottomBarTab;

    private final int INDEX_HOME = FragNavController.TAB1;
    private final int INDEX_NOTIFICATION = FragNavController.TAB2;
    private final int INDEX_PROFILE = FragNavController.TAB3;

    public static FragNavController mNavController;
    private UniversityApiService mUniversityApiService;
/*    @BindView(R.id.bottomNavViewBar)
    BottomNavigationViewEx bottomNavigationViewEx;

    @BindView(R.id.viewpager)
    ViewPager viewPager;*/

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpFirebaseAuth();
        mUserApiService = ApiUtils.getAPIService();
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
        //mBottomBarTab.setBadgeCount(1);
        mMyBottomBarTab = new MyBottomBarTab(mBottomBarTab);

        // setupBottomNavigationView();
        /*viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mPrevMenuItem != null) {
                    mPrevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationViewEx.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page",""+position);
                bottomNavigationViewEx.getMenu().getItem(position).setChecked(true);
                mPrevMenuItem = bottomNavigationViewEx.getMenu().getItem(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);*/
        //Manually displaying the first fragment - one time only
        /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, SearchFragment.newInstance());
        transaction.commit();*/

       /* mService = SQApiUtils.getSOService();
        loadAnswers();*/
       /* mUniversityApiService = UniversityApiUtils.getAPIService();
        getUniversityList();*/
    }

    public void getUniversityList() {
        mUniversityApiService.getUniversity().enqueue(new Callback<University>() {
            @Override
            public void onResponse(Call<University> call, Response<University> response) {
                /*for(University.Content content : response.body().getDataSearch().getContent()) {
                    Log.d(TAG, "getUniversityList : " + content.getCampusName());
                }*/
                if (response.isSuccessful()) {
                    Log.d(TAG, "getUniversityList : success");
                } else {
                    int statusCode = response.code();
                    // handle request errors depending on status code
                    Log.d(TAG, "getUniversityList : fail : " + statusCode);
                }

            }

            @Override
            public void onFailure(Call<University> call, Throwable t) {
                Log.d(TAG, "getUniversityList : fail : onFailure");
            }
        });
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
            Log.d(TAG,"onAuthStateChanged()");
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                getFromServer(user.getUid());
                Log.d(TAG, "onAuthStateChanged : singed_in" + user.getUid());
                user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            //Prefs.putString("idToken", idToken);
                            Log.d("token", "token : success : " + idToken);

                            //Prefs.putBoolean("isLogin", true);
                            //new ConnectServer().execute();
                            //sendPost("타이틀:제발되라", "body:될거라");
                            //getPost();
                            //sendPost("abc@example.com","홍길동","스마일대학교");
                        } else {
                            Log.d("token", "token : fail");
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
        Log.d(TAG,"onStart()");
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        //checkCurrentUser(mAuth.getCurrentUser());
    }

    @Override
    public void onStop() {
        Log.d(TAG,"onStop()");
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
                return NotificationFragment.newInstance(0, mMyBottomBarTab);
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

    public void getFromServer(String uid) {
        mUserApiService.getLoginInfo(uid).enqueue(new Callback<LoginInfo>() {
            @Override
            public void onResponse(Call<LoginInfo> call, Response<LoginInfo> response) {
                Gson gson = new Gson();
                LoginInfo loginInfo = new LoginInfo(response.body().getName(), response.body().getUniversity());
                String json = gson.toJson(loginInfo);
                Log.d(TAG,"Json : "+json);
                Prefs.putString("userInfo",json);
            }

            @Override
            public void onFailure(Call<LoginInfo> call, Throwable t) {
                Log.d(TAG,"Failed : user login info(name, university)");
            }
        });
    }

/*    public void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView : setting up BottomNavigationView");
        BottomNavigationViewHelper.setupBottomNavigationViewEx(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx, this, viewPager);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        Fragment searchFragment = SearchFragment.newInstance();
        Fragment notificationFragment = NotificationFragment.newInstance();
        Fragment profileFragment = ProfileFragment.newInstance();

        adapter.addFragment(searchFragment);
        adapter.addFragment(notificationFragment);
        adapter.addFragment(profileFragment);
        viewPager.setAdapter(adapter);
    }*/

}
