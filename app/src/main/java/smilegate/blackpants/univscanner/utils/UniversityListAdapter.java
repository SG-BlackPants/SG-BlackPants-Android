package smilegate.blackpants.univscanner.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import smilegate.blackpants.univscanner.R;

/**
 * Created by user on 2018-01-24.
 */

public class UniversityListAdapter extends ArrayAdapter<String>{

    private static final String TAG = "UniversityListAdapter";

    private LayoutInflater mInflater;
    private List<String> mUniversity = null;
    private int mLayoutResource;
    private Context mContext;
    private Filter filter;

    public UniversityListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLayoutResource = resource;
        this.mUniversity = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutResource, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.name = convertView.findViewById(R.id.text_univ_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(getItem(position));
        return convertView;
    }

    public static class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return mUniversity.size();
    }

    @Override
    public String getItem(int position) {
        return mUniversity.get(position);
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new AppFilter<String>(mUniversity);
        return filter;
    }

    private class AppFilter<T> extends Filter {

        private ArrayList<T> sourceObjects;

        public AppFilter(List<T> objects) {
            sourceObjects = new ArrayList<T>();
            synchronized (this) {
                sourceObjects.addAll(objects);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence chars) {
            String filterSeq = chars.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (filterSeq != null && filterSeq.length() > 0) {
                ArrayList<T> filter = new ArrayList<T>();

                for (T object : sourceObjects) {
                    // the filtering itself:
                    if (object.toString().toLowerCase().contains(filterSeq))
                        filter.add(object);
                }
                result.count = filter.size();
                result.values = filter;

            } else {
                // add all objects
                synchronized (this) {
                    result.values = sourceObjects;
                    result.count = sourceObjects.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // NOTE: this function is *always* called from the UI thread.
            ArrayList<T> filtered = (ArrayList<T>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filtered.size(); i < l; i++)
                add((String) filtered.get(i));
            notifyDataSetInvalidated();
        }
    }


}
