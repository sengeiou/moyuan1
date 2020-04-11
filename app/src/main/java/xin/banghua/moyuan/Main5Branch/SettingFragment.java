package xin.banghua.moyuan.Main5Branch;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.orhanobut.dialogplus.DialogPlus;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xin.banghua.moyuan.R;
import xin.banghua.moyuan.SharedPreferences.SharedHelper;
import xin.banghua.moyuan.Signin.SigninActivity;

import static io.rong.imkit.fragment.ConversationListFragment.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    Button phone_reset;
    Button personalinfo_btn;
    Button sound_set;
    Button email_reset;
    Button password_reset;
    Button private_set;
    Button common_set;
    Button feedback_btn;
    Button help_btn;
    Button version_btn;
    Button logout_btn;
    Button accountdelete_btn;

    private Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();


    }
    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("设置");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        super.onViewCreated(view, savedInstanceState);
        phone_reset = view.findViewById(R.id.phone_reset);
        personalinfo_btn = view.findViewById(R.id.personalinfo_btn);
        sound_set = view.findViewById(R.id.sound_set);
        email_reset = view.findViewById(R.id.email_reset);
        password_reset = view.findViewById(R.id.password_reset);
        private_set = view.findViewById(R.id.private_set);
        common_set = view.findViewById(R.id.common_set);
        feedback_btn = view.findViewById(R.id.feedback_btn);
        help_btn = view.findViewById(R.id.help_btn);
        version_btn = view.findViewById(R.id.version_btn);
        logout_btn = view.findViewById(R.id.logout_btn);
        accountdelete_btn = view.findViewById(R.id.accountdelete_btn);

        phone_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ResetActivity.class);
                intent.putExtra("title","手机绑定");
                startActivity(intent);
            }
        });
        personalinfo_btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.setting_reset_action));
        sound_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,SoundActivity.class);
                intent.putExtra("title","关于声音");
                startActivity(intent);
            }
        });
        email_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ResetActivity.class);
                intent.putExtra("title","邮箱绑定");
                startActivity(intent);
            }
        });
        password_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ResetActivity.class);
                intent.putExtra("title","密码重置");
                startActivity(intent);
            }
        });
        private_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,PrivateSettingActivity.class);
                intent.putExtra("title","隐私设置");
                startActivity(intent);
            }
        });
        common_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,CommonSettingActivity.class);
                intent.putExtra("title","通用设置");
                startActivity(intent);
            }
        });
        feedback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ResetActivity.class);
                intent.putExtra("title","意见反馈");
                startActivity(intent);
            }
        });
        help_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "功能维护中", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mContext, HelpCenter.class);
                startActivity(intent);
            }
        });
        version_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "当前版本：8.0", Toast.LENGTH_LONG).show();
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = mContext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("userID", "");
                editor.commit();
                Intent intent = new Intent(mContext, SigninActivity.class);
                startActivity(intent);
            }
        });
        accountdelete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                prompt.setText("确定要删除此账号吗？");
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
                        setAccountdelete_btn("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=Accountdelete&m=moyuan");
                        dialog.dismiss();
                    }
                });

            }
        });


    }

    //TODO okhttp获取用户信息
    public void setAccountdelete_btn(final String url){
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
                    message.what=1;
                    Log.d(TAG, "run: 删除结果"+message.obj.toString());
                    handler.sendMessageDelayed(message,10);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (msg.obj.toString().equals("删除成功")){
                        Toast.makeText(getActivity().getApplicationContext(), "删除成功", Toast.LENGTH_LONG).show();
                        SharedPreferences sp = mContext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("userID", "");
                        editor.commit();
                        Intent intent = new Intent(mContext, SigninActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getActivity().getApplicationContext(), "删除失败", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };
    @Override  //菜单的点击，其中返回键的id是android.R.id.home
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
