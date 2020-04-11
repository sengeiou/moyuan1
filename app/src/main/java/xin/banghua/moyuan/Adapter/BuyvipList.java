package xin.banghua.moyuan.Adapter;

public class BuyvipList {

    String vipid,vipname,viptime,vipprice;

    public BuyvipList(String vipid, String vipname, String viptime, String vipprice) {
        this.vipid = vipid;
        this.vipname = vipname;
        this.viptime = viptime;
        this.vipprice = vipprice;
    }

    public String getVipid() {
        return vipid;
    }

    public void setVipid(String vipid) {
        this.vipid = vipid;
    }

    public String getVipname() {
        return vipname;
    }

    public void setVipname(String vipname) {
        this.vipname = vipname;
    }

    public String getViptime() {
        return viptime;
    }

    public void setViptime(String viptime) {
        this.viptime = viptime;
    }

    public String getVipprice() {
        return vipprice;
    }

    public void setVipprice(String vipprice) {
        this.vipprice = vipprice;
    }
}
