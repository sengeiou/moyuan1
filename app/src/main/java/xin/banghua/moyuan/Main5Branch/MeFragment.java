package xin.banghua.moyuan.Main5Branch;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orhanobut.dialogplus.DialogPlus;

import androidx.navigation.Navigation;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xin.banghua.moyuan.CircleImageViewExtension;
import xin.banghua.moyuan.R;
import xin.banghua.moyuan.SharedPreferences.SharedHelper;
import xin.banghua.moyuan.SliderWebViewActivity;
import xin.banghua.moyuan.introduction.IntroductionActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {

    private static final String TAG = "MeFragment";
    CircleImageViewExtension userportrait_iv;
    TextView usernickname_tv;
    Button moyuanid_btn;
    Button personalinfo_btn;
    Button xiangce_btn;
    Button openvip_btn;
    Button luntan_btn;
    Button jifen_btn;
    Button tuiguangma_btn;
    Button sawme_btn;
    Button setting_btn;
    String myportrait;

    Button introduction_btn;

    private Button privacypolity_btn,useragreement_btn;
    private Context mContext;

    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData(view);
        //vip
        getVipinfo("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=viptimeinsousuo&m=moyuan");


        useragreement_btn = view.findViewById(R.id.useragreement_btn);
        useragreement_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SliderWebViewActivity.class);
                intent.putExtra("slidername","陌缘用户协议");
                intent.putExtra("sliderurl","https://www.banghua.xin/useragreement.html");
                mContext.startActivity(intent);
            }
        });
        introduction_btn = view.findViewById(R.id.introduction_btn);
        introduction_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, IntroductionActivity.class);
                mContext.startActivity(intent);
            }
        });

        privacypolity_btn = view.findViewById(R.id.privacypolicy_btn);
        privacypolity_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SliderWebViewActivity.class);
                intent.putExtra("slidername","陌缘隐私政策");
                intent.putExtra("sliderurl","https://www.banghua.xin/privacypolicy.html");
                mContext.startActivity(intent);
            }
        });
    }
    //TODO okhttp获取用户信息
    public void getVipinfo(final String url){
        new Thread(new Runnable() {
            @Override
            public void run(){
                SharedHelper shuserinfo = new SharedHelper(getActivity().getApplicationContext());
                String myid = shuserinfo.readUserInfo().get("userID");

                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("id", myid)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Message message=handler.obtainMessage();
                    message.obj=response.body().string();
                    message.what=3;
                    Log.d(TAG, "run: Userinfo发送的值"+message.obj.toString());
                    handler.sendMessageDelayed(message,10);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void initData(View view) {
        userportrait_iv = view.findViewById(R.id.userportrait_iv);
        userportrait_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ResetActivity.class);
                intent.putExtra("title","头像设置");
                startActivity(intent);
            }
        });
        usernickname_tv = view.findViewById(R.id.usernickname_tv);
        usernickname_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ResetActivity.class);
                intent.putExtra("title","昵称设置");
                startActivity(intent);
            }
        });
        moyuanid_btn = view.findViewById(R.id.moyuanid_btn);
        personalinfo_btn = view.findViewById(R.id.personalinfo_btn);
        xiangce_btn = view.findViewById(R.id.xiangce_btn);
        openvip_btn = view.findViewById(R.id.openvip_btn);
        luntan_btn= view.findViewById(R.id.luntan_btn);
        jifen_btn = view.findViewById(R.id.jifen_btn);
        tuiguangma_btn = view.findViewById(R.id.tuiguangma_btn);
        sawme_btn = view.findViewById(R.id.sawme_btn);
        setting_btn = view.findViewById(R.id.setting_btn);

        SharedHelper shuserinfo = new SharedHelper(getActivity().getApplicationContext());
        final String myid = shuserinfo.readUserInfo().get("userID");
        String mynickname = shuserinfo.readUserInfo().get("userNickName");
        myportrait = shuserinfo.readUserInfo().get("userPortrait");


        usernickname_tv.setText(mynickname);


        moyuanid_btn.setText("陌缘号："+myid);
        personalinfo_btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.me_reset_action));
        setting_btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.me_setting_action));
        xiangce_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CircleActivity.class);
                startActivity(intent);
            }
        });
        sawme_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SawMeActivity.class);
                startActivity(intent);
            }
        });

        tuiguangma_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "您的推广码是："+myid, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mContext, SliderWebViewActivity.class);
                intent.putExtra("slidername","分享推广码");
                intent.putExtra("sliderurl","https://applet.banghua.xin/app/index.php?i=99999&c=entry&do=referralgetscore_page&m=moyuan&userid="+myid);
                mContext.startActivity(intent);
            }
        });
        jifen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getScore("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=getscore&m=moyuan");
            }
        });
        openvip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),BuyvipActivity.class);
                startActivity(intent);
            }
        });
        luntan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedHelper shuserinfo = new SharedHelper(getActivity());
                String mUserID = shuserinfo.readUserInfo().get("userID");
                Intent intent = new Intent(getActivity(),SomeonesluntanActivity.class);
                intent.putExtra("authid",mUserID);
                startActivity(intent);
            }
        });
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
                    final String allscore = msg.obj.toString();
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
                            .setFooter(R.layout.dialog_foot_vipconversion)
                            .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                            .create();
                    dialog.show();
                    View view = dialog.getFooterView();
                    TextView scoreresult = view.findViewById(R.id.scoreresult);
                    scoreresult.setText("您共有"+allscore+"积分！");
                    Button vipconversion_btn = view.findViewById(R.id.vipconversion_btn);
                    vipconversion_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sorttovip("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=sorttovip&m=moyuan",allscore);
                        }
                    });
                    Button dismissdialog_btn = view.findViewById(R.id.dismissdialog_btn);
                    dismissdialog_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    break;
                case 2:
                    Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_LONG).show();
                    break;
                case 3:

                    String resultJson1 = msg.obj.toString();
                    Log.d(TAG, "handleMessage: 用户数据接收的值"+msg.obj.toString());
                    if (!(msg.obj.toString().equals("会员已到期")))  userportrait_iv.isVIP(true,getResources(),false);
                    Glide.with(mContext)
                            .asBitmap()
                            .load(myportrait)
                            .into(userportrait_iv);

                    break;
            }
        }
    };
    //TODO okhttp获取用户信息
    public void getScore(final String url){
        new Thread(new Runnable() {
            @Override
            public void run(){
                SharedHelper shuserinfo = new SharedHelper(getActivity().getApplicationContext());
                String myid = shuserinfo.readUserInfo().get("userID");

                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("userid", myid)
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

    //TODO okhttp获取用户信息
    public void sorttovip(final String url,final String allscore){
        new Thread(new Runnable() {
            @Override
            public void run(){
                SharedHelper shuserinfo = new SharedHelper(getActivity().getApplicationContext());
                String myid = shuserinfo.readUserInfo().get("userID");

                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("myid", myid)
                        .add("allscore", allscore)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Message message=handler.obtainMessage();
                    message.obj=response.body().string();
                    message.what=2;
                    Log.d(TAG, "run: Userinfo发送的值"+message.obj.toString());
                    handler.sendMessageDelayed(message,10);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
