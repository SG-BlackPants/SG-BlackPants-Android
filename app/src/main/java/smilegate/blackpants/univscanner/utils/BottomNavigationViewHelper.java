package smilegate.blackpants.univscanner.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import smilegate.blackpants.univscanner.MainActivity;
import smilegate.blackpants.univscanner.NotificationActivity;
import smilegate.blackpants.univscanner.ProfileActivity;
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

    public static void enableNavigation(final Context context, BottomNavigationViewEx view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.ic_search :
                        Intent intent1 = new Intent(context, MainActivity.class);   // ACTIVITY_NUM = 0
                        context.startActivity(intent1);
                        break;

                    case R.id.ic_notification :
                        Intent intent2 = new Intent(context, NotificationActivity.class); // ACTIVITY_NUM = 1
                        context.startActivity(intent2);
                        break;

                    case R.id.ic_account :
                        Intent intent3 = new Intent(context, ProfileActivity.class); // ACTIVITY_NUM = 2
                        context.startActivity(intent3);
                        break;
                }
                return false;
            }
        });
    }
}
