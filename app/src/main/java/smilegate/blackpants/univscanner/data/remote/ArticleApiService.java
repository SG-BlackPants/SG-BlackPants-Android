package smilegate.blackpants.univscanner.data.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import smilegate.blackpants.univscanner.data.model.Article;

/**
 * Created by user on 2018-02-12.
 */

public interface ArticleApiService {

    @GET("/articles/{keyword}")
    Call<Article> getArticles(@Path("keyword") String keyword);
}
