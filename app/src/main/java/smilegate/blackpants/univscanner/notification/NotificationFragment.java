package smilegate.blackpants.univscanner.notification;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pixplicity.easyprefs.library.Prefs;
import com.roughike.bottombar.BottomBarTab;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.data.model.MyBottomBarTab;
import smilegate.blackpants.univscanner.data.model.Notification;
import smilegate.blackpants.univscanner.data.model.NotificationMessage;
import smilegate.blackpants.univscanner.data.remote.ApiUtils;
import smilegate.blackpants.univscanner.data.remote.RedisApiService;
import smilegate.blackpants.univscanner.utils.BaseFragment;
import smilegate.blackpants.univscanner.utils.NotificationListAdapter;

/**
 * Created by user on 2018-01-22.
 */

public class NotificationFragment extends BaseFragment {
    private static final String TAG = "NotificationFragment";
    private View view;
    private BottomBarTab mBottomBarTab;
    private List<NotificationMessage> mNotificationList;
    private NotificationListAdapter mAdapter;
    private RedisApiService mRedisApiService;

    @BindView(R.id.list_notification)
    ListView notificationListView;

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
            mRedisApiService = ApiUtils.getRedisApiService();
            initNotificationList();
            /*MyBottomBarTab myBottomBarTab = (MyBottomBarTab) getArguments().getSerializable("badge");
            mBottomBarTab = myBottomBarTab.getBottomBarTab();
            mBottomBarTab.removeBadge();*/
            setBadge();
        }
        return view;
    }

    public void initNotificationList() {
        mNotificationList = new ArrayList<>();
      /*  mRegisteredKeywordList.add("비트코인");
        mRegisteredKeywordList.add("꿀교양");*/
        mRedisApiService.getPushHistory(Prefs.getString("userToken", null)).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if (response.body() != null) {
                    Log.d(TAG, "알림 히스토리 서버통신 성공");
                    mNotificationList = response.body().getMessages();
                    if (mNotificationList.size() > 0) {
                        mAdapter = new NotificationListAdapter(getContext(), R.layout.layout_notification_listitem, mNotificationList);
                        notificationListView.setAdapter(mAdapter);
                    } else {
                        Log.d(TAG, "현재까지는 알림 히스토리가 없음");
                    }
                } else {
                    Log.d(TAG, "알림 히스토리 서버통신 실패 : onResponse : " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                Log.d(TAG, "알림 히스토리 서버통신 실패 : onFailure : " + t.getMessage());
            }
        });
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

        Prefs.putInt("badgeCount", 0);

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
