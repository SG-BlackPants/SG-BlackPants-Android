package smilegate.blackpants.univscanner.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import smilegate.blackpants.univscanner.R;

/**
 * Created by Semin on 2018-02-12.
 */

public class SearchResultImageListAdapter extends ArrayAdapter<String> {
    private static final String TAG = "SearchResultImageListAdapter";

    private LayoutInflater mInflater;
    private List<String> mSearchResultImages = null;
    private int mLayoutResource;
    private Context mContext;


    public SearchResultImageListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLayoutResource = resource;
        this.mSearchResultImages = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SearchResultImageListAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutResource, parent, false);
            viewHolder = new SearchResultImageListAdapter.ViewHolder();

            viewHolder.imageView = convertView.findViewById(R.id.img_detail_image);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SearchResultImageListAdapter.ViewHolder) convertView.getTag();
        }


        Picasso.with(mContext)
                .load(mSearchResultImages.get(position))
                .resize(1280, 720)
                .centerCrop()
                .placeholder(R.drawable.app_logo2)
                .into(viewHolder.imageView);

        return convertView;
    }

    public static class ViewHolder {
        ImageView imageView;
    }
}