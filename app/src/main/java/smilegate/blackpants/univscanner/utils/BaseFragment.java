package smilegate.blackpants.univscanner.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

/**
 * Created by niccapdevila on 3/26/16.
 */
public class BaseFragment extends Fragment {
    public static final String ARGS_INSTANCE = "smilegate.blackpants.univscanner.argsInstance";

    Button btn;
    public FragmentNavigation mFragmentNavigation;
    int mInt = 0;
    public View cachedView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mInt = args.getInt(ARGS_INSTANCE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNavigation) {
            mFragmentNavigation = (FragmentNavigation) context;
        }
    }

    public interface FragmentNavigation {
        void pushFragment(Fragment fragment);
    }
}
