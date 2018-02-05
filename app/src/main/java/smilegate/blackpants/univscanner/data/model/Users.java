package smilegate.blackpants.univscanner.data.model;

/**
 * Created by user on 2018-01-16.
 */

public class Users {

    private String email;

    private String name;

    private String university;

    private String registrationToken;

    private String loginToken;

    public Users(String email, String name, String university, String registrationToken, String loginToken) {
        this.email = email;
        this.name = name;
        this.university = university;
        this.registrationToken = registrationToken;
        this.loginToken = loginToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String _id) {
        this.email = email;
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

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    @Override
    public String toString() {
        return "Users{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", university=" + university +
                ", registrationToken=" + registrationToken +
                ", loginToken=" + loginToken +
                '}';
    }
}
