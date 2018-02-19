package smilegate.blackpants.univscanner.notification;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;
import com.roughike.bottombar.BottomBarTab;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.leolin.shortcutbadger.ShortcutBadger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smilegate.blackpants.univscanner.MainActivity;
import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.data.model.Notification;
import smilegate.blackpants.univscanner.data.model.NotificationDetail;
import smilegate.blackpants.univscanner.data.model.NotificationMessage;
import smilegate.blackpants.univscanner.data.remote.ApiUtils;
import smilegate.blackpants.univscanner.data.remote.RedisApiService;
import smilegate.blackpants.univscanner.utils.BaseFragment;
import smilegate.blackpants.univscanner.utils.NotificationListAdapter;

/**
 * Created by user on 2018-01-22.
 */

public class NotificationFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "NotificationFragment";
    private View view;
    private BottomBarTab mBottomBarTab;
    private List<NotificationDetail> mNotificationList;
    private NotificationListAdapter mAdapter;
    private RedisApiService mRedisApiService;
    private HashMap<String, String> mCommunityHashMap;
    private SwipeRefreshLayout.OnRefreshListener listener = this;

    @BindView(R.id.list_notification)
    ListView notificationListView;

    @BindView(R.id.progressbar_notification)
    ProgressBar progressBar;

    @BindView(R.id.text_noHistory)
    TextView noHistoryTxt;

    @BindView(R.id.swipe_notification)
    SwipeRefreshLayout swipeLayout;

    public static NotificationFragment newInstance(int instance) {
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
            swipeLayout.setOnRefreshListener(listener);
            noHistoryTxt.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            mRedisApiService = ApiUtils.getRedisApiService();
            getCommunityList();
            initNotificationList();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"알림 뷰 화면");
        setBadge();
    }

    public void initNotificationList() {
        progressBar.setVisibility(View.VISIBLE);
        notificationListView.setVisibility(View.VISIBLE);
      /*  mRegisteredKeywordList.add("비트코인");
        mRegisteredKeywordList.add("꿀교양");*/
        mRedisApiService.getPushHistory(Prefs.getString("userToken", null)).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if (response.body() != null) {
                    Log.d(TAG, "알림 히스토리 서버통신 성공");
                    List<NotificationMessage> notificationList = new ArrayList<>();
                    notificationList = response.body().getMessages();
                    if (notificationList.size() > 0) {
                        addData(notificationList);
                        mAdapter = new NotificationListAdapter(getContext(), R.layout.layout_notification_listitem, mNotificationList);
                        notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String communityId = mNotificationList.get(position).getCommunityId();
                                String communityName = mNotificationList.get(position).getCommunityName();
                                String boardAddr = mNotificationList.get(position).getBoardAddr();
                                String createdDate = mNotificationList.get(position).getCreatedDate();
                                if (mFragmentNavigation != null) {
                                    mFragmentNavigation.pushFragment(NotificationDetailFragment.newInstance(0, communityId, communityName, boardAddr, createdDate));
                                }
                            }
                        });
                        notificationListView.setAdapter(mAdapter);
                       /* for(int i=0; i<mNotificationList.size();i++) {
                            Log.d(TAG,mNotificationList.get(i).toString());
                        }*/
                    } else {
                        noHistoryTxt.setVisibility(View.VISIBLE);
                        notificationListView.setVisibility(View.GONE);
                        Log.d(TAG, "현재까지는 알림 히스토리가 없음");
                    }
                } else {
                    Log.d(TAG, "알림 히스토리 서버통신 실패 : onResponse : " + response.message());
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                Log.d(TAG, "알림 히스토리 서버통신 실패 : onFailure : " + t.getMessage());
                noHistoryTxt.setVisibility(View.VISIBLE);
                noHistoryTxt.setText("현재 인터넷과의 연결이 원할하지 않습니다.");
            }
        });
    }

    public void addData(List<NotificationMessage> notificationMessages) {
        mNotificationList = new ArrayList<>();
        NotificationDetail notificationDetail;
        String keyword, createdDate, communityId, communityName, boardAddr;

        for (int i = notificationMessages.size()-1; i >= 0; i--) {
            keyword = notificationMessages.get(i).getKeyword();
            createdDate = transformCreatedTime(notificationMessages.get(i).getCreatedDate());
            communityId = notificationMessages.get(i).getCommunity();
            communityName = transformCommunity(notificationMessages.get(i).getCommunity());
            boardAddr = notificationMessages.get(i).getBoardAddr();

            notificationDetail = new NotificationDetail(keyword , communityId, communityName, boardAddr, createdDate);
            //mNotificationList.add(i, notificationDetail);
            mNotificationList.add(0, notificationDetail);
        }
    }

    public String transformCreatedTime(String time) {
        String result;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(time);
            Log.d(TAG,dateFormat.format(convertedDate));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //포스트된 날짜 datetime으로 변경
        Calendar postDay = Calendar.getInstance();
        postDay.setTime(convertedDate);
        int postDayYear = postDay.get(Calendar.YEAR);
        int postDayMonth = postDay.get(Calendar.MONTH)+1;
        int postDayDate = postDay.get(Calendar.DATE);
        int postDayAM_PM_temp = postDay.get(Calendar.AM_PM);
        String postDayAM_PM;
        if(postDayAM_PM_temp==0) {
            postDayAM_PM = "오전";
        } else {
            postDayAM_PM = "오후";
        }
        int postDayHour = postDay.get(Calendar.HOUR);
        if(postDayHour==0) {
            postDayHour = 12;
        }
        int postDayMinute_temp = postDay.get(Calendar.MINUTE);
        String postDayMinute;
        if(postDayMinute_temp<10) {
            postDayMinute = "0"+postDayMinute_temp;
        } else {
            postDayMinute = postDayMinute_temp+"";
        }
        //오늘 날짜
        Calendar today = Calendar.getInstance();
        int todayYear = today.get(Calendar.YEAR);
        int todayMonth = today.get(Calendar.MONTH)+1;
        int todayDate = today.get(Calendar.DATE);


        if(!((postDayYear==todayYear)&&(postDayMonth==todayMonth)&&(postDayDate==todayDate))) {
            // 오늘이 아닌 경우
            if(postDayYear == todayYear) {
                // 같은 년도일 경우 년 생략
                result = postDayMonth+"월 "+postDayDate+"일 "+postDayAM_PM+" "+postDayHour+":"+postDayMinute;
            } else {
                result = postDayYear+"년 "+postDayMonth+"월 "+postDayDate+"일 "+postDayAM_PM+" "+postDayHour+":"+postDayMinute;
            }
        } else {
            // 오늘인 경우
            result = "오늘 "+postDayAM_PM+" "+postDayHour+":"+postDayMinute;
        }
        return result;
    }

    public String transformCommunity(String data) {

        if(mCommunityHashMap.containsKey(data)) {
            return mCommunityHashMap.get(data);
        } else {
            return data;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //ButterKnife.bind(this, view).unbind();
    }

    public void setBadge() {
        MainActivity.mBottomBarTab.removeBadge();
        ShortcutBadger.removeCount(getContext());
        Prefs.putInt("badgeCount", 0);
    }

    public String loadJSONFromAsset(String mode) {
        String json = null;
        try {
            InputStream is;
            if(mode.equals("community_id")) {
                is = getActivity().getAssets().open("community_id.json");
            } else {
                is = getActivity().getAssets().open("university_communitylist.json");
            }
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void getCommunityList() {
        mCommunityHashMap = new HashMap<>();
        try {

            //원래 소스
           /* JSONObject obj = new JSONObject(loadJSONFromAsset("community_list"));
            String university = loginInfo.getUniversity();
            if(university.contains("경희대학교")) {
                university = "경희대학교";
            } else if(university.contains("세종대학교")){
                university = "세종대학교";
            }

            JSONArray content = obj.getJSONArray(university);
            for (int i = 0; i < content.length(); i++) {
                JSONObject communityInfo = content.getJSONObject(i);
                String id = communityInfo.getString("id");
                String name = communityInfo.getString("name");
                mCommunityHashMap.put(id, name);
                mCommunityList.add(new Community(id,name));
            }*/

            //테스트 소스 - 경희, 세종, 한성대 커뮤니티 일단 다 집어넣음
            JSONObject obj = new JSONObject(loadJSONFromAsset("community_list"));
            String university = "경희대학교";
            JSONArray content = obj.getJSONArray(university);
            for (int i = 0; i < content.length(); i++) {
                JSONObject communityInfo = content.getJSONObject(i);
                String id = communityInfo.getString("id");
                String name = communityInfo.getString("name");
                mCommunityHashMap.put(id, name);
            }

            university = "세종대학교";
            content = obj.getJSONArray(university);
            for (int i = 0; i < content.length(); i++) {
                JSONObject communityInfo = content.getJSONObject(i);
                String id = communityInfo.getString("id");
                String name = communityInfo.getString("name");
                mCommunityHashMap.put(id, name);
            }

            university = "한성대학교";
            content = obj.getJSONArray(university);
            for (int i = 0; i < content.length(); i++) {
                JSONObject communityInfo = content.getJSONObject(i);
                String id = communityInfo.getString("id");
                String name = communityInfo.getString("name");
                mCommunityHashMap.put(id, name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        initNotificationList();
        swipeLayout.setRefreshing(false);
    }
}
