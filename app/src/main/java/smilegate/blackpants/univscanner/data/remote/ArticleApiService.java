package smilegate.blackpants.univscanner.data.remote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import smilegate.blackpants.univscanner.data.model.Article;
import smilegate.blackpants.univscanner.data.model.NotificationArticle;

/**
 * Created by user on 2018-02-12.
 */

public interface ArticleApiService {

    @POST("/articles/{keyword}")
    @FormUrlEncoded
    Call<Article> getArticles(@Path("keyword") String keyword,
                              @Field("userToken") String userToken,
                              @Field("community") List<String> community,
                              @Field("startDate") String startDate,
                              @Field("endDate") String endDate,
                              @Field("secondWord") String secondWord);

    @GET("/articles/{communityId}/{boardAddr}")
    Call<NotificationArticle> getNotificationDetail(@Path("communityId") String communityId, @Path("boardAddr") String boardAddr);
}
