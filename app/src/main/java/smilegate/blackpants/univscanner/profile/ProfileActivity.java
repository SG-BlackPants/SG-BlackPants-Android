package smilegate.blackpants.univscanner.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import butterknife.BindView;
import butterknife.ButterKnife;
import smilegate.blackpants.univscanner.R;

/**
 * Created by user on 2018-01-22.
 */

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 2;

    private Context mContext = ProfileActivity.this;

    @BindView(R.id.bottomNavViewBar)
    public BottomNavigationViewEx bottomNavigationViewEx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        //setupBottomNavigationView();
    }

    /*public void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView : setting up BottomNavigationView");
        BottomNavigationViewHelper.setupBottomNavigationViewEx(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx, this);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }*/
}
