package smilegate.blackpants.univscanner.utils;

import android.content.Context;
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
import smilegate.blackpants.univscanner.data.model.NotificationMessage;

/**
 * Created by user on 2018-02-13.
 */

public class NotificationListAdapter extends ArrayAdapter<NotificationMessage> {
    private static final String TAG = "NotificationListAdapter";

    private LayoutInflater mInflater;
    private List<NotificationMessage> mNotificationList = null;
    private int mLayoutResource;
    private Context mContext;

    public NotificationListAdapter(Context context, int resource, List<NotificationMessage> objects) {
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
        viewHolder.notificationSourceImg.setImageResource(R.drawable.kyunghee_bamboo);
        viewHolder.notificationInfoTxt.setText(getItem(position).getCommunity() + " " + getItem(position).getBoardAddr() + "에서 " + getItem(position).getKeyword() + "관련 게시물이 올라왔습니다.");
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
}
