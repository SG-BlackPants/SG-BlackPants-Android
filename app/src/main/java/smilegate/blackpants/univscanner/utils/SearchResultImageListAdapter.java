package smilegate.blackpants.univscanner.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
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
    ImageClickListener imageClickListener;

    public interface ImageClickListener {
        public void onImageClickListener(String url);
    }

    public void setImageClickListner(ImageClickListener listener) {
        this.imageClickListener = listener;
    }

    public SearchResultImageListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLayoutResource = resource;
        this.mSearchResultImages = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final SearchResultImageListAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutResource, parent, false);
            viewHolder = new SearchResultImageListAdapter.ViewHolder();

            viewHolder.imageView = convertView.findViewById(R.id.img_detail_image);
            viewHolder.progressBar = convertView.findViewById(R.id.progressbar_detail_progressbar);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SearchResultImageListAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.progressBar.setVisibility(View.VISIBLE);
        viewHolder.imageView.setVisibility(View.VISIBLE);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (imageClickListener != null) {
                    imageClickListener.onImageClickListener(mSearchResultImages.get(position));
                }
            }
        });

        Picasso.with(mContext)
                .load(mSearchResultImages.get(position))
                .resize(1280, 720)
                .centerCrop()
                .into(viewHolder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        viewHolder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        viewHolder.progressBar.setVisibility(View.GONE);
                    }
                });

        return convertView;
    }

    public static class ViewHolder {
        FrameLayout frameLayout;
        ImageView imageView;
        ProgressBar progressBar;
    }
}