package smilegate.blackpants.univscanner.data.remote;

/**
 * Created by user on 2018-02-01.
 */

public class UniversityApiUtils {
    private UniversityApiUtils() {}

    public static final String BASE_URL = "http://www.career.go.kr/cnet/openapi/";

    public static UniversityApiService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(UniversityApiService.class);
    }
}
