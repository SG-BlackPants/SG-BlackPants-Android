package smilegate.blackpants.univscanner.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import smilegate.blackpants.univscanner.R;

/**
 * Created by user on 2018-01-22.
 */

public class SearchFragment extends Fragment{
    private static final String TAG = "SearchFragment";
    private static final int ACTIVITY_NUM = 0;
    private View view;

    @BindView(R.id.reLayout_search)
    RelativeLayout includeLayoutSearch;

    @OnClick(R.id.reLayout_search)
    public void search(RelativeLayout layout) {
        Toast.makeText(getActivity(), "검색클릭!",
                Toast.LENGTH_SHORT).show();
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, null);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



}
