package xin.banghua.beiyuan.Adapter;

public class PostList {
    String authid,authnickname,authportrait,followtext,followpicture1,followpicture2,followpicture3,time;

    public PostList(String authid, String authnickname, String authportrait, String followtext, String followpicture1, String followpicture2, String followpicture3, String time) {
        this.authid = authid;
        this.authnickname = authnickname;
        this.authportrait = authportrait;
        this.followtext = followtext;
        this.followpicture1 = followpicture1;
        this.followpicture2 = followpicture2;
        this.followpicture3 = followpicture3;
        this.time = time;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
