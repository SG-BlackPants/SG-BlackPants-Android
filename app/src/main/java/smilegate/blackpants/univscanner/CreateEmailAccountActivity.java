package smilegate.blackpants.univscanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smilegate.blackpants.univscanner.data.model.University;
import smilegate.blackpants.univscanner.data.remote.UniversityApiService;
import smilegate.blackpants.univscanner.utils.UniversityListAdapter;

/**
 * Created by user on 2018-01-17.
 */

public class CreateEmailAccountActivity extends AppCompatActivity {
    private static final String TAG = "CreatAccountActivity";
    private FirebaseAuth mAuth;
    private List<String> mUniversityList;
    private UniversityListAdapter mAdapter;
    private UniversityApiService mUniversityApiService;

    @BindView(R.id.list_univ)
    ListView univListView;

    @BindView(R.id.btn_create_account_back)
    ImageButton backBtn;

    @BindView(R.id.univ_list)
    ConstraintLayout univListLayout;

    @BindView(R.id.create_emailinfo)
    ConstraintLayout createEmailInfoLayout;

    @OnClick(R.id.btn_create_account_back)
    public void createAccountBack(ImageButton imageButton) {
        finish();
    }

    @BindView(R.id.autoText_univ)
    AutoCompleteTextView inputUnivText;

    @BindView(R.id.autoText_email)
    AutoCompleteTextView inputEmailText;

    @BindView(R.id.autoText_password)
    AutoCompleteTextView passwordText;

    @BindView(R.id.autoText_password_again)
    AutoCompleteTextView passwordAgainText;

    @BindView(R.id.btn_emailcreate)
    Button createEmailBtn;

    @BindView(R.id.text_logininfo_status)
    TextView loginInfoStatusTxt;

    @OnClick(R.id.btn_emailcreate)
    public void createAccount(Button button) {
        /*String email = inputEmailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        Log.i("입력값", email + ", " + password);
        createUser(email, password);*/
        if(validateForm()) {
            loginInfoStatusTxt.setText("모두 일치");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_emailaccount);
        ButterKnife.bind(this);
        //mUniversityApiService = UniversityApiUtils.getAPIService();
        initTextListener();
        changeMiddleView("init");

        //getUniversityList();
        //mAuth = FirebaseAuth.getInstance();
        /*mService = SQApiUtils.getSOService();
        loadAnswers();*/
    }

    public void initTextListener() {
        Log.d(TAG, "initTextListener : initializing");

        mUniversityList = new ArrayList<>();

        inputUnivText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changeMiddleView("univList");
                String text = inputUnivText.getText().toString();
                Log.d(TAG, "afterTextChanged : " + text);
                searchForMatch(text);
            }
        });
    }

    public void searchForMatch(String univName) {
        Log.d(TAG, "searchForMatch : searcing for a match: " + univName);
        mUniversityList.clear();

        if (univName.length() == 0) {
            changeMiddleView("init");
        } else {
            for (int i = 0; i < 13; i++) {
                mUniversityList.add(univName);
            }

            //여기서 서버와 연동
            updateUniversityList();
        }
    }

    public void updateUniversityList() {
        Log.d(TAG, "updateUniversityList : updating university list");

        mAdapter = new UniversityListAdapter(this, R.layout.layout_univ_listitem, mUniversityList);
        setListViewHeightBasedOnChildren(univListView);
        univListView.setAdapter(mAdapter);

        univListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick : selected : " + mUniversityList.get(position).toString());
                inputUnivText.setText(mUniversityList.get(position).toString());
                changeMiddleView("emailInfo");
            }
        });
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    public void getUniversityList() {
        mUniversityApiService.getUniversity().enqueue(new Callback<University>() {
            @Override
            public void onResponse(Call<University> call, Response<University> response) {
                /*for(University.Content content : response.body().getDataSearch().getContent()) {
                    Log.d(TAG, "getUniversityList : " + content.getCampusName());
                }*/
                if (response.isSuccessful()) {
                    Log.d(TAG, "getUniversityList : " + response.body());
                } else {
                    int statusCode = response.code();
                    // handle request errors depending on status code
                    Log.d(TAG, "getUniversityList : fail : " + statusCode);
                }

            }

            @Override
            public void onFailure(Call<University> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    private boolean validateForm() {
        boolean valid;

        // 1. 이메일 유효성 검사 - empty, 이메일 format
        // 2. 비밀번호 6자리 이상인지 검사
        // 3. 비빌번호 입력과 확인이 같은지 검사.

        String email = inputEmailText.getText().toString();
        String password = passwordText.getText().toString();
        String passwordAgain = passwordAgainText.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Animation shake = AnimationUtils.loadAnimation(this,R.anim.edittext_shake);
            inputEmailText.startAnimation(shake);
            inputEmailText.setError("!!!");
            loginInfoStatusTxt.setText("이메일을 입력하세요.");
            inputEmailText.requestFocus();
            valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmailText.setError(null);
            loginInfoStatusTxt.setText("올바른 이메일을 입력하세요.");
            valid = false;
        } else if (TextUtils.isEmpty(password)) {
            passwordText.setError(null);
            loginInfoStatusTxt.setText("비밀번호를 입력하세요.");
            valid = false;
        } else if (password.length() < 6) {
            passwordText.setError(null);
            loginInfoStatusTxt.setText("비밀번호를 6자리 이상 입력하세요.");
            valid = false;
        } else if (!password.equals(passwordAgain)) {
            passwordAgainText.setError(null);
            loginInfoStatusTxt.setText("비밀번호가 일치하지 않습니다.");
            valid = false;
        } else {
            valid = true;
        }

        return valid;
    }


    public void changeMiddleView(String option) {
        switch (option) {
            case "init":
                univListLayout.setVisibility(View.GONE);
                createEmailInfoLayout.setVisibility(View.GONE);
                loginInfoStatusTxt.setText("");
                break;
            case "univList":
                univListLayout.setVisibility(View.VISIBLE);
                createEmailInfoLayout.setVisibility(View.GONE);
                break;
            case "emailInfo":
                univListLayout.setVisibility(View.GONE);
                createEmailInfoLayout.setVisibility(View.VISIBLE);
                break;
        }

    }

    public void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(CreateEmailAccountActivity.this, LoginActivity.class);
                            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                Toast.makeText(CreateEmailAccountActivity.this, "비밀번호는 6자리 이상 입력해 주세요.",
                                        Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(CreateEmailAccountActivity.this, "올바른 이메일을 입력하여 주세요.",
                                        Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(CreateEmailAccountActivity.this, "이미 존재하는 이메일입니다.",
                                        Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }

                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
