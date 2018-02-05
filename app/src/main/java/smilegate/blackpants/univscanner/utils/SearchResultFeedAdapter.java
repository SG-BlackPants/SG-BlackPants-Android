package smilegate.blackpants.univscanner.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.data.model.SearchResults;

/**
 * Created by user on 2018-01-25.
 */

public class SearchResultFeedAdapter extends RecyclerView.Adapter<SearchResultFeedAdapter.MyViewHolder> {

    private Context mContext;
    private List<SearchResults> mSearchResultsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.text_post_name);
            image = (ImageView) view.findViewById(R.id.text_post_image);
        }
    }

    public SearchResultFeedAdapter(Context mContext, List<SearchResults> mSearchResultsList) {
        this.mContext = mContext;
        this.mSearchResultsList = mSearchResultsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate new view when we create new items in our recyclerview
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_searchresult_listitem, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final SearchResults searchResults = mSearchResultsList.get(position);
        holder.name.setText(searchResults.getName());

        Picasso.with(mContext)
                .load(searchResults.getImage())
                .resize(1280, 720)
                .centerCrop()
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mSearchResultsList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

}

