package smilegate.blackpants.univscanner.data.model;

/**
 * Created by user on 2018-02-06.
 */

public class KeywordRank {

    private int rank;

    private String name;

    public KeywordRank(int rank, String name) {
        this.rank = rank;
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "KeywordRank{" +
                "rank='" + rank + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
