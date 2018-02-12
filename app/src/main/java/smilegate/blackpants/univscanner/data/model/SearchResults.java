package smilegate.blackpants.univscanner.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2018-01-25.
 */

public class SearchResults implements Parcelable {

    @SerializedName("community")
    @Expose
    private String id;
    @SerializedName("result")
    @Expose
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

    public SearchResults(Parcel parcel) {
        readFromParcel(parcel);
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

    @Override
    public String toString() {
        return "SearchResults{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", content='" + content + '\'' +
                ", community='" + community + '\'' +
                ", boardAddr='" + boardAddr + '\'' +
                ", author='" + author + '\'' +
                ", url='" + url + '\'' +
                ", images=" + images +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        // We just need to write each field into the
        // parcel. When we read from parcel, they
        // will come back in the same order
        dest.writeString(id);
        dest.writeString(community);
        dest.writeString(boardAddr);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(createdDate);
        dest.writeList(images);
    }

    /**
     *
     * Called from the constructor to create this
     * object from a parcel.
     *
     * @param in parcel from which to re-create object
     */
    public void readFromParcel(Parcel in) {

        // We just need to read back each
        // field in the order that it was
        // written to the parcel
        id = in.readString();
        community = in.readString();
        boardAddr = in.readString();
        title = in.readString();
        author = in.readString();
        content = in.readString();
        createdDate = in.readString();
        images = new ArrayList<String>();
        in.readList(images, null);
    }

    /**
     *
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays.
     *
     * This also means that you can use use the default
     * constructor to create the object and use another
     * method to hyrdate it as necessary.
     *
     * I just find it easier to use the constructor.
     * It makes sense for the way my brain thinks ;-)
     *
     */
    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public SearchResults createFromParcel(Parcel in) {
                    return new SearchResults(in);
                }

                public SearchResults[] newArray(int size) {
                    return new SearchResults[size];
                }
            };

}
