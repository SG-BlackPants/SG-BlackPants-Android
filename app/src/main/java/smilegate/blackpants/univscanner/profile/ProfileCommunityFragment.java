package smilegate.blackpants.univscanner.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.data.model.LoginInfo;
import smilegate.blackpants.univscanner.utils.BaseFragment;
import smilegate.blackpants.univscanner.utils.CommunityListAdapter;

import static smilegate.blackpants.univscanner.MainActivity.mNavController;

/**
 * Created by Semin on 2018-02-17.
 */

public class ProfileCommunityFragment extends BaseFragment {
    private static final String TAG = "ProfileCommunityFragment";
    private View view;
    private List<String> mCommunityList;
    private CommunityListAdapter mAdapter;

    @BindView(R.id.list_community)
    ListView communityListView;

    @BindView(R.id.btn_profilecommunity_back)
    ImageButton backBtn;

    @OnClick(R.id.btn_profilecommunity_back)
    public void backClick(ImageButton imageButton) {
        if (!mNavController.popFragment()) {
            getActivity().onBackPressed();
        }
    }

    public static ProfileCommunityFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        ProfileCommunityFragment fragment = new ProfileCommunityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_profile_community, container, false);
            ButterKnife.bind(this, view);
            initCommunityList();
        }
        return view;
    }

    public void initCommunityList() {
        Gson gson = new Gson();
        String json = Prefs.getString("userInfo","");
        LoginInfo loginInfo = gson.fromJson(json, LoginInfo.class);

        mCommunityList = new ArrayList<String>();
        try {
            //원래 소스
            JSONObject obj = new JSONObject(loadJSONFromAsset("community_list"));
            String university = loginInfo.getUniversity();
            if(university.contains("경희대학교")) {
                university = "경희대학교";
            } else if(university.contains("세종대학교")){
                university = "세종대학교";
            }

            JSONArray content = obj.getJSONArray(university);
            for (int i = 0; i < content.length(); i++) {
                JSONObject communityInfo = content.getJSONObject(i);
                String name = communityInfo.getString("name");
                mCommunityList.add(name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter = new CommunityListAdapter(getContext(), R.layout.layout_profilecommunity_listitem, mCommunityList);
        communityListView.setAdapter(mAdapter);
    }

    public String loadJSONFromAsset(String mode) {
        String json = null;
        try {
            InputStream is;
            if(mode.equals("community_id")) {
                is = getActivity().getAssets().open("community_id.json");
            } else {
                is = getActivity().getAssets().open("university_communitylist.json");
            }
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
