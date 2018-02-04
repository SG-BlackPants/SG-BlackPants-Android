package smilegate.blackpants.univscanner;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import smilegate.blackpants.univscanner.data.remote.UniversityApiService;
import smilegate.blackpants.univscanner.utils.UniversityListAdapter;

/**
 * Created by Semin on 2018-02-04.
 */

public class CreateSocialAccountActivity extends AppCompatActivity {
    private static final String TAG = "CreatAccountActivity";
    private FirebaseAuth mAuth;
    private List<String> mUniversityList;
    private UniversityListAdapter mAdapter;
    private UniversityApiService mUniversityApiService;

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

    @OnClick(R.id.btn_create_account_back)
    public void createAccountBack(ImageButton imageButton) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_emailaccount);
        ButterKnife.bind(this);

        initTextListener();
        changeMiddleView("init");

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
            for(int i=0;i<13;i++){
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
            case "emailInfo" :
                univListLayout.setVisibility(View.GONE);
                createSocialInfoLayout.setVisibility(View.VISIBLE);
                break;
        }

    }
}
