package smilegate.blackpants.univscanner.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import smilegate.blackpants.univscanner.MainActivity;
import smilegate.blackpants.univscanner.R;

/**
 * Created by user on 2018-02-02.
 */

public class KeywordNotificationManager {

    private Context mContext;
    private static KeywordNotificationManager mInstance;

    // 키워드 알림 싱글톤
  /*  private KeywordNotificationManager(Context context) {
        mContext = context;
    }

    public static KeywordNotificationManager getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new KeywordNotificationManager(context);
        }

        return mInstance;
    }*/

    public KeywordNotificationManager(Context context) {
        mContext = context;
    }

    public void displayNotification(String title, String body) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, "M_CH_ID");

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(Notification.PRIORITY_MAX) // this is deprecated in API 26 but you can still use for below 26. check below update for 26 API
                .setContentTitle(title)
                .setContentText(body);

        Intent resultIntent = new Intent(mContext, MainActivity.class);
       /* setBadge(getApplicationContext(), 1);*/
        // The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_ONE_SHOT
                );
        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(0, notificationBuilder.build());

    }
}
