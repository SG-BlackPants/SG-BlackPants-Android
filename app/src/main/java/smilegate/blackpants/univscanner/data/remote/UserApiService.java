package smilegate.blackpants.univscanner.data.remote;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import smilegate.blackpants.univscanner.data.model.Users;

/**
 * Created by user on 2018-01-16.
 */

public interface UserApiService {
    @POST("/users")
    @FormUrlEncoded
    Call<Users> setUsers(@Field("userToken") String userToken,
                         @Field("registrationToken") String registrationToken,
                         @Field("name") String name,
                         @Field("university") String university);

    @DELETE("/users/{id}")
    Call<Users> deleteUser(@Path("email") String email);

}
