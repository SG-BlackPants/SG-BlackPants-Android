package smilegate.blackpants.univscanner.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user on 2018-02-08.
 */

public class LoginInfo {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("university")
    @Expose
    private String university;

    @SerializedName("isRegistered")
    @Expose
    private boolean isRegistered;

    @SerializedName("keywords")
    @Expose
    private List<Keywords> keywords;

    public LoginInfo(String name, String university, boolean isRegistered) {
        this.name = name;
        this.university = university;
        this.isRegistered = isRegistered;
    }

    public LoginInfo(List<Keywords> keywords) {
        this.keywords = keywords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public List<Keywords> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Keywords> keywords) {
        this.keywords = keywords;
    }
}
