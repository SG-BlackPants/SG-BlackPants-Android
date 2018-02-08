package smilegate.blackpants.univscanner.profile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import smilegate.blackpants.univscanner.R;
import smilegate.blackpants.univscanner.utils.BaseFragment;
import smilegate.blackpants.univscanner.utils.RegisteredKeywordListAdapter;

import static smilegate.blackpants.univscanner.MainActivity.mNavController;

/**
 * Created by user on 2018-02-08.
 */

public class ProfileRegisteredKeywordFragment extends BaseFragment implements RegisteredKeywordListAdapter.KeywordDeleteListener {
    private static final String TAG = "RegisteredKeydFragment";
    private View view;
    private List<String> mRegisteredKeywordList;
    private RegisteredKeywordListAdapter mAdapter;

    @BindView(R.id.list_registered_keyword)
    ListView registeredKeywordListView;

    @BindView(R.id.btn_profileregisteredkeyword_back)
    ImageButton backBtn;

    @OnClick(R.id.btn_profileregisteredkeyword_back)
    public void backClick(ImageButton imageButton) {
        if (!mNavController.popFragment()) {
            getActivity().onBackPressed();
        }
    }

    /*@OnClick(R.id.btn_delete_registeredkeyword)
    public void keywordDeleteClick(ImageButton imageButton) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        Log.d(TAG,"등록 키워드 클릭");

        alertDialogBuilder.setTitle("등록된 키워드 삭제");

        alertDialogBuilder
                .setMessage("해당 키워드를 삭제하시면, 알람을 받을 수 없습니다.\n삭제하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("삭제",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                // 서버로 보내기
                                Toast.makeText(getContext(), "삭제", Toast.LENGTH_LONG).show();
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                // 다이얼로그를 취소한다
                                dialog.cancel();
                            }
                        });
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();

    }
*/
    public static ProfileRegisteredKeywordFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        ProfileRegisteredKeywordFragment fragment = new ProfileRegisteredKeywordFragment();
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
            view = inflater.inflate(R.layout.fragment_profile_registeredkeyword, container, false);
            ButterKnife.bind(this, view);
            initRegisteredKeywordList();
        }
        return view;
    }

    public void initRegisteredKeywordList() {
        mRegisteredKeywordList = new ArrayList<>();
        mRegisteredKeywordList.add("비트코인");
        mRegisteredKeywordList.add("꿀교양");

        mAdapter = new RegisteredKeywordListAdapter(getContext(), R.layout.layout_profileregisteredkeyword_listitem, mRegisteredKeywordList);
        mAdapter.setCustomButtonListner(this);
        registeredKeywordListView.setAdapter(mAdapter);
    }

    @Override
    public void onButtonClickListner(int position, String value) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        Log.d(TAG,"등록 키워드 클릭");

        alertDialogBuilder.setTitle("등록된 키워드 삭제");

        alertDialogBuilder
                .setMessage("해당 키워드를 삭제하시면, 알람을 받을 수 없습니다.\n삭제하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("삭제",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                // 서버로 보내기
                                Toast.makeText(getContext(), "삭제", Toast.LENGTH_LONG).show();
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                // 다이얼로그를 취소한다
                                dialog.cancel();
                            }
                        });
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();

    }
}
