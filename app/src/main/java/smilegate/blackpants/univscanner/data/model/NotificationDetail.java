package smilegate.blackpants.univscanner.data.model;

/**
 * Created by Semin on 2018-02-16.
 */

public class NotificationDetail {

    private String keyword;

    private String communityId;

    private String communityName;

    private String boardAddr;

    private String createdDate;

    public NotificationDetail(String keyword, String communityId, String communityName, String boardAddr, String createdDate) {
        this.keyword = keyword;
        this.communityId = communityId;
        this.communityName = communityName;
        this.boardAddr = boardAddr;
        this.createdDate = createdDate;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getBoardAddr() {
        return boardAddr;
    }

    public void setBoardAddr(String boardAddr) {
        this.boardAddr = boardAddr;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
