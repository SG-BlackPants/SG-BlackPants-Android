package smilegate.blackpants.univscanner.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import me.leolin.shortcutbadger.ShortcutBadger;
import smilegate.blackpants.univscanner.MainActivity;

/**
 * Created by user on 2018-01-26.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int badgeCount = Prefs.getInt("badgeCount", 0);
            MainActivity.mBottomBarTab.setBadgeCount(badgeCount);
        }
    };


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //String bodyStrFromServer = remoteMessage.getNotification().getBody();
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Notification Title : " + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "Notification Message : " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "메세지 출처 : " + remoteMessage.getFrom());
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
            new KeywordNotificationManager(getApplicationContext()).displayNotification("키워드 알림", "등록하신 '" + keyword + "'에 대한 게시글이 '" + getCommunityName(community) + "'에 새로 올라왔습니다.");
            //scheduleJob();

            Log.d(TAG, "메세지 출처 : " + remoteMessage.getFrom());
            // 이거 추가 하면
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
            wakeLock.acquire(3000);
            setBadge();
        }
    }

    public void setBadge() {

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
        int badgeCount = Prefs.getInt("badgeCount", 0);
        badgeCount++;

        Prefs.putInt("badgeCount", badgeCount);
        ShortcutBadger.applyCount(getApplicationContext(), badgeCount);

        if (Prefs.getBoolean("appState", false)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                }
            }).start();
        }


    }

    public String loadJSONFromAsset(String mode) {
        String json = null;
        try {
            InputStream is = getApplicationContext().getAssets().open(mode);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public String getCommunityName(String communityId) {
        try {
            //테스트 소스 - 경희, 세종, 한성대 커뮤니티 일단 다 집어넣음
            JSONObject obj = new JSONObject(loadJSONFromAsset("community_id.json"));
            JSONObject content = obj.getJSONObject(communityId);
            String name = content.getString("name");
            return name;

        } catch (JSONException e) {
            e.printStackTrace();
            return "게시판";
        }
    }

}
