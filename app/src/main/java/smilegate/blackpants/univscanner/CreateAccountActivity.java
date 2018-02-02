package smilegate.blackpants.univscanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smilegate.blackpants.univscanner.data.model.University;
import smilegate.blackpants.univscanner.data.remote.UniversityApiService;
import smilegate.blackpants.univscanner.data.remote.UniversityApiUtils;
import smilegate.blackpants.univscanner.utils.UniversityListAdapter;

/**
 * Created by user on 2018-01-17.
 */

public class CreateAccountActivity extends AppCompatActivity {
    private static final String TAG = "CreatAccountActivity";
    private FirebaseAuth mAuth;
    private List<University> mUniversityList;
    private UniversityListAdapter mAdapter;
    private UniversityApiService mUniversityApiService;

    @BindView(R.id.autoText_univ)
    AutoCompleteTextView inputUnivText;

    @BindView(R.id.list_univ)
    ListView univListView;

    /*@BindView(R.id.autoText_school)
    AutoCompleteTextView inputSchoolText;

    @BindView(R.id.autoText_email)
    AutoCompleteTextView inputEmailText;

    @BindView(R.id.editText_password)
    EditText passwordText;

    @BindView(R.id.btn_create_account_complete)
    Button createAccountBtn;

    @OnClick(R.id.btn_create_account_complete)
    public void createAccount(Button button) {
        String email = inputEmailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        Log.i("입력값",email+", "+password);
        createUser(email, password);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        ButterKnife.bind(this);
        mUniversityApiService = UniversityApiUtils.getAPIService();
        initTextListener();
        getUniversityList();
        //mAuth = FirebaseAuth.getInstance();
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
           /*     String text = inputUnivText.getText().toString();
                Log.d(TAG, "afterTextChanged : " + text);
                searchForMatch(text);*/
            }
        });
    }

    public void searchForMatch(String univName) {
        Log.d(TAG, "searchForMatch : searcing for a match: " + univName);
        mUniversityList.clear();

        if (univName.length() == 0) {

        } else {
            //여기서 서버와 연동
            updateUniversityList();
        }
    }

    public void updateUniversityList() {
        Log.d(TAG, "updateKeywordList : updating kewords list");

        mAdapter = new UniversityListAdapter(this, R.layout.layout_univ_listitem, mUniversityList);

        univListView.setAdapter(mAdapter);

        univListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick : selected : " + mUniversityList.get(position).toString());
            }
        });
    }

    public void getUniversityList() {
        mUniversityApiService.getUniversity().enqueue(new Callback<University>() {
            @Override
            public void onResponse(Call<University> call, Response<University> response) {
                for(University.Content content : response.body().getDataSearch().getContent()) {
                    Log.d(TAG, "getUniversityList : " + content.getCampusName());
                }

            }

            @Override
            public void onFailure(Call<University> call, Throwable t) {

            }
        });
    }

   /* public void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                Toast.makeText(CreateAccountActivity.this, "비밀번호는 6자리 이상 입력해 주세요.",
                                        Toast.LENGTH_SHORT).show();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(CreateAccountActivity.this, "올바른 이메일을 입력하여 주세요.",
                                        Toast.LENGTH_SHORT).show();
                            } catch(FirebaseAuthUserCollisionException e) {
                                Toast.makeText(CreateAccountActivity.this, "이미 존재하는 이메일입니다.",
                                        Toast.LENGTH_SHORT).show();
                            } catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                            }

                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }*/
}
