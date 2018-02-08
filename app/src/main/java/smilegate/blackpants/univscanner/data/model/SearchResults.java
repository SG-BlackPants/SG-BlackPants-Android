package smilegate.blackpants.univscanner.data.model;

import java.util.List;

/**
 * Created by user on 2018-01-25.
 */

public class SearchResults {

    private String id;
    private String title;
    private String createdDate;
    private String content;
    private String community;
    private String boardAddr;
    private String author;
    private String url;
    private List<String> images;


    public SearchResults(String id, String title, String createdDate, String content, String community, String boardAddr,  String author, String url, List<String> images) {
        this.id = id;
        this.community = community;
        this.boardAddr = boardAddr;
        this.title = title;
        this.author = author;
        this.content = content;
        this.images = images;
        this.url = url;
        this.createdDate = createdDate;
    }

    public String get_id() {
        return id;
    }

    public void set_id(String _id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
