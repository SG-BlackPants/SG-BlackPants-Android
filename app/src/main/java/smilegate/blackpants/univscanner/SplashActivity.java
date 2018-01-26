package smilegate.blackpants.univscanner;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pixplicity.easyprefs.library.Prefs;

/**
 * Created by user on 2018-01-15.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        boolean isLogin = Prefs.getBoolean("isLogin", false);
        Intent intent;
        //intent  = new Intent(this, MainActivity.class);
        if(isLogin) {
            // 로그인이 이미 되어있는 경우
            intent  = new Intent(this, MainActivity.class);
        } else {
            // 로그인이 안되어 있을 경우
            intent  = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
