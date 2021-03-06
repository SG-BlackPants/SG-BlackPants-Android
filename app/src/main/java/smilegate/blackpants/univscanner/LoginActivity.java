package smilegate.blackpants.univscanner;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.pixplicity.easyprefs.library.Prefs;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import smilegate.blackpants.univscanner.data.remote.ApiUtils;
import smilegate.blackpants.univscanner.data.remote.UserApiService;

/**
 * Created by user on 2018-01-15.
 */

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;
    private UserApiService mUserApiService;
    private FirebaseAuthStateListener mAuthListener;
    private android.app.AlertDialog mDialog;
    private boolean mIsFirstLogin;
    private List<String> mUniversityList;
    private TimerTask mTask;
    private Timer mTimer;
    private boolean mode;

    @BindView(R.id.btn_google_login)
    Button googleLoginBtn;

    @BindView(R.id.btn_facebook_login)
    Button facebookLoginBtn;

    @BindView(R.id.btn_email_login)
    Button emailLoginBtn;

    @BindView(R.id.text_create_account)
    TextView createAccountBtn;

    @BindView(R.id.text_information)
    TextView informationTxt;

    @BindView(R.id.text_change)
    TextView changeTxt;

    @OnClick(R.id.btn_google_login)
    public void googleLogin(Button button) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @OnClick(R.id.btn_facebook_login)
    public void facebookLogin(Button button) {
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
    }

    @OnClick(R.id.btn_email_login)
    public void emailLogin(Button button) {
        Intent intent = new Intent(this, EmailLoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.text_create_account)
    public void createAccount(TextView textView) {
        Intent intent = new Intent(this, CreateEmailAccountActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mDialog = new SpotsDialog(this, R.style.loginLodingTheme);
        mIsFirstLogin = false;
        setChangeText();
        String infoStr = "계속 진행하시면 유니브스캐너의 <(>서비스 약관<)>과 <(>개인정보처리방침<)>에<br> 동의하시게 됩니다.";
        String colorCodeStart = "<font color='#ff7473'>";
        String colorCodeEnd = "</font>";
        infoStr = infoStr.replace("<(>", colorCodeStart); // <(> and <)> are different replace them with your color code String, First one with start tag
        infoStr = infoStr.replace("<)>", colorCodeEnd); // then end tag

        informationTxt.setText((Html.fromHtml(infoStr)));

        // 구글 로그인 설정
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        mUserApiService = ApiUtils.getUserApiService();
        mAuthListener = new FirebaseAuthStateListener();

        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);
                    }
                });


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "smilegate.blackpants.univscanner",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 페이스북 로그인 시
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // 구글 로그인 시
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // 로그인 성공 시 파이어베이스로 인증

                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // 구글 로그인 실패
                Log.d(TAG, "googlelogin : fail");
            }
        }
    }

    // 파이어베이스 구글로 인증
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle :" + acct.getId());
        Log.d(TAG, "Google JWT : " + acct.getIdToken());
        mDialog.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 인증 성공
                            mIsFirstLogin = task.getResult().getAdditionalUserInfo().isNewUser();
                            Log.d(TAG, "Google Login : mIsFirstLogin : " + mIsFirstLogin);
                            Log.d(TAG, "signInWithCredential : success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Prefs.putString("loginRoute", "google");
                        } else {
                            // 로그인 인증 실패
                            Log.w(TAG, "signInWithCredential : failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        mDialog.dismiss();
                    }
                });
    }


    // 파이어베이스 페이스북으로 인증
    private void handleFacebookAccessToken(AccessToken token) {
        mDialog.show();
        //Log.d(TAG, "facebook token :" + token.get);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 인증 성공
                            mIsFirstLogin = task.getResult().getAdditionalUserInfo().isNewUser();
                            Log.d(TAG, "Facebook Login : mIsFirstLogin : " + mIsFirstLogin);
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Prefs.putString("loginRoute", "facebook");
                        } else {
                            // 로그인 인증 실패
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        mDialog.dismiss();
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        // 유저 로그인 상태변화 listener 장착
        mAuth.addAuthStateListener(mAuthListener);
        Log.d(TAG, "AuthStateListener : add");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onCreate()");
        // 유저 로그인 상태변화 listener 해제
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
            Log.d(TAG, "AuthStateListener : remove");
        }
    }


    class FirebaseAuthStateListener implements FirebaseAuth.AuthStateListener {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // 유저가 로그인 했을 때
                user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            Prefs.putString("idToken", idToken);
                            Log.i("token", "token : success : " + idToken);
                            String registrationToken = FirebaseInstanceId.getInstance().getToken();
                            Log.d(TAG, "registrationToken : " + registrationToken);
                            if (mIsFirstLogin) {
                                Intent intent = new Intent(LoginActivity.this, CreateSocialAccountActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                mTimer.cancel();
                                finish();
                            }
                        } else {
                            Log.i("token", "token : fail");
                        }
                    }
                });
            }
        }
    }

    public void setChangeText() {
        mode = true;
        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(500);

        final Animation out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(500);

        mTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mode) {
                            mode = false;
                            changeTxt.startAnimation(out);
                            changeTxt.setText("키워드 기반 대학교 커뮤니티 정보 알림 서비스");
                            changeTxt.startAnimation(in);
                        } else {
                            mode = true;
                            changeTxt.startAnimation(out);
                            changeTxt.setText("원하는 키워드에 대한 알림을 받아보세요");
                            changeTxt.startAnimation(in);
                        }
                    }
                });
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTask, 0, 2000);
    }


}
