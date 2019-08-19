package xin.banghua.beiyuan.Main5Branch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xin.banghua.beiyuan.Main5Activity;
import xin.banghua.beiyuan.R;
import xin.banghua.beiyuan.SharedPreferences.SharedHelper;
import xin.banghua.beiyuan.Signin.Userset;

public class ResetActivity extends AppCompatActivity {

    private static final String TAG = "ResetActivity";
    //var
    Button submit_btn;
    EditText value_et;
    TextView title_tv;
    CircleImageView portrait;
    RadioGroup userGender;
    RadioGroup userProperty;

    String title,userPortrait,value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        title = getIntent().getStringExtra("title");


        submit_btn = findViewById(R.id.submit_btn);
        value_et = findViewById(R.id.value_et);
        value_et.setHint(title);
        title_tv = findViewById(R.id.title_tv);
        title_tv.setText(title);
        portrait = findViewById(R.id.portrait);
        userGender = findViewById(R.id.userGender);
        userProperty = findViewById(R.id.userProperty);



        if (title.equals("头像设置")){
            SharedHelper shuserinfo = new SharedHelper(getApplicationContext());
            String myportrait = shuserinfo.readUserInfo().get("userPortrait");
            value_et.setVisibility(View.GONE);
            portrait.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .asBitmap()
                    .load(myportrait)
                    .into(portrait);
        }
        if (title.equals("性别设置")){
            value_et.setVisibility(View.GONE);
            userGender.setVisibility(View.VISIBLE);
        }
        if (title.equals("属性设置")){
            value_et.setVisibility(View.GONE);
            userProperty.setVisibility(View.VISIBLE);
        }

        portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImagePicker.Builder(ResetActivity.this)
                        .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                        .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                        .directory(ImagePicker.Directory.DEFAULT)
                        .extension(ImagePicker.Extension.PNG)
                        .scale(600, 600)
                        .allowMultipleImages(false)
                        .enableDebuggingMode(true)
                        .build();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (title){
                    case "头像设置":
                        setUserPortrait("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=reset&m=socialchat");
                        break;
                    case "性别设置":
                        submitValue("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=reset&m=socialchat","性别");
                        break;
                    case "属性设置":
                        submitValue("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=reset&m=socialchat","属性");
                        break;
                    case "意见反馈":
                        submitValue("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=feedback&m=socialchat","意见");
                        break;
                    default:
                        submitValue("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=reset&m=socialchat","其他");
                        break;
                }
                Toast.makeText(v.getContext(), "提交成功", Toast.LENGTH_LONG).show();

            }
        });

        ImageView back_btn = findViewById(R.id.iv_back_left);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
            switch (msg.what){
                case 1:
                    if (title.equals("昵称设置")){
                        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("userNickName", value);
                        editor.commit();
                        //融云更新缓存
                        SharedHelper shuserinfo = new SharedHelper(getApplicationContext());
                        String myid = shuserinfo.readUserInfo().get("userID");
                        String mynickname = shuserinfo.readUserInfo().get("userNickName");
                        String myportrait = shuserinfo.readUserInfo().get("userPortrait");
                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(myid, mynickname, Uri.parse(myportrait)));
                    }
                    break;
                case 2:
                    SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    Log.d(TAG, "handleMessage: 修改头像"+msg.obj.toString());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("userPortrait", msg.obj.toString());
                    editor.commit();
                    //融云更新缓存
                    SharedHelper shuserinfo = new SharedHelper(getApplicationContext());
                    String myid = shuserinfo.readUserInfo().get("userID");
                    String mynickname = shuserinfo.readUserInfo().get("userNickName");
                    String myportrait = shuserinfo.readUserInfo().get("userPortrait");
                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(myid, mynickname, Uri.parse(myportrait)));
                    break;
            }
            Intent intent = new Intent(ResetActivity.this, Main5Activity.class);
            startActivity(intent);
        }
    };
    //TODO okhttp设置手机，密码，邮箱,昵称,年龄，地区。
    public void submitValue(final String url, final String type){
        new Thread(new Runnable() {
            @Override
            public void run(){
                SharedHelper shuserinfo = new SharedHelper(getApplicationContext());
                String myid = shuserinfo.readUserInfo().get("userID");
                String mynickname = shuserinfo.readUserInfo().get("userNickName");
                String myportrait = shuserinfo.readUserInfo().get("userPortrait");
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody;
                switch (type){
                    case "性别":
                        value = ((RadioButton) findViewById(userGender.getCheckedRadioButtonId())).getText().toString();
                         formBody = new FormBody.Builder()
                                .add("type",title)
                                .add("userID", myid)
                                .add("value",value)
                                .build();
                    break;
                    case "属性":
                        value = ((RadioButton) findViewById(userProperty.getCheckedRadioButtonId())).getText().toString();
                         formBody = new FormBody.Builder()
                                .add("type",title)
                                .add("userID", myid)
                                .add("value",value)
                                .build();
                        break;
                    case "意见":
                        value = value_et.getText().toString();
                        formBody = new FormBody.Builder()
                                .add("type",title)
                                .add("userID", myid)
                                .add("nickname", mynickname)
                                .add("portrait", myportrait)
                                .add("content",value)
                                .build();
                        break;
                    default:
                        value = value_et.getText().toString();
                         formBody = new FormBody.Builder()
                                .add("type",title)
                                .add("userID", myid)
                                .add("value",value)
                                .build();
                        break;
                }

                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Message message=handler.obtainMessage();
                    message.what=1;

                    handler.sendMessageDelayed(message,10);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //TODO 注册 form形式的post
    public void setUserPortrait(final String url){
        new Thread(new Runnable() {
            @Override
            public void run(){
                //获取文件名
                Log.d("进入run","run");
                File tempFile =new File(userPortrait.trim());
                String fileName = tempFile.getName();
                //用户id
                SharedHelper shuserinfo = new SharedHelper(getApplicationContext());
                String myid = shuserinfo.readUserInfo().get("userID");
                //开始网络传输
                OkHttpClient client = new OkHttpClient();
                MediaType MEDIA_TYPE_PNG = MediaType.parse("image");
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("type", title)
                        .addFormDataPart("userID", myid)
                        .addFormDataPart("userPortrait",fileName,RequestBody.create(new File(userPortrait),MEDIA_TYPE_PNG))
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                Log.d("进入try","try");
                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    //Log.d("form形式的post",response.body().string());
                    //格式：{"error":"0","info":"登陆成功"}
                    Message message=handler.obtainMessage();
                    message.what=2;
                    message.obj=response.body().string();
                    handler.sendMessageDelayed(message,10);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            //Your Code
            ListIterator<String> listIterator = mPaths.listIterator();
            while (listIterator.hasNext()){
                String mPath = listIterator.next();
                Log.d("path", mPath);
                portrait.setImageURI(Uri.parse(mPath));
                userPortrait = mPath;
            }
        }
    }
}
