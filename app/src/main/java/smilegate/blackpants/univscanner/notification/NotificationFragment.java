package smilegate.blackpants.univscanner.notification;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixplicity.easyprefs.library.Prefs;
import com.roughike.bottombar.BottomBarTab;

import java.util.List;

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
        //args.putSerializable("badge", myBottomBarTab);
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
            /*MyBottomBarTab myBottomBarTab = (MyBottomBarTab) getArguments().getSerializable("badge");
            mBottomBarTab = myBottomBarTab.getBottomBarTab();
            mBottomBarTab.removeBadge();*/
            setBadge();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.bind(this, view).unbind();
    }

    public void setBadge() {
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");

        intent.putExtra("badge_count", 0);
        intent.putExtra("badge_count_package_name", getContext().getPackageName());
        intent.putExtra("badge_count_class_name", getLauncherClassName(getContext()));

        Prefs.putInt("badgeCount",0);

        getContext().sendBroadcast(intent);
    }

    public String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }

}
