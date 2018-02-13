package smilegate.blackpants.univscanner.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 2018-02-13.
 */

public class NotificationMessage {

    @SerializedName("keyword")
    @Expose
    private String keyword;
    @SerializedName("community")
    @Expose
    private String community;
    @SerializedName("boardAddr")
    @Expose
    private String boardAddr;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;

    public NotificationMessage(String keyword, String community, String boardAddr, String createdDate) {
        this.keyword = keyword;
        this.community = community;
        this.boardAddr = boardAddr;
        this.createdDate = createdDate;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getBoardAddr() {
        return boardAddr;
    }

    public void setBoardAddr(String boardAddr) {
        this.boardAddr = boardAddr;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "NotificationMessage{" +
                "keyword='" + keyword + '\'' +
                ", community='" + community + '\'' +
                ", boardAddr='" + boardAddr + '\'' +
                ", createdDate='" + createdDate + '\'' +
                '}';
    }
}
