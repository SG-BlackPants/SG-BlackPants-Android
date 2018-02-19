package smilegate.blackpants.univscanner.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
    private List<String> community;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("secondWord")
    @Expose
    private String secondWord;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private String message;

    public Push(String keyword, String university, List<String> community, String startDate, String endDate, String secondWord) {
        this.keyword = keyword;
        this.university = university;
        this.community = community;
        this.startDate = startDate;
        this.endDate = endDate;
        this.secondWord = secondWord;
    }

    public Push(String keyword, String university) {
        this.keyword = keyword;
        this.university = university;
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

    public List<String> getCommunity() {
        return community;
    }

    public void setCommunity(List<String> community) {
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

    @Override
    public String toString() {
        return "Push{" +
                "keyword='" + keyword + '\'' +
                ", university='" + university + '\'' +
                ", community=" + community +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", secondWord='" + secondWord + '\'' +
                '}';
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
