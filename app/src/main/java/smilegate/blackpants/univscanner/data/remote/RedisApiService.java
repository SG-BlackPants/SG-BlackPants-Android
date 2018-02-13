package smilegate.blackpants.univscanner.data.remote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import smilegate.blackpants.univscanner.data.model.Notification;

/**
 * Created by user on 2018-02-13.
 */

public interface RedisApiService {

    @GET("/redis/rank/search/{university}/{prefix}")
    Call<List<String>> getAutocompleteKeywords(@Path("university") String university, @Path("prefix") String prefix);

    @POST("/redis/rank/push")
    @FormUrlEncoded
    Call<Notification> getPushHistory(@Field("userToken") String userToken);
}

