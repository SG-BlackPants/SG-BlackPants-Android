package smilegate.blackpants.univscanner.data.model;

import java.util.HashMap;

/**
 * Created by user on 2018-02-17.
 */

public class UserSetting {

    private String secondWord;

    private String startDate;

    private String endDate;

    private HashMap<String, Boolean> communityHashMap;

    public UserSetting(String secondWord, String startDate, String endDate, HashMap<String, Boolean> communityHashMap) {
        this.secondWord = secondWord;
        this.startDate = startDate;
        this.endDate = endDate;
        this.communityHashMap = communityHashMap;
    }

    public String getSecondWord() {
        return secondWord;
    }

    public void setSecondWord(String secondWord) {
        this.secondWord = secondWord;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public HashMap<String, Boolean> getCommunityHashMap() {
        return communityHashMap;
    }

    public void setCommunityHashMap(HashMap<String, Boolean> communityHashMap) {
        this.communityHashMap = communityHashMap;
    }
}
