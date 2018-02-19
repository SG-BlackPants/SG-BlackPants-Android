package smilegate.blackpants.univscanner.search;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.data.model.SearchResults;
import smilegate.blackpants.univscanner.utils.BaseFragment;
import smilegate.blackpants.univscanner.utils.SearchResultImageListAdapter;

import static smilegate.blackpants.univscanner.MainActivity.mNavController;

/**
 * Created by Semin on 2018-02-11.
 */

public class SearchDetailFragment extends BaseFragment implements SearchResultImageListAdapter.ImageClickListener {
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
    @BindView(R.id.img_post_icon)
    CircleImageView postIcon;

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
        postSource.setText(mSearchResults.getCommunity());
        postAuthor.setText(mSearchResults.getAuthor());
        postUrl.setText(mSearchResults.getUrl());

        if (mSearchResults.getImages().size() < 1) {
            searchImageLayout.setVisibility(View.GONE);
            Log.d(TAG,"initContent() : size <1");
        } else {
            mImageList = mSearchResults.getImages();
            mAdapter = new SearchResultImageListAdapter(getContext(), R.layout.layout_searchresult_detail_listitem, mImageList);
            mAdapter.setImageClickListner(this);
            postImageListView.setAdapter(mAdapter);
            setListViewHeightBasedOnChildren(postImageListView);
            Log.d(TAG,"initContent() : size >= 1");
        }

        Resources res = getResources();
        String mDrawableName = getCommunityLogo(mSearchResults.getCommunity());
        int resID = res.getIdentifier(mDrawableName , "drawable", getActivity().getPackageName());
        Drawable drawable = res.getDrawable(resID );
        postIcon.setImageDrawable(drawable);
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

    public String getCommunityLogo(String community) {
        if(community.equals("Kyunghee bamboo grove")) {
            return "kyunghee_bamboo";
        } else if(community.equals("경희대학교 - 국제캠 대신 전해드립니다")) {
            return "kyunghee_daeshin";
        } else if(community.equals("애브리타임")) {
            return "everytime";
        } else if(community.equals("세종대학교 대나무숲")) {
            return "sejong_bamboo";
        } else if(community.equals("세종대학교 대신 전해드립니다")) {
            return "sejong_daeshin";
        } else if(community.equals("한성대학교 대나무숲")) {
            return "hansung_daeshin";
        } else {
            return "ic_hashtag";
        }
    }

    @Override
    public void onImageClickListener(String url) {
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
                .load(url)
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
}


