package smilegate.blackpants.univscanner.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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
    ContentDetailClickListener contentDetailClickListener;

    public interface ContentDetailClickListener {
        public void onButtonClickListner(SearchResults searchResults, String value);

        //public void onUrlClickListener(String url);
    }

    public void setContentDetailClickListner(SearchResultFeedAdapter.ContentDetailClickListener listener) {
        this.contentDetailClickListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_post_name)
        TextView postName;
        @BindView(R.id.text_post_time)
        TextView postTime;
        @BindView(R.id.text_post_content)
        TextView postContent;
        @BindView(R.id.text_post_source)
        TextView postSource;
        @BindView(R.id.text_post_author)
        TextView postAuthor;
        @BindView(R.id.text_post_url)
        TextView postUrl;
        @BindView(R.id.text_post_image)
        ImageView postImage;


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
        String transformedContent = settingViewDetails(searchResults.getContent(), searchResults.getImages());
        SpannableString content = settingContentTextView(transformedContent, searchResults);
        if (content != null) {
            holder.postContent.setText(content);
            holder.postContent.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            holder.postContent.setText(searchResults.getContent());
        }
        holder.postSource.setText(searchResults.getCommunity() + " " + searchResults.getBoardAddr());
        holder.postAuthor.setText(searchResults.getAuthor());
        holder.postUrl.setText(searchResults.getUrl());


        if (searchResults.getImages() == null) {
            holder.postImage.setVisibility(View.GONE);
        } else {
            holder.postImage.setVisibility(View.VISIBLE);
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

    public SpannableString settingContentTextView(String content, final SearchResults searchResults) {
        SpannableString spanString = new SpannableString(content);
        //Matcher matcher = Pattern.compile("@([A-Za-z0-9_-]+)").matcher(spanString);
        final String spanStr = content.substring(content.length() - 6, content.length());

        if (spanStr.equals("자세히 보기")) {
            spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#607D8B")), content.length() - 6, content.length(), 0);
            //final String tag = spanStr..group(0);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    if (contentDetailClickListener != null) {
                        contentDetailClickListener.onButtonClickListner(searchResults, spanStr);
                        Log.e("click", "click " + spanStr);
                    }
                    //String searchText=tag.replace("@",""); //replace '@' with blank character to search on google.
                    //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.in/search?q=" + searchText));
                    //startActivity(browserIntent);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    ds.setColor(Color.parseColor("#607D8B"));
                }
            };
            spanString.setSpan(clickableSpan, content.length() - 6, content.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            return spanString;
        } else {
            return null;
        }

    }

    public String settingViewDetails(String content, List<String> images) {
        String result;

        if (content.length() > 1000) {
            result = content.substring(0, 1000) + "...  자세히 보기";
        } else if (images != null) {
            if (images.size() > 1) {
                result = content + "... 자세히 보기";
            } else {
                result = content;
            }
        } else {
            result = content;
        }

        return result;
    }

}

