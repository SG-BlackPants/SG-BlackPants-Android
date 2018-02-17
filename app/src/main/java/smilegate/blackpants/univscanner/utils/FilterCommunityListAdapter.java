package smilegate.blackpants.univscanner.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.data.model.Community;

/**
 * Created by Semin on 2018-02-15.
 */

public class FilterCommunityListAdapter extends ArrayAdapter<Community> {
    private static final String TAG = "RegisteredKeywordListAdapter";

    private LayoutInflater mInflater;
    private List<Community> mRegisteredKeywords = null;
    private int mLayoutResource;
    private Context mContext;
    private HashMap<String, Boolean> mCommunityCheckHashMap;

    CommunityCheckboxListener communityCheckboxListener;

    public interface CommunityCheckboxListener {
        public void onCheckboxClickListner(String value, boolean isChecked);
    }

    public void setCheckBoxListner(CommunityCheckboxListener listener) {
        this.communityCheckboxListener = listener;
    }


    public FilterCommunityListAdapter(Context context, int resource, List<Community> objects, HashMap<String, Boolean> hashMap) {
        super(context, resource, objects);
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLayoutResource = resource;
        this.mRegisteredKeywords = objects;
        this.mCommunityCheckHashMap = hashMap;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        FilterCommunityListAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutResource, parent, false);
            viewHolder = new FilterCommunityListAdapter.ViewHolder(convertView, mContext);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FilterCommunityListAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.communityCheckbox.setText(getItem(position).getName());

        if(mCommunityCheckHashMap.get(getItem(position).getId())) {
            viewHolder.communityCheckbox.setChecked(true);
        } else {
            viewHolder.communityCheckbox.setChecked(false);
        }

        viewHolder.communityCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (communityCheckboxListener != null) {
                    communityCheckboxListener.onCheckboxClickListner(getItem(position).getId(), isChecked);
                }
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        Context context;

        @BindView(R.id.checkbox_community)
        CheckBox communityCheckbox;

        ViewHolder(View view, Context context) {
            ButterKnife.bind(this, view);
            this.context = context;
        }
    }
}
