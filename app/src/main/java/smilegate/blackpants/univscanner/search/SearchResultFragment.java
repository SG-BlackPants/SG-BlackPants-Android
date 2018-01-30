package smilegate.blackpants.univscanner.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    ImageButton searchViewBackBtn;
    @OnClick(R.id.btn_searchresult_back)
    public void searchViewBack(ImageButton imageButton) {
        if (!mNavController.popFragment()) {
            getActivity().onBackPressed();
        }
    }

    @BindView(R.id.list_searchresult)
    RecyclerView searchResultRecyclerView;

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
        SearchResults searchResults = new SearchResults("post1", "http://androidprime.info/android/images/Tutorial5.jpg" );
        mSearchResultsList.add(0, searchResults);
        searchResults = new SearchResults("post2", "http://androidprime.info/android/images/Tutorial6.jpg" );
        mSearchResultsList.add(1, searchResults);
        searchResults = new SearchResults("post3", "http://androidprime.info/android/images/Tutorial7.jpg" );
        mSearchResultsList.add(2, searchResults);
        searchResults = new SearchResults("post4", "http://androidprime.info/android/images/Tutorial8.jpg" );
        mSearchResultsList.add(3, searchResults);
        searchResults = new SearchResults("post5", "http://androidprime.info/android/images/Tutorial5.jpg" );
        mSearchResultsList.add(4, searchResults);
        searchResults = new SearchResults("post6", "http://androidprime.info/android/images/Tutorial6.jpg" );
        mSearchResultsList.add(5, searchResults);
        searchResults = new SearchResults("post7", "http://androidprime.info/android/images/Tutorial7.jpg" );
        mSearchResultsList.add(6, searchResults);
        searchResults = new SearchResults("post8", "http://androidprime.info/android/images/Tutorial8.jpg" );
        mSearchResultsList.add(7, searchResults);
        searchResults = new SearchResults("post9", "http://androidprime.info/android/images/Tutorial5.jpg" );
        mSearchResultsList.add(8, searchResults);
    }


}