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
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smilegate.blackpants.univscanner.data.model.Users;
import smilegate.blackpants.univscanner.data.remote.ApiUtils;
import smilegate.blackpants.univscanner.data.remote.UserApiService;
import smilegate.blackpants.univscanner.utils.UniversityListAdapter;

/**
 * Created by Semin on 2018-02-04.
 */

public class CreateSocialAccountActivity extends AppCompatActivity {
    private static final String TAG = "CreatAccountActivity";
    private FirebaseAuth mAuth;
    private List<String> mUniversityList;
    private UniversityListAdapter mAdapter;
    private int mFilterCount;
    private android.app.AlertDialog mDialog;
    private UserApiService mUserApiService;

    @BindView(R.id.autoText_univ)
    AutoCompleteTextView inputUnivText;

    @BindView(R.id.list_univ)
    ListView univListView;

    @BindView(R.id.btn_create_account_back)
    ImageButton backBtn;

    @BindView(R.id.univ_list)
    ConstraintLayout univListLayout;

    @BindView(R.id.create_socialinfo)
    ConstraintLayout createSocialInfoLayout;

    @BindView(R.id.autoText_social_name)
    AutoCompleteTextView inputNameText;

    @BindView(R.id.text_logininfo_status)
    TextView loginInfoStatusTxt;

    @OnClick(R.id.btn_create_account_back)
    public void createAccountBack(ImageButton imageButton) {
        finish();
    }

    @OnClick(R.id.btn_socialcreate)
    public void createAccount(Button button) {
        if (validateForm()) {
            String university= inputUnivText.getText().toString().trim();
            university = university.replace(" ", "-");
            sendToServer(university, inputNameText.getText().toString().trim());
        }

    }

    @OnItemClick(R.id.list_univ)
    public void univListItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemClick : selected : " + mUniversityList.get(position).toString());
        String codeStart = "<b><font color='#000'>";
        String codeEnd = "</font></b>";
        String item = mUniversityList.get(position).toString();
        item = item.replace(codeStart, ""); // <(> and <)> are different replace them with your color code String, First one with start tag
        item = item.replace(codeEnd, ""); // then end tag
        inputUnivText.setText(item);
        changeMiddleView("nameInfo");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_socialaccount);
        ButterKnife.bind(this);

        initTextListener();
        saveToList();
        changeMiddleView("init");
        mAdapter = new UniversityListAdapter(this, R.layout.layout_univ_listitem, mUniversityList);
        mDialog = new SpotsDialog(this, R.style.dataLodingTheme);
        univListView.setTextFilterEnabled(true);
        univListView.setAdapter(mAdapter);
        mUserApiService = ApiUtils.getAPIService();
        mAuth = FirebaseAuth.getInstance();
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
                if (text.length() == 0) {
                    changeMiddleView("init");
                } else {
                    if (mAdapter != null) {
                        mAdapter.getFilter().filter(s, new Filter.FilterListener() {
                            @Override
                            public void onFilterComplete(int count) {
                                mFilterCount = count;
                                setListViewHeightBasedOnChildren(univListView, mFilterCount);
                            }
                        });
                    } else {
                        Log.d("filter", "no filter availible");
                    }
                }
                //searchForMatch(text);
            }
        });
    }

    public static void setListViewHeightBasedOnChildren(ListView listView, int count) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        if (count > 0) {
            View listItem = listAdapter.getView(0, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight = listItem.getMeasuredHeight() * count;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("university_list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void saveToList() {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray content = obj.getJSONObject("dataSearch").getJSONArray("content");
            mUniversityList = new ArrayList<String>();
            for (int i = 0; i < content.length(); i++) {
                JSONObject univInfo = content.getJSONObject(i);
                String schoolName = univInfo.getString("schoolName");
                String campusName = univInfo.getString("campusName");
                String result = schoolName + " " + campusName;
                Log.d(TAG, i + " : " + result);
                mUniversityList.add(result);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void changeMiddleView(String option) {
        switch (option) {
            case "init" :
                univListLayout.setVisibility(View.GONE);
                createSocialInfoLayout.setVisibility(View.GONE);
                break;
            case "univList" :
                univListLayout.setVisibility(View.VISIBLE);
                createSocialInfoLayout.setVisibility(View.GONE);
                break;
            case "nameInfo" :
                univListLayout.setVisibility(View.GONE);
                createSocialInfoLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    private boolean validateForm() {
        boolean valid;

        String name = inputNameText.getText().toString();

        if (TextUtils.isEmpty(name)) {
            loginInfoStatusTxt.setText("이름을 입력하세요.");
            valid = false;
        } else {
            valid = true;
        }

        return valid;
    }


    public void sendToServer(final String universityInfo,final String name) {
        mDialog.show();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // 유저가 로그인 했을 때
            user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        // 서버에 보내기
                        String loginToken = task.getResult().getToken();
                        String registrationToken = FirebaseInstanceId.getInstance().getToken();
                        //서버와 통신
                        mUserApiService.setUsers(loginToken, registrationToken, name, universityInfo).enqueue(new Callback<Users>() {
                            @Override
                            public void onResponse(Call<Users> call, Response<Users> response) {
                                if (response.isSuccessful()) {
                                    Log.e("ResponseData", response.body().toString());
                                    Log.i(TAG, "post submitted to API." + response.body().toString());

                                    mDialog.dismiss();
                                    Intent intent = new Intent(CreateSocialAccountActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<Users> call, Throwable t) {
                                Log.e(TAG, "Unable to submit post to API.");
                                mDialog.dismiss();
                                finish();
                            }
                        });
                    }
                }
            });
        }



    }
}
