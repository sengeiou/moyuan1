package xin.banghua.moyuan.introduction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.vod.upload.VODUploadCallback;
import com.alibaba.sdk.android.vod.upload.VODUploadClientImpl;
import com.alibaba.sdk.android.vod.upload.model.UploadFileInfo;
import com.alibaba.sdk.android.vod.upload.model.VodInfo;
import com.aliyun.player.AliPlayer;
import com.aliyun.player.AliPlayerFactory;
import com.aliyun.player.IPlayer;
import com.aliyun.player.bean.ErrorInfo;
import com.aliyun.player.bean.InfoBean;
import com.aliyun.player.nativeclass.TrackInfo;
import com.aliyun.player.source.UrlSource;
import com.aliyun.svideo.recorder.activity.AlivcSvideoRecordActivity;
import com.aliyun.svideo.recorder.bean.AlivcRecordInputParam;
import com.bumptech.glide.Glide;
import com.quanturium.android.library.bottomsheetpicker.BottomSheetPickerFragment;
import com.quanturium.android.library.bottomsheetpicker.MyFileVariable;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jzvd.Jzvd;
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

import static com.quanturium.android.library.bottomsheetpicker.MyFileVariable.TAKE_FROM_CAMERA;
import static com.quanturium.android.library.bottomsheetpicker.MyFileVariable.TAKE_FROM_FOLDER;
import static com.quanturium.android.library.bottomsheetpicker.MyFileVariable.TAKE_FROM_SHEET;

public class IntroductionVideoActivity extends AppCompatActivity implements BottomSheetPickerFragment.BottomSheetPickerListener {
    private final static String TAG = "VideoActivity";
    private BottomSheetPickerFragment bottomSheetPickerFragment;
    private final int REQUEST_CODE_SELECT_THUMBNAIL = 1010;
    private final static int PERMISSION_REQUEST_EXTERNAL_STORAGE = 1;
    private Activity activity = this;
    ImageView video_cover;

    String postVideoCover = "";

    View mView;

    ImageView playbutton_img;
    Button setcover_btn,video_record;


    SurfaceView surfaceView;
    AliPlayer aliyunVodPlayer;

    TableRow video_cover_row,surface_row;

    String uploadAuth = "";
    String uploadAddress = "";
    String videoID = "";
    String videoUrl = "";

    SharedHelper shuserinfo;
    String myid;

    String videoFilePath;


    //视频播放状态
    int video_state;


