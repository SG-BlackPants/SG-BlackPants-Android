package smilegate.blackpants.univscanner.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user on 2018-02-06.
 */

public class KeywordRank {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private List<String> message;

    public KeywordRank(String result, String code, List<String> message) {
        this.result = result;
        this.code = code;
        this.message = message;
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

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "KeywordRank{" +
                "result='" + result + '\'' +
                ", code='" + code + '\'' +
                ", message=" + message +
                '}';
    }
}
