package smilegate.blackpants.univscanner.data.remote;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import smilegate.blackpants.univscanner.data.model.Article;

/**
 * Created by user on 2018-02-12.
 */

public interface ArticleApiService {

    @POST("/articles/{keyword}")
    @FormUrlEncoded
    Call<Article> getArticles(@Path("keyword") String keyword, @Field("userToken") String userToken);
}
