package smilegate.blackpants.univscanner.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Semin on 2018-02-03.
 */

public class Content {
    @SerializedName("campusName")
    @Expose
    private String campusName;
    @SerializedName("collegeinfourl")
    @Expose
    private String collegeinfourl;
    @SerializedName("schoolType")
    @Expose
    private String schoolType;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("schoolGubun")
    @Expose
    private String schoolGubun;
    @SerializedName("adres")
    @Expose
    private String adres;
    @SerializedName("schoolName")
    @Expose
    private String schoolName;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("totalCount")
    @Expose
    private String totalCount;
    @SerializedName("estType")
    @Expose
    private String estType;
    @SerializedName("seq")
    @Expose
    private String seq;

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public String getCollegeinfourl() {
        return collegeinfourl;
    }

    public void setCollegeinfourl(String collegeinfourl) {
        this.collegeinfourl = collegeinfourl;
    }

    public String getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(String schoolType) {
        this.schoolType = schoolType;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSchoolGubun() {
        return schoolGubun;
    }

    public void setSchoolGubun(String schoolGubun) {
        this.schoolGubun = schoolGubun;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getEstType() {
        return estType;
    }

    public void setEstType(String estType) {
        this.estType = estType;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    @Override
    public String toString() {
        return "ClassPojo [region = " + region + ", estType = " + estType + ", schoolName = " + schoolName + ", schoolGubun = " + schoolGubun + ", totalCount = " + totalCount + ", adres = " + adres + ", link = " + link + ", campusName = " + campusName + ", seq = " + seq + ", schoolType = " + schoolType + ", collegeinfourl = " + collegeinfourl + "]";
    }
}
