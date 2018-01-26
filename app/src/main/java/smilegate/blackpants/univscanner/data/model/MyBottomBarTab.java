package smilegate.blackpants.univscanner.data.model;

import com.roughike.bottombar.BottomBarTab;

import java.io.Serializable;

/**
 * Created by user on 2018-01-26.
 */

public class MyBottomBarTab implements Serializable{

    private BottomBarTab bottomBarTab;

    public MyBottomBarTab(BottomBarTab bottomBarTab) {
        this.bottomBarTab = bottomBarTab;
    }

    public BottomBarTab getBottomBarTab() {
        return bottomBarTab;
    }

    public void setBottomBarTab(BottomBarTab bottomBarTab) {
        this.bottomBarTab = bottomBarTab;
    }
}
