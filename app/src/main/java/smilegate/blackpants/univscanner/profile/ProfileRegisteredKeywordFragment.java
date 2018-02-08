package smilegate.blackpants.univscanner.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.utils.BaseFragment;

/**
 * Created by user on 2018-02-08.
 */

public class ProfileRegisteredKeywordFragment extends BaseFragment {
    private static final String TAG = "ProfileRegisteredKeywordFragment";
    private View view;

    public static ProfileRegisteredKeywordFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        ProfileRegisteredKeywordFragment fragment = new ProfileRegisteredKeywordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_notification, container, false);
            ButterKnife.bind(this, view);

        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.bind(this, view).unbind();
    }
}
