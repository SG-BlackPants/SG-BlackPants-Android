package smilegate.blackpants.univscanner.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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

/**
 * Created by Semin on 2018-02-17.
 */

public class CommunityListAdapter  extends ArrayAdapter<String> {
    private static final String TAG = "CommunityListAdapter";

    private LayoutInflater mInflater;
    private List<String> mCommunityList = null;
    private int mLayoutResource;
    private Context mContext;

    public CommunityListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLayoutResource = resource;
        this.mCommunityList = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CommunityListAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutResource, parent, false);
            viewHolder = new CommunityListAdapter.ViewHolder(convertView, mContext);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CommunityListAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.communityNameTxt.setText(getItem(position));

        Resources res = mContext.getResources();
        String mDrawableName = getCommunityLogo(getItem(position));
        int resID = res.getIdentifier(mDrawableName , "drawable", mContext.getPackageName());
        Drawable drawable = res.getDrawable(resID );
        viewHolder.communityIcon.setImageDrawable(drawable);

        return convertView;
    }

    public static class ViewHolder {
        Context context;

        @BindView(R.id.img_profile_community)
        ImageView communityIcon;

        @BindView(R.id.text_profile_communityName)
        TextView communityNameTxt;

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

