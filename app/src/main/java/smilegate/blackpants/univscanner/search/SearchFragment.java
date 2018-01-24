package smilegate.blackpants.univscanner.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.utils.BaseFragment;

/**
 * Created by user on 2018-01-22.
 */

public class SearchFragment extends BaseFragment {
    private static final String TAG = "SearchFragment";
    private View view;

    @BindView(R.id.top_searchbar)
    RelativeLayout includeLayoutSearch;

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

}
