package smilegate.blackpants.univscanner.data.model;

/**
 * Created by user on 2018-01-24.
 */

public class Keywords {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Users{" +
                ", name='" + name + '\'' +
                '}';
    }
}
