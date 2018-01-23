package smilegate.blackpants.univscanner;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import butterknife.BindView;
import butterknife.ButterKnife;
import smilegate.blackpants.univscanner.data.remote.UserApiService;
import smilegate.blackpants.univscanner.notification.NotificationFragment;
import smilegate.blackpants.univscanner.profile.ProfileFragment;
import smilegate.blackpants.univscanner.search.SearchFragment;
import smilegate.blackpants.univscanner.utils.BottomNavigationViewHelper;
import smilegate.blackpants.univscanner.utils.ViewPageAdapter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int ACTIVITY_NUM = 0;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private GoogleSignInClient mGoogleSignInClient;
    private UserApiService mUserApiService;
    private Context mContext = MainActivity.this;
    private MenuItem mPrevMenuItem;

    @BindView(R.id.bottomNavViewBar)
    BottomNavigationViewEx bottomNavigationViewEx;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
        setupBottomNavigationView();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        setupViewPager(viewPager);
        //Manually displaying the first fragment - one time only
        /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, SearchFragment.newInstance());
        transaction.commit();*/


    }

    public void setupBottomNavigationView() {
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
    }

}
