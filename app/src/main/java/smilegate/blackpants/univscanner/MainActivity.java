package smilegate.blackpants.univscanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smilegate.blackpants.univscanner.data.model.Users;
import smilegate.blackpants.univscanner.data.remote.UserApiService;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.btn_logout)
    Button logoutBtn;
    @BindView(R.id.btn_revoke)
    Button revokeBtn;

    @OnClick(R.id.btn_logout)
    public void logout(Button button) {
        signOut();
    }

    @OnClick(R.id.btn_revoke)
    public void revoke(Button button) {
        //revokeAccess();
        deleteAccount();
    }

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "MainActivity";
    private UserApiService mUserApiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
    }

    private void signOut() {

        String loginRoute = Prefs.getString("loginRoute", null);

        mAuth.signOut();

        switch (loginRoute) {
            case "google":
                // Google sign out
                mGoogleSignInClient.signOut().addOnCompleteListener(this,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //updateUI(null);
                                goLoginActivity();
                            }
                        });
                break;
            case "facebook":
                LoginManager.getInstance().logOut();
                goLoginActivity();
                break;
            case "email":
                goLoginActivity();
                break;
            default:
                break;
        }
    }

    private void deleteAccount() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential;

        String loginRoute = Prefs.getString("loginRoute", null);
        String idToken = Prefs.getString("idToken", null);

        if(idToken != null) {
            switch (loginRoute) {
                case "google":
                    credential = GoogleAuthProvider.getCredential(idToken, null);
                    break;
                case "facebook":
                    credential = FacebookAuthProvider.getCredential(idToken);
                    break;
                case "email":
                    credential = EmailAuthProvider.getCredential(mUser.getEmail(), Prefs.getString("password",null));
                    break;
                default:
                    return;
            }

            mUser.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mUser.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User account deleted.");
                                                // 자동로그인 off
                                                //delete("abc@example.com");
                                                signOut();
                                            }
                                        }
                                    });
                        }
                    });
        } else {
            Toast.makeText(MainActivity.this, "Account delete failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void delete(String _id) {
        mUserApiService.deleteUser(_id).enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if(response.isSuccessful()) {
                    Log.e("ResponseData","삭제완료");
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Log.e(TAG, "삭제실패");
            }
        });
    }

    public void goLoginActivity() {
        Prefs.putBoolean("isLogin", false);
        Prefs.putString("loginRoute", null);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
