package smilegate.blackpants.univscanner.search;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

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
import smilegate.blackpants.univscanner.data.model.Article;
import smilegate.blackpants.univscanner.data.model.ArticleMessage;
import smilegate.blackpants.univscanner.data.model.SearchResults;
import smilegate.blackpants.univscanner.data.remote.ApiUtils;
import smilegate.blackpants.univscanner.data.remote.ArticleApiService;
import smilegate.blackpants.univscanner.utils.BaseFragment;
import smilegate.blackpants.univscanner.utils.SearchResultFeedAdapter;

import static smilegate.blackpants.univscanner.MainActivity.mNavController;

/**
 * Created by user on 2018-01-25.
 */

public class SearchResultFragment extends BaseFragment implements SearchResultFeedAdapter.ContentDetailClickListener {
    private static final String TAG = "SearchResultFragment";
    private View mView;

    private RecyclerView.LayoutManager mLayoutManager;
    private SearchResultFeedAdapter mAdapter;
    private List<SearchResults> mSearchResultsList;
    private SearchResults mSearchResults;
    private ArticleApiService mArticleApiService;
    private String mKeyword;

    @BindView(R.id.btn_searchresult_back)
    ImageButton searchResultBackBtn;

    @OnClick(R.id.btn_searchresult_back)
    public void searchResultBack(ImageButton imageButton) {
        if (!mNavController.popFragment()) {
            getActivity().onBackPressed();
        }
    }

    @BindView(R.id.list_searchresult)
    RecyclerView searchResultRecyclerView;

    @BindView(R.id.drawerLayout_searchresult)
    DrawerLayout drawerLayout;

    /*@BindView(R.id.drawer_filter)
    View drawerView;
*/
    @BindView(R.id.btn_register_keyword)
    ImageButton registerKeywordBtn;

    @BindView(R.id.nvView)
    View drawerView;

    @BindView(R.id.btn_filter)
    Button filterBtn;

    @OnClick(R.id.btn_filter)
    public void filterClick(Button button) {
        drawerLayout.openDrawer(drawerView);
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
                                Toast.makeText(getContext(), "등록", Toast.LENGTH_LONG).show();
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
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                mKeyword = bundle.getString("keyword");
                Log.d(TAG, "키워드 : " + mKeyword);
            }
            mArticleApiService = ApiUtils.getArticleApiService();
            mSearchResultsList = new ArrayList<>();
            mAdapter = new SearchResultFeedAdapter(getContext(), mSearchResultsList);
            mLayoutManager = new LinearLayoutManager(getContext());
            searchResultRecyclerView.setLayoutManager(mLayoutManager);
            searchResultRecyclerView.setItemAnimator(new DefaultItemAnimator());
            searchResultRecyclerView.setAdapter(mAdapter);
            mAdapter.setContentDetailClickListner(this);
            getDataFromServer(mKeyword);

