package smilegate.blackpants.univscanner.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smilegate.blackpants.univscanner.R;

/**
 * Created by Semin on 2018-02-09.
 */

public class RegisteredKeywordListAdapter extends ArrayAdapter<String> {
    private static final String TAG = "RegisteredKeywordListAdapter";

    private LayoutInflater mInflater;
    private List<String> mRegisteredKeywords = null;
    private int mLayoutResource;
    private Context mContext;

    KeywordDeleteListener keywordDeleteListener;

    public interface KeywordDeleteListener {
        public void onButtonClickListner(int position, String value);
    }

    public void setCustomButtonListner(KeywordDeleteListener listener) {
        this.keywordDeleteListener = listener;
    }


    public RegisteredKeywordListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLayoutResource = resource;
        this.mRegisteredKeywords = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RegisteredKeywordListAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutResource, parent, false);
            viewHolder = new RegisteredKeywordListAdapter.ViewHolder(convertView, mContext);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RegisteredKeywordListAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(getItem(position));
        viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (keywordDeleteListener != null) {
                    keywordDeleteListener.onButtonClickListner(position, getItem(position));
                }
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        Context context;

        @BindView(R.id.text_registered_keywordName)
        TextView name;

        @BindView(R.id.btn_delete_registeredkeyword)
        ImageButton imageButton;

        ViewHolder(View view, Context context) {
            ButterKnife.bind(this, view);
            this.context = context;
        }
    }


}

