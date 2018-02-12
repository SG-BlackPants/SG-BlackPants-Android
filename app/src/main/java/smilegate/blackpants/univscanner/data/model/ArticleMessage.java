package smilegate.blackpants.univscanner.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 2018-02-12.
 */

public class ArticleMessage {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("_source")
    @Expose
    private Source source;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "SubHits{" +
                "id='" + id + '\'' +
                ", source=" + source +
                '}';
    }
}
