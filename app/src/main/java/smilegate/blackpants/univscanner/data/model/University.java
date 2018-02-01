package smilegate.blackpants.univscanner.data.model;

/**
 * Created by user on 2018-02-01.
 */

public class University {
    private String schoolName;

    public University(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getName() {
        return schoolName;
    }

    public void setName(String schoolName) {
        this.schoolName = schoolName;
    }

    @Override
    public String toString() {
        return "University{" +
                "name='" + schoolName + '\'' +
                '}';
    }
}
