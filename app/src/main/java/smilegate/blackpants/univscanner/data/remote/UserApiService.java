package smilegate.blackpants.univscanner.data.remote;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import smilegate.blackpants.univscanner.data.model.LoginInfo;
import smilegate.blackpants.univscanner.data.model.Push;
import smilegate.blackpants.univscanner.data.model.RefreshToken;
import smilegate.blackpants.univscanner.data.model.Users;

/**
 * Created by user on 2018-01-16.
 */

public interface UserApiService {

    @GET("/users/{uid}")
    Call<LoginInfo> getLoginInfo(@Path("uid") String uId);

    @GET("/users/{uid}/recently")
    Call<List<String>> getRecentlyKeywords(@Path("uid") String uId);

    @POST("/users")
    @FormUrlEncoded
    Call<Users> setUsers(@Field("userToken") String userToken,
                         @Field("registrationToken") String registrationToken,
                         @Field("name") String name,
                         @Field("university") String university);

    @DELETE("/users/{uid}")
    Call<Users> deleteUser(@Path("uid") String uid);

    @PUT("/users/{uid}/keyword/push")
    Call<ResponseBody> pushKeyword(@Path("uid") String uid, @Body Push push);

    @PUT("/users/{uid}/keyword/pop")
    Call<ResponseBody> popKeyword(@Path("uid") String uid, @Body Push push);

    @PUT("/users/{uid}/refreshToken")
    Call<ResponseBody> setRefreshRegistrationToken(@Path("uid") String uid, @Body RefreshToken refreshToken);
}


