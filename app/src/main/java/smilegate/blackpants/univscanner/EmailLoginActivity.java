package smilegate.blackpants.univscanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 2018-01-17.
 */

public class EmailLoginActivity extends AppCompatActivity{
    private static final String TAG = "EmailLoginActivity";
    private FirebaseAuth mAuth;

    @BindView(R.id.autoText_login_email)
    AutoCompleteTextView inputEmailText;

    @BindView(R.id.editText_login_password)
    EditText passwordText;

    @BindView(R.id.btn_login)
    Button loginBtn;

    @OnClick(R.id.btn_login)
    public void login(Button button) {
        String email = inputEmailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        emailLogin(email, password);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
    }

    public void emailLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                @Override
                                public void onComplete(@NonNull Task<GetTokenResult> task) {
                                    if (task.isSuccessful()) {
                                        String idToken = task.getResult().getToken();
                                        Log.i("토큰", idToken);
                                        Prefs.putString("idToken", idToken);
                                        Log.i("토큰", "토큰 얻기 성공 및 저장");
                                        Prefs.putBoolean("isLogin", true);
                                        //new ConnectServer().execute();
                                        //sendPost("타이틀:제발되라", "body:될거라");
                                        //getPost();
                                        //sendPost("abc@example.com","홍길동","스마일대학교");
                                        Intent intent = new Intent(EmailLoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Log.i("토큰", "토큰 얻기 실패");
                                    }
                                }
                            });
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(EmailLoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                        // ...
                    }
                });
    }

}
