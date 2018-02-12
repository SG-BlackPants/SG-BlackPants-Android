package smilegate.blackpants.univscanner.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.data.model.SearchResults;
import smilegate.blackpants.univscanner.utils.BaseFragment;
import smilegate.blackpants.univscanner.utils.SearchResultImageListAdapter;

import static smilegate.blackpants.univscanner.MainActivity.mNavController;

/**
 * Created by Semin on 2018-02-11.
 */

public class SearchDetailFragment extends BaseFragment {
    private static final String TAG = "SearchDetailFragment";
    private View mView;
    private SearchResults mSearchResults;
    private List<String> mImageList;
    private SearchResultImageListAdapter mAdapter;

    @BindView(R.id.text_post_name)
    TextView postName;
    @BindView(R.id.text_post_time)
    TextView postTime;
    @BindView(R.id.text_post_content)
    TextView postContent;
    @BindView(R.id.text_post_source)
    TextView postSource;
    @BindView(R.id.text_post_author)
    TextView postAuthor;
    @BindView(R.id.text_post_url)
    TextView postUrl;
    @BindView(R.id.list_postimage)
    ListView postImageListView;
    @BindView(R.id.btn_searchresult_detail_back)
    ImageButton searchResultDetailBackBtn;
    @BindView(R.id.LinLayout_searchImage)
    LinearLayout searchImageLayout;

    @OnClick(R.id.btn_searchresult_detail_back)
    public void searchResultBack(ImageButton imageButton) {
        if (!mNavController.popFragment()) {
            getActivity().onBackPressed();
        }
    }

    public static SearchDetailFragment newInstance(int instance, SearchResults searchResults) {
        Bundle args = new Bundle();
        Log.d(TAG, "searchResults : "+searchResults.toString());
        args.putInt(ARGS_INSTANCE, instance);
        args.putParcelable("SearchResults", searchResults);
        SearchDetailFragment fragment = new SearchDetailFragment();
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
            mView = inflater.inflate(R.layout.fragment_searchresult_detail, container, false);
            ButterKnife.bind(this, mView);
            Bundle bundle = this.getArguments();
            if(bundle!=null){
                mSearchResults = bundle.getParcelable("SearchResults");
            }

            initContent();
        }
        return mView;
    }

    public void initContent() {
        postName.setText(mSearchResults.getTitle());
        postTime.setText(mSearchResults.getCreatedDate());

        postContent.setText(mSearchResults.getContent());

        postSource.setText(mSearchResults.getCommunity() + " " + mSearchResults.getBoardAddr());
        postAuthor.setText(mSearchResults.getAuthor());
        postUrl.setText(mSearchResults.getUrl());

        if (mSearchResults.getImages().size() < 1) {
            searchImageLayout.setVisibility(View.GONE);
            Log.d(TAG,"initContent() : size <1");
        } else {
            mImageList = mSearchResults.getImages();
            mAdapter = new SearchResultImageListAdapter(getContext(), R.layout.layout_searchresult_detail_listitem, mImageList);
            postImageListView.setAdapter(mAdapter);
            SearchFragment.setListViewHeightBasedOnChildren(postImageListView);
            Log.d(TAG,"initContent() : size >= 1");
        }
    }
}
