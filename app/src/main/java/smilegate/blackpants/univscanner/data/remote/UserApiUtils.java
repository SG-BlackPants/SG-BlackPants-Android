package smilegate.blackpants.univscanner.data.remote;

/**
 * Created by user on 2018-01-16.
 */

public class UserApiUtils {
    private UserApiUtils() {}

    public static final String BASE_URL = "http://52.78.22.122:3000/";

    public static UserApiService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(UserApiService.class);
    }
}

