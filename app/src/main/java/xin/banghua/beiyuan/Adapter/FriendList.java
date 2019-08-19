package xin.banghua.beiyuan.Adapter;

public class FriendList {
    String mUserID;
    String mUserPortrait;
    String mUserNickName;
    public FriendList(String userID, String userPotrait, String userNickName){
        this.mUserID = userID;
        this.mUserPortrait = userPotrait;
        this.mUserNickName = userNickName;
    }

    public String getmUserID() {
        return mUserID;
    }

    public String getmUserNickName() {
        return mUserNickName;
    }

    public String getmUserPortrait() {
        return mUserPortrait;
    }

    public void setmUserID(String mUserID) {
        this.mUserID = mUserID;
    }

    public void setmUserPortrait(String mUserPortrait) {
        this.mUserPortrait = mUserPortrait;
    }

    public void setmUserNickName(String mUserNickName) {
        this.mUserNickName = mUserNickName;
    }
}
