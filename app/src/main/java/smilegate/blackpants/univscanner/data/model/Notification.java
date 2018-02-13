package smilegate.blackpants.univscanner.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user on 2018-02-13.
 */

public class Notification {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private List<NotificationMessage> messages;

    public Notification(String result, String code, List<NotificationMessage> messages) {
        this.result = result;
        this.code = code;
        this.messages = messages;
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

    public List<NotificationMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<NotificationMessage> messages) {
        this.messages = messages;
    }
}
