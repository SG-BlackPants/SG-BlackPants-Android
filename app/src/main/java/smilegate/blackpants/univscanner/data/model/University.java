package smilegate.blackpants.univscanner.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 2018-02-01.
 */

public class University {

    @SerializedName("dataSearch")
    @Expose
    private DataSearch dataSearch;

    public DataSearch getDataSearch() {
        return dataSearch;
    }

    public void setDataSearch(DataSearch dataSearch) {
        this.dataSearch = dataSearch;
    }

    @Override
    public String toString() {
        return "ClassPojo [dataSearch = " + dataSearch + "]";
    }

}
