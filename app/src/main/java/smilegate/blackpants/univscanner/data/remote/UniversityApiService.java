package smilegate.blackpants.univscanner.data.remote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import smilegate.blackpants.univscanner.data.model.University;

/**
 * Created by user on 2018-02-01.
 */

public interface UniversityApiService {

    @GET("/getOpenApi?apiKey=48bc56d5d3c0ce3568aeb43ed3133924&svcType=api&svcCode=SCHOOL&contentType=json&gubun=univ_list&thisPage=1&perPage=448")
    Call<List<University>> getUniversity();

    @GET("/getOpenApi?apiKey=48bc56d5d3c0ce3568aeb43ed3133924&svcType=api&svcCode=SCHOOL&contentType=json&gubun=univ_list&thisPage=1&perPage=448")
    @FormUrlEncoded
    Call<List<University>> saveUser(@Field("university") String university);
}