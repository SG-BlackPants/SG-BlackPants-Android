package smilegate.blackpants.univscanner.data.model;

/**
 * Created by user on 2018-01-16.
 */

public class Users {

    private String _id;

    private String name;

    private String university;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", university=" + university +
                '}';
    }
}
