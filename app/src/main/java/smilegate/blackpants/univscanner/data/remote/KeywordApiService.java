package smilegate.blackpants.univscanner.data.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import smilegate.blackpants.univscanner.data.model.KeywordRank;

/**
 * Created by Semin on 2018-02-10.
 */

public interface KeywordApiService {

    @GET("/keywords/rank/{university}")
    Call<KeywordRank> getPopularKeywords(@Path("university") String university);
}
