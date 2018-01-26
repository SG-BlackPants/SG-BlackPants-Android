package smilegate.blackpants.univscanner.notification;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roughike.bottombar.BottomBarTab;

import butterknife.ButterKnife;
import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.data.model.MyBottomBarTab;
import smilegate.blackpants.univscanner.utils.BaseFragment;

/**
 * Created by user on 2018-01-22.
 */

public class NotificationFragment extends BaseFragment {
    private static final String TAG = "NotificationFragment";
    private View view;
    private BottomBarTab mBottomBarTab;

    public static NotificationFragment newInstance(int instance, MyBottomBarTab myBottomBarTab) {
        Bundle args = new Bundle();
        args.putSerializable("badge", myBottomBarTab);
        args.putInt(ARGS_INSTANCE, instance);
        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(args);
        //this.mBottomBarTab = mBottomBarTab;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_notification, container, false);
            //btn = cachedView.findViewById(R.id.button);
            ButterKnife.bind(this, view);
            MyBottomBarTab myBottomBarTab = (MyBottomBarTab) getArguments().getSerializable("badge");
            mBottomBarTab = myBottomBarTab.getBottomBarTab();
            mBottomBarTab.removeBadge();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.bind(this, view).unbind();
    }
}
