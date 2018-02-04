package smilegate.blackpants.univscanner.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Semin on 2018-02-03.
 */

public class DataSearch {
    @SerializedName("content")
    @Expose
    private List<Content> content = null;

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ClassPojo [content = " + content + "]";
    }
}

