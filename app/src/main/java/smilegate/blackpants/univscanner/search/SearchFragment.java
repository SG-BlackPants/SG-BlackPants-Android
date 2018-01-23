package smilegate.blackpants.univscanner.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import smilegate.blackpants.univscanner.R;

/**
 * Created by user on 2018-01-22.
 */

public class SearchFragment extends Fragment{
    private static final String TAG = "SearchFragment";
    private static final int FRAGMENT_NUM = 0;
    private View view;

    @BindView(R.id.reLayout_search)
    RelativeLayout includeLayoutSearch;

    @OnClick(R.id.reLayout_search)

    public void search(RelativeLayout layout) {
/*        SearchViewFragment searchViewFragment = new SearchViewFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, searchViewFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/
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
