package smilegate.blackpants.univscanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import smilegate.blackpants.univscanner.data.remote.UserApiService;
import smilegate.blackpants.univscanner.data.remote.UserApiUtils;

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
    private android.app.AlertDialog dialog;

    @BindView(R.id.btn_google_login)
    SignInButton googleLoginBtn;

    @BindView(R.id.btn_facebook_login)
    LoginButton facebookLoginBtn;

    @BindView(R.id.btn_email_login)
    Button emailLoginBtn;

    @BindView(R.id.btn_create_account)
    Button createAccountBtn;

    @OnClick(R.id.btn_google_login)
    public void googleLogin(SignInButton button) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @OnClick(R.id.btn_facebook_login)
    public void facebookLogin(LoginButton button) {
    }

    @OnClick(R.id.btn_email_login)
    public void emailLogin(Button button) {
        Intent intent = new Intent(this, EmailLoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_create_account)
    public void createAccount(Button button) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        dialog = new SpotsDialog(this, R.style.loginLodingTheme);
        ;

        // 구글 로그인 설정
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        mUserApiService = UserApiUtils.getAPIService();
        mAuthListener = new FirebaseAuthStateListener();

        mCallbackManager = CallbackManager.Factory.create();

        facebookLoginBtn.setReadPermissions("email", "public_profile");
        facebookLoginBtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
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
                Log.d(TAG, "googlelogin : success");
            }
        }
    }

    // 파이어베이스 구글로 인증
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle :" + acct.getId());
        Log.d(TAG, "Google JWT : " + acct.getIdToken());
        dialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 인증 성공
                            Log.d(TAG, "signInWithCredential : success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Prefs.putString("loginRoute", "google");
                        } else {
                            // 로그인 인증 실패
                            Log.w(TAG, "signInWithCredential : failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
    }


    // 파이어베이스 페이스북으로 인증
    private void handleFacebookAccessToken(AccessToken token) {
        dialog.show();
        //Log.d(TAG, "facebook token :" + token.get);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 인증 성공
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Prefs.putString("loginRoute", "facebook");
                        } else {
                            // 로그인 인증 실패
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // 유저 로그인 상태변화 listener 장착
        mAuth.addAuthStateListener(mAuthListener);
        Log.d(TAG, "AuthStateListener : add");
    }

    @Override
    public void onStop() {
        super.onStop();
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

                            Prefs.putBoolean("isLogin", true);
                            //new ConnectServer().execute();
                            //sendPost("타이틀:제발되라", "body:될거라");
                            //getPost();
                            //sendPost("abc@example.com","홍길동","스마일대학교");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.i("token", "token : fail");
                        }
                    }
                });
            }
        }
    }

  /*  public static class ConnectServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            final String url = "https://androidtutorials.herokuapp.com/";

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UsersApi userApi = retrofit.create(UsersApi.class);

            Call<List<User>> response = userApi.getUsersGet();

            try {
                for(User user : response.execute().body())
                    Log.e("ResponseData",user.getName()+" "+user.getNickName());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
*/
    /*public void sendPost(String title, String body) {
        mUserApiService.savePost(title, body, 1).enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {

                if(response.isSuccessful()) {
                    Log.e("ResponseData",response.body().toString());
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }
*/
   /* public void getPost() {
        mUserApiService.getPost().enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                for(Users res : response.body())
                    Log.e("ResponseData",res.getTitle()+" "+res.getBody());
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                Log.e(TAG, "Unable to get from API.");
            }
        });
    }*/

    /*public void sendPost(String _id, String name, String university) {
        mUserApiService.saveUser(_id, name, university).enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {
                    Log.e("ResponseData", response.body().toString());
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }*/

}
