package smilegate.blackpants.univscanner.data.remote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import smilegate.blackpants.univscanner.data.model.Users;

/**
 * Created by user on 2018-01-16.
 */

public interface UserApiService {
    @POST("/posts")
    @FormUrlEncoded
    Call<Users> savePost(@Field("title") String title,
                         @Field("body") String body,
                         @Field("userId") long userId);

    @GET("/posts")
    Call<List<Users>> getPost();

}