            //addData();


        }
        return mView;
    }

    public void getDataFromServer(String keyword) {
        mArticleApiService.getArticles(keyword, Prefs.getString("userToken",null)).enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                if (response.body() != null) {
                    Log.d(TAG, "Article info getDataFromServer : success");
                    List<ArticleMessage> articleMessages = response.body().getArticleMessage();
                    addData(articleMessages);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Article info getDataFromServer : onResponse : fail");
                }
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {
                Log.d(TAG, "Article info getDataFromServer : fail");
            }
        });
    }

    public void addData(List<ArticleMessage> articleMessages) {
        SearchResults searchResults;
        String id, title, createdDate, content, community, boardAddr, author, url;
        List<String> images;

        for (int i = 0; i < articleMessages.size(); i++) {
            id = articleMessages.get(i).getId();
            content = articleMessages.get(i).getSource().getContent();
            title = transformTitle(articleMessages.get(i).getSource().getTitle(), content);
            createdDate = transformCreatedTime(articleMessages.get(i).getSource().getCreatedDate());
            community = transformCommunity(articleMessages.get(i).getSource().getCommunity());
            boardAddr = transformBoardAddr(articleMessages.get(i).getSource().getBoardAddr());
            author = articleMessages.get(i).getSource().getAuthor();
            url = transformUrl(boardAddr, community);
            images = articleMessages.get(i).getSource().getImages();
            //Log.d(TAG, "이미지 : " + articleMessages.get(i).getSource().getImages().get(0));
            //Log.d(TAG, "이미지 갯수: " + images.toString());
            searchResults = new SearchResults(id, title, createdDate, content, community, boardAddr, author, url, images);
            mSearchResultsList.add(i, searchResults);
        }

        /*SearchResults searchResults = new SearchResults("0", "수강신청 날짜가 언제인가요?", "3시간 전", "수강신청 12일부터인데\n학년이 월요일날 하잖아요\n그러면 앞서서 4학년3학년들이 좋은과목 인원 수 다채우면 1,2학년들은 그냥 다른과목을 선택해야하나요? 아니면 학년별로 수강신청 제한이 있나요?"
                , "페이스북", "대나무숲", "익명", "www.facebook.com", new ArrayList<String>() {
            {
                add("https://homepages.cae.wisc.edu/~ece533/images/fruits.png");
            }
        });
        mSearchResultsList.add(0, searchResults);
        searchResults = new SearchResults("1", "수강신청 날짜가 언제인가요?", "3시간 전", "수강신청 12일부터인데\n학년이 월요일날 하잖아요\n그러면 앞서서 4학년3학년들이 좋은과목 인원 수 다채우면 1,2학년들은 그냥 다른과목을 선택해야하나요? 아니면 학년별로 수강신청 제한이 있나요?.. 자세히 보기"
                , "페이스북", "대나무숲", "익명", "www.facebook.com", new ArrayList<String>());
        mSearchResultsList.add(1, searchResults);
        searchResults = new SearchResults("2", "수강신청 날짜가 언제인가요?", "3시간 전", "수강신청 12일부터인데\n학년이 월요일날 하잖아요\n그러면 앞서서 4학년3학년들이 좋은과목 인원 수 다채우면 1,2학년들은 그냥 다른과목을 선택해야하나요? 아니면 학년별로 수강신청 제한이 있나요?.. 자세히 보기"
                , "페이스북", "대나무숲", "익명", "www.facebook.com", new ArrayList<String>() {
            {
                add("https://homepages.cae.wisc.edu/~ece533/images/arctichare.png");
                add("https://homepages.cae.wisc.edu/~ece533/images/fruits.png");
                add("https://homepages.cae.wisc.edu/~ece533/images/arctichare.png");
            }
        });
        mSearchResultsList.add(2, searchResults);
        searchResults = new SearchResults("3", "수강신청 날짜가 언제인가요?", "3시간 전", "수강신청 12일부터인데\n학년이 월요일날 하잖아요\n그러면 앞서서 4학년3학년들이 좋은과목 인원 수 다채우면 1,2학년들은 그냥 다른과목을 선택해야하나요? 아니면 학년별로 수강신청 제한이 있나요?"
                , "페이스북", "대나무숲", "익명", "www.facebook.com", new ArrayList<String>());
        mSearchResultsList.add(3, searchResults);*/

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

    public String transformCommunity(String data) {
        if (data.contains("facebook")) {
            return "페이스북";
        } else if (data.contains("everytime")) {
            return "애브리타임";
        } else {
            return "기타 커뮤니티";
        }
    }

    public String transformBoardAddr(String data) {
        return data;
    }

    public String transformTitle(String data, String content) {
        if (content.length() > 20) {
            return content.substring(0, 20) + "...";
        } else {
            return content;
        }
    }

    public String transformUrl(String boardAddr, String community) {
        if (community.equals("페이스북")) {
            return "http://www.facebook.com/" + boardAddr;
        } else if (community.equals("애브리타임")) {
            return "없음";
        } else {
            return "없음";
        }
    }

    public String transformCreatedTime(String data) {
        return data;
    }
}
