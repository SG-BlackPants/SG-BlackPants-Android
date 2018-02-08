package smilegate.blackpants.univscanner.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.data.model.SearchResults;
import smilegate.blackpants.univscanner.utils.BaseFragment;
import smilegate.blackpants.univscanner.utils.SearchResultFeedAdapter;

import static smilegate.blackpants.univscanner.MainActivity.mNavController;

/**
 * Created by user on 2018-01-25.
 */

public class SearchResultFragment extends BaseFragment {
    private static final String TAG = "SearchResultFragment";
    private View mView;

    private RecyclerView.LayoutManager mLayoutManager;
    private SearchResultFeedAdapter mAdapter;
    private List<SearchResults> mSearchResultsList;

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
    @BindView(R.id.nvView)
    View drawerView;

    @BindView(R.id.btn_filter)
    Button filterBtn;

    @OnClick(R.id.btn_filter)
    public void filterClick(Button button) {
        drawerLayout.openDrawer(drawerView);
    }

    public static SearchResultFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
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
            mSearchResultsList = new ArrayList<>();
            mAdapter = new SearchResultFeedAdapter(getContext(), mSearchResultsList);
            mLayoutManager = new LinearLayoutManager(getContext());
            searchResultRecyclerView.setLayoutManager(mLayoutManager);
            searchResultRecyclerView.setItemAnimator(new DefaultItemAnimator());
            searchResultRecyclerView.setAdapter(mAdapter);
            addData();
            mAdapter.notifyDataSetChanged();
        }
        return mView;
    }


    public void addData() {
        SearchResults searchResults = new SearchResults("0", "수강신청 날짜가 언제인가요?", "3시간 전", "수강신청 12일부터인데\n학년이 월요일날 하잖아요\n그러면 앞서서 4학년3학년들이 좋은과목 인원 수 다채우면 1,2학년들은 그냥 다른과목을 선택해야하나요? 아니면 학년별로 수강신청 제한이 있나요?"
                , "페이스북", "대나무숲", "익명", "www.facebook.com", new ArrayList<String>() {
            {
                add("https://homepages.cae.wisc.edu/~ece533/images/fruits.png");
            }
        });
        mSearchResultsList.add(0, searchResults);
        searchResults = new SearchResults("1", "수강신청 날짜가 언제인가요?", "3시간 전", "수강신청 12일부터인데\n학년이 월요일날 하잖아요\n그러면 앞서서 4학년3학년들이 좋은과목 인원 수 다채우면 1,2학년들은 그냥 다른과목을 선택해야하나요? 아니면 학년별로 수강신청 제한이 있나요?"
                , "페이스북", "대나무숲", "익명", "www.facebook.com", new ArrayList<String>());
        mSearchResultsList.add(1, searchResults);
        searchResults = new SearchResults("2", "수강신청 날짜가 언제인가요?", "3시간 전", "수강신청 12일부터인데\n학년이 월요일날 하잖아요\n그러면 앞서서 4학년3학년들이 좋은과목 인원 수 다채우면 1,2학년들은 그냥 다른과목을 선택해야하나요? 아니면 학년별로 수강신청 제한이 있나요?"
                , "페이스북", "대나무숲", "익명", "www.facebook.com", new ArrayList<String>() {
            {
                add("https://homepages.cae.wisc.edu/~ece533/images/arctichare.png");
            }
        });
        mSearchResultsList.add(2, searchResults);
        searchResults = new SearchResults("3", "수강신청 날짜가 언제인가요?", "3시간 전", "수강신청 12일부터인데\n학년이 월요일날 하잖아요\n그러면 앞서서 4학년3학년들이 좋은과목 인원 수 다채우면 1,2학년들은 그냥 다른과목을 선택해야하나요? 아니면 학년별로 수강신청 제한이 있나요?"
                , "페이스북", "대나무숲", "익명", "www.facebook.com", new ArrayList<String>());
        mSearchResultsList.add(3, searchResults);

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

}
