package smilegate.blackpants.univscanner.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import smilegate.blackpants.univscanner.R;

/**
 * Created by user on 2018-01-23.
 */

public class SearchViewFragment  extends Fragment {
    private static final String TAG = "SearchViewFragment";
    private static final int ACTIVITY_NUM = 0;
    private View view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_view, null);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
