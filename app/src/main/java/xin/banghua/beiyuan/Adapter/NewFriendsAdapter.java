package xin.banghua.beiyuan.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xin.banghua.beiyuan.Personage.PersonageActivity;
import xin.banghua.beiyuan.R;
import xin.banghua.beiyuan.SharedPreferences.SharedHelper;

public class NewFriendsAdapter extends RecyclerView.Adapter<NewFriendsAdapter.ViewHolder>  {
    private static final String TAG = "NewFriendsAdapter";

    //用户id,头像，昵称

    private ArrayList<String> mUserID = new ArrayList<>();
    private ArrayList<String> mUserPortrait = new ArrayList<>();
    private ArrayList<String> mUserNickName = new ArrayList<>();
    private ArrayList<String> mUserLeaveWords = new ArrayList<>();
    private ArrayList<Integer> mUserAgree = new ArrayList<>();

    ViewHolder viewHolder_btn;

    private Context mContext;

    public NewFriendsAdapter(Context mContext,ArrayList<String> userID, ArrayList<String> userPortrait, ArrayList<String> userNickName,ArrayList<String> userLeaveWords,ArrayList<Integer> userAgree) {

        this.mUserID = userID;
        this.mUserPortrait = userPortrait;
        this.mUserNickName = userNickName;
        this.mUserLeaveWords = userLeaveWords;
        this.mUserAgree = userAgree;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_haoyouapply,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.userID.setText(mUserID.get(i));
        Glide.with(mContext)
                .asBitmap()
                .load(mUserPortrait.get(i))
                .into(viewHolder.userPortrait);
        viewHolder.userNickName.setText(mUserNickName.get(i));
        viewHolder.userLeaveWords.setText(mUserLeaveWords.get(i));

        if (mUserAgree.get(i)==0) {
            Log.d(TAG, "onBindViewHolder: 123456");
            viewHolder.agree_btn.setText("同意");
            viewHolder.agree_btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: clicked on: " + mUserID.get(i));
                    //Toast.makeText(mContext, mUserID.get(i) + mUserNickName.get(i), Toast.LENGTH_LONG).show();
                    viewHolder_btn = viewHolder;
                    agreeFriend("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=agreefriend&m=socialchat",mUserID.get(i),mUserPortrait.get(i),mUserNickName.get(i));
                }
            });
        }else {
            Log.d(TAG, "onBindViewHolder: 876543");
            viewHolder.agree_btn.setText("已同意");
        }

        viewHolder.haoyouLayout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: rongyun开始");
                //跳转个人信息
                Log.d(TAG, "onClick: rongyun开始");
                Intent intent = new Intent(v.getContext(), PersonageActivity.class);
                intent.putExtra("userID",mUserID.get(i));
                Log.d(TAG, "onClick: 跳转个人页面");
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserID.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView userID;
        CircleImageView userPortrait;
        TextView userNickName;
        TextView userLeaveWords;
        Button agree_btn;

        RelativeLayout haoyouLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userID = itemView.findViewById(R.id.userID);
            userPortrait = itemView.findViewById(R.id.authportrait);
            userNickName = itemView.findViewById(R.id.userNickName);
            userLeaveWords = itemView.findViewById(R.id.userLeaveWords);
            agree_btn = itemView.findViewById(R.id.agree_btn);


            haoyouLayout = itemView.findViewById(R.id.haoyou_layout);
        }
    }


    //网络数据部分
//处理返回的数据
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //1是用户数据，2是幻灯片
            switch (msg.what){
                case 1:
                    viewHolder_btn.agree_btn.setText("已同意");
                    break;
            }
        }
    };
    //TODO okhttp同意好友申请
    public void agreeFriend(final String url,final String yourid,final String yourportrait,final String yournickname){
        new Thread(new Runnable() {
            @Override
            public void run(){
                SharedHelper shUserInfo = new SharedHelper(mContext.getApplicationContext());
                String myid = shUserInfo.readUserInfo().get("userID");
                String mynickname = shUserInfo.readUserInfo().get("userNickName");
                String myportrait = shUserInfo.readUserInfo().get("userPortrait");
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("type", "agreefriend")
                        .add("myid",myid)
                        .add("myportrait",myportrait)
                        .add("mynickname",mynickname)
                        .add("yourid",yourid)
                        .add("yourportrait",yourportrait)
                        .add("yournickname",yournickname)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Message message=handler.obtainMessage();
                    message.obj=response.body().string();
                    message.what=1;
                    Log.d(TAG, "run: Userinfo发送的值"+message.obj.toString());
                    handler.sendMessageDelayed(message,10);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
