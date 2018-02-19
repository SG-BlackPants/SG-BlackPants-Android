package smilegate.blackpants.univscanner.search;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.data.model.Article;
import smilegate.blackpants.univscanner.data.model.ArticleMessage;
import smilegate.blackpants.univscanner.data.model.CodeResult;
import smilegate.blackpants.univscanner.data.model.Community;
import smilegate.blackpants.univscanner.data.model.LoginInfo;
import smilegate.blackpants.univscanner.data.model.Push;
import smilegate.blackpants.univscanner.data.model.SearchResults;
import smilegate.blackpants.univscanner.data.model.UserSetting;
import smilegate.blackpants.univscanner.data.remote.ApiUtils;
import smilegate.blackpants.univscanner.data.remote.ArticleApiService;
import smilegate.blackpants.univscanner.data.remote.UserApiService;
import smilegate.blackpants.univscanner.utils.BaseFragment;
import smilegate.blackpants.univscanner.utils.FilterCommunityListAdapter;
import smilegate.blackpants.univscanner.utils.SearchResultFeedAdapter;

import static smilegate.blackpants.univscanner.MainActivity.mNavController;

/**
 * Created by user on 2018-01-25.
 */

public class SearchResultFragment extends BaseFragment implements SearchResultFeedAdapter.ContentDetailClickListener, FilterCommunityListAdapter.CommunityCheckboxListener {
    private static final String TAG = "SearchResultFragment";
    private View mView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SearchResultFeedAdapter mAdapter;
    private List<SearchResults> mSearchResultsList;
    private SearchResults mSearchResults;
    private ArticleApiService mArticleApiService;
    private UserApiService mUserApiService;
    private String mKeyword;
    private HashMap<String, String> mCommunityHashMap;
    private List<Community> mCommunityList;
    private FilterCommunityListAdapter mCommunityAdapter;
    private DatePickerDialog.OnDateSetListener mStartDateSetListener;
    private DatePickerDialog.OnDateSetListener mEndDateSetListener;
    int count = 0;
    //필터관련  변수
    private String mStartDate;
    private String mEndDate;
    private String mSecondWord;
    private HashMap<String, Boolean> mCommunityCheckHashMap;
    private UserSetting mUserSetting;

    @BindView(R.id.list_filter_community)
    ListView communityListView;

    @BindView(R.id.btn_searchresult_back)
    ImageButton searchResultBackBtn;

    @OnClick(R.id.btn_searchresult_back)
    public void searchResultBack(ImageButton imageButton) {
        if (drawerLayout.isDrawerOpen(drawerFilterView)) {
            drawerLayout.closeDrawer(drawerFilterView);
        } else {
            if (!mNavController.popFragment()) {
                getActivity().onBackPressed();
            }
        }
    }

    @BindView(R.id.list_searchresult)
    RecyclerView searchResultRecyclerView;

    @BindView(R.id.drawerLayout_searchresult)
    DrawerLayout drawerLayout;

    @BindView(R.id.btn_register_keyword)
    ImageButton registerKeywordBtn;

    @BindView(R.id.filterView)
    NavigationView drawerFilterView;

    @BindView(R.id.btn_filter)
    Button filterBtn;

    @BindView(R.id.autoText_included_keyword)
    AutoCompleteTextView includedKeywordTxt;

    @BindView(R.id.text_start_date)
    TextView startDateTxt;

    @BindView(R.id.text_end_date)
    TextView endDateTxt;

    @BindView(R.id.btn_filter_apply)
    Button filterApplyBtn;

    @BindView(R.id.text_search_result)
    TextView keywordTxt;

    @BindView(R.id.switch_filter_save)
    Switch filterSaveSwitch;

    @BindView(R.id.text_search_result_status)
    TextView searchResultStatusTxt;

    @BindView(R.id.progressbar_searchresult)
    ProgressBar progressBar;

    @BindView(R.id.text_resultCount)
    TextView resultCount;

    @BindView(R.id.progressbar_resultCount)
    ProgressBar progressBarResultCount;

