package smilegate.blackpants.univscanner.data.model;

/**
 * Created by user on 2018-01-24.
 */

public class Keywords {

    private String name;

    public Keywords(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void Keywords() {}

    @Override
    public String toString() {
        return "Users{" +
                ", name='" + name + '\'' +
                '}';
    }
}
