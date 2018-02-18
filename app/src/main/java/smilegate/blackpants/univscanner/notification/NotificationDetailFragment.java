package smilegate.blackpants.univscanner.notification;

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
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.data.model.NotificationArticle;
import smilegate.blackpants.univscanner.data.model.Source;
import smilegate.blackpants.univscanner.data.remote.ApiUtils;
import smilegate.blackpants.univscanner.data.remote.ArticleApiService;
import smilegate.blackpants.univscanner.utils.BaseFragment;
import smilegate.blackpants.univscanner.utils.SearchResultImageListAdapter;

import static smilegate.blackpants.univscanner.MainActivity.mNavController;

/**
 * Created by Semin on 2018-02-16.
 */

public class NotificationDetailFragment extends BaseFragment {
    private static final String TAG = "NotifiDetailFragment";
    private View mView;
    private List<String> mImageList;
    private SearchResultImageListAdapter mAdapter;
    private String mCommunityId;
    private String mCommunityName;
    private String mBoardAddr;
    private String mCreatedDate;
    private ArticleApiService mArticleApiService;

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
    @BindView(R.id.scrollView_searchresult)
    ScrollView scrollView;
    @BindView(R.id.progressbar_searchdetail)
    ProgressBar progressBar;
    @BindView(R.id.text_search_result_noInternet)
    TextView noInternetTxt;

    @OnClick(R.id.btn_searchresult_detail_back)
    public void searchResultBack(ImageButton imageButton) {
        if (!mNavController.popFragment()) {
            getActivity().onBackPressed();
        }
    }

    public static NotificationDetailFragment newInstance(int instance, String communityId, String communityName, String boardAddr, String createdDate) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        args.putString("communityId", communityId);
        args.putString("communityName", communityName);
        args.putString("boardAddr", boardAddr);
        args.putString("createdDate", createdDate);
        NotificationDetailFragment fragment = new NotificationDetailFragment();
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
            scrollView.setVisibility(View.GONE);
            noInternetTxt.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                mCommunityId = bundle.getString("communityId");
                mCommunityName = bundle.getString("communityName");
                mBoardAddr = bundle.getString("boardAddr");
                mCreatedDate = bundle.getString("createdDate");
            }
            mBoardAddr = mBoardAddr.replace('/', '-');
            mArticleApiService = ApiUtils.getArticleApiService();
            getNotificationDetailFromServer();
        }
        return mView;
    }

    public void getNotificationDetailFromServer() {
        progressBar.setVisibility(View.VISIBLE);
        mArticleApiService.getNotificationDetail(mCommunityId, mBoardAddr).enqueue(new Callback<NotificationArticle>() {
            @Override
            public void onResponse(Call<NotificationArticle> call, Response<NotificationArticle> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body() != null) {
                    scrollView.setVisibility(View.VISIBLE);
                    Log.d(TAG, "알림 상세내용 서버통신 성공");
                    Source source = response.body().getSource();
                    initContent(source);
                } else {
                    Log.d(TAG, "알림 상세내용 서버통신 실패 : onResponse : " + response.message());
                    noInternetTxt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<NotificationArticle> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                noInternetTxt.setVisibility(View.VISIBLE);
                Log.d(TAG, "알림 상세내용 서버통신 실패 : onFailure : " + t.getMessage());
            }
        });
    }

    public void initContent(Source source) {
        String title = transformTitle(source.getTitle(), source.getContent());
        String url = transformUrl(source.getBoardAddr(), source.getCommunity());

        postName.setText(title);
        postTime.setText(mCreatedDate);
        postContent.setText(source.getContent());
        postSource.setText(mCommunityName);
        postAuthor.setText(source.getAuthor());
        postUrl.setText(url);

        if (source.getImages().size() < 1) {
            searchImageLayout.setVisibility(View.GONE);
            Log.d(TAG, "initContent() : size <1");
        } else {
            mImageList = source.getImages();
            mAdapter = new SearchResultImageListAdapter(getContext(), R.layout.layout_searchresult_detail_listitem, mImageList);
            postImageListView.setAdapter(mAdapter);
            setListViewHeightBasedOnChildren(postImageListView);
            Log.d(TAG, "initContent() : size >= 1");
        }

        Resources res = getResources();
        String mDrawableName = getCommunityLogo(mCommunityName);
        int resID = res.getIdentifier(mDrawableName, "drawable", getActivity().getPackageName());
        Drawable drawable = res.getDrawable(resID);
        postIcon.setImageDrawable(drawable);
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
        if (community.equals("Kyunghee bamboo grove")) {
            return "kyunghee_bamboo";
        } else if (community.equals("경희대학교 - 국제캠 대신 전해드립니다")) {
            return "kyunghee_daeshin";
        } else if (community.equals("애브리타임")) {
            return "everytime";
        } else if (community.equals("세종대학교 대나무숲")) {
            return "sejong_bamboo";
        } else if (community.equals("세종대학교 대신 전해드립니다")) {
            return "sejong_daeshin";
        } else if (community.equals("한성대학교 대나무숲")) {
            return "hansung_daeshin";
        } else {
            return "ic_hashtag";
        }
    }
}
