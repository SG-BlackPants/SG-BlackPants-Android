package smilegate.blackpants.univscanner.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.data.model.LoginInfo;
import smilegate.blackpants.univscanner.data.remote.ApiUtils;
import smilegate.blackpants.univscanner.data.remote.RedisApiService;
import smilegate.blackpants.univscanner.utils.BaseFragment;
import smilegate.blackpants.univscanner.utils.KeywordListAdapter;

import static smilegate.blackpants.univscanner.MainActivity.mNavController;

/**
 * Created by user on 2018-01-23.
 */

public class SearchViewFragment extends BaseFragment {
    private static final String TAG = "SearchViewFragment";
    private View mView;
    private List<String> mKeywordsList;
    private KeywordListAdapter mAdapter;
    private RedisApiService mRedisApiService;
    private LoginInfo mLoginInfo;

    int count;
    @BindView(R.id.autoText_search)
    AutoCompleteTextView searchAutoText;

    @BindView(R.id.btn_searchview_back)
    ImageButton searchViewBackBtn;

    @BindView(R.id.btn_searchview_clear)
    ImageButton searchViewClearBtn;

    @BindView(R.id.progressbar_searchview)
    ProgressBar progressBar;

    @OnClick(R.id.btn_searchview_back)
    public void searchViewBack(ImageButton imageButton) {
        if (!mNavController.popFragment()) {
            getActivity().onBackPressed();
        }
    }

    @OnClick(R.id.btn_searchview_clear)
    public void searchViewClear(ImageButton imageButton) {
        searchAutoText.setText("");
        searchViewClearBtn.setVisibility(View.GONE);
    }

    @BindView(R.id.list_search)
    ListView searchListView;

    public static SearchViewFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        SearchViewFragment fragment = new SearchViewFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_search_view, container, false);
            ButterKnife.bind(this, mView);
            mRedisApiService = ApiUtils.getRedisApiService();
            Gson gson = new Gson();
            String json = Prefs.getString("userInfo","");
            mLoginInfo = gson.fromJson(json, LoginInfo.class);
            initTextListener();
            searchViewClearBtn.setVisibility(View.GONE);
            count=0;
            progressBar.setVisibility(View.GONE);
        }
        return mView;
    }

    public void initTextListener() {
        Log.d(TAG, "initTextListener : initializing");

        mKeywordsList = new ArrayList<>();

        searchAutoText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               /* String text = searchAutoText.getText().toString().toLowerCase(Locale.getDefault());
                if(text.length()==0) {
                    mKeywordsList.clear();
                    searchViewClearBtn.setVisibility(View.GONE);
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = searchAutoText.getText().toString().toLowerCase(Locale.getDefault());
                Log.d(TAG, "afterTextChanged : " + text);
                count++;
                searchForMatch(text);
            }
        });
    }

    public void searchForMatch(String keyword) {
        Log.d(TAG, "searchForMatch : searcing for a match: " + keyword);
        mKeywordsList.clear();
        if(keyword.length()==0 || keyword.equals("")) {
            count=0;
            searchViewClearBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            searchViewClearBtn.setVisibility(View.VISIBLE);
            //서버통신
            mRedisApiService.getAutocompleteKeywords(mLoginInfo.getUniversity(),keyword).enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    progressBar.setVisibility(View.GONE);
                    if(response.body()!=null) {
                        Log.d(TAG, "자동완성 키워드 서버통신 성공");
                        mKeywordsList = response.body();
                        if(mKeywordsList.size()>0) {
                            Log.d(TAG, "1개 이상");
                            updateKewordsList();
                        } else {
                            Log.d(TAG, "만족하는 결과 없음");
                            mKeywordsList.add(searchAutoText.getText().toString().trim());
                            updateKewordsList();
                        }
                    } else {
                        Log.d(TAG, "자동완성 키워드 서버통신 실패 : onResponse : "+response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "자동완성 키워드 서버통신 실패 : onFailure "+ t.getMessage());
                }
            });

            //mKeywordsList.add(new Keywords(searchAutoText.getText().toString().trim()));

        }
    }

    public void updateKewordsList() {
        Log.d(TAG, "updateKeywordList : updating kewords list");

        mAdapter = new KeywordListAdapter(getContext(), R.layout.layout_keyword_listitem, mKeywordsList);

        searchListView.setAdapter(mAdapter);

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick : selected : " + mKeywordsList.get(position).toString());
                if (mFragmentNavigation != null) {
                    mFragmentNavigation.pushFragment(SearchResultFragment.newInstance(0, mKeywordsList.get(position)));
                }
            }
        });
    }

}
