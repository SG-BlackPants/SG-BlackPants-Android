package smilegate.blackpants.univscanner.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

/**
 * Created by user on 2018-01-26.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //String bodyStrFromServer = remoteMessage.getNotification().getBody();

        if(remoteMessage.getNotification() != null) {
            Log.d(TAG, "Notification Title : " + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "Notification Message : " + remoteMessage.getNotification().getBody());

            KeywordNotificationManager.getInstance(getApplicationContext()).displayNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());

        }

        if(remoteMessage.getData().size() > 0) {
            String keyword = remoteMessage.getData().get("keyword");
            String community = remoteMessage.getData().get("community");
            Log.d(TAG, "Message data payload (keyword) : " + remoteMessage.getData().get("keyword"));
            Log.d(TAG, "Message data payload (university) : " + remoteMessage.getData().get("university"));
            Log.d(TAG, "Message data payload (community) : " + remoteMessage.getData().get("community"));
            Log.d(TAG, "Message data payload (boardAddr) : " + remoteMessage.getData().get("boardAddr"));

            KeywordNotificationManager.getInstance(getApplicationContext()).displayNotification("키워드 알림","등록하신 "+keyword+"에 대한 게시글이 "+community+"에서 새로 올라왔습니다.");

        }


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

    public static void setBadge(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        Log.d(TAG,launcherClassName);
        context.sendBroadcast(intent);
    }

    public static String getLauncherClassName(Context context) {

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

}
