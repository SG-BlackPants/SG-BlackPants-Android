package smilegate.blackpants.univscanner.data.remote;

/**
 * Created by user on 2018-02-07.
 */

public class ApiUtils {

    public static final String BASE_URL = "http://ec2-52-23-164-26.compute-1.amazonaws.com:3000/";

    public static UserApiService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(UserApiService.class);
    }

    public static FirebaseApiService getFireAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(FirebaseApiService.class);
    }
}
