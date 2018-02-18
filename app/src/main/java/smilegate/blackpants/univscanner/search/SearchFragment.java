package smilegate.blackpants.univscanner.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.aotasoft.taggroup.TagGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.ncapdevi.fragnav.FragNavController;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.data.model.KeywordRank;
import smilegate.blackpants.univscanner.data.remote.ApiUtils;
import smilegate.blackpants.univscanner.data.remote.KeywordApiService;
import smilegate.blackpants.univscanner.data.remote.UserApiService;
import smilegate.blackpants.univscanner.utils.BaseFragment;
import smilegate.blackpants.univscanner.utils.KeywordRankListAdapter;

/**
 * Created by user on 2018-01-22.
 */

public class SearchFragment extends BaseFragment {
    private static final String TAG = "SearchFragment";
    private final int INDEX_NOTIFICATION = FragNavController.TAB2;

    private View view;
    private List<String> mKeywordRankList;
    private KeywordRankListAdapter mAdapter;
    private List<String> mRecenetKeywordList;
    private KeywordApiService mKeywordApiService;
    private UserApiService mUserApiService;
    private String mUid;
    private String mUniversity;

    @BindView(R.id.top_searchbar)
    RelativeLayout includeLayoutSearch;

    @BindView(R.id.list_keywordRank)
    ListView keywordRankListView;

    @BindView(R.id.tag_group)
    TagGroup tagGroup;

    @OnClick(R.id.tag_group)
    public void tagClick(TagGroup tagGroup) {
        Log.d(TAG, "클릭 : "+tagGroup.getInputTagText());
    }

    @OnClick(R.id.top_searchbar)
    public void search(RelativeLayout layout) {
        if (mFragmentNavigation != null) {
            mFragmentNavigation.pushFragment(SearchViewFragment.newInstance(0));
        }
    }

    private TagGroup.OnTagClickListener mTagClickListener = new TagGroup.OnTagClickListener() {
        @Override
        public void onTagClick(TagGroup tagGroup, String tag, int position) {
            if (mFragmentNavigation != null) {
                mFragmentNavigation.pushFragment(SearchResultFragment.newInstance(0, tag));
            }
        }
    };


    public static SearchFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            Log.d(TAG,"검색 화면 생성");
            view = inflater.inflate(R.layout.fragment_search, container, false);
            ButterKnife.bind(this, view);
            mUserApiService = ApiUtils.getUserApiService();
            mKeywordApiService = ApiUtils.getKeywordApiService();
            Gson gson = new Gson();
            String json = Prefs.getString("userInfo","");
            //mUniversity = gson.fromJson(json, LoginInfo.class).getUniversity();
            mUniversity = "경희대학교-국제캠퍼스";
            mUid = FirebaseAuth.getInstance().getUid();
            if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                initRecentKeywordList();
                initkeywordRankList();
            }
            Log.d(TAG,"registrationToken : " +FirebaseInstanceId.getInstance().getToken());
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void initRecentKeywordList() {
        Log.d(TAG,"최근 키워드 가져오기 함수 실행");
        tagGroup.setGravity(TagGroup.TagGravity.MIDDLE);
        //mTagGroup.setTags(new String[]{"도레미파솔", "김치찌개", "비트코인","수강신청 날짜","멀머날어ㅓ아머리너ㅣㅏ렁니러ㅣ","꿀강","수강편람"});
        mRecenetKeywordList = new ArrayList<String>();
        mUserApiService.getRecentlyKeywords(mUid).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                mRecenetKeywordList = response.body();
                if(mRecenetKeywordList!=null) {
                    tagGroup.setTags(mRecenetKeywordList);
                    tagGroup.setOnTagClickListener(mTagClickListener);
                    Log.d(TAG, "최근 키워드 서버통신 성공");
                } else {
                    Log.d(TAG, "최근 키워드 서버통신 실패 : "+ response.body());
                }

            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.d(TAG, "최근 키워드 서버통신 실패 : "+ t.getMessage());
            }
        });

    }

    public void initkeywordRankList() {
        mKeywordRankList = new ArrayList<>();
        mKeywordApiService.getPopularKeywords(mUniversity).enqueue(new Callback<KeywordRank>() {
            @Override
            public void onResponse(Call<KeywordRank> call, Response<KeywordRank> response) {

                if(response.body()!=null) {
                    //Log.d(TAG,"JSON OBJECT 보기 : "+response.body().toString());
                    Log.d(TAG,"키워드 순위 서버통신 성공");

                    mKeywordRankList = response.body().getMessage();
                    if(mKeywordRankList!=null) {
                        mAdapter = new KeywordRankListAdapter(getContext(), R.layout.layout_rank_listitem, mKeywordRankList);
                        keywordRankListView.setAdapter(mAdapter);
                        setListViewHeightBasedOnChildren(keywordRankListView);
                    }
                } else {
                    Log.d(TAG,"키워드 순위 서버통신 실패 : "+response.message());
                }
            }

            @Override
            public void onFailure(Call<KeywordRank> call, Throwable t) {
                Log.d(TAG,"키워드 순위 서버통신 실패 : "+t.getMessage());
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
        Log.d(TAG, listAdapter.getCount()+"");
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
    public void onResume() {
        super.onResume();
        Log.d(TAG,"검색 뷰 화면");
        if(mRecenetKeywordList!=null) {
            tagGroup.setTags(mRecenetKeywordList);
            tagGroup.setOnTagClickListener(mTagClickListener);
        }
    }
}
