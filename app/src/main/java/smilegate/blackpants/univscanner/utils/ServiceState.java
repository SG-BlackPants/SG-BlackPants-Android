package smilegate.blackpants.univscanner.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.pixplicity.easyprefs.library.Prefs;

/**
 * Created by Semin on 2018-02-18.
 */

public class ServiceState  extends Service {
    private static final String TAG = "ServiceState";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service Started");
        Prefs.putBoolean("appState",true);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service Destroyed");
        Prefs.putBoolean("appState",false);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e(TAG, "END");
        Prefs.putBoolean("appState",false);
        stopSelf();
    }
}
