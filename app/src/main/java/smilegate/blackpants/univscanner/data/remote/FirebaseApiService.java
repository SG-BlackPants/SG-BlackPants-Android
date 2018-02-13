package smilegate.blackpants.univscanner.data.remote;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import smilegate.blackpants.univscanner.data.model.EmailVerification;
import smilegate.blackpants.univscanner.data.model.Notification;
import smilegate.blackpants.univscanner.data.model.SendEmail;

/**
 * Created by user on 2018-02-09.
 */

public interface FirebaseApiService {

    @POST("/firebase/{uid}/email/request")
    @FormUrlEncoded
    Call<SendEmail> sendEmail(@Path("uid") String uId,
                              @Field("email") String email);

    @GET("/firebase/{uid}/email/check")
    Call<EmailVerification> getIsEmailVerified(@Path("uid") String uId);


}
