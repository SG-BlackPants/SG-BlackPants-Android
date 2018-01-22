package smilegate.blackpants.univscanner.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import smilegate.blackpants.univscanner.R;

/**
 * Created by user on 2018-01-22.
 */

public class SearchFragment extends Fragment{
    private static final String TAG = "SearchFragment";
    private static final int ACTIVITY_NUM = 0;

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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }


}
