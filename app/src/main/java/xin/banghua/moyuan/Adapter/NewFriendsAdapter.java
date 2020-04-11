package xin.banghua.moyuan.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.dialogplus.DialogPlus;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xin.banghua.moyuan.Personage.PersonageActivity;
import xin.banghua.moyuan.R;
import xin.banghua.moyuan.SharedPreferences.SharedHelper;

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
                    if (mUserAgree.get(i)==0){
                    Log.d(TAG, "是否已同意: " + mUserAgree.get(i));
                    //Toast.makeText(mContext, mUserID.get(i) + mUserNickName.get(i), Toast.LENGTH_LONG).show();
                    viewHolder_btn = viewHolder;
                    agreeFriend("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=agreefriend&m=moyuan",mUserID.get(i),mUserPortrait.get(i),mUserNickName.get(i));
                    mUserAgree.set(i,1);
                    viewHolder.agree_btn.setText("已同意");
                    }
                }
            });
        }else {
            Log.d(TAG, "onBindViewHolder: 876543");
            viewHolder.agree_btn.setText("已同意");
        }

        viewHolder.haoyouapply_layout.setOnClickListener(new View.OnClickListener(){

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

        viewHolder.haoyouapply_layout.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "长按中");
                final DialogPlus dialog = DialogPlus.newDialog(mContext)
                        .setAdapter(new BaseAdapter() {
                            @Override
                            public int getCount() {
                                return 0;
                            }

                            @Override
                            public Object getItem(int position) {
                                return null;
                            }

                            @Override
                            public long getItemId(int position) {
                                return 0;
                            }

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                return null;
                            }
                        })
                        .setFooter(R.layout.dialog_foot_confirm)
                        .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();
                dialog.show();
                View view = dialog.getFooterView();
                TextView prompt = view.findViewById(R.id.prompt_tv);
                prompt.setText("确定要删除吗？");
                Button dismissdialog_btn = view.findViewById(R.id.cancel_btn);
                dismissdialog_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button confirm_btn = view.findViewById(R.id.confirm_btn);
                confirm_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteFriendNumber(i,mUserID.get(i),"https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=DeleteFriendsapply&m=moyuan");
                        dialog.dismiss();
                    }
                });
                return true;
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

        RelativeLayout haoyouapply_layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userID = itemView.findViewById(R.id.userID);
            userPortrait = itemView.findViewById(R.id.authportrait);
            userNickName = itemView.findViewById(R.id.userNickName);
            userLeaveWords = itemView.findViewById(R.id.userLeaveWords);
            agree_btn = itemView.findViewById(R.id.agree_btn);


            haoyouapply_layout = itemView.findViewById(R.id.haoyouapply_layout);
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
                case 2:
                    mUserID.remove((int)msg.obj);//删除数据源,移除集合中当前下标的数据
                    mUserPortrait.remove((int)msg.obj);
                    mUserNickName.remove((int)msg.obj);
                    mUserLeaveWords.remove((int)msg.obj);
                    mUserAgree.remove((int)msg.obj);
                    notifyItemRemoved((int)msg.obj);//刷新被删除的地方
                    notifyItemRangeChanged((int)msg.obj,getItemCount()); //刷新被删除数据，以及其后面的数据
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


    //TODO okhttp获取好友人数
    public void deleteFriendNumber(final int position,final String yourid,final String url){
        new Thread(new Runnable() {
            @Override
            public void run(){
                SharedHelper shuserinfo = new SharedHelper(mContext.getApplicationContext());
                String myid = shuserinfo.readUserInfo().get("userID");
                Log.d(TAG, "删除新好友myid"+myid+"yourid"+yourid);
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("myid", yourid)
                        .add("yourid", myid)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Message message=handler.obtainMessage();
                    message.obj=position;
                    message.what=2;
                    Log.d(TAG, "run: 查看返回值"+response.body().string());
                    handler.sendMessageDelayed(message,10);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
