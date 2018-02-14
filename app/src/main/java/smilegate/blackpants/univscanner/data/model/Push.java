package smilegate.blackpants.univscanner.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 2018-02-14.
 */

public class Push {

    @SerializedName("keyword")
    @Expose
    private String keyword;
    @SerializedName("university")
    @Expose
    private String university;
    @SerializedName("community")
    @Expose
    private String[] community;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("secondWord")
    @Expose
    private String secondWord;

    public Push(String keyword, String university, String[] community, String startDate, String endDate, String secondWord) {
        this.keyword = keyword;
        this.university = university;
        this.community = community;
        this.startDate = startDate;
        this.endDate = endDate;
        this.secondWord = secondWord;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String[] getCommunity() {
        return community;
    }

    public void setCommunity(String[] community) {
        this.community = community;
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

    public String getSecondWord() {
        return secondWord;
    }

    public void setSecondWord(String secondWord) {
        this.secondWord = secondWord;
    }
}