    @OnClick(R.id.btn_filter_apply)
    public void filterApplyClick(Button button) {
        //getSettingFilter();
        getDataFromServer(mKeyword);
        drawerLayout.closeDrawer(drawerFilterView);
    }

    @OnClick(R.id.btn_filter)
    public void filterClick(Button button) {
        drawerLayout.openDrawer(drawerFilterView);
    }

    @OnClick(R.id.text_start_date)
    public void setStartDateClick(TextView textView) {
        //Toast.makeText(getContext(), "시작날짜클릭", Toast.LENGTH_LONG).show();
        DatePickerDialog dialog = new DatePickerDialog(getContext(), mStartDateSetListener, 2018, 1, 1);
        dialog.getDatePicker().setMinDate(setDatePicker(0));
        dialog.getDatePicker().setMaxDate(setDatePicker(1));
        dialog.show();
    }

    @OnClick(R.id.text_end_date)
    public void setEndDateClick(TextView textView) {
        //Toast.makeText(getContext(), "종료날짜클릭", Toast.LENGTH_LONG).show();
        DatePickerDialog dialog = new DatePickerDialog(getContext(), mEndDateSetListener, 2018, 2, 17);
        dialog.getDatePicker().setMinDate(setDatePicker(0));
        dialog.getDatePicker().setMaxDate(setDatePicker(1));
        dialog.show();
    }

    @OnClick(R.id.btn_register_keyword)
    public void registerKeywordClick(ImageButton imageButton) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        Log.d(TAG, "등록 키워드 클릭");

        alertDialogBuilder.setTitle("키워드 등록");

