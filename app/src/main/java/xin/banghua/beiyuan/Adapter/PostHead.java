package xin.banghua.beiyuan.Adapter;

public class PostHead {
    String posttitle,authid,authnickname,authportrait,posttext,postpicture1,postpicture2,postpicture3,time,followtext,followpicture1,followpicture2,followpicture3;

    public PostHead(String posttitle, String authid, String authnickname, String authportrait, String posttext, String postpicture1, String postpicture2, String postpicture3, String time) {
        this.posttitle = posttitle;
        this.authid = authid;
        this.authnickname = authnickname;
        this.authportrait = authportrait;
        this.posttext = posttext;
        this.postpicture1 = postpicture1;
        this.postpicture2 = postpicture2;
        this.postpicture3 = postpicture3;
        this.time = time;
    }

    public PostHead(String authid, String authnickname, String authportrait, String followtext, String followpicture1, String followpicture2, String followpicture3, String time) {
        this.authid = authid;
        this.authnickname = authnickname;
        this.authportrait = authportrait;
        this.followtext = followtext;
        this.followpicture1 = followpicture1;
        this.followpicture2 = followpicture2;
        this.followpicture3 = followpicture3;
        this.time = time;
    }

    public String getFollowtext() {
        return followtext;
    }

    public void setFollowtext(String followtext) {
        this.followtext = followtext;
    }

    public String getFollowpicture1() {
        return followpicture1;
    }

    public void setFollowpicture1(String followpicture1) {
        this.followpicture1 = followpicture1;
    }

    public String getFollowpicture2() {
        return followpicture2;
    }

    public void setFollowpicture2(String followpicture2) {
        this.followpicture2 = followpicture2;
    }

    public String getFollowpicture3() {
        return followpicture3;
    }

    public void setFollowpicture3(String followpicture3) {
        this.followpicture3 = followpicture3;
    }

    public String getPosttitle() {
        return posttitle;
    }

    public void setPosttitle(String posttitle) {
        this.posttitle = posttitle;
    }

    public String getAuthid() {
        return authid;
    }

    public void setAuthid(String authid) {
        this.authid = authid;
    }

    public String getAuthnickname() {
        return authnickname;
    }

    public void setAuthnickname(String authnickname) {
        this.authnickname = authnickname;
    }

    public String getAuthportrait() {
        return authportrait;
    }

    public void setAuthportrait(String authportrait) {
        this.authportrait = authportrait;
    }

    public String getPosttext() {
        return posttext;
    }

    public void setPosttext(String posttext) {
        this.posttext = posttext;
    }

    public String getPostpicture1() {
        return postpicture1;
    }

    public void setPostpicture1(String postpicture1) {
        this.postpicture1 = postpicture1;
    }

    public String getPostpicture2() {
        return postpicture2;
    }

    public void setPostpicture2(String postpicture2) {
        this.postpicture2 = postpicture2;
    }

    public String getPostpicture3() {
        return postpicture3;
    }

    public void setPostpicture3(String postpicture3) {
        this.postpicture3 = postpicture3;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
