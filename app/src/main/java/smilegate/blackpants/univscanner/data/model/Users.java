package smilegate.blackpants.univscanner.data.model;

/**
 * Created by user on 2018-01-16.
 */

public class Users {

    private String userToken;

    private String registrationToken;

    private String name;

    private String university;

    public Users(String userToken, String registrationToken, String name, String university) {
        this.userToken = userToken;
        this.registrationToken = registrationToken;
        this.name = name;
        this.university = university;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userToken='" + userToken + '\'' +
                ", registrationToken='" + registrationToken + '\'' +
                ", name='" + name + '\'' +
                ", university='" + university + '\'' +
                '}';
    }
}
