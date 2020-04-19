package xin.banghua.moyuan.Main4Branch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.vod.upload.VODUploadCallback;
import com.alibaba.sdk.android.vod.upload.VODUploadClientImpl;
import com.alibaba.sdk.android.vod.upload.model.UploadFileInfo;
import com.alibaba.sdk.android.vod.upload.model.VodInfo;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.bumptech.glide.Glide;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.quanturium.android.library.bottomsheetpicker.BottomSheetPickerFragment;
import com.quanturium.android.library.bottomsheetpicker.MyFileVariable;
import com.xinlan.imageeditlibrary.FileUtils;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import opensource.theboloapp.com.videothumbselect.ChooseThumbnailActivity;
import opensource.theboloapp.com.videothumbselect.VideoThumbnailSelectHelper;
import xin.banghua.moyuan.ParseJSON.ParseJSONArray;
import xin.banghua.moyuan.ParseJSON.ParseJSONObject;
import xin.banghua.moyuan.R;
import xin.banghua.moyuan.RealPathFromUriUtils;
import xin.banghua.moyuan.SharedPreferences.SharedHelper;
import xin.banghua.moyuan.introduction.IntroductionActivity;
import xin.banghua.moyuan.introduction.IntroductionAlbumActivity;
import xin.banghua.moyuan.introduction.MyJzvdStd;

import static android.view.View.VISIBLE;
import static com.quanturium.android.library.bottomsheetpicker.MyFileVariable.TAKE_FROM_CAMERA;
import static com.quanturium.android.library.bottomsheetpicker.MyFileVariable.TAKE_FROM_FOLDER;
import static com.quanturium.android.library.bottomsheetpicker.MyFileVariable.TAKE_FROM_SHEET;

public class FabutieziActivity extends AppCompatActivity implements BottomSheetPickerFragment.BottomSheetPickerListener,PoiSearch.OnPoiSearchListener{
    private static final String TAG = "FabutieziActivity";
    private final static int PERMISSION_REQUEST_EXTERNAL_STORAGE = 1;
    public static final int ACTION_REQUEST_EDITIMAGE = 9;
    private final int REQUEST_CODE_SELECT_THUMBNAIL = 1010;
    private BottomSheetPickerFragment bottomSheetPickerFragmentImage;
    private BottomSheetPickerFragment bottomSheetPickerFragmentVideo;

    String whichSelector = "";
    //var
    EditText content_et;
    TextView photo_selector,video_selector,location_selector;
    ImageView imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageView7,imageView8,imageView9;

    String posttitle = "";
    String posttext = "";
    String postpicture1 = "";
    String postpicture2 = "";
    String postpicture3 = "";
    String postpicture4 = "";
    String postpicture5 = "";
    String postpicture6 = "";
    String postpicture7 = "";
    String postpicture8 = "";
    String postpicture9 = "";
    String platename = "";
    LinearLayout image_layout_1;
    LinearLayout image_layout_2;
    LinearLayout image_layout_3;
    Button imageview1_edit_btn,imageview2_edit_btn,imageview3_edit_btn,imageview4_edit_btn,imageview5_edit_btn,imageview6_edit_btn,imageview7_edit_btn,imageview8_edit_btn,imageview9_edit_btn;
    Button imageview1_close_btn,imageview2_close_btn,imageview3_close_btn,imageview4_close_btn,imageview5_close_btn,imageview6_close_btn,imageview7_close_btn,imageview8_close_btn,imageview9_close_btn;

    String uploadAuth = "";
    String uploadAddress = "";
    String videoID = "";
    String videoUrl = "";
    String videoWidth = "";
    String videoHeight = "";
    String postVideoCover = "";
    LinearLayout video_layout;
    //新视频
    MyJzvdStd myJzvdStd;
    Button bottomsheet_btn;

    SharedHelper shuserinfo;
    String myid;

    String videoFilePath;

    int imageView_state;


