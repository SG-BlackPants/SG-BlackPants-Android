package smilegate.blackpants.univscanner.data.remote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Semin on 2018-02-10.
 */

public interface KeywordApiService {

    @GET("/keywords/popular/{university}")
    Call<List<String>> getPopularKeywords(@Path("university") String university);
}