    //新视频
    MyJzvdStd myJzvdStd;
    Button bottomsheet_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction_video);

        myJzvdStd = findViewById(R.id.jz_video);
        myJzvdStd.widthRatio = 9;
        myJzvdStd.heightRatio = 16;

        video_cover = findViewById(R.id.video_cover);

        if (savedInstanceState != null) {
            Log.d(TAG,"savedInstanceState不为空");
            final BottomSheetPickerFragment fragment = (BottomSheetPickerFragment) getSupportFragmentManager().findFragmentByTag("picker");
            if (fragment != null) {
                Log.d(TAG,"设置了picker的监听");
                fragment.setListener(this);
            }
        }

        bottomsheet_btn = findViewById(R.id.bottomsheet_btn);
        bottomsheet_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetPicker();
            }
        });


        video_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetPicker();
            }
        });

        shuserinfo = new SharedHelper(getApplicationContext());
        myid = shuserinfo.readUserInfo().get("userID");
        //初始化组件
        initConponent();
        //录制完成后获取视频地址
        afterRecord();
        //工具栏
        initToolbar();
        //封面选择
        //initVideoCover();
        //录制按钮
        initVideoRecord();
        //获取已有的视频信息
        getVideoInfo("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=Getintroductionvideo&m=moyuan");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    private void showBottomSheetPicker() {
        bottomSheetPickerFragment = new BottomSheetPickerFragment.Builder()
                .setBrowseMoreButtonEnabled(true)
                .setFileBrowserButtonEnabled(true)
                .setMaxItems(99)
                .setCameraButtonEnabled(true)
                .setMultiSelectEnabled(false)
                .setSelectionMode(BottomSheetPickerFragment.SelectionMode.VIDEOS)
                .build();

        bottomSheetPickerFragment.setListener(this);
        bottomSheetPickerFragment.show(getSupportFragmentManager(), "picker");
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
        Log.d(TAG,"调用了地址信息");
        if (bottomSheetPickerFragment != null) {
            Log.d(TAG,"bottomSheetPickerFragment不为空");
            bottomSheetPickerFragment.dismiss();
            //Toast.makeText(getApplicationContext(), "# selected files: " + uriList.size(), Toast.LENGTH_LONG).show();
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

            videoUrl = MyFileVariable.getInstance().getFileMap().get("fileName0").toString();
            videoFilePath = videoUrl;
            startThumbSelectActivity(MyFileVariable.getInstance().getFileMap().get("fileName0").toString());
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
        super.onActivityResult(requestCode,resultCode,data);
        Log.d(TAG,"Main的回调");
        if (requestCode == REQUEST_CODE_SELECT_THUMBNAIL) {
            if (resultCode == RESULT_OK) {
                Uri selectedThumbUri = data.getParcelableExtra(ChooseThumbnailActivity.INTENT_RESULT_EXTRA_THUMB_BITMAP_FILE_URI);
                Log.d(TAG, "封面地址"+selectedThumbUri.toString());
                if (selectedThumbUri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedThumbUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        MyFileVariable.saveJPG_After(bitmap,getFilesDir().getAbsolutePath()+"/video_cover.jpg");
                        Log.d(TAG, "保存的封面地址"+getFilesDir().getAbsolutePath()+"/video_cover.jpg");
                        postVideoCover = getFilesDir().getAbsolutePath()+"/video_cover.jpg";
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    video_cover.setImageURI(selectedThumbUri);
                    initVideoPlay();
                }
            }
        }
    }
    //上传
    public void startUpload(){
        Log.d(TAG,"获取了文件");
        if (videoFilePath == null || videoFilePath == ""){
            Toast.makeText(activity, "请录制视频", Toast.LENGTH_LONG).show();
            return;
        }else if(postVideoCover == null || postVideoCover == ""){
            Toast.makeText(activity, "请提交视频封面", Toast.LENGTH_LONG).show();
        }else {
            Log.d(TAG,videoFilePath);
            Toast.makeText(activity, "正在上传...", Toast.LENGTH_LONG).show();
            //获取上传凭证
            createUploadVideo("https://aliyun.banghua.xin/createUploadVideo.php");
        }
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
    //初始化工具栏
    public void initToolbar(){
        //工具栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("我的视频介绍");
        toolbar.inflateMenu(R.menu.menu_toolbar_submit);
        toolbar.setNavigationIcon(R.drawable.rc_back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed(); // back button
                Intent intent = new Intent(IntroductionVideoActivity.this, IntroductionActivity.class);
                startActivity(intent);
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.submit_toolbar){
                    Log.d(TAG, "提交了");
                    //判断是否录制了视频和视频封面后，获取上传凭证，再获上传并获取播放地址，然后将封面和视频播放地址提交数据库
                    startUpload();
                }
                return true;
            }
        });
    }
    //初始化组件
    public void initConponent(){
        video_cover = findViewById(R.id.video_cover);
        video_cover_row = findViewById(R.id.video_cover_row);
        surface_row = findViewById(R.id.surface_row);
        playbutton_img = findViewById(R.id.playbutton_img);
        video_record = findViewById(R.id.video_record);
        setcover_btn = findViewById(R.id.setcover_btn);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
    }
    //录制完成后获取视频地址
    public void afterRecord(){
        Intent intent = getIntent();
        videoFilePath = intent.getStringExtra("filePath");
        Log.d(TAG,"获取了文件"+videoFilePath);
        if (videoFilePath != null && videoFilePath != ""){
            Log.d(TAG, "准备播放刚录制的视频。");
            videoUrl = videoFilePath;
            startThumbSelectActivity(videoUrl);
        }
    }
    //初始化录制
    public void initVideoRecord(){

        video_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlivcRecordInputParam recordParam = new AlivcRecordInputParam.Builder().build();
                AlivcSvideoRecordActivity.startRecord(getBaseContext(), recordParam);
            }
        });
    }
    //初始化播放器。
    public void initVideoPlay(){

        Log.d(TAG,"初始化播放器。");
        //准备播放器
        initAliyunVodPlayer();
        initDataSource();
        initSurfaceView();

        //显示播放按钮
        playbutton_img.setVisibility(View.VISIBLE);

        playbutton_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetPicker();
                Log.d(TAG,"点击了播放。。");
                surface_row.setVisibility(View.VISIBLE);
                video_cover_row.setVisibility(View.GONE);
                aliyunVodPlayer.start();
            }
        });
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetPicker();
                Log.d(TAG,"点击了播放。。");
                switch (video_state){
                    case 3:
                        aliyunVodPlayer.pause();
                        break;
                    case 4:
                        aliyunVodPlayer.start();
                        break;
                }

            }
        });
    }
    //上传视频
    private void uploadVideo(String path){

        final VODUploadClientImpl uploader = new VODUploadClientImpl(activity);

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



    //处理返回的数据
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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
                        updateDatabase("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=Introductionvideo&m=moyuan");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    Toast.makeText(activity, "上传成功", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    try {
                        Log.d(TAG, "handleMessage: 获取已有的视频信息"+msg.obj.toString());

                        JSONObject jsonObject = new ParseJSONObject(msg.obj.toString()).getParseJSON();

                        if (jsonObject.getString("videoid").length() > 5){
                            Log.d(TAG, "准备播放已存在的视频。");
                            videoID = jsonObject.getString("videoid");
                            videoUrl = jsonObject.getString("videourl");
                            postVideoCover = jsonObject.getString("videocover");
                            Glide.with(activity)
                                    .asBitmap()
                                    .load(postVideoCover)
                                    .into(video_cover);
                            //播放按钮
                            initVideoPlay();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    //TODO okhttp获取已有的视频信息
    public void getVideoInfo(final String url){
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
                    message.what=4;
                    Log.d(TAG, "run: 获取的视频信息"+message.obj.toString());
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
                MediaType MEDIA_TYPE_PNG = MediaType.parse("image");
                MultipartBody.Builder multipartBody = new MultipartBody.Builder();
                multipartBody.setType(MultipartBody.FORM);
                multipartBody.addFormDataPart("authid", myid);
                multipartBody.addFormDataPart("videoID", videoID);
                multipartBody.addFormDataPart("videoUrl", videoUrl);
                if (!postVideoCover.isEmpty()) {
                    if (!postVideoCover.contains("https")) {
                        multipartBody.addFormDataPart("postVideoCover", fileName, RequestBody.create(new File(postVideoCover), MEDIA_TYPE_PNG));
                    }else {
                        multipartBody.addFormDataPart("postVideoCover", postVideoCover);
                    }
                }
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

    public void initSurfaceView(){

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                aliyunVodPlayer.setDisplay(holder);
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                aliyunVodPlayer.redraw();
            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                aliyunVodPlayer.setDisplay(null);
            }
        });
    }
    public void initAliyunVodPlayer(){
        Log.d(TAG, "播放监听。");


        aliyunVodPlayer = AliPlayerFactory.createAliPlayer(this);


        aliyunVodPlayer.setOnCompletionListener(new IPlayer.OnCompletionListener() {
            @Override
            public void onCompletion() {
                //播放完成事件
                surface_row.setVisibility(View.VISIBLE);
                video_cover_row.setVisibility(View.GONE);
            }
        });
        aliyunVodPlayer.setOnErrorListener(new IPlayer.OnErrorListener() {
            @Override
            public void onError(ErrorInfo errorInfo) {
                //出错事件
            }
        });
        aliyunVodPlayer.setOnPreparedListener(new IPlayer.OnPreparedListener() {
            @Override
            public void onPrepared() {
                //准备成功事件
                Log.d(TAG,"准备成功");
                Log.d(TAG,"视频长度："+aliyunVodPlayer.getDuration());
            }
        });
        aliyunVodPlayer.setOnVideoSizeChangedListener(new IPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(int width, int height) {
                //视频分辨率变化回调
            }
        });
        aliyunVodPlayer.setOnRenderingStartListener(new IPlayer.OnRenderingStartListener() {
            @Override
            public void onRenderingStart() {
                //首帧渲染显示事件
            }
        });
        aliyunVodPlayer.setOnInfoListener(new IPlayer.OnInfoListener() {
            @Override
            public void onInfo(InfoBean infoBean) {
                //其他信息的事件，type包括了：循环播放开始，缓冲位置，当前播放位置，自动播放开始等
            }
        });
        aliyunVodPlayer.setOnLoadingStatusListener(new IPlayer.OnLoadingStatusListener() {
            @Override
            public void onLoadingBegin() {
                //缓冲开始。
            }
            @Override
            public void onLoadingProgress(int percent, float kbps) {
                //缓冲进度
            }
            @Override
            public void onLoadingEnd() {
                //缓冲结束
            }
        });
        aliyunVodPlayer.setOnSeekCompleteListener(new IPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete() {
                //拖动结束
            }
        });
        aliyunVodPlayer.setOnSubtitleDisplayListener(new IPlayer.OnSubtitleDisplayListener() {
            @Override
            public void onSubtitleShow(long id, String data) {
                //显示字幕
            }
            @Override
            public void onSubtitleHide(long id) {
                //隐藏字幕
            }
        });
        aliyunVodPlayer.setOnTrackChangedListener(new IPlayer.OnTrackChangedListener() {
            @Override
            public void onChangedSuccess(TrackInfo trackInfo) {
                //切换音视频流或者清晰度成功
            }
            @Override
            public void onChangedFail(TrackInfo trackInfo, ErrorInfo errorInfo) {
                //切换音视频流或者清晰度失败
            }
        });
        aliyunVodPlayer.setOnStateChangedListener(new IPlayer.OnStateChangedListener() {
            @Override
            public void onStateChanged(int newState) {
                //播放器状态改变事件
                video_state = newState;
                if (newState == 6){
                    surface_row.setVisibility(View.GONE);
                    video_cover_row.setVisibility(View.VISIBLE);
                    aliyunVodPlayer.reset();
                    aliyunVodPlayer.prepare();
                }
            }
        });
        aliyunVodPlayer.setOnSnapShotListener(new IPlayer.OnSnapShotListener() {
            @Override
            public void onSnapShot(Bitmap bm, int with, int height) {
                //截图事件
            }
        });
    }
    public void initDataSource(){
        //新播放器
        myJzvdStd.setUp(videoUrl, "");
        Glide.with(this).load(postVideoCover).into(myJzvdStd.posterImageView);

        Log.d(TAG, "初始化视频数据源。");

        UrlSource aliyunUrlSource = new UrlSource();
        aliyunUrlSource.setUri(videoUrl);


        //设置播放源
        aliyunVodPlayer.setDataSource(aliyunUrlSource);
        aliyunVodPlayer.prepare();

    }

    //初始化封面
    public void initVideoCover(){
        //封面选择
        setcover_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImagePicker.Builder(activity)
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
    }
}
