package smilegate.blackpants.univscanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smilegate.blackpants.univscanner.data.model.EmailVerification;
import smilegate.blackpants.univscanner.data.model.SendEmail;
import smilegate.blackpants.univscanner.data.remote.ApiUtils;
import smilegate.blackpants.univscanner.data.remote.FirebaseApiService;

/**
 * Created by user on 2018-02-09.
 */

public class UniversityAuthActivity extends AppCompatActivity {
    private static final String TAG = "UniversityAuthActivity";

    private Unbinder unbinder;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private String mUid;
    private String mUniversity;
    private FirebaseApiService mFirebaseApiService;
    private boolean mIsSendRecently;
    private boolean mIsAutentificate;

    @BindView(R.id.btn_authentification_confirm)
    Button authConfirmBtn;

    @BindView(R.id.autoText_univemail)
    AutoCompleteTextView univEmailTxt;

    @BindView(R.id.text_univdomain)
    TextView univDomainTxt;

    @OnClick(R.id.btn_authentification_confirm)
    public void authConfirmClick(Button button) {
        //Prefs.putBoolean("isUnivAuth", true);
        mFirebaseApiService.getIsEmailVerified(mUid).enqueue(new Callback<EmailVerification>() {
            @Override
            public void onResponse(Call<EmailVerification> call, Response<EmailVerification> response) {
                if(response.body().isMessage()) {
                    Intent intent = new Intent(UniversityAuthActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UniversityAuthActivity.this, "이메일 인증을 해주세요.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<EmailVerification> call, Throwable t) {

            }
        });


    }

    @OnClick(R.id.btn_sendEmail)
    public void sendEmailClick(Button button) {
        String emailId = univEmailTxt.getText().toString().trim();
        String univEmail = emailId + univDomainTxt.getText().toString().trim();
        Log.d(TAG,univEmail);
        if(!mIsSendRecently) {
            Toast.makeText(UniversityAuthActivity.this, "이메일이 발송되었습니다.", Toast.LENGTH_LONG).show();
            mIsSendRecently = true;
            mFirebaseApiService.sendEmail(mUid, univEmail).enqueue(new Callback<SendEmail>() {
                @Override
                public void onResponse(Call<SendEmail> call, Response<SendEmail> response) {
                    Log.d(TAG,"이메일 인증발송 성공");
                }

                @Override
                public void onFailure(Call<SendEmail> call, Throwable t) {
                    Log.d(TAG,"이메일 인증발송 실패");
                    Log.d(TAG,t.getMessage());
                    mIsSendRecently = false;
                }
            });
        }  else {
            Toast.makeText(UniversityAuthActivity.this, "이미 이메일이 발송되었습니다.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_univauthentification);
        unbinder = ButterKnife.bind(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mFirebaseApiService = ApiUtils.getFirebaseApiService();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        mUid = intent.getStringExtra("uid");
        mUniversity = intent.getStringExtra("university");
        Log.d(TAG,mUid+" / "+mUniversity);
        mIsAutentificate = false;
        mIsSendRecently = false;
      /*  if (mUniversity.contains("세종대학교")) {*/
            univDomainTxt.setText("@naver.com");
        //}

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        signOut();
    }

    public void signOut() {
        String loginRoute = Prefs.getString("loginRoute", "email");
        Intent intent;
        mAuth.signOut();
        switch (loginRoute) {
            case "google":
                // Google sign out
                mGoogleSignInClient.signOut().addOnCompleteListener(this,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(UniversityAuthActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                break;
            case "facebook":
                LoginManager.getInstance().logOut();
                intent = new Intent(UniversityAuthActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case "email":
                intent = new Intent(UniversityAuthActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }


}
