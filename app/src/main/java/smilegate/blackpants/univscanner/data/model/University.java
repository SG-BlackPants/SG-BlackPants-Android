package smilegate.blackpants.univscanner.data.model;

import java.util.List;

/**
 * Created by user on 2018-02-01.
 */

public class University {

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

    public class DataSearch {
        private List<Content> content;

        public List<Content> getContent() {
            return content;
        }

        public void setContent(List<Content> content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "ClassPojo [content = " + content + "]";
        }
    }

    public class Content {
        private String region;

        private String estType;

        private String schoolName;

        private String schoolGubun;

        private String totalCount;

        private String adres;

        private String link;

        private String campusName;

        private String seq;

        private String schoolType;

        private String collegeinfourl;

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getEstType() {
            return estType;
        }

        public void setEstType(String estType) {
            this.estType = estType;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public String getSchoolGubun() {
            return schoolGubun;
        }

        public void setSchoolGubun(String schoolGubun) {
            this.schoolGubun = schoolGubun;
        }

        public String getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(String totalCount) {
            this.totalCount = totalCount;
        }

        public String getAdres() {
            return adres;
        }

        public void setAdres(String adres) {
            this.adres = adres;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getCampusName() {
            return campusName;
        }

        public void setCampusName(String campusName) {
            this.campusName = campusName;
        }

        public String getSeq() {
            return seq;
        }

        public void setSeq(String seq) {
            this.seq = seq;
        }

        public String getSchoolType() {
            return schoolType;
        }

        public void setSchoolType(String schoolType) {
            this.schoolType = schoolType;
        }

        public String getCollegeinfourl() {
            return collegeinfourl;
        }

        public void setCollegeinfourl(String collegeinfourl) {
            this.collegeinfourl = collegeinfourl;
        }

        @Override
        public String toString() {
            return "ClassPojo [region = " + region + ", estType = " + estType + ", schoolName = " + schoolName + ", schoolGubun = " + schoolGubun + ", totalCount = " + totalCount + ", adres = " + adres + ", link = " + link + ", campusName = " + campusName + ", seq = " + seq + ", schoolType = " + schoolType + ", collegeinfourl = " + collegeinfourl + "]";
        }
    }
}
