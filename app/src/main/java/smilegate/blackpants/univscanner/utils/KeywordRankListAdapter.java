package smilegate.blackpants.univscanner.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.data.model.KeywordRank;

/**
 * Created by user on 2018-01-24.
 */

public class KeywordRankListAdapter extends ArrayAdapter<KeywordRank>{

    private static final String TAG = "KeywordRankListAdapter";

    private LayoutInflater mInflater;
    private List<KeywordRank> mKeywordRank = null;
    private int mLayoutResource;
    private Context mContext;

    public KeywordRankListAdapter(Context context, int resource, List<KeywordRank> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLayoutResource = resource;
        this.mKeywordRank = objects;
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

        viewHolder.name.setText(getItem(position).getName());
        viewHolder.rank.setText(getItem(position).getRank());

        return convertView;
    }

    public static class ViewHolder {
        TextView name;
        TextView rank;
    }

}
