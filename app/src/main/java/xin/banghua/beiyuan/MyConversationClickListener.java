package xin.banghua.beiyuan;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import xin.banghua.beiyuan.Personage.PersonageActivity;

public class MyConversationClickListener implements RongIM.ConversationClickListener {
    private static final String TAG = "MyConversationClickList";
    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
        Log.d(TAG, "onUserPortraitClick: 点击头像了");
        //Toast.makeText(mContext,mUserID.get(i)+mUserNickName.get(i),Toast.LENGTH_LONG).show();
        Intent intent = new Intent(context, PersonageActivity.class);
        intent.putExtra("userID",userInfo.getUserId());
        context.startActivity(intent);
        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
        return false;
    }

    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        return false;
    }

    @Override
    public boolean onMessageLinkClick(Context context, String s, Message message) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        return false;
    }
}
