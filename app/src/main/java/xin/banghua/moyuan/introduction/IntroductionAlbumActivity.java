package xin.banghua.moyuan.introduction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.quanturium.android.library.bottomsheetpicker.BottomSheetPickerFragment;
import com.quanturium.android.library.bottomsheetpicker.MyFileVariable;
import com.xinlan.imageeditlibrary.FileUtils;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xin.banghua.moyuan.ParseJSON.ParseJSONObject;
import xin.banghua.moyuan.R;
import xin.banghua.moyuan.RealPathFromUriUtils;
import xin.banghua.moyuan.SharedPreferences.SharedHelper;

import static com.quanturium.android.library.bottomsheetpicker.MyFileVariable.TAKE_FROM_CAMERA;
import static com.quanturium.android.library.bottomsheetpicker.MyFileVariable.TAKE_FROM_FOLDER;
import static com.quanturium.android.library.bottomsheetpicker.MyFileVariable.TAKE_FROM_SHEET;

public class IntroductionAlbumActivity extends AppCompatActivity implements BottomSheetPickerFragment.BottomSheetPickerListener{
    private final static int PERMISSION_REQUEST_EXTERNAL_STORAGE = 1;
    public static final int ACTION_REQUEST_EDITIMAGE = 9;
    private BottomSheetPickerFragment bottomSheetPickerFragment;

    ImageView picture1,picture2,picture3,picture4,picture5,picture6;

    int picture_state = 1;
    String postpicture1 = "";
    String postpicture2 = "";
    String postpicture3 = "";
    String postpicture4 = "";
    String postpicture5 = "";
    String postpicture6 = "";

    String edit_path;
    int edit_num;

    Button picture1_edit_btn,picture2_edit_btn,picture3_edit_btn,picture4_edit_btn,picture5_edit_btn,picture6_edit_btn;
    Button picture1_close_btn,picture2_close_btn,picture3_close_btn,picture4_close_btn,picture5_close_btn,picture6_close_btn;

    private static final String TAG = "AlbumActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction_album);

        if (savedInstanceState != null) {
            final BottomSheetPickerFragment fragment = (BottomSheetPickerFragment) getSupportFragmentManager().findFragmentByTag("picker");
            if (fragment != null) {
                Log.d("TAG","fragment不为空");
                fragment.setListener(this);
            }
        }


        initToolbar();
        initImage();

