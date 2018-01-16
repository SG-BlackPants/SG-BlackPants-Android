package smilegate.blackpants.univscanner.data.remote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import smilegate.blackpants.univscanner.data.model.Users;

/**
 * Created by user on 2018-01-16.
 */

public interface UserApiService {
    @POST("/users")
    @FormUrlEncoded
    Call<Users> saveUser(@Field("_id") String _id,
                         @Field("name") String name,
                         @Field("university") String university);

    @GET("/posts")
    Call<List<Users>> getPost();

    @DELETE("/users/{id}")
    Call<Users> deleteUser(@Path("_id") String _id);

}
