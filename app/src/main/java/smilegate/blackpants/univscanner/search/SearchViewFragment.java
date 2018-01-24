package smilegate.blackpants.univscanner.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.utils.BaseFragment;

/**
 * Created by user on 2018-01-23.
 */

public class SearchViewFragment extends BaseFragment {
    private static final String TAG = "SearchViewFragment";
    private View view;

    public static SearchViewFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        SearchViewFragment fragment = new SearchViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_search_view, container, false);
            ButterKnife.bind(this, view);
        }
        return view;
    }
}