    PoiSearch poiSearch;
    PoiSearch.Query query;
    String[] poiList;
    TextView location_textview;
    String poiSelected = "";

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabutiezi);

        this.mContext = this;
        //初始化工具栏
        initToolbar();
        //初始化组件
        initComponent();


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
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.submit_toolbar){
                    Log.d(TAG, "提交了");
                    Toast.makeText(FabutieziActivity.this, "正在上传...", Toast.LENGTH_LONG).show();
                    if (whichSelector.equals("video")) {
                        //获取上传凭证
                        createUploadVideo("https://aliyun.banghua.xin/createUploadVideo.php");
                    }else {
                        postFabutiezi("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=fabutiezi&m=moyuan");
                    }
                }
                return true;
            }
        });
    }
    //初始化组件
    public void initComponent(){
        shuserinfo = new SharedHelper(getApplicationContext());
        myid = shuserinfo.readUserInfo().get("userID");

        content_et = findViewById(R.id.content_et);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        imageView6 = findViewById(R.id.imageView6);
        imageView7 = findViewById(R.id.imageView7);
        imageView8 = findViewById(R.id.imageView8);
        imageView9 = findViewById(R.id.imageView9);

        image_layout_1 = findViewById(R.id.image_layout_1);
        image_layout_2 = findViewById(R.id.image_layout_2);
        image_layout_3 = findViewById(R.id.image_layout_3);

        imageview1_edit_btn = findViewById(R.id.imageview1_edit_btn);
        imageview2_edit_btn = findViewById(R.id.imageview2_edit_btn);
        imageview3_edit_btn = findViewById(R.id.imageview3_edit_btn);
        imageview4_edit_btn = findViewById(R.id.imageview4_edit_btn);
        imageview5_edit_btn = findViewById(R.id.imageview5_edit_btn);
        imageview6_edit_btn = findViewById(R.id.imageview6_edit_btn);
        imageview7_edit_btn = findViewById(R.id.imageview7_edit_btn);
        imageview8_edit_btn = findViewById(R.id.imageview8_edit_btn);
        imageview9_edit_btn = findViewById(R.id.imageview9_edit_btn);
        imageview1_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFileVariable.getInstance().setEdit_num(1);
                File outputFile = FileUtils.genEditFile();
                EditImageActivity.start(FabutieziActivity.this,postpicture1,outputFile.getAbsolutePath(),ACTION_REQUEST_EDITIMAGE);
            }
        });
        imageview2_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFileVariable.getInstance().setEdit_num(2);
                File outputFile = FileUtils.genEditFile();
                EditImageActivity.start(FabutieziActivity.this,postpicture2,outputFile.getAbsolutePath(),ACTION_REQUEST_EDITIMAGE);
            }
        });
        imageview3_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFileVariable.getInstance().setEdit_num(3);
                File outputFile = FileUtils.genEditFile();
                EditImageActivity.start(FabutieziActivity.this,postpicture3,outputFile.getAbsolutePath(),ACTION_REQUEST_EDITIMAGE);
            }
        });
        imageview4_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFileVariable.getInstance().setEdit_num(4);
                File outputFile = FileUtils.genEditFile();
                EditImageActivity.start(FabutieziActivity.this,postpicture4,outputFile.getAbsolutePath(),ACTION_REQUEST_EDITIMAGE);
            }
        });
        imageview5_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFileVariable.getInstance().setEdit_num(5);
                File outputFile = FileUtils.genEditFile();
                EditImageActivity.start(FabutieziActivity.this,postpicture5,outputFile.getAbsolutePath(),ACTION_REQUEST_EDITIMAGE);
            }
        });
        imageview6_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFileVariable.getInstance().setEdit_num(6);
                File outputFile = FileUtils.genEditFile();
                EditImageActivity.start(FabutieziActivity.this,postpicture6,outputFile.getAbsolutePath(),ACTION_REQUEST_EDITIMAGE);
            }
        });
        imageview7_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFileVariable.getInstance().setEdit_num(7);
                File outputFile = FileUtils.genEditFile();
                EditImageActivity.start(FabutieziActivity.this,postpicture7,outputFile.getAbsolutePath(),ACTION_REQUEST_EDITIMAGE);
            }
        });
        imageview8_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFileVariable.getInstance().setEdit_num(8);
                File outputFile = FileUtils.genEditFile();
                EditImageActivity.start(FabutieziActivity.this,postpicture8,outputFile.getAbsolutePath(),ACTION_REQUEST_EDITIMAGE);
            }
        });
        imageview9_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFileVariable.getInstance().setEdit_num(9);
                File outputFile = FileUtils.genEditFile();
                EditImageActivity.start(FabutieziActivity.this,postpicture9,outputFile.getAbsolutePath(),ACTION_REQUEST_EDITIMAGE);
            }
        });

        imageview1_close_btn = findViewById(R.id.imageview1_close_btn);
        imageview2_close_btn = findViewById(R.id.imageview2_close_btn);
        imageview3_close_btn = findViewById(R.id.imageview3_close_btn);
        imageview4_close_btn = findViewById(R.id.imageview4_close_btn);
        imageview5_close_btn = findViewById(R.id.imageview5_close_btn);
        imageview6_close_btn = findViewById(R.id.imageview6_close_btn);
        imageview7_close_btn = findViewById(R.id.imageview7_close_btn);
        imageview8_close_btn = findViewById(R.id.imageview8_close_btn);
        imageview9_close_btn = findViewById(R.id.imageview9_close_btn);
        imageview1_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView1.setVisibility(View.INVISIBLE);
                postpicture1 = "";
                imageview1_edit_btn.setVisibility(View.GONE);
                imageview1_close_btn.setVisibility(View.GONE);
            }
        });
        imageview2_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView2.setVisibility(View.INVISIBLE);
                postpicture2 = "";
                imageview2_edit_btn.setVisibility(View.GONE);
                imageview2_close_btn.setVisibility(View.GONE);
            }
        });
        imageview3_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView3.setVisibility(View.INVISIBLE);
                postpicture3 = "";
                imageview3_edit_btn.setVisibility(View.GONE);
                imageview3_close_btn.setVisibility(View.GONE);
            }
        });
        imageview4_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView4.setVisibility(View.INVISIBLE);
                postpicture4 = "";
                imageview4_edit_btn.setVisibility(View.GONE);
                imageview4_close_btn.setVisibility(View.GONE);
            }
        });
        imageview5_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView5.setVisibility(View.INVISIBLE);
                postpicture5 = "";
                imageview5_edit_btn.setVisibility(View.GONE);
                imageview5_close_btn.setVisibility(View.GONE);
            }
        });
        imageview6_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView6.setVisibility(View.INVISIBLE);
                postpicture6 = "";
                imageview6_edit_btn.setVisibility(View.GONE);
                imageview6_close_btn.setVisibility(View.GONE);
            }
        });
        imageview7_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView7.setVisibility(View.INVISIBLE);
                postpicture7 = "";
                imageview7_edit_btn.setVisibility(View.GONE);
                imageview7_close_btn.setVisibility(View.GONE);
            }
        });
        imageview8_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView8.setVisibility(View.INVISIBLE);
                postpicture8 = "";
                imageview8_edit_btn.setVisibility(View.GONE);
                imageview8_close_btn.setVisibility(View.GONE);
            }
        });
        imageview9_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView9.setVisibility(View.INVISIBLE);
                postpicture9 = "";
                imageview9_edit_btn.setVisibility(View.GONE);
                imageview9_close_btn.setVisibility(View.GONE);
            }
        });

        photo_selector = findViewById(R.id.photo_selector);
        video_selector = findViewById(R.id.video_selector);
        location_selector = findViewById(R.id.location_selector);

        myJzvdStd = findViewById(R.id.jz_video);
        //myJzvdStd.widthRatio = 9;
        //myJzvdStd.heightRatio = 16;

        video_layout = findViewById(R.id.video_layout);

        photo_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichSelector = "image";
                showBottomSheetPickerImage();
            }
        });
        video_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichSelector = "video";
                showBottomSheetPickerVideo();
            }
        });
        location_textview = findViewById(R.id.location_textview);
        location_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取定位值
                SharedHelper shlocation = new SharedHelper(getApplicationContext());
                Map<String,String> locationInfo = shlocation.readLocation();
                //locationInfo.get("latitude"),locationInfo.get("longitude")
                query = new PoiSearch.Query("", "");
                query.setPageSize(10);// 设置每页最多返回多少条poiitem
                query.setPageNum(1);//设置查询页码
                poiSearch = new PoiSearch(FabutieziActivity.this,query);
                poiSearch.setOnPoiSearchListener(FabutieziActivity.this);
                poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(Double.valueOf(locationInfo.get("latitude")),
                        Double.valueOf(locationInfo.get("longitude"))), 1000));
                poiSearch.searchPOIAsyn();
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //showBottomSheetPicker();
                } else {
                    Toast.makeText(this, "Permission required to access files", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void showBottomSheetPickerImage() {
        bottomSheetPickerFragmentImage = new BottomSheetPickerFragment.Builder()
                .setBrowseMoreButtonEnabled(true)
                .setFileBrowserButtonEnabled(true)
                .setMaxItems(99)
                .setCameraButtonEnabled(true)
                .setMultiSelectEnabled(true)
                .setSelectionMode(BottomSheetPickerFragment.SelectionMode.IMAGES)
                .build();

        bottomSheetPickerFragmentImage.setListener(this);
        bottomSheetPickerFragmentImage.show(getSupportFragmentManager(), "picker");
    }
    private void showBottomSheetPickerVideo() {
        bottomSheetPickerFragmentVideo = new BottomSheetPickerFragment.Builder()
                .setBrowseMoreButtonEnabled(true)
                .setFileBrowserButtonEnabled(true)
                .setMaxItems(99)
                .setCameraButtonEnabled(true)
                .setMultiSelectEnabled(false)
                .setSelectionMode(BottomSheetPickerFragment.SelectionMode.VIDEOS)
                .build();

        bottomSheetPickerFragmentVideo.setListener(this);
        bottomSheetPickerFragmentVideo.show(getSupportFragmentManager(), "picker");
    }

    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("进入handler", "handler");
            //Toast.makeText(mContext, "已发布成功，等待审核", Toast.LENGTH_LONG).show();
            //1是用户数据，2是幻灯片
            switch (msg.what){
                case 1:
                    try {
                        Log.d(TAG, "handleMessage: 视频上传证书"+msg.obj.toString());

                        JSONObject jsonObject = new ParseJSONObject(msg.obj.toString()).getParseJSON();
                        videoID = jsonObject.getString("VideoId");
                        uploadAuth = jsonObject.getString("UploadAuth");
                        uploadAddress = jsonObject.getString("UploadAddress");
                        //已经获取到上传证书，直接上传视频
                        uploadVideo(videoFilePath);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONArray jsonArray = new ParseJSONArray(msg.obj.toString()).getParseJSON();
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        videoUrl = jsonObject.getString("PlayURL");

                        Log.d(TAG, "handleMessage: 播放地址"+videoUrl);
                        //保存视频播放地址,视频id,和封面
                        updateDatabase("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=Fabutiezi&m=moyuan");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    Toast.makeText(FabutieziActivity.this, "动态发布成功", Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case 4:
                    String resultJson = msg.obj.toString();
                    Log.d(TAG,"动态返回值"+resultJson);
                    Toast.makeText(FabutieziActivity.this, "动态发布成功", Toast.LENGTH_LONG).show();
                    finish();
                    break;
            }

        }
    };

    //TODO 注册 form形式的post
    public void postFabutiezi(final String url){
        new Thread(new Runnable() {
            @Override
            public void run(){
                //获取文件名
                Log.d("进入run","run");
                String fileName = "postpicture.png";
                //开始网络传输
                OkHttpClient client = new OkHttpClient();
                MediaType MEDIA_TYPE_PNG = MediaType.parse("image");
                MultipartBody.Builder multipartBody = new MultipartBody.Builder();
                multipartBody.setType(MultipartBody.FORM);
                multipartBody.addFormDataPart("authid", myid);
                multipartBody.addFormDataPart("posttitle", "image");
                multipartBody.addFormDataPart("posttext", content_et.getText().toString());
                multipartBody.addFormDataPart("poi", poiSelected);
                if (whichSelector.equals("image")) {//不是图片则只传文字
                    if (!postpicture1.isEmpty())
                        multipartBody.addFormDataPart("postpicture1", fileName, RequestBody.create(new File(postpicture1), MEDIA_TYPE_PNG));
                    if (!postpicture2.isEmpty())
                        multipartBody.addFormDataPart("postpicture2", fileName, RequestBody.create(new File(postpicture2), MEDIA_TYPE_PNG));
                    if (!postpicture3.isEmpty())
                        multipartBody.addFormDataPart("postpicture3", fileName, RequestBody.create(new File(postpicture3), MEDIA_TYPE_PNG));
                    if (!postpicture4.isEmpty())
                        multipartBody.addFormDataPart("postpicture4", fileName, RequestBody.create(new File(postpicture4), MEDIA_TYPE_PNG));
                    if (!postpicture5.isEmpty())
                        multipartBody.addFormDataPart("postpicture5", fileName, RequestBody.create(new File(postpicture5), MEDIA_TYPE_PNG));
                    if (!postpicture6.isEmpty())
                        multipartBody.addFormDataPart("postpicture6", fileName, RequestBody.create(new File(postpicture6), MEDIA_TYPE_PNG));
                    if (!postpicture7.isEmpty())
                        multipartBody.addFormDataPart("postpicture7", fileName, RequestBody.create(new File(postpicture7), MEDIA_TYPE_PNG));
                    if (!postpicture8.isEmpty())
                        multipartBody.addFormDataPart("postpicture8", fileName, RequestBody.create(new File(postpicture8), MEDIA_TYPE_PNG));
                    if (!postpicture9.isEmpty())
                        multipartBody.addFormDataPart("postpicture9", fileName, RequestBody.create(new File(postpicture9), MEDIA_TYPE_PNG));
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
                    message.obj=response.body().string();
                    message.what=4;
                    Log.d("帖子","");
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
        if (bottomSheetPickerFragmentImage != null || bottomSheetPickerFragmentVideo != null) {

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

            if (whichSelector.equals("image")) {
                video_layout.setVisibility(View.GONE);
                bottomSheetPickerFragmentImage.dismiss();
                switch (MyFileVariable.getInstance().getFileMap().size()) {
                    case 9:
                        postpicture9 = MyFileVariable.getInstance().getFileMap().get("fileName" + 8).toString();
                        imageView9.setVisibility(VISIBLE);
                        imageview9_edit_btn.setVisibility(VISIBLE);
                        imageview9_close_btn.setVisibility(VISIBLE);
                        imageView9.setImageURI(Uri.fromFile(new File(MyFileVariable.getInstance().getFileMap().get("fileName" + 8).toString())));
                    case 8:
                        postpicture8 = MyFileVariable.getInstance().getFileMap().get("fileName" + 7).toString();
                        imageView8.setVisibility(VISIBLE);
                        imageview8_edit_btn.setVisibility(VISIBLE);
                        imageview8_close_btn.setVisibility(VISIBLE);
                        imageView8.setImageURI(Uri.fromFile(new File(MyFileVariable.getInstance().getFileMap().get("fileName" + 7).toString())));
                    case 7:
                        postpicture7 = MyFileVariable.getInstance().getFileMap().get("fileName" + 6).toString();
                        image_layout_3.setVisibility(VISIBLE);
                        imageView7.setVisibility(VISIBLE);
                        imageview7_edit_btn.setVisibility(VISIBLE);
                        imageview7_close_btn.setVisibility(VISIBLE);
                        imageView7.setImageURI(Uri.fromFile(new File(MyFileVariable.getInstance().getFileMap().get("fileName" + 6).toString())));
                    case 6:
                        postpicture6 = MyFileVariable.getInstance().getFileMap().get("fileName" + 5).toString();
                        imageView6.setVisibility(VISIBLE);
                        imageview6_edit_btn.setVisibility(VISIBLE);
                        imageview6_close_btn.setVisibility(VISIBLE);
                        imageView6.setImageURI(Uri.fromFile(new File(MyFileVariable.getInstance().getFileMap().get("fileName" + 5).toString())));
                    case 5:
                        postpicture5 = MyFileVariable.getInstance().getFileMap().get("fileName" + 4).toString();
                        imageView5.setVisibility(VISIBLE);
                        imageview5_edit_btn.setVisibility(VISIBLE);
                        imageview5_close_btn.setVisibility(VISIBLE);
                        imageView5.setImageURI(Uri.fromFile(new File(MyFileVariable.getInstance().getFileMap().get("fileName" + 4).toString())));
                    case 4:
                        postpicture4 = MyFileVariable.getInstance().getFileMap().get("fileName" + 3).toString();
                        image_layout_2.setVisibility(VISIBLE);
                        imageView4.setVisibility(VISIBLE);
                        imageview4_edit_btn.setVisibility(VISIBLE);
                        imageview4_close_btn.setVisibility(VISIBLE);
                        imageView4.setImageURI(Uri.fromFile(new File(MyFileVariable.getInstance().getFileMap().get("fileName" + 3).toString())));
                    case 3:
                        postpicture3 = MyFileVariable.getInstance().getFileMap().get("fileName" + 2).toString();
                        imageView3.setVisibility(VISIBLE);
                        imageview3_edit_btn.setVisibility(VISIBLE);
                        imageview3_close_btn.setVisibility(VISIBLE);
                        imageView3.setImageURI(Uri.fromFile(new File(MyFileVariable.getInstance().getFileMap().get("fileName" + 2).toString())));
                    case 2:
                        postpicture2 = MyFileVariable.getInstance().getFileMap().get("fileName" + 1).toString();
                        imageView2.setVisibility(VISIBLE);
                        imageview2_edit_btn.setVisibility(VISIBLE);
                        imageview2_close_btn.setVisibility(VISIBLE);
                        imageView2.setImageURI(Uri.fromFile(new File(MyFileVariable.getInstance().getFileMap().get("fileName" + 1).toString())));
                    case 1:
                        postpicture1 = MyFileVariable.getInstance().getFileMap().get("fileName" + 0).toString();
                        image_layout_1.setVisibility(VISIBLE);
                        imageView1.setVisibility(VISIBLE);
                        imageview1_edit_btn.setVisibility(VISIBLE);
                        imageview1_close_btn.setVisibility(VISIBLE);
                        imageView1.setImageURI(Uri.fromFile(new File(MyFileVariable.getInstance().getFileMap().get("fileName" + 0).toString())));
                }
            }else {
                image_layout_1.setVisibility(View.GONE);
                image_layout_2.setVisibility(View.GONE);
                image_layout_3.setVisibility(View.GONE);

                bottomSheetPickerFragmentVideo.dismiss();

                videoUrl = MyFileVariable.getInstance().getFileMap().get("fileName0").toString();
                videoFilePath = videoUrl;
                startThumbSelectActivity(MyFileVariable.getInstance().getFileMap().get("fileName0").toString());
            }

        }
    }

    public void startThumbSelectActivity(String videoSource) {
        VideoThumbnailSelectHelper videoThumbnailSelectHelper = new VideoThumbnailSelectHelper();
        videoThumbnailSelectHelper
                .setActivity(this)
                .setRequestCode(REQUEST_CODE_SELECT_THUMBNAIL)
                .setVideoSource(videoSource)
                .setLayoutRESId(R.layout.custom_choose_thumbnail_activity)
                .setNumThumbnails(1)
                .setTimelineSeekViewHandleColor(Color.GREEN)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: 动态图片地址");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_REQUEST_EDITIMAGE){
            handleEditorImage(data);//图片编辑
        }
        if (requestCode == REQUEST_CODE_SELECT_THUMBNAIL) {

            if (resultCode == RESULT_OK) {
                Uri selectedThumbUri = data.getParcelableExtra(ChooseThumbnailActivity.INTENT_RESULT_EXTRA_THUMB_BITMAP_FILE_URI);
                Log.d(TAG, "封面地址"+selectedThumbUri.toString());
                if (selectedThumbUri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedThumbUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                        postVideoCover = getFilesDir().getAbsolutePath()+"/video_cover"+timeStamp+".png";
                        MyFileVariable.savePNG_After(bitmap,postVideoCover);
                        Log.d(TAG, "保存的封面地址"+postVideoCover);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    videoHeight = MyFileVariable.getImageHeight(postVideoCover)+"";
                    videoWidth = MyFileVariable.getImageWidth(postVideoCover)+"";

                    if (Integer.parseInt(videoHeight) / Integer.parseInt(videoWidth) == 1) {
                        myJzvdStd.getLayoutParams().width = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_200);
                        myJzvdStd.widthRatio = 1;
                        myJzvdStd.heightRatio = 1;
                    }
                    if (Integer.parseInt(videoHeight) / Integer.parseInt(videoWidth) > 1) {
                        myJzvdStd.getLayoutParams().width = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_150);
                        myJzvdStd.widthRatio = 100;
                        myJzvdStd.heightRatio = Integer.parseInt(videoHeight) * 100 / Integer.parseInt(videoWidth);
                    }
                    if (Integer.parseInt(videoHeight) / Integer.parseInt(videoWidth) < 1) {
                        myJzvdStd.getLayoutParams().height = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_150);
                        myJzvdStd.widthRatio = 100;
                        myJzvdStd.heightRatio = Integer.parseInt(videoHeight) * 100 / Integer.parseInt(videoWidth);
                    }
                    //新播放器
                    video_layout.setVisibility(VISIBLE);
                    myJzvdStd.setUp(videoUrl, "");
                    Glide.with(this).load(postVideoCover).into(myJzvdStd.posterImageView);

                }
            }
        }
    }

    //上传视频
    private void uploadVideo(String path){

        final VODUploadClientImpl uploader = new VODUploadClientImpl(FabutieziActivity.this);

        // setup callback
        VODUploadCallback callback;
        callback = new VODUploadCallback() {
            public void onUploadSucceed(UploadFileInfo info) {
                Log.d(TAG,"上传成功");
                //获取视频播放地址
                getPlayInfo("https://aliyun.banghua.xin/getPlayInfo.php");
                OSSLog.logDebug("onsucceed ------------------" + info.getFilePath());
            }
            public void onUploadFailed(UploadFileInfo info, String code, String message) {
                Log.d(TAG,"上传失败"+code+"||"+message);
                //获取视频播放地址
                OSSLog.logError("onfailed ------------------ " + info.getFilePath() + " " + code + " " + message);
            }
            public void onUploadProgress(UploadFileInfo info, long uploadedSize, long totalSize) {
                Log.d(TAG,"上传进度"+uploadedSize);
                OSSLog.logDebug("onProgress ------------------ " + info.getFilePath() + " " + uploadedSize + " " + totalSize);
            }
            public void onUploadTokenExpired() {
                Log.d(TAG,"上传token超时");
                OSSLog.logError("onExpired ------------- ");

                // 重新刷新上传凭证:RefreshUploadVideo
                //uploadAuth = "此处需要设置重新刷新凭证之后的值";
                //uploader.resumeWithAuth(uploadAuth);
            }
            public void onUploadRetry(String code, String message) {
                Log.d(TAG,"上传重试");
                OSSLog.logError("onUploadRetry ------------- ");
            }
            public void onUploadRetryResume() {
                Log.d(TAG,"上传重试尝试");
                OSSLog.logError("onUploadRetryResume ------------- ");
            }
            public void onUploadStarted(UploadFileInfo uploadFileInfo) {
                Log.d(TAG,"开始上传");
                OSSLog.logError("onUploadStarted ------------- ");
                uploader.setUploadAuthAndAddress(uploadFileInfo, uploadAuth, uploadAddress);
            }
        };

        //上传初始化
        uploader.init(callback);

        String filePath = path;
        VodInfo vodInfo = new VodInfo();
        vodInfo.setTitle("标题");
        vodInfo.setDesc("描述.");
        uploader.addFile(filePath,vodInfo);


        uploader.start();
    }

    //TODO okhttp获取视频上传凭证
    public void createUploadVideo(final String url){
        new Thread(new Runnable() {
            @Override
            public void run(){
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
                    message.what=1;
                    Log.d(TAG, "run: 视频上传凭证"+message.obj.toString());
                    handler.sendMessageDelayed(message,10);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    //TODO okhttp获取播放信息
    public void getPlayInfo(final String url){
        new Thread(new Runnable() {
            @Override
            public void run(){

                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("videoID", videoID)
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
                    Log.d(TAG, "run: 播放信息"+message.obj.toString());
                    handler.sendMessageDelayed(message,10);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //TODO okhttp将视频封面和地址写入数据库
    public void updateDatabase(final String url){
        new Thread(new Runnable() {
            @Override
            public void run(){
                Log.d(TAG, "run: 更新数据库"+myid+"|"+postVideoCover+"|"+videoID+"|"+videoUrl);
                String fileName = "postpicture.png";
                //开始网络传输
                OkHttpClient client = new OkHttpClient();
                MultipartBody.Builder multipartBody = new MultipartBody.Builder();

                    MediaType MEDIA_TYPE_PNG = MediaType.parse("image");
                    multipartBody.setType(MultipartBody.FORM);
                    multipartBody.addFormDataPart("authid", myid);
                    multipartBody.addFormDataPart("videoID", videoID);
                    multipartBody.addFormDataPart("videoUrl", videoUrl);
                    multipartBody.addFormDataPart("videoWidth", videoWidth);
                    multipartBody.addFormDataPart("videoHeight", videoHeight);
                    multipartBody.addFormDataPart("posttitle", "video");
                    multipartBody.addFormDataPart("posttext", content_et.getText().toString());
                    multipartBody.addFormDataPart("poi", poiSelected);
                if (!postVideoCover.isEmpty())multipartBody.addFormDataPart("postVideoCover",fileName, RequestBody.create(new File(postVideoCover),MEDIA_TYPE_PNG));

                RequestBody requestBody = multipartBody.build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Message message=handler.obtainMessage();
                    message.obj=response.body().string();
                    message.what=3;
                    Log.d(TAG, "run: 更新数据库"+message.obj.toString());
                    handler.sendMessageDelayed(message,10);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        List<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();
        Log.d(TAG,"poi总数："+poiResult.getPois().size());

        for (int x=0;x<poiResult.getPois().size();x=x+1) {
            PoiItem poiItem = poiResult.getPois().get(x);

            Map<String, Object> showitem = new HashMap<String, Object>();
            showitem.put("imgtou", "");
            showitem.put("name", poiItem.getTitle());
            showitem.put("says", poiItem.getSnippet());
            listitem.add(showitem);
        }
        DialogPlus dialog = DialogPlus.newDialog(this)
                .setAdapter(new SimpleAdapter(getApplicationContext(), listitem, R.layout.simpleadapter_listitem, new String[]{"imgtou", "name", "says"}, new int[]{R.id.imgtou, R.id.name, R.id.says}))
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        location_textview.setVisibility(VISIBLE);
                        location_textview.setText(listitem.get(position).get("name").toString());
                        poiSelected = listitem.get(position).get("name").toString();
                        dialog.dismiss();
                    }
                })
                .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                .create();
        dialog.show();
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
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
                imageView1.setImageURI(Uri.fromFile(new File(newFilePath)));
                postpicture1 = newFilePath;

                imageview1_close_btn.setVisibility(View.VISIBLE);
                imageview1_edit_btn.setVisibility(View.VISIBLE);
                break;
            case 2:
                imageView2.setImageURI(Uri.fromFile(new File(newFilePath)));
                postpicture2 = newFilePath;

                imageview2_close_btn.setVisibility(View.VISIBLE);
                imageview2_edit_btn.setVisibility(View.VISIBLE);
                break;
            case 3:
                imageView3.setImageURI(Uri.fromFile(new File(newFilePath)));
                postpicture3 = newFilePath;

                imageview3_close_btn.setVisibility(View.VISIBLE);
                imageview3_edit_btn.setVisibility(View.VISIBLE);
                break;
            case 4:
                imageView4.setImageURI(Uri.fromFile(new File(newFilePath)));
                postpicture4 = newFilePath;

                imageview4_close_btn.setVisibility(View.VISIBLE);
                imageview4_edit_btn.setVisibility(View.VISIBLE);
                break;
            case 5:
                imageView5.setImageURI(Uri.fromFile(new File(newFilePath)));
                postpicture5 = newFilePath;

                imageview5_close_btn.setVisibility(View.VISIBLE);
                imageview5_edit_btn.setVisibility(View.VISIBLE);
                break;
            case 6:
                imageView6.setImageURI(Uri.fromFile(new File(newFilePath)));
                postpicture6 = newFilePath;

                imageview6_close_btn.setVisibility(View.VISIBLE);
                imageview6_edit_btn.setVisibility(View.VISIBLE);
                break;
            case 7:
                imageView7.setImageURI(Uri.fromFile(new File(newFilePath)));
                postpicture7 = newFilePath;

                imageview7_close_btn.setVisibility(View.VISIBLE);
                imageview7_edit_btn.setVisibility(View.VISIBLE);
                break;
            case 8:
                imageView8.setImageURI(Uri.fromFile(new File(newFilePath)));
                postpicture8 = newFilePath;

                imageview8_close_btn.setVisibility(View.VISIBLE);
                imageview8_edit_btn.setVisibility(View.VISIBLE);
                break;
            case 9:
                imageView9.setImageURI(Uri.fromFile(new File(newFilePath)));
                postpicture9 = newFilePath;

                imageview9_close_btn.setVisibility(View.VISIBLE);
                imageview9_edit_btn.setVisibility(View.VISIBLE);
                break;
        }

        //LoadImageTask loadTask = new LoadImageTask();
        //loadTask.execute(newFilePath);
    }
}
