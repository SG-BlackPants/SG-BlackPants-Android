package smilegate.blackpants.univscanner.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user on 2018-02-12.
 */

public class Article {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private List<ArticleMessage> articleMessage;

    public Article(String result, String code, List<ArticleMessage> articleMessage) {
        this.result = result;
        this.code = code;
        this.articleMessage = articleMessage;
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

    public List<ArticleMessage> getArticleMessage() {
        return articleMessage;
    }

    public void setArticleMessage(List<ArticleMessage> articleMessage) {
        this.articleMessage = articleMessage;
    }

    @Override
    public String toString() {
        return "Article{" +
                "result='" + result + '\'' +
                ", code='" + code + '\'' +
                ", articleMessage=" + articleMessage +
                '}';
    }
}
