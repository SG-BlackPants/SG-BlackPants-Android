package smilegate.blackpants.univscanner.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;

/**
 * Created by user on 2018-02-14.
 */

public class BadgeJobService extends JobService {
    private static final String TAG = "BadgeJobService";

    @Override
    public boolean onStartJob(JobParameters job) {
        //setBadge();
        Helper helper = new Helper();
        if (helper.isAppRunning(getApplicationContext(), "smilegate.blackpants.univscanner")) {
            Log.d(TAG,"앱 실행중");
            setBadge();
        } else {
            Log.d(TAG, "앱 실행중 아님");
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

    public void setBadge() {
        //String launcherClassName = getLauncherClassName(getApplicationContext());
        String launcherClassName = "smilegate.blackpants.univscanner.SplashActivity";
        Log.d(TAG,"launcherClassName : "+launcherClassName);
        int badgeCount;

        if (launcherClassName == null) {
            return;
        }

        badgeCount = Prefs.getInt("badgeCount",0);
        badgeCount++;

        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", badgeCount);
        intent.putExtra("badge_count_package_name", getApplicationContext().getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        Log.d(TAG,launcherClassName);

        Prefs.putInt("badgeCount", badgeCount);
        sendBroadcast(intent);

    }

    public class Helper {

        public boolean isAppRunning(final Context context, final String packageName) {
            final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
            if (procInfos != null)
            {
                for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                    if (processInfo.processName.equals(packageName)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

}
