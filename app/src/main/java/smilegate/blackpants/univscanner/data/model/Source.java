package smilegate.blackpants.univscanner.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user on 2018-02-12.
 */

public class Source {

    @SerializedName("community")
    @Expose
    private String community;
    @SerializedName("boardAddr")
    @Expose
    private String boardAddr;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("images")
    @Expose
    private List<String> images;

    public Source(String community, String boardAddr, String author, String content, String createdDate, String title, List<String> images) {
        this.community = community;
        this.boardAddr = boardAddr;
        this.author = author;
        this.content = content;
        this.createdDate = createdDate;
        this.title = title;
        this.images = images;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Source{" +
                "community='" + community + '\'' +
                ", boardAddr='" + boardAddr + '\'' +
                ", author='" + author + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", images=" + images +
                '}';
    }
}
