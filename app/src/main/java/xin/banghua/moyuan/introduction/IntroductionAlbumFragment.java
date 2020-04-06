package xin.banghua.moyuan.introduction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xin.banghua.moyuan.ParseJSON.ParseJSONArray;
import xin.banghua.moyuan.ParseJSON.ParseJSONObject;
import xin.banghua.moyuan.R;
import xin.banghua.moyuan.SharedPreferences.SharedHelper;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IntroductionAlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntroductionAlbumFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button submit_btn;
    ImageView picture1,picture2,picture3,picture4,picture5,picture6;
    int picture_state;
    String postpicture1 = "";
    String postpicture2 = "";
    String postpicture3 = "";
    String postpicture4 = "";
    String postpicture5 = "";
    String postpicture6 = "";

    private Context mContext;
    View mView;

    private static final String TAG = "IntroductionAlbum";

    public IntroductionAlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IntroductionAlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IntroductionAlbumFragment newInstance(String param1, String param2) {
        IntroductionAlbumFragment fragment = new IntroductionAlbumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_introduction_album, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("我的相册介绍");
        toolbar.inflateMenu(R.menu.menu_toolbar_submit);
        toolbar.setNavigationIcon(R.drawable.rc_back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed(); // back button
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


        picture1 = view.findViewById(R.id.picture1);
        picture2 = view.findViewById(R.id.picture2);
        picture3 = view.findViewById(R.id.picture3);
        picture4 = view.findViewById(R.id.picture4);
        picture5 = view.findViewById(R.id.picture5);
        picture6 = view.findViewById(R.id.picture6);

        picture1.setImageResource(R.drawable.plus);
        picture2.setImageResource(R.drawable.plus);
        picture3.setImageResource(R.drawable.plus);
        picture4.setImageResource(R.drawable.plus);
        picture5.setImageResource(R.drawable.plus);
        picture6.setImageResource(R.drawable.plus);



        picture1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImagePicker.Builder(getActivity())
                        .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                        .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                        .directory(ImagePicker.Directory.DEFAULT)
                        .extension(ImagePicker.Extension.PNG)
                        .scale(600, 600)
                        .allowMultipleImages(false)
                        .enableDebuggingMode(true)
                        .build();
                picture_state = 1;
            }
        });
        picture2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImagePicker.Builder(getActivity())
                        .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                        .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                        .directory(ImagePicker.Directory.DEFAULT)
                        .extension(ImagePicker.Extension.PNG)
                        .scale(600, 600)
                        .allowMultipleImages(false)
                        .enableDebuggingMode(true)
                        .build();
                picture_state = 2;
            }
        });
        picture3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImagePicker.Builder(getActivity())
                        .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                        .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                        .directory(ImagePicker.Directory.DEFAULT)
                        .extension(ImagePicker.Extension.PNG)
                        .scale(600, 600)
                        .allowMultipleImages(false)
                        .enableDebuggingMode(true)
                        .build();
                picture_state = 3;
            }
        });
        picture4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImagePicker.Builder(getActivity())
                        .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                        .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                        .directory(ImagePicker.Directory.DEFAULT)
                        .extension(ImagePicker.Extension.PNG)
                        .scale(600, 600)
                        .allowMultipleImages(false)
                        .enableDebuggingMode(true)
                        .build();
                picture_state = 4;
            }
        });
        picture5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImagePicker.Builder(getActivity())
                        .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                        .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                        .directory(ImagePicker.Directory.DEFAULT)
                        .extension(ImagePicker.Extension.PNG)
                        .scale(600, 600)
                        .allowMultipleImages(false)
                        .enableDebuggingMode(true)
                        .build();
                picture_state = 5;
            }
        });
        picture6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImagePicker.Builder(getActivity())
                        .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                        .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                        .directory(ImagePicker.Directory.DEFAULT)
                        .extension(ImagePicker.Extension.PNG)
                        .scale(600, 600)
                        .allowMultipleImages(false)
                        .enableDebuggingMode(true)
                        .build();
                picture_state = 6;
            }
        });

        submit_btn = view.findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postAlbum("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=Introductionalbum&m=moyuan");
            }
        });

        getAlbum("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=Getintroductionalbum&m=moyuan");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: 动态图片地址");
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            //Your Code
            ListIterator<String> listIterator = mPaths.listIterator();
            while (listIterator.hasNext()){
                String mPath = listIterator.next();

                Log.d("path", mPath);
                switch (picture_state){
                    case 1:
                        picture1.setImageURI(Uri.parse(mPath));
                        postpicture1 = mPath;
                        picture2.setVisibility(View.VISIBLE);
                        Log.d(TAG, "onActivityResult: 动态图片地址"+picture_state+":"+postpicture1);
                        break;
                    case 2:
                        picture2.setImageURI(Uri.parse(mPath));
                        postpicture2 = mPath;
                        picture3.setVisibility(View.VISIBLE);
                        Log.d(TAG, "onActivityResult: 动态图片地址"+picture_state+":"+postpicture2);
                        break;
                    case 3:
                        picture3.setImageURI(Uri.parse(mPath));
                        postpicture3 = mPath;
                        picture4.setVisibility(View.VISIBLE);
                        Log.d(TAG, "onActivityResult: 动态图片地址"+picture_state+":"+postpicture3);
                        break;
                    case 4:
                        picture4.setImageURI(Uri.parse(mPath));
                        postpicture4 = mPath;
                        picture5.setVisibility(View.VISIBLE);
                        Log.d(TAG, "onActivityResult: 动态图片地址"+picture_state+":"+postpicture4);
                        break;
                    case 5:
                        picture5.setImageURI(Uri.parse(mPath));
                        postpicture5 = mPath;
                        picture6.setVisibility(View.VISIBLE);
                        Log.d(TAG, "onActivityResult: 动态图片地址"+picture_state+":"+postpicture5);
                        break;
                    case 6:
                        picture6.setImageURI(Uri.parse(mPath));
                        postpicture6 = mPath;
                        Log.d(TAG, "onActivityResult: 动态图片地址"+picture_state+":"+postpicture6);
                        break;
                }

            }
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("进入handler", "handler");

            if (msg.arg1==1) {
                Toast.makeText(mContext, "已发布成功，等待审核", Toast.LENGTH_LONG).show();
                Log.d("跳转", "Navigation");
                Navigation.findNavController(mView).navigate(R.id.action_introductionAlbumFragment_to_introductionMainFragment);
            }
            if (msg.arg1==2) {
                try {
                    Log.d(TAG, "getAlbum");
                    JSONObject jsonObject = new ParseJSONObject(msg.obj.toString()).getParseJSON();//自定义的
                    String resultJson = jsonObject.getString("album");
                    Log.d(TAG, resultJson);
                    if (resultJson == "null")return;
                    String[] postPicture = resultJson.split(",");
                    Log.d(TAG, "长度"+postPicture.length);
                    //Log.d(TAG, "postPicture"+postPicture[0]+postPicture[1]+postPicture[2]+postPicture[3]+postPicture[4]+postPicture[5]);

                    switch (postPicture.length){
                        case 6:
                            Glide.with(mContext)
                                    .asBitmap()
                                    .load(postPicture[5])
                                    .into(picture6);
                            postpicture6 = postPicture[5];
                        case 5:
                            Glide.with(mContext)
                                    .asBitmap()
                                    .load(postPicture[4])
                                    .into(picture5);
                            picture6.setVisibility(View.VISIBLE);
                            postpicture5 = postPicture[4];
                        case 4:
                            Glide.with(mContext)
                                    .asBitmap()
                                    .load(postPicture[3])
                                    .into(picture4);
                            picture5.setVisibility(View.VISIBLE);
                            postpicture4 = postPicture[3];
                        case 3:
                            Glide.with(mContext)
                                    .asBitmap()
                                    .load(postPicture[2])
                                    .into(picture3);
                            picture4.setVisibility(View.VISIBLE);
                            postpicture3 = postPicture[2];
                        case 2:
                            Glide.with(mContext)
                                    .asBitmap()
                                    .load(postPicture[1])
                                    .into(picture2);
                            picture3.setVisibility(View.VISIBLE);
                            postpicture2 = postPicture[1];
                        case 1:
                            Glide.with(mContext)
                                    .asBitmap()
                                    .load(postPicture[0])
                                    .into(picture1);
                            picture2.setVisibility(View.VISIBLE);
                            postpicture1 = postPicture[0];
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
                SharedHelper shuserinfo = new SharedHelper(getActivity().getApplicationContext());
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
                SharedHelper shuserinfo = new SharedHelper(getActivity().getApplicationContext());
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
}
