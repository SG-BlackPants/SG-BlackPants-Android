package smilegate.blackpants.univscanner.data.model;

/**
 * Created by Semin on 2018-02-16.
 */

public class Community {

    private String id;
    private String name;
    private String university;

    public Community(String id, String name) {
        this.id = id;
        this.name = name;
        this.university = null;
    }

    public Community(String id, String name, String university) {
        this.id = id;
        this.name = name;
        this.university = university;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return "Community{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", university='" + university + '\'' +
                '}';
    }
}
