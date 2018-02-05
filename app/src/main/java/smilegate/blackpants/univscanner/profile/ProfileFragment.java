package smilegate.blackpants.univscanner.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import smilegate.blackpants.univscanner.LoginActivity;
import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.data.remote.UserApiService;
import smilegate.blackpants.univscanner.utils.BaseFragment;

/**
 * Created by user on 2018-01-22.
 */

public class ProfileFragment extends BaseFragment {
    private static final String TAG = "NotificationFragment";
    private View view;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private GoogleSignInClient mGoogleSignInClient;
    private UserApiService mUserApiService;

    @BindView(R.id.btn_signout)
    Button logoutBtn;
    @BindView(R.id.btn_delete)
    Button revokeBtn;

    @OnClick(R.id.btn_signout)
    public void logout(Button button) {
        signOut();
    }
    @OnClick(R.id.btn_delete)
    public void revoke(Button button) {
        deleteAccount();
    }
    public static ProfileFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_profile, container, false);
            //btn = cachedView.findViewById(R.id.button);

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

            mAuth = FirebaseAuth.getInstance();

            ButterKnife.bind(this, view);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.bind(this, view).unbind();
    }

    public void signOut() {
        String loginRoute = Prefs.getString("loginRoute", "email");

        mAuth.signOut();
        switch (loginRoute) {
            case "google":
                // Google sign out
                mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(),
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

        if (idToken != null) {
            switch (loginRoute) {
                case "google":
                    credential = GoogleAuthProvider.getCredential(idToken, null);
                    break;
                case "facebook":
                    credential = FacebookAuthProvider.getCredential(idToken);
                    break;
                case "email":
                    credential = EmailAuthProvider.getCredential(mUser.getEmail(), Prefs.getString("password", null));
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
            Toast.makeText(getContext(), "Account delete failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void goLoginActivity() {
        Prefs.putString("loginRoute", null);
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}
