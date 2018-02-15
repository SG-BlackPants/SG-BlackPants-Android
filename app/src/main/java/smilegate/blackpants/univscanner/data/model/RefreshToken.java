package smilegate.blackpants.univscanner.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Semin on 2018-02-15.
 */

public class RefreshToken {

    @SerializedName("userToken")
    @Expose
    private String userToken;
    @SerializedName("registrationToken")
    @Expose
    private String registrationToken;

    public RefreshToken(String userToken, String registrationToken) {
        this.userToken = userToken;
        this.registrationToken = registrationToken;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "userToken='" + userToken + '\'' +
                ", registrationToken='" + registrationToken + '\'' +
                '}';
    }
}
