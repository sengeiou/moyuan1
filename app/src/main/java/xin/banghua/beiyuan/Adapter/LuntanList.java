package xin.banghua.beiyuan.Adapter;

public class LuntanList {
    String id;
    String plateid;
    String platename;
    String authid;
    String authnickname;
    String authportrait;
    String posttip;
    String posttitle;
    String posttext;
    String[] postpicture;
    String like;
    String favorite;
    String time;

    public LuntanList(String id, String plateid, String platename, String authid, String authnickname, String authportrait, String posttip, String posttitle, String posttext, String[] postpicture, String like, String favorite, String time) {
        this.id = id;
        this.plateid = plateid;
        this.platename = platename;
        this.authid = authid;
        this.authnickname = authnickname;
        this.authportrait = authportrait;
        this.posttip = posttip;
        this.posttitle = posttitle;
        this.posttext = posttext;
        this.postpicture = postpicture;
        this.like = like;
        this.favorite = favorite;
        this.time = time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPlateid(String plateid) {
        this.plateid = plateid;
    }

    public void setPlatename(String platename) {
        this.platename = platename;
    }

    public void setAuthid(String authid) {
        this.authid = authid;
    }

    public void setAuthnickname(String authnickname) {
        this.authnickname = authnickname;
    }

    public void setAuthportrait(String authportrait) {
        this.authportrait = authportrait;
    }

    public void setPosttip(String posttip) {
        this.posttip = posttip;
    }

    public void setPosttitle(String posttitle) {
        this.posttitle = posttitle;
    }

    public void setPosttext(String posttext) {
        this.posttext = posttext;
    }

    public void setPostpicture(String[] postpicture) {
        this.postpicture = postpicture;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getPlateid() {
        return plateid;
    }

    public String getPlatename() {
        return platename;
    }

    public String getAuthid() {
        return authid;
    }

    public String getAuthnickname() {
        return authnickname;
    }

    public String getAuthportrait() {
        return authportrait;
    }

    public String getPosttip() {
        return posttip;
    }

    public String getPosttitle() {
        return posttitle;
    }

    public String getPosttext() {
        return posttext;
    }

    public String[] getPostpicture() {
        return postpicture;
    }

    public String getLike() {
        return like;
    }

    public String getFavorite() {
        return favorite;
    }

    public String getTime() {
        return time;
    }
}
