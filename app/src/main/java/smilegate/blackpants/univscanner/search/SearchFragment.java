package smilegate.blackpants.univscanner.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.aotasoft.taggroup.TagGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.data.model.KeywordRank;
import smilegate.blackpants.univscanner.utils.BaseFragment;
import smilegate.blackpants.univscanner.utils.KeywordRankListAdapter;

/**
 * Created by user on 2018-01-22.
 */

public class SearchFragment extends BaseFragment {
    private static final String TAG = "SearchFragment";
    private View view;
    private List<KeywordRank> mKeywordRankList;
    private KeywordRankListAdapter mAdapter;

    @BindView(R.id.top_searchbar)
    RelativeLayout includeLayoutSearch;

    @BindView(R.id.list_keywordRank)
    ListView keywordRankListView;

    @OnClick(R.id.top_searchbar)
    public void search(RelativeLayout layout) {
        if (mFragmentNavigation != null) {
            mFragmentNavigation.pushFragment(SearchViewFragment.newInstance(0));
        }
    }

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
            view = inflater.inflate(R.layout.fragment_search, container, false);
            ButterKnife.bind(this, view);
            TagGroup mTagGroup = (TagGroup) view.findViewById(R.id.tag_group);
            mTagGroup.setGravity(TagGroup.TagGravity.MIDDLE);
            mTagGroup.setTags(new String[]{"도레미파솔", "김치찌개", "비트코인","수강신청 날짜","멀머날어ㅓ아머리너ㅣㅏ렁니러ㅣ","꿀강","수강편람"});
            initkeywordRankList();
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

    public void initkeywordRankList() {
        mKeywordRankList = new ArrayList<>();
        mKeywordRankList.add(new KeywordRank(1,"비트코인"));
        mKeywordRankList.add(new KeywordRank(2,"꿀교양"));
        mKeywordRankList.add(new KeywordRank(3,"수강편람"));
        mKeywordRankList.add(new KeywordRank(4,"국가장학금 소득분위"));
        mKeywordRankList.add(new KeywordRank(5,"강의교재"));
        mKeywordRankList.add(new KeywordRank(6,"평창올림픽"));
        mKeywordRankList.add(new KeywordRank(7,"지드래곤 대학원"));
        mKeywordRankList.add(new KeywordRank(8,"평창올림픽"));
        mKeywordRankList.add(new KeywordRank(9,"개강일"));
        mKeywordRankList.add(new KeywordRank(10,"수강신청"));
        mAdapter = new KeywordRankListAdapter(getContext(), R.layout.layout_rank_listitem, mKeywordRankList);
        setListViewHeightBasedOnChildren(keywordRankListView,mKeywordRankList.size());
        keywordRankListView.setAdapter(mAdapter);

    }

    public static void setListViewHeightBasedOnChildren(ListView listView, int count) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        if (count > 0) {
            View listItem = listAdapter.getView(0, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight = listItem.getMeasuredHeight() * count;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
