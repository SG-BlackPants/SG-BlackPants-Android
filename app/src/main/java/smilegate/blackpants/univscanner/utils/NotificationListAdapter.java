package smilegate.blackpants.univscanner.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.data.model.NotificationDetail;

/**
 * Created by user on 2018-02-13.
 */

public class NotificationListAdapter extends ArrayAdapter<NotificationDetail> {
    private static final String TAG = "NotificationListAdapter";

    private LayoutInflater mInflater;
    private List<NotificationDetail> mNotificationList = null;
    private int mLayoutResource;
    private Context mContext;

    public NotificationListAdapter(Context context, int resource, List<NotificationDetail> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLayoutResource = resource;
        this.mNotificationList = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        NotificationListAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutResource, parent, false);
            viewHolder = new NotificationListAdapter.ViewHolder(convertView, mContext);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NotificationListAdapter.ViewHolder) convertView.getTag();
        }
        Log.d(TAG,getItem(position).toString());

        Resources res = mContext.getResources();
        String mDrawableName = getCommunityLogo(getItem(position).getCommunityName());
        int resID = res.getIdentifier(mDrawableName , "drawable", mContext.getPackageName());
        Drawable drawable = res.getDrawable(resID );
        viewHolder.notificationSourceImg.setImageDrawable(drawable);

        String communityName = "<b><font color='#000'>"+getItem(position).getCommunityName()+"</font></b>";
        String keyword = "<b><font color='#000'>"+getItem(position).getKeyword()+"</font></b>";
        viewHolder.notificationInfoTxt.setText(Html.fromHtml(communityName+ "에서 " + keyword + "관련 게시물이 올라왔습니다."));
        viewHolder.notificationTimeTxt.setText(getItem(position).getCreatedDate());

        return convertView;
    }

    public static class ViewHolder {
        Context context;

        @BindView(R.id.img_notification_source)
        ImageView notificationSourceImg;

        @BindView(R.id.text_notification_info)
        TextView notificationInfoTxt;

        @BindView(R.id.text_notification_time)
        TextView notificationTimeTxt;

        ViewHolder(View view, Context context) {
            ButterKnife.bind(this, view);
            this.context = context;
        }
    }

    public String getCommunityLogo(String community) {
        if(community.equals("Kyunghee bamboo grove")) {
            return "kyunghee_bamboo";
        } else if(community.equals("경희대학교 - 국제캠 대신 전해드립니다")) {
            return "kyunghee_daeshin";
        } else if(community.equals("애브리타임")) {
            return "everytime";
        } else if(community.equals("세종대학교 대나무숲")) {
            return "sejong_bamboo";
        } else if(community.equals("세종대학교 대신 전해드립니다")) {
            return "sejong_daeshin";
        } else if(community.equals("한성대학교 대나무숲")) {
            return "hansung_daeshin";
        } else {
            return "ic_hashtag";
        }
    }
}
