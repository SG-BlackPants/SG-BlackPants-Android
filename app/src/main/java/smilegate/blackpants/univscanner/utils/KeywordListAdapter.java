package smilegate.blackpants.univscanner.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import smilegate.blackpants.univscanner.data.model.Keywords;

/**
 * Created by user on 2018-01-24.
 */

public class KeywordListAdapter extends ArrayAdapter<Keywords>{

    private static final String TAG = "KeywordListAdapter";

    private LayoutInflater mInflater;
    private List<Keywords> mKeywords = null;
    private int mLayoutResource;
    private Context mContext;

    public KeywordListAdapter(Context context, int resource, List<Keywords> objects) {
        super(context, resource, objects);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutResource = resource;
        this.mKeywords = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            //convertView = View.inflate(mLayoutResource, parent, false);
            viewHolder = new ViewHolder();


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //String location = filteredItems.get(position);
        //if (!location.isEmpty() || viewHolder != null) {
        //    viewHolder.tvTitle.setText(location);
       // }
        return convertView;
    }

    public static class ViewHolder {
        TextView name;
    }


}
