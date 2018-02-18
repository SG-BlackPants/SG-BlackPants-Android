package smilegate.blackpants.univscanner.profile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.data.model.LoginInfo;
import smilegate.blackpants.univscanner.data.model.Users;
import smilegate.blackpants.univscanner.data.remote.ApiUtils;
import smilegate.blackpants.univscanner.data.remote.UserApiService;
import smilegate.blackpants.univscanner.utils.BaseFragment;

/**
 * Created by user on 2018-01-22.
 */

public class ProfileFragment extends BaseFragment {
    private static final String TAG = "ProfileFragment";
    private View view;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private GoogleSignInClient mGoogleSignInClient;
    private UserApiService mUserApiService;
    private android.app.AlertDialog mLogoutDialog;
    private android.app.AlertDialog mDeleteDialog;

    @BindView(R.id.relLayout_logout)
    RelativeLayout logoutBtn;
    @BindView(R.id.relLayout_delete)
    RelativeLayout deleteAccountBtn;
    @BindView(R.id.text_profile_name)
    TextView profileNameTxt;
    @BindView(R.id.text_profile_email)
    TextView profileEmailTxt;
    @BindView(R.id.text_profile_university)
    TextView profileUniversityTxt;
    @BindView(R.id.relLayout_registeredKeywordView)
    RelativeLayout regKeywordViewBtn;
    @BindView(R.id.relLayout_communityView)
    RelativeLayout regCommunityViewBtn;

    @OnClick(R.id.relLayout_registeredKeywordView)
    public void keywordViewClick(RelativeLayout relativeLayout) {
        if (mFragmentNavigation != null) {
            mFragmentNavigation.pushFragment(ProfileRegisteredKeywordFragment.newInstance(0));
        }
    }

    @OnClick(R.id.relLayout_communityView)
    public void communityViewClick(RelativeLayout relativeLayout) {
        if (mFragmentNavigation != null) {
            mFragmentNavigation.pushFragment(ProfileCommunityFragment.newInstance(0));
        }
    }

    @OnClick(R.id.relLayout_logout)
    public void logoutClick(RelativeLayout relativeLayout) {
        Log.d(TAG,"로그아웃 클릭");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle("로그아웃");

        alertDialogBuilder
                .setMessage("정말 로그아웃 하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                // 서버로 보내기
                                signOut();
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                // 다이얼로그를 취소한다
                                dialog.cancel();
                            }
                        });
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }

    @OnClick(R.id.relLayout_delete)
    public void deleteAccountClick(RelativeLayout relativeLayout) {
        Log.d(TAG,"회원탈퇴 클릭");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle("회원탈퇴");

        alertDialogBuilder
                .setMessage("정말 탈퇴하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                // 서버로 보내기
                                deleteAccount();
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                // 다이얼로그를 취소한다
                                dialog.cancel();
                            }
                        });
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
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
            mUserApiService = ApiUtils.getUserApiService();
            mAuth = FirebaseAuth.getInstance();
            ButterKnife.bind(this, view);
            Gson gson = new Gson();
            String json = Prefs.getString("userInfo","");
            LoginInfo loginInfo = gson.fromJson(json, LoginInfo.class);
            profileNameTxt.setText(loginInfo.getName());
            profileUniversityTxt.setText(loginInfo.getUniversity());
            profileEmailTxt.setText(mAuth.getCurrentUser().getEmail());
            mLogoutDialog = new SpotsDialog(getContext(), R.style.logoutTheme);
            mDeleteDialog = new SpotsDialog(getContext(), R.style.deleteTheme);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void signOut() {
        mLogoutDialog.show();
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

/*    private void deleteAccount() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential;

        String loginRoute = Prefs.getString("loginRoute", null);
        String idToken = Prefs.getString("idToken", null);
        Log.d(TAG, "loginRoute : "+ loginRoute + " / idToken : " + idToken);
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


                                            } else {
                                                Log.e(TAG, "firebase user delete fail");
                                            }
                                        }
                                    });
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Account delete failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }*/

    private void deleteAccount() {
        mDeleteDialog.show();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mUser != null) {
            // 유저가 로그인 했을 때
            mUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        // 서버에 보내기
                        AuthCredential credential;

                        String loginRoute = Prefs.getString("loginRoute", null);
                        String idToken = task.getResult().getToken();
                        Log.d(TAG, "loginRoute : " + loginRoute + " / idToken : " + idToken);
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

                        mUser.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User account deleted.");
                                            sendToServer();
                                        } else {
                                            Log.e(TAG, "firebase user delete fail");
                                        }
                                    }
                                });
                    }
                }
            });
        }
    }

    public void sendToServer() {
        mUserApiService.deleteUser(mUser.getUid()).enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {
                    Log.e("ResponseData", response.body().toString());
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                    //signOut();
                    goLoginActivity();
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }


    public void goLoginActivity() {
        Prefs.putString("loginRoute", null);
        //Intent intent = new Intent(getContext(), LoginActivity.class);
        //startActivity(intent);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mLogoutDialog.dismiss();
        mDeleteDialog.dismiss();
        getActivity().finish();
    }

}
