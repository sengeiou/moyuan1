package xin.banghua.moyuan.Adapter;

import java.util.HashMap;
import java.util.Map;

public class LuntanList {
    String id = "";
    String plateid = "";
    String platename = "";
    String authid = "";
    String authnickname = "";
    String authportrait = "";
    String poi = "";
    String posttitle = "";
    String posttext = "";
    String[] postpicture = {};
    String like = "";
    String favorite = "";
    String time = "";

    String authage = "";
    String authgender = "";
    String authregion = "";
    String authproperty = "";

    String videourl = "";
    String videocover = "";
    String videowidth = "";
    String videoheight = "";

    public LuntanList(Map argsMap,String []postpicture) {
        if (argsMap.get("authage")!=null) {
            this.authage = argsMap.get("authage").toString();
        }
        if (argsMap.get("authgender")!=null) {
            this.authgender = argsMap.get("authgender").toString();
        }
        if (argsMap.get("authregion")!=null) {
            this.authregion = argsMap.get("authregion").toString();
        }
        if (argsMap.get("authproperty")!=null) {
            this.authproperty = argsMap.get("authproperty").toString();
        }
        if (argsMap.get("id")!=null) {
            this.id = argsMap.get("id").toString();
        }
        if (argsMap.get("plateid")!=null) {
            this.plateid = argsMap.get("plateid").toString();
        }
        if (argsMap.get("plateid")!=null) {
            this.plateid = argsMap.get("plateid").toString();
        }
        if (argsMap.get("platename")!=null) {
            this.platename = argsMap.get("platename").toString();
        }
        if (argsMap.get("authid")!=null) {
            this.authid = argsMap.get("authid").toString();
        }
        if (argsMap.get("authnickname")!=null) {
            this.authnickname = argsMap.get("authnickname").toString();
        }
        if (argsMap.get("authportrait")!=null) {
            this.authportrait = argsMap.get("authportrait").toString();
        }
        if (argsMap.get("poi")!=null) {
            this.poi = argsMap.get("poi").toString();
        }
        if (argsMap.get("posttitle")!=null) {
            this.posttitle = argsMap.get("posttitle").toString();
        }
        if (argsMap.get("posttext")!=null) {
            this.posttext = argsMap.get("posttext").toString();
        }
        if (argsMap.get("like")!=null) {
            this.like = argsMap.get("like").toString();
        }
        if (argsMap.get("favorite")!=null) {
            this.favorite = argsMap.get("favorite").toString();
        }
        if (argsMap.get("time")!=null) {
            this.time = argsMap.get("time").toString();
        }
        if (argsMap.get("videourl")!=null) {
            this.videourl = argsMap.get("videourl").toString();
        }
        if (argsMap.get("videocover")!=null) {
            this.videocover = argsMap.get("videocover").toString();
        }
        if (argsMap.get("videowidth")!=null) {
            this.videowidth = argsMap.get("videowidth").toString();
        }
        if (argsMap.get("videoheight")!=null) {
            this.videoheight = argsMap.get("videoheight").toString();
        }
        if (postpicture!=null) {
            this.postpicture = postpicture;
        }
    }

    public String getVideowidth() {
        return videowidth;
    }

    public void setVideowidth(String videowidth) {
        this.videowidth = videowidth;
    }

    public String getVideoheight() {
        return videoheight;
    }

    public void setVideoheight(String videoheight) {
        this.videoheight = videoheight;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getVideocover() {
        return videocover;
    }

    public void setVideocover(String videocover) {
        this.videocover = videocover;
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

    public void setPoi(String poi) {
        this.poi = poi;
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

    public String getPoi() {
        return poi;
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
