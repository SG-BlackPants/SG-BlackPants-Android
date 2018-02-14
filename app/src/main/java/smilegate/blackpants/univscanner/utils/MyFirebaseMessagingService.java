package smilegate.blackpants.univscanner.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;

/**
 * Created by user on 2018-01-26.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //String bodyStrFromServer = remoteMessage.getNotification().getBody();
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Notification Title : " + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "Notification Message : " + remoteMessage.getNotification().getBody());

            //KeywordNotificationManager.getInstance(getApplicationContext()).displayNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
            new KeywordNotificationManager(getApplicationContext()).displayNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0) {
            String keyword = remoteMessage.getData().get("keyword");
            String community = remoteMessage.getData().get("community");
            Log.d(TAG, "Message data payload (keyword) : " + remoteMessage.getData().get("keyword"));
            Log.d(TAG, "Message data payload (university) : " + remoteMessage.getData().get("university"));
            Log.d(TAG, "Message data payload (community) : " + remoteMessage.getData().get("community"));
            Log.d(TAG, "Message data payload (boardAddr) : " + remoteMessage.getData().get("boardAddr"));
            //KeywordNotificationManager.getInstance(getApplicationContext()).displayNotification("키워드 알림","등록하신 "+keyword+"에 대한 게시글이 "+community+"에서 새로 올라왔습니다.");
            new KeywordNotificationManager(getApplicationContext()).displayNotification("키워드 알림", "등록하신 " + keyword + "에 대한 게시글이 " + community + "에서 새로 올라왔습니다.");
            //scheduleJob();
            setBadge();
        }

      /*  Helper helper = new Helper();
        if (helper.isAppRunning(getApplicationContext(), "smilegate.blackpants.univscanner")) {
            Log.d(TAG,"앱 실행중");
            setBadge();
        } else {
            Log.d(TAG,"앱 실행중 아님");
        }*/
 /*       NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "M_CH_ID");

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(Notification.PRIORITY_MAX) // this is deprecated in API 26 but you can still use for below 26. check below update for 26 API
                .setContentTitle("키워드 알림")
                .setContentText(bodyStrFromServer);

        Intent resultIntent = new Intent(this, MainActivity.class);
        setBadge(getApplicationContext(), 1);
        // The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(0, notificationBuilder.build());*/

    }

    private void scheduleJob() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(BadgeJobService.class)
                .setTag("job_execute")
                .build();
        dispatcher.schedule(myJob);
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

    public String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
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