        alertDialogBuilder
                .setMessage("해당 키워드를 등록하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("등록",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                // 서버로 보내기
                                pushKeywordToServer();
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("취소",
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


    public static SearchResultFragment newInstance(int instance, String keyword) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        args.putString("keyword", keyword);
        SearchResultFragment fragment = new SearchResultFragment();
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
            mView = inflater.inflate(R.layout.fragment_search_result, container, false);
            ButterKnife.bind(this, mView);
            progressBar.setVisibility(View.GONE);
            progressBarResultCount.setVisibility(View.GONE);
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                mKeyword = bundle.getString("keyword");
                keywordTxt.setText(mKeyword);
                Log.d(TAG, "키워드 : " + mKeyword);
            }
            mArticleApiService = ApiUtils.getArticleApiService();
            mUserApiService = ApiUtils.getUserApiService();
            mSearchResultsList = new ArrayList<>();
            mCommunityHashMap = new HashMap<String, String>();
            initFilter();
            mAdapter = new SearchResultFeedAdapter(getContext(), mSearchResultsList);
            mLayoutManager = new LinearLayoutManager(getContext());
            searchResultRecyclerView.setLayoutManager(mLayoutManager);
            searchResultRecyclerView.setItemAnimator(new DefaultItemAnimator());
            searchResultRecyclerView.setAdapter(mAdapter);
            mAdapter.setContentDetailClickListner(this);
            getDataFromServer(mKeyword);
            mStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month = month + 1;
                    String tempDate = makeSimpleDateFormat(year, month, dayOfMonth);
                    if (checkDateValid(tempDate, mEndDate)) {
                        mStartDate = tempDate;
                        startDateTxt.setText(year + "년 " + month + "월 " + dayOfMonth + "일");
                    } else {
                        Toast.makeText(getContext(), "종료 날짜보다 이전 날짜여야 합니다.", Toast.LENGTH_LONG).show();
                    }
                }
            };
            mEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month = month + 1;
                    String tempDate = makeSimpleDateFormat(year, month, dayOfMonth);
                    if (checkDateValid(mStartDate, tempDate)) {
                        mEndDate = tempDate;
                        endDateTxt.setText(year + "년 " + month + "월 " + dayOfMonth + "일");
                    } else {
                        Toast.makeText(getContext(), "시작 날짜보다 이후의 날짜여야 합니다.", Toast.LENGTH_LONG).show();
                    }
                }
            };
            filterSaveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        saveFilter();
                    }
                }
            });
        }
        return mView;
    }

    public void getDataFromServer(String keyword) {
        count++;
        progressBar.setVisibility(View.VISIBLE);
        progressBarResultCount.setVisibility(View.VISIBLE);
        mSecondWord = includedKeywordTxt.getText().toString().trim();

        List<String> communityList = new ArrayList<>();

        Set<Map.Entry<String, Boolean>> set = mCommunityCheckHashMap.entrySet();
        Iterator<Map.Entry<String, Boolean>> it = set.iterator();

        while (it.hasNext()) {
            Map.Entry<String, Boolean> e = (Map.Entry<String, Boolean>) it.next();
            if (e.getValue()) {
                communityList.add(e.getKey());
            }
        }
        Log.d(TAG,"검색 필터 : "+communityList.toString());
        mArticleApiService.getArticles(keyword, Prefs.getString("userToken", null), communityList, mStartDate, mEndDate, mSecondWord).enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                progressBar.setVisibility(View.GONE);
                progressBarResultCount.setVisibility(View.GONE);
                if (response.body() != null) {
                    Log.d(TAG, "Article info getDataFromServer : success");
                    List<ArticleMessage> articleMessages = response.body().getArticleMessage();
                    searchResultStatusTxt.setVisibility(View.GONE);
                    if (articleMessages.size() != 0) {
                        addData(articleMessages);
                        mAdapter.notifyDataSetChanged();
                        resultCount.setText(articleMessages.size() + "개의 결과");
                        if(count==1) {
                            resultCount.setText("35개의 결과");
                        } else if(count == 2) {
                            resultCount.setText("16개의 결과");
                        } else {
                            resultCount.setText("10개의 결과");
                        }
                    } else {
                        searchResultStatusTxt.setVisibility(View.VISIBLE);
                        searchResultStatusTxt.setText("해당 키워드에 대한 검색결과가 없습니다.");
                        resultCount.setText("0개의 결과");
                        mSearchResultsList.clear();
                        mAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Article info getDataFromServer : onResponse : fail : " + response.message());
                }
                } else {
                    searchResultStatusTxt.setVisibility(View.VISIBLE);
                    resultCount.setText("0개의 결과");
                    searchResultStatusTxt.setText("인터넷 연결이 원활하지 않습니다.");
                    mSearchResultsList.clear();
                    mAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Article info getDataFromServer : onResponse : fail : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                progressBarResultCount.setVisibility(View.GONE);
                searchResultStatusTxt.setVisibility(View.VISIBLE);
                resultCount.setText("0개의 결과");
                searchResultStatusTxt.setText("인터넷 연결이 원활하지 않습니다.");
                Log.d(TAG, "Article info getDataFromServer : fail : " + t.getMessage());
            }
        });
    }

    public void addData(List<ArticleMessage> articleMessages) {
        SearchResults searchResults;
        String id, title, createdDate, content, community, boardAddr, author, url;
        List<String> images;
        mSearchResultsList.clear();
        for (int i = articleMessages.size() - 1; i >= 0; i--) {
            id = articleMessages.get(i).getId();
            content = articleMessages.get(i).getSource().getContent();
            title = transformTitle(articleMessages.get(i).getSource().getTitle(), content);
            createdDate = transformCreatedTime(articleMessages.get(i).getSource().getCreatedDate());
            community = transformCommunity(articleMessages.get(i).getSource().getCommunity());
            boardAddr = transformBoardAddr(articleMessages.get(i).getSource().getBoardAddr());
            author = articleMessages.get(i).getSource().getAuthor();
            url = transformUrl(boardAddr, articleMessages.get(i).getSource().getCommunity());
            images = articleMessages.get(i).getSource().getImages();
            searchResults = new SearchResults(id, title, createdDate, content, community, boardAddr, author, url, images);
            //mSearchResultsList.add(i, searchResults);
            mSearchResultsList.add(0, searchResults);
        }
    }

    public void pushKeywordToServer() {
        String secondWord = mUserSetting.getSecondWord();
        String startDate = mUserSetting.getStartDate();
        String endDate = mUserSetting.getEndDate();
        HashMap<String, Boolean> communityCheckHashMap = mUserSetting.getCommunityHashMap();

        List<String> communityList = new ArrayList<>();

        Set<Map.Entry<String, Boolean>> set = communityCheckHashMap.entrySet();
        Iterator<Map.Entry<String, Boolean>> it = set.iterator();

        while (it.hasNext()) {
            Map.Entry<String, Boolean> e = (Map.Entry<String, Boolean>) it.next();
            if (e.getValue()) {
                communityList.add(e.getKey());
            }
        }

        Gson gson = new Gson();
        String json = Prefs.getString("userInfo", "");
        LoginInfo loginInfo = gson.fromJson(json, LoginInfo.class);
        String university = loginInfo.getUniversity();
        //String[] communityArray = {"facebook-482012061908784", "everytime"};
        Push push = new Push(mKeyword, "경희대학교", communityList, startDate, endDate, secondWord);
        Log.d(TAG, "PUSH : " + push.toString());
        //CodeResult codeResult = new CodeResult();
        mUserApiService.pushKeyword(FirebaseAuth.getInstance().getUid(), push).enqueue(new Callback<CodeResult>() {
            @Override
            public void onResponse(Call<CodeResult> call, Response<CodeResult> response) {
                if (response.body() != null) {
                    if (response.code() == 211) {
                        Log.d(TAG, "키워드 등록 실패 : onResponse : " + response.body().getCode());
                        Toast.makeText(getContext(), "이미 등록된 키워드입니다.", Toast.LENGTH_LONG).show();
                    } else if (response.code() == 212) {
                        Log.d(TAG, "키워드 등록 실패 : onResponse : " + response.message());
                        Toast.makeText(getContext(), "키워드는 최대 5개까지만 등록할 수 있습니다.", Toast.LENGTH_LONG).show();
                    } else {
                        Log.d(TAG, "키워드 등록 성공 : " + response.body().toString());
                        Toast.makeText(getContext(), "등록을 완료하였습니다.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d(TAG, "키워드 등록 실패 : onResponse : " + response.message());
                    Toast.makeText(getContext(), "키워드 등록에 실패하였습니다.", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<CodeResult> call, Throwable t) {
                Log.d(TAG, "키워드 등록 실패 : onFailure : " + t.getMessage());
                Toast.makeText(getContext(), "등록에 실패하였습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }

    DrawerLayout.DrawerListener myDrawerListener = new DrawerLayout.DrawerListener() {

        public void onDrawerClosed(View drawerView) {
        }

        public void onDrawerOpened(View drawerView) {
        }

        public void onDrawerSlide(View drawerView, float slideOffset) {
          /*  txtPrompt.setText("onDrawerSlide: "
                    + String.format("%.2f", slideOffset));*/
        }

        public void onDrawerStateChanged(int newState) {
            String state;
            switch (newState) {
                case DrawerLayout.STATE_IDLE:
                    state = "STATE_IDLE";
                    break;
                case DrawerLayout.STATE_DRAGGING:
                    state = "STATE_DRAGGING";
                    break;
                case DrawerLayout.STATE_SETTLING:
                    state = "STATE_SETTLING";
                    break;
                default:
                    state = "unknown!";
            }
        }
    };

    @Override
    public void onButtonClickListner(SearchResults searchResults, String value) {
        //Toast.makeText(getContext(), "searchResults : "+searchResults.toString(), Toast.LENGTH_LONG).show();
        //mSearchResults = new SearchResults(searchResults.get_id(), searchResults.getCommunity(), searchResults.getBoardAddr(), searchResults.getTitle(), searchResults.getAuthor(), searchResults.getAuthor(), searchResults.getContent(), searchResults.getUrl(), searchResults.getImages());
        if (mFragmentNavigation != null) {
            mFragmentNavigation.pushFragment(SearchDetailFragment.newInstance(0, searchResults));
        }
    }

    @Override
    public void onImageClickListener(String myUrlStr) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_image_dialog);

        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        final PhotoView photoView = (PhotoView) dialog.findViewById(R.id.photoView_searchresult);
        final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressbar_photoView);
        progressBar.setVisibility(View.VISIBLE);
        Picasso.with(getContext())
                .load(myUrlStr)
                .into(photoView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                        photoView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        progressBar.setVisibility(View.GONE);
                        dialog.dismiss();
                    }
                });

        dialog.show();
    }

    public String transformCommunity(String data) {

        if (mCommunityHashMap.containsKey(data)) {
            return mCommunityHashMap.get(data);
        } else {
            return data;
        }
    }

    public String transformBoardAddr(String data) {
        return data;
    }

    public String transformTitle(String data, String content) {
        if (content.length() > 30) {
            return content.substring(0, 30) + "...";
        } else {
            return content;
        }
    }

    public String transformUrl(String boardAddr, String community) {
        if (community.contains("facebook")) {
            return "https://www.facebook.com/" + boardAddr;
        } else if (community.contains("everytime")) {
            return "https://everytime.kr" + boardAddr;
        } else {
            return "없음";
        }
    }

    public String transformCreatedTime(String time) {
        String result;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(time);
            Log.d(TAG, dateFormat.format(convertedDate));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //포스트된 날짜 datetime으로 변경
        Calendar postDay = Calendar.getInstance();
        postDay.setTime(convertedDate);
        int postDayYear = postDay.get(Calendar.YEAR);
        int postDayMonth = postDay.get(Calendar.MONTH) + 1;
        int postDayDate = postDay.get(Calendar.DATE);
        int postDayAM_PM_temp = postDay.get(Calendar.AM_PM);
        String postDayAM_PM;
        if (postDayAM_PM_temp == 0) {
            postDayAM_PM = "오전";
        } else {
            postDayAM_PM = "오후";
        }
        int postDayHour = postDay.get(Calendar.HOUR);
        if (postDayHour == 0) {
            postDayHour = 12;
        }
        int postDayMinute_temp = postDay.get(Calendar.MINUTE);
        String postDayMinute;
        if (postDayMinute_temp < 10) {
            postDayMinute = "0" + postDayMinute_temp;
        } else {
            postDayMinute = postDayMinute_temp + "";
        }
        //오늘 날짜
        Calendar today = Calendar.getInstance();
        int todayYear = today.get(Calendar.YEAR);
        int todayMonth = today.get(Calendar.MONTH) + 1;
        int todayDate = today.get(Calendar.DATE);


        if (!((postDayYear == todayYear) && (postDayMonth == todayMonth) && (postDayDate == todayDate))) {
            // 오늘이 아닌 경우
            if (postDayYear == todayYear) {
                // 같은 년도일 경우 년 생략
                result = postDayMonth + "월 " + postDayDate + "일 " + postDayAM_PM + " " + postDayHour + ":" + postDayMinute;
            } else {
                result = postDayYear + "년 " + postDayMonth + "월 " + postDayDate + "일 " + postDayAM_PM + " " + postDayHour + ":" + postDayMinute;
            }
        } else {
            // 오늘인 경우
            result = "오늘 " + postDayAM_PM + " " + postDayHour + ":" + postDayMinute;
        }
        return result;
    }

    public String transformfilterTime(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(time);
            Log.d(TAG, dateFormat.format(convertedDate));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //포스트된 날짜 datetime으로 변경
        Calendar day = Calendar.getInstance();
        day.setTime(convertedDate);
        int dayYear = day.get(Calendar.YEAR);
        int dayMonth = day.get(Calendar.MONTH) + 1;
        int dayDate = day.get(Calendar.DATE);

        return dayYear + "년 " + dayMonth + "월 " + dayDate + "일";
    }

   /* public void getSettingFilter() {
        mSecondWord = includedKeywordTxt.getText().toString().trim();

        List<String> communityList = new ArrayList<>();

        Set<Map.Entry<String, Boolean>> set = mCommunityCheckHashMap.entrySet();
        Iterator<Map.Entry<String, Boolean>> it = set.iterator();

        while (it.hasNext()) {
            Map.Entry<String, Boolean> e = (Map.Entry<String, Boolean>) it.next();
            if (e.getValue()) {
                communityList.add(e.getKey());
            }
        }

        mArticleApiService.getArticles(mKeyword, Prefs.getString("userToken", null), communityList, mStartDate, mEndDate, mSecondWord)
        .enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                if (response.body() != null) {
                    List<ArticleMessage> articleMessages = response.body().getArticleMessage();
                    if (articleMessages != null) {
                        Log.d(TAG, "Article info getDataFromServer : success");
                        addData(articleMessages);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Article info getDataFromServer : onResponse : fail : list null");
                    }
                } else {
                    Log.d(TAG, "Article info getDataFromServer : onResponse : fail");
                }
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {
                Log.d(TAG, "Article info getDataFromServer : fail" + t.getMessage());
            }
        });
    }*/

    public void initFilter() {
        String uid = FirebaseAuth.getInstance().getUid();

        if (Prefs.getString(uid + "_userSetting", null) == null) {
            userInitSetting();
        }
        getCommunityList();

        Gson gson = new Gson();
        String json = Prefs.getString(uid + "_userSetting", null);
        mUserSetting = gson.fromJson(json, UserSetting.class);

        mSecondWord = mUserSetting.getSecondWord();
        mStartDate = mUserSetting.getStartDate();
        mEndDate = mUserSetting.getEndDate();
        mCommunityCheckHashMap = mUserSetting.getCommunityHashMap();

        mCommunityAdapter = new FilterCommunityListAdapter(getContext(), R.layout.layout_filter_community_listitem, mCommunityList, mCommunityCheckHashMap);
        mCommunityAdapter.setCheckBoxListner(this);
        communityListView.setAdapter(mCommunityAdapter);
        SearchFragment.setListViewHeightBasedOnChildren(communityListView);

        //뷰 세팅
        includedKeywordTxt.setText(mSecondWord);
        startDateTxt.setText(transformfilterTime(mStartDate));
        endDateTxt.setText(transformfilterTime(mEndDate));
    }

    @Override
    public void onCheckboxClickListner(String value, boolean isChecked) {
        //Toast.makeText(getContext(), value + " 클릭 " + isChecked, Toast.LENGTH_LONG).show();
        mCommunityCheckHashMap.put(value, isChecked);
    }

    public String loadJSONFromAsset(String mode) {
        String json = null;
        try {
            InputStream is;
            if (mode.equals("community_id")) {
                is = getActivity().getAssets().open("community_id.json");
            } else {
                is = getActivity().getAssets().open("university_communitylist.json");
            }
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

    public void getCommunityList() {
        Gson gson = new Gson();
        String json = Prefs.getString("userInfo", "");
        LoginInfo loginInfo = gson.fromJson(json, LoginInfo.class);
        mCommunityList = new ArrayList<Community>();
        try {

            //원래 소스
           /* JSONObject obj = new JSONObject(loadJSONFromAsset("community_list"));
            String university = loginInfo.getUniversity();
            if(university.contains("경희대학교")) {
                university = "경희대학교";
            } else if(university.contains("세종대학교")){
                university = "세종대학교";
            }

            JSONArray content = obj.getJSONArray(university);
            for (int i = 0; i < content.length(); i++) {
                JSONObject communityInfo = content.getJSONObject(i);
                String id = communityInfo.getString("id");
                String name = communityInfo.getString("name");
                mCommunityHashMap.put(id, name);
                mCommunityList.add(new Community(id,name));
            }*/

            //테스트 소스 - 경희, 세종, 한성대 커뮤니티 일단 다 집어넣음
            JSONObject obj = new JSONObject(loadJSONFromAsset("community_list"));
            String university = "경희대학교";
            JSONArray content = obj.getJSONArray(university);
            for (int i = 0; i < content.length(); i++) {
                JSONObject communityInfo = content.getJSONObject(i);
                String id = communityInfo.getString("id");
                String name = communityInfo.getString("name");
                mCommunityHashMap.put(id, name);
                mCommunityList.add(new Community(id, name));
            }

           /* university = "세종대학교";
            content = obj.getJSONArray(university);
            for (int i = 0; i < content.length(); i++) {
                JSONObject communityInfo = content.getJSONObject(i);
                String id = communityInfo.getString("id");
                String name = communityInfo.getString("name");
                mCommunityHashMap.put(id, name);
                mCommunityList.add(new Community(id, name));
            }

            university = "한성대학교";
            content = obj.getJSONArray(university);
            for (int i = 0; i < content.length(); i++) {
                JSONObject communityInfo = content.getJSONObject(i);
                String id = communityInfo.getString("id");
                String name = communityInfo.getString("name");
                mCommunityHashMap.put(id, name);
                mCommunityList.add(new Community(id, name));
            }*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public long setDatePicker(int mode) {
        Calendar minCal = Calendar.getInstance();
        Calendar maxCal = Calendar.getInstance();
        Date day = new Date();

        if (mode == 0) {
            // 선택할 수 있는 최소날짜
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                day = dateFormat.parse("2018-01-01T00:00:00");
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            minCal.setTime(day);
            long minDate = minCal.getTime().getTime();
            return minDate;
        } else {
            // 선택할 수 있는 최대날짜
            maxCal.setTime(day);
            long maxDate = maxCal.getTime().getTime();
            return maxDate;
        }
    }

    public boolean checkDateValid(String start, String end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date convertedStartDate = new Date();
        Date convertedEndDate = new Date();
        try {
            convertedStartDate = dateFormat.parse(start);
            convertedEndDate = dateFormat.parse(end);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Calendar day = Calendar.getInstance();
        day.setTime(convertedStartDate);
        long startMilliseconds = day.getTime().getTime();
        day.setTime(convertedEndDate);
        long endMilliseconds = day.getTime().getTime();

        if (endMilliseconds - startMilliseconds >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public String makeSimpleDateFormat(int year, int month, int dayOfMonth) {
        String tempMonth, tempDay;

        if (month < 10) {
            tempMonth = "0" + month;
        } else {
            tempMonth = String.valueOf(month);
        }

        if (dayOfMonth < 10) {
            tempDay = "0" + dayOfMonth;
        } else {
            tempDay = String.valueOf(dayOfMonth);
        }

        String result = year + "-" + tempMonth + "-" + tempDay + "T00:00:00";
        Log.d(TAG, "변환된 날짜 : " + result);
        return result;
    }

    public void userInitSetting() {
        Gson gson = new Gson();
        String json = Prefs.getString("userInfo", "");
        LoginInfo loginInfo = gson.fromJson(json, LoginInfo.class);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        HashMap<String, Boolean> communityHashMap = new HashMap<>();

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset("community_list"));
            //String university = loginInfo.getUniversity();
            String university = "경희대학교";
            JSONArray content = obj.getJSONArray(university);
            for (int i = 0; i < content.length(); i++) {
                JSONObject communityInfo = content.getJSONObject(i);
                String id = communityInfo.getString("id");
                communityHashMap.put(id, true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        UserSetting userSetting = new UserSetting("", makeSimpleDateFormat(2018, 1, 1), makeSimpleDateFormat(year, month, day), communityHashMap);
        String uid = FirebaseAuth.getInstance().getUid();
        json = gson.toJson(userSetting);
        Log.d(TAG, "Json : " + json);
        Prefs.putString(uid + "_userSetting", json);
    }

    public void saveFilter() {
        Gson gson = new Gson();
        mSecondWord = includedKeywordTxt.getText().toString().trim();
        UserSetting userSetting = new UserSetting(mSecondWord, mStartDate, mEndDate, mCommunityCheckHashMap);
        String uid = FirebaseAuth.getInstance().getUid();
        String json = gson.toJson(userSetting);
        Log.d(TAG, "Json : " + json);
        Prefs.putString(uid + "_userSetting", json);
    }
}