        showBottomSheetPicker();
    }
    //初始化工具栏
    public void initToolbar(){
        //工具栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("我的相册介绍");
        toolbar.inflateMenu(R.menu.menu_toolbar_submit);
        toolbar.setNavigationIcon(R.drawable.rc_back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed(); // back button
                Intent intent = new Intent(IntroductionAlbumActivity.this, IntroductionActivity.class);
                startActivity(intent);
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.submit_toolbar){
                    Log.d(TAG, "提交了");
                    postAlbum("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=Introductionalbum&m=moyuan");
                }
                return true;
            }
        });
    }
    public void initImage(){
        picture1_edit_btn = findViewById(R.id.picture1_edit_btn);
        picture2_edit_btn = findViewById(R.id.picture2_edit_btn);
        picture3_edit_btn = findViewById(R.id.picture3_edit_btn);
        picture4_edit_btn = findViewById(R.id.picture4_edit_btn);
        picture5_edit_btn = findViewById(R.id.picture5_edit_btn);
        picture6_edit_btn = findViewById(R.id.picture6_edit_btn);

        picture1_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFileVariable.getInstance().setEdit_num(1);
                File outputFile = FileUtils.genEditFile();
                EditImageActivity.start(IntroductionAlbumActivity.this,postpicture1,outputFile.getAbsolutePath(),ACTION_REQUEST_EDITIMAGE);
            }
        });
        picture2_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFileVariable.getInstance().setEdit_num(2);
                File outputFile = FileUtils.genEditFile();
                EditImageActivity.start(IntroductionAlbumActivity.this,postpicture2,outputFile.getAbsolutePath(),ACTION_REQUEST_EDITIMAGE);
            }
        });
        picture3_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFileVariable.getInstance().setEdit_num(3);
                File outputFile = FileUtils.genEditFile();
                EditImageActivity.start(IntroductionAlbumActivity.this,postpicture3,outputFile.getAbsolutePath(),ACTION_REQUEST_EDITIMAGE);
            }
        });
        picture4_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFileVariable.getInstance().setEdit_num(4);
                File outputFile = FileUtils.genEditFile();
                EditImageActivity.start(IntroductionAlbumActivity.this,postpicture4,outputFile.getAbsolutePath(),ACTION_REQUEST_EDITIMAGE);
            }
        });
        picture5_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFileVariable.getInstance().setEdit_num(5);
                File outputFile = FileUtils.genEditFile();
                EditImageActivity.start(IntroductionAlbumActivity.this,postpicture5,outputFile.getAbsolutePath(),ACTION_REQUEST_EDITIMAGE);
            }
        });
        picture6_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFileVariable.getInstance().setEdit_num(6);
                File outputFile = FileUtils.genEditFile();
                EditImageActivity.start(IntroductionAlbumActivity.this,postpicture6,outputFile.getAbsolutePath(),ACTION_REQUEST_EDITIMAGE);
            }
        });


        picture1_close_btn = findViewById(R.id.picture1_close_btn);
        picture2_close_btn = findViewById(R.id.picture2_close_btn);
        picture3_close_btn = findViewById(R.id.picture3_close_btn);
        picture4_close_btn = findViewById(R.id.picture4_close_btn);
        picture5_close_btn = findViewById(R.id.picture5_close_btn);
        picture6_close_btn = findViewById(R.id.picture6_close_btn);

        picture1_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture1.setImageResource(R.drawable.plus);
                picture_state = 1;
                postpicture1 = "";
                picture1_close_btn.setVisibility(View.GONE);
                picture1_edit_btn.setVisibility(View.GONE);
            }
        });
        picture2_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture2.setImageResource(R.drawable.plus);
                picture_state = 2;
                postpicture2 = "";
                picture2_close_btn.setVisibility(View.GONE);
                picture2_edit_btn.setVisibility(View.GONE);
            }
        });
        picture3_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture3.setImageResource(R.drawable.plus);
                picture_state = 3;
                postpicture3 = "";
                picture3_close_btn.setVisibility(View.GONE);
                picture3_edit_btn.setVisibility(View.GONE);
            }
        });
        picture4_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture4.setImageResource(R.drawable.plus);
                picture_state = 4;
                postpicture4 = "";
                picture4_close_btn.setVisibility(View.GONE);
                picture4_edit_btn.setVisibility(View.GONE);
            }
        });
        picture5_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture5.setImageResource(R.drawable.plus);
                picture_state = 5;
                postpicture5 = "";
                picture5_close_btn.setVisibility(View.GONE);
                picture5_edit_btn.setVisibility(View.GONE);
            }
        });
        picture6_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture6.setImageResource(R.drawable.plus);
                picture_state = 6;
                postpicture6 = "";
                picture6_close_btn.setVisibility(View.GONE);
                picture6_edit_btn.setVisibility(View.GONE);
            }
        });


        picture1 = findViewById(R.id.picture1);
        picture2 = findViewById(R.id.picture2);
        picture3 = findViewById(R.id.picture3);
        picture4 = findViewById(R.id.picture4);
        picture5 = findViewById(R.id.picture5);
        picture6 = findViewById(R.id.picture6);

        picture1.setImageResource(R.drawable.plus);
        picture2.setImageResource(R.drawable.plus);
        picture3.setImageResource(R.drawable.plus);
        picture4.setImageResource(R.drawable.plus);
        picture5.setImageResource(R.drawable.plus);
        picture6.setImageResource(R.drawable.plus);

        picture1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture_state = 1;
                showBottomSheetPicker();
            }
        });
        picture2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture_state = 2;
                showBottomSheetPicker();
            }
        });
        picture3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture_state = 3;
                showBottomSheetPicker();
            }
        });
        picture4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture_state = 4;
                showBottomSheetPicker();
            }
        });
        picture5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture_state = 5;
                showBottomSheetPicker();
            }
        });
        picture6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture_state = 6;
                showBottomSheetPicker();
            }
        });

        getAlbum("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=Getintroductionalbum&m=moyuan");
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showBottomSheetPicker();
                } else {
                    Toast.makeText(this, "Permission required to access files", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void showBottomSheetPicker() {
        bottomSheetPickerFragment = new BottomSheetPickerFragment.Builder()
                .setBrowseMoreButtonEnabled(true)
                .setFileBrowserButtonEnabled(true)
                .setMaxItems(99)
                .setCameraButtonEnabled(true)
                .setMultiSelectEnabled(true)
                .setSelectionMode(BottomSheetPickerFragment.SelectionMode.IMAGES)
                .build();

        bottomSheetPickerFragment.setListener(this);
        bottomSheetPickerFragment.show(getSupportFragmentManager(), "picker");
    }
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("进入handler", "handler");

            if (msg.arg1==1) {
               Toast.makeText(getBaseContext(), "已发布成功，等待审核", Toast.LENGTH_LONG).show();
//                Log.d("跳转", "Navigation");
//                Navigation.findNavController(mView).navigate(R.id.action_introductionAlbumFragment_to_introductionMainFragment);
            }
            if (msg.arg1==2) {
                try {
                    Log.d(TAG, "getAlbum");
                    JSONObject jsonObject = new ParseJSONObject(msg.obj.toString()).getParseJSON();//自定义的
                    String resultJson = jsonObject.getString("album");
                    Log.d(TAG, resultJson);
                    if (resultJson == "null" || resultJson == "")return;
                    String[] postPicture = resultJson.split(",");
                    Log.d(TAG, "长度"+postPicture.length);
                    //Log.d(TAG, "postPicture"+postPicture[0]+postPicture[1]+postPicture[2]+postPicture[3]+postPicture[4]+postPicture[5]);

                    switch (postPicture.length){
                        case 6:
                            Glide.with(getBaseContext())
                                    .asBitmap()
                                    .load(postPicture[5])
                                    .into(picture6);
                            postpicture6 = postPicture[5];
                            picture6_close_btn.setVisibility(View.VISIBLE);
                        case 5:
                            Glide.with(getBaseContext())
                                    .asBitmap()
                                    .load(postPicture[4])
                                    .into(picture5);
                            picture6.setVisibility(View.VISIBLE);
                            postpicture5 = postPicture[4];
                            picture5_close_btn.setVisibility(View.VISIBLE);
                        case 4:
                            Glide.with(getBaseContext())
                                    .asBitmap()
                                    .load(postPicture[3])
                                    .into(picture4);
                            picture5.setVisibility(View.VISIBLE);
                            postpicture4 = postPicture[3];
                            picture4_close_btn.setVisibility(View.VISIBLE);
                        case 3:
                            Glide.with(getBaseContext())
                                    .asBitmap()
                                    .load(postPicture[2])
                                    .into(picture3);
                            picture4.setVisibility(View.VISIBLE);
                            postpicture3 = postPicture[2];
                            picture3_close_btn.setVisibility(View.VISIBLE);
                        case 2:
                            Glide.with(getBaseContext())
                                    .asBitmap()
                                    .load(postPicture[1])
                                    .into(picture2);
                            picture3.setVisibility(View.VISIBLE);
                            postpicture2 = postPicture[1];
                            picture2_close_btn.setVisibility(View.VISIBLE);
                        case 1:
                            Glide.with(getBaseContext())
                                    .asBitmap()
                                    .load(postPicture[0])
                                    .into(picture1);
                            picture2.setVisibility(View.VISIBLE);
                            postpicture1 = postPicture[0];
                            picture1_close_btn.setVisibility(View.VISIBLE);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    };
    //TODO 注册 form形式的post
    public void postAlbum(final String url){
        new Thread(new Runnable() {
            @Override
            public void run(){
                //获取文件名
                Log.d("进入run","run");
                String fileName = "postpicture.png";
                SharedHelper shuserinfo = new SharedHelper(getApplicationContext());
                String myid = shuserinfo.readUserInfo().get("userID");
                //开始网络传输
                OkHttpClient client = new OkHttpClient();
                MediaType MEDIA_TYPE_PNG = MediaType.parse("image");
                MultipartBody.Builder multipartBody = new MultipartBody.Builder();
                multipartBody.setType(MultipartBody.FORM);
                multipartBody.addFormDataPart("authid", myid);
                if (!postpicture1.isEmpty()) {
                    if (!postpicture1.contains("https")) {
                        multipartBody.addFormDataPart("postpicture1", fileName, RequestBody.create(new File(postpicture1), MEDIA_TYPE_PNG));
                    }else {
                        multipartBody.addFormDataPart("postpicture1", postpicture1);
                    }
                }
                if (!postpicture2.isEmpty()){
                    if (!postpicture2.contains("https")) {
                        multipartBody.addFormDataPart("postpicture2",fileName,RequestBody.create(new File(postpicture2),MEDIA_TYPE_PNG));
                    }else {
                        multipartBody.addFormDataPart("postpicture2", postpicture2);
                    }
                }
                if (!postpicture3.isEmpty()){
                    if (!postpicture3.contains("https")) {
                        multipartBody.addFormDataPart("postpicture3",fileName,RequestBody.create(new File(postpicture3),MEDIA_TYPE_PNG));
                    }else {
                        multipartBody.addFormDataPart("postpicture3", postpicture3);
                    }
                }
                if (!postpicture4.isEmpty()){
                    if (!postpicture4.contains("https")) {
                        multipartBody.addFormDataPart("postpicture4",fileName,RequestBody.create(new File(postpicture4),MEDIA_TYPE_PNG));
                    }else {
                        multipartBody.addFormDataPart("postpicture4", postpicture4);
                    }
                }
                if (!postpicture5.isEmpty()){
                    if (!postpicture5.contains("https")) {
                        multipartBody.addFormDataPart("postpicture5",fileName,RequestBody.create(new File(postpicture5),MEDIA_TYPE_PNG));
                    }else {
                        multipartBody.addFormDataPart("postpicture5", postpicture5);
                    }
                }
                if (!postpicture6.isEmpty()){
                    if (!postpicture6.contains("https")) {
                        multipartBody.addFormDataPart("postpicture6",fileName,RequestBody.create(new File(postpicture6),MEDIA_TYPE_PNG));
                    }else {
                        multipartBody.addFormDataPart("postpicture6", postpicture6);
                    }
                }
                RequestBody requestBody = multipartBody.build();

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
                    message.arg1=1;
                    Log.d("相册介绍","");
                    handler.sendMessageDelayed(message,10);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //TODO 注册 form形式的post
    public void getAlbum(final String url){
        new Thread(new Runnable() {
            @Override
            public void run(){
                SharedHelper shuserinfo = new SharedHelper(getApplicationContext());
                String myid = shuserinfo.readUserInfo().get("userID");

                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("authid", myid)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Message message=handler.obtainMessage();
                    message.obj=response.body().string();
                    message.arg1=2;
                    Log.d(TAG, "获得的相册值"+message.obj.toString());
                    handler.sendMessageDelayed(message,10);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @Override
    public void onFileLoad(ImageView imageView, Uri uri) {
        Glide.with(this)
                .load(uri)
                .placeholder(R.drawable.thumbnail_loading)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void onFilesSelected(List<Uri> uriList) {
        Log.d("调用了地址信息","");
        if (bottomSheetPickerFragment != null) {
            bottomSheetPickerFragment.dismiss();
            Toast.makeText(getApplicationContext(), "# 选择了: " + uriList.size() + "个文件", Toast.LENGTH_LONG).show();
            Log.d("视频地址信息", uriList.get(0).toString().substring(7));
            int takeFrom = MyFileVariable.getInstance().getTakeFrom();
            Map map = new HashMap();
            Log.d(TAG,"takeFrom="+takeFrom+"uriList.size()"+uriList.size());

            if (takeFrom == TAKE_FROM_SHEET){//来自选单
                for (int i = 0; i < uriList.size(); i = i+1) {
                    map.put("fileName" + i, uriList.get(i).toString().substring(7));
                    Log.d(TAG, "来自选单的地址信息"+map.get("fileName" + i));
                }
                MyFileVariable.getInstance().setFileMap(map);
                for (int i = 0; i <  MyFileVariable.getInstance().getFileMap().size(); i = i+1) {
                    Log.d(TAG, "循环取出地址信息"+MyFileVariable.getInstance().getFileMap().get("fileName" + i));
                }
            }else if (takeFrom == TAKE_FROM_FOLDER){//来自文件夹
                for (int i = 0; i < uriList.size(); i = i+1) {
                    map.put("fileName" + i, RealPathFromUriUtils.getRealPathFromUri(this, uriList.get(i), "video"));
                    Log.d(TAG, "来自文件夹的地址信息"+map.get("fileName" + i));
                }
                MyFileVariable.getInstance().setFileMap(map);
                for (int i = 0; i <  MyFileVariable.getInstance().getFileMap().size(); i = i+1) {
                    Log.d(TAG, "循环取出地址信息"+MyFileVariable.getInstance().getFileMap().get("fileName" + i));
                }
            }else if (takeFrom == TAKE_FROM_CAMERA){//来自相机
                for (int i = 0; i < uriList.size(); i = i+1) {
                    map.put("fileName" + i, MyFileVariable.getInstance().getTakeFilePath());
                    Log.d(TAG, "来自相机的地址信息"+map.get("fileName" + i));
                }
                MyFileVariable.getInstance().setFileMap(map);
                for (int i = 0; i <  MyFileVariable.getInstance().getFileMap().size(); i = i+1) {
                    Log.d(TAG, "循环取出地址信息"+MyFileVariable.getInstance().getFileMap().get("fileName" + i));
                }
            }else {
                Log.d("来自选单的单选地址", uriList.get(0).toString().substring(7));
                map.put("fileName0", uriList.get(0).toString().substring(7));
                MyFileVariable.getInstance().setFileMap(map);
            }
            int picture_state_duplicate = picture_state;
            for (int i = 0; i <  MyFileVariable.getInstance().getFileMap().size(); i = i+1) {
                picture_state = i+picture_state_duplicate;
                switch (picture_state){
                    case 1:
                        picture1.setImageURI(Uri.fromFile(new File(MyFileVariable.getInstance().getFileMap().get("fileName" + i).toString())));
                        postpicture1 = MyFileVariable.getInstance().getFileMap().get("fileName" + i).toString();
                        Log.d(TAG, "onActivityResult: 动态图片地址1"+picture_state+":"+postpicture1);
                        picture1_close_btn.setVisibility(View.VISIBLE);
                        picture1_edit_btn.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        picture2.setImageURI(Uri.fromFile(new File(MyFileVariable.getInstance().getFileMap().get("fileName" + i).toString())));
                        postpicture2 = MyFileVariable.getInstance().getFileMap().get("fileName" + i).toString();
                        Log.d(TAG, "onActivityResult: 动态图片地址2"+picture_state+":"+postpicture2);
                        picture2_close_btn.setVisibility(View.VISIBLE);
                        picture2_edit_btn.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        picture3.setImageURI(Uri.fromFile(new File(MyFileVariable.getInstance().getFileMap().get("fileName" + i).toString())));
                        postpicture3 = MyFileVariable.getInstance().getFileMap().get("fileName" + i).toString();
                        Log.d(TAG, "onActivityResult: 动态图片地址3"+picture_state+":"+postpicture3);
                        picture3_close_btn.setVisibility(View.VISIBLE);
                        picture3_edit_btn.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        picture4.setImageURI(Uri.fromFile(new File(MyFileVariable.getInstance().getFileMap().get("fileName" + i).toString())));
                        postpicture4 = MyFileVariable.getInstance().getFileMap().get("fileName" + i).toString();
                        Log.d(TAG, "onActivityResult: 动态图片地址4"+picture_state+":"+postpicture4);
                        picture4_close_btn.setVisibility(View.VISIBLE);
                        picture4_edit_btn.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        picture5.setImageURI(Uri.fromFile(new File(MyFileVariable.getInstance().getFileMap().get("fileName" + i).toString())));
                        postpicture5 = MyFileVariable.getInstance().getFileMap().get("fileName" + i).toString();
                        Log.d(TAG, "onActivityResult: 动态图片地址5"+picture_state+":"+postpicture5);
                        picture5_close_btn.setVisibility(View.VISIBLE);
                        picture5_edit_btn.setVisibility(View.VISIBLE);
                        break;
                    case 6:
                        picture6.setImageURI(Uri.fromFile(new File(MyFileVariable.getInstance().getFileMap().get("fileName" + i).toString())));
                        postpicture6 = MyFileVariable.getInstance().getFileMap().get("fileName" + i).toString();
                        Log.d(TAG, "onActivityResult: 动态图片地址6"+picture_state+":"+postpicture6);
                        picture6_close_btn.setVisibility(View.VISIBLE);
                        picture6_edit_btn.setVisibility(View.VISIBLE);
                        break;
                }
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // System.out.println("RESULT_OK");
            switch (requestCode) {
                case ACTION_REQUEST_EDITIMAGE://
                    handleEditorImage(data);
                    break;
            }// end switch
        }
    }
    //图片编辑完成后，重新填充各个图片，编辑的图片另行处理
    private void handleEditorImage(Intent data) {
        String newFilePath = data.getStringExtra(EditImageActivity.EXTRA_OUTPUT);
        boolean isImageEdit = data.getBooleanExtra(EditImageActivity.IMAGE_IS_EDIT, false);

        if (isImageEdit){
            //Toast.makeText(this, getString(R.string.save_path, newFilePath), Toast.LENGTH_LONG).show();
        }else{//未编辑  还是用原来的图片
            newFilePath = data.getStringExtra(EditImageActivity.FILE_PATH);;
        }
        //System.out.println("newFilePath---->" + newFilePath);
        //File file = new File(newFilePath);
        //System.out.println("newFilePath size ---->" + (file.length() / 1024)+"KB");
        Log.d("image is edit", isImageEdit + "");

            switch (MyFileVariable.getInstance().getEdit_num()){
                case 1:
                        picture1.setImageURI(Uri.fromFile(new File(newFilePath)));
                        postpicture1 = newFilePath;

                    picture1_close_btn.setVisibility(View.VISIBLE);
                    picture1_edit_btn.setVisibility(View.VISIBLE);
                    break;
                case 2:
                        picture2.setImageURI(Uri.fromFile(new File(newFilePath)));
                        postpicture2 = newFilePath;

                    picture2_close_btn.setVisibility(View.VISIBLE);
                    picture2_edit_btn.setVisibility(View.VISIBLE);
                    break;
                case 3:
                        picture3.setImageURI(Uri.fromFile(new File(newFilePath)));
                        postpicture3 = newFilePath;

                    picture3_close_btn.setVisibility(View.VISIBLE);
                    picture3_edit_btn.setVisibility(View.VISIBLE);
                    break;
                case 4:
                        picture4.setImageURI(Uri.fromFile(new File(newFilePath)));
                        postpicture4 = newFilePath;

                    picture4_close_btn.setVisibility(View.VISIBLE);
                    picture4_edit_btn.setVisibility(View.VISIBLE);
                    break;
                case 5:
                        picture5.setImageURI(Uri.fromFile(new File(newFilePath)));
                        postpicture5 = newFilePath;

                    picture5_close_btn.setVisibility(View.VISIBLE);
                    picture5_edit_btn.setVisibility(View.VISIBLE);
                    break;
                case 6:
                        picture6.setImageURI(Uri.fromFile(new File(newFilePath)));
                        postpicture6 = newFilePath;

                    picture6_close_btn.setVisibility(View.VISIBLE);
                    picture6_edit_btn.setVisibility(View.VISIBLE);
                    break;
            }

        //LoadImageTask loadTask = new LoadImageTask();
        //loadTask.execute(newFilePath);
    }
}
