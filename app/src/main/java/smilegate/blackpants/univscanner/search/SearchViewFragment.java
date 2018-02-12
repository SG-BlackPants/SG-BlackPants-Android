package smilegate.blackpants.univscanner.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.data.model.Keywords;
import smilegate.blackpants.univscanner.utils.BaseFragment;
import smilegate.blackpants.univscanner.utils.KeywordListAdapter;

import static smilegate.blackpants.univscanner.MainActivity.mNavController;

/**
 * Created by user on 2018-01-23.
 */

public class SearchViewFragment extends BaseFragment {
    private static final String TAG = "SearchViewFragment";
    private View mView;
    private List<Keywords> mKeywordsList;
    private KeywordListAdapter mAdapter;
    int count;
    @BindView(R.id.autoText_search)
    AutoCompleteTextView searchAutoText;

    @BindView(R.id.btn_searchview_back)
    ImageButton searchViewBackBtn;

    @BindView(R.id.btn_searchview_clear)
    ImageButton searchViewClearBtn;

    @OnClick(R.id.btn_searchview_back)
    public void searchViewBack(ImageButton imageButton) {
        if (!mNavController.popFragment()) {
            getActivity().onBackPressed();
        }
    }

    @OnClick(R.id.btn_searchview_clear)
    public void searchViewClear(ImageButton imageButton) {
        searchAutoText.setText("");
        searchViewClearBtn.setVisibility(View.GONE);
    }

    @BindView(R.id.list_search)
    ListView searchListView;

    public static SearchViewFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        SearchViewFragment fragment = new SearchViewFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_search_view, container, false);
            ButterKnife.bind(this, mView);
            initTextListener();
            searchViewClearBtn.setVisibility(View.GONE);
            count=0;
        }
        return mView;
    }

    public void initTextListener() {
        Log.d(TAG, "initTextListener : initializing");

        mKeywordsList = new ArrayList<>();

        searchAutoText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = searchAutoText.getText().toString().toLowerCase(Locale.getDefault());
                Log.d(TAG, "afterTextChanged : " + text);
                count++;
                searchForMatch(text);

            }
        });
    }

    public void searchForMatch(String keyword) {
        Log.d(TAG, "searchForMatch : searcing for a match: " + keyword);
        mKeywordsList.clear();

        if(keyword.length()==0) {
            count=0;
            searchViewClearBtn.setVisibility(View.GONE);
        } else {
            searchViewClearBtn.setVisibility(View.VISIBLE);
             mKeywordsList.add(new Keywords(searchAutoText.getText().toString().trim()));
            updateKewordsList();
        }
    }

    public void updateKewordsList() {
        Log.d(TAG, "updateKeywordList : updating kewords list");

        mAdapter = new KeywordListAdapter(getContext(), R.layout.layout_keyword_listitem, mKeywordsList);

        searchListView.setAdapter(mAdapter);

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick : selected : " + mKeywordsList.get(position).toString());
                if (mFragmentNavigation != null) {
                    mFragmentNavigation.pushFragment(SearchResultFragment.newInstance(0, mKeywordsList.get(position).getName()));
                }
            }
        });
    }

}
