package smilegate.blackpants.univscanner.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import smilegate.blackpants.univscanner.R;

/**
 * Created by user on 2018-01-24.
 */

public class KeywordRankListAdapter extends ArrayAdapter<String>{

    private static final String TAG = "KeywordRankListAdapter";

    private LayoutInflater mInflater;
    private List<String> mKeywordRank = null;
    private int mLayoutResource;
    private Context mContext;
    private int mRank;

    public KeywordRankListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLayoutResource = resource;
        this.mKeywordRank = objects;
        mRank = 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutResource, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.name = (TextView) convertView.findViewById(R.id.text_rank_keywordName);
            viewHolder.rank = (TextView) convertView.findViewById(R.id.text_rank_num);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //mRank++;
        viewHolder.name.setText(getItem(position));
        //viewHolder.rank.setText(String.valueOf(getItem(position).getRank()));
        viewHolder.rank.setText(String.valueOf(position+1));
        return convertView;
    }

    public static class ViewHolder {
        TextView name, rank;
    }

}
