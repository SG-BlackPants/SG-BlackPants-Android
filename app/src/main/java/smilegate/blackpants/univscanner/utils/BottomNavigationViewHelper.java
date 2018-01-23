package smilegate.blackpants.univscanner.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import smilegate.blackpants.univscanner.R;

/**
 * Created by user on 2018-01-19.
 */

public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavigationViewHel";

    public static void setupBottomNavigationViewEx(BottomNavigationViewEx bottomNavigationViewEx) {
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void enableNavigation(final Context context, final BottomNavigationViewEx view, final Activity activity, final ViewPager viewPager) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int num=0;
                switch (item.getItemId()) {
                    case R.id.ic_search :
                       /* Intent intent1 = new Intent(context, MainActivity.class);   // ACTIVITY_NUM = 0
                        context.startActivity(intent1);*/
                        //selectedFragment = SearchFragment.newInstance();
                        num = 0;
                        viewPager.setCurrentItem(0);
                        break;

                    case R.id.ic_notification :
/*                        Intent intent2 = new Intent(context, NotificationActivity.class); // ACTIVITY_NUM = 1
                        context.startActivity(intent2);*/
                        //selectedFragment = NotificationFragment.newInstance();
                        num = 1;
                        viewPager.setCurrentItem(1);
                        break;

                    case R.id.ic_account :
                       /* Intent intent3 = new Intent(context, ProfileActivity.class); // ACTIVITY_NUM = 2
                        context.startActivity(intent3);*/
                        //selectedFragment = ProfileFragment.newInstance();
                        num = 2;
                        viewPager.setCurrentItem(2);
                        break;
                }
               /* FragmentTransaction transaction = ((MainActivity)activity).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
                Menu menu = view.getMenu();
                MenuItem menuItem = menu.getItem(num);
                menuItem.setChecked(true);*/
                return false;
            }
        });
    }

}
