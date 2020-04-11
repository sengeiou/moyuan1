package xin.banghua.moyuan.Adapter;

public class DongtaiList {
    String id,myid,mynickname,myportrait,context,picture1,picture2,picture3,video,share,like,time;
    String authage;
    String authgender;
    String authregion;
    String authproperty;

    public DongtaiList(String authage,String authgender,String authregion,String authproperty,String id, String myid, String mynickname, String myportrait, String context, String picture1,String picture2,String picture3, String video, String share, String like, String time) {
        this.authage = authage;
        this.authgender = authgender;
        this.authregion = authregion;
        this.authproperty = authproperty;
        this.id = id;
        this.myid = myid;
        this.mynickname = mynickname;
        this.myportrait = myportrait;
        this.context = context;
        this.picture1 = picture1;
        this.picture2 = picture2;
        this.picture3 = picture3;
        this.video = video;
        this.share = share;
        this.like = like;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMyid() {
        return myid;
    }

    public void setMyid(String myid) {
        this.myid = myid;
    }

    public String getMynickname() {
        return mynickname;
    }

    public void setMynickname(String mynickname) {
        this.mynickname = mynickname;
    }

    public String getMyportrait() {
        return myportrait;
    }

    public void setMyportrait(String myportrait) {
        this.myportrait = myportrait;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getPicture1() {
        return picture1;
    }

    public void setPicture1(String picture1) {
        this.picture1 = picture1;
    }

    public String getPicture2() {
        return picture2;
    }

    public void setPicture2(String picture2) {
        this.picture2 = picture2;
    }

    public String getPicture3() {
        return picture3;
    }

    public void setPicture3(String picture3) {
        this.picture3 = picture3;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAuthage() {
        return authage;
    }

    public void setAuthage(String authage) {
        this.authage = authage;
    }

    public String getAuthgender() {
        return authgender;
    }

    public void setAuthgender(String authgender) {
        this.authgender = authgender;
    }

    public String getAuthregion() {
        return authregion;
    }

    public void setAuthregion(String authregion) {
        this.authregion = authregion;
    }

    public String getAuthproperty() {
        return authproperty;
    }

    public void setAuthproperty(String authproperty) {
        this.authproperty = authproperty;
    }
}
