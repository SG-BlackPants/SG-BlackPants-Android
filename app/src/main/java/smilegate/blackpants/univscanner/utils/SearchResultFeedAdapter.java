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

import butterknife.BindView;
import butterknife.ButterKnife;
import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.data.model.SearchResults;

/**
 * Created by user on 2018-01-25.
 */

public class SearchResultFeedAdapter extends RecyclerView.Adapter<SearchResultFeedAdapter.MyViewHolder> {

    private Context mContext;
    private List<SearchResults> mSearchResultsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_post_name) TextView postName;
        @BindView(R.id.text_post_time) TextView postTime;
        @BindView(R.id.text_post_content) TextView postContent;
        @BindView(R.id.text_post_source) TextView postSource;
        @BindView(R.id.text_post_author) TextView postAuthor;
        @BindView(R.id.text_post_url) TextView postUrl;
        @BindView(R.id.text_post_image) ImageView postImage;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
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
        holder.postName.setText(searchResults.getTitle());
        holder.postTime.setText(searchResults.getCreatedDate());
        holder.postContent.setText(searchResults.getContent());
        holder.postSource.setText(searchResults.getCommunity()+" "+searchResults.getBoardAddr());
        holder.postAuthor.setText(searchResults.getAuthor());
        holder.postUrl.setText(searchResults.getUrl());

        if(searchResults.getImages().size()<1) {
            holder.postImage.setVisibility(View.GONE);
        } else {
            Picasso.with(mContext)
                    .load(searchResults.getImages().get(0))
                    .resize(1280, 720)
                    .centerCrop()
                    .placeholder(R.drawable.app_logo2)
                    .into(holder.postImage);

        }
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

