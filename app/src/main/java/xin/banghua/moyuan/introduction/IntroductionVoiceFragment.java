package xin.banghua.moyuan.introduction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.weileyou.voicebutton.library.VoiceButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import br.com.safety.audio_recorder.AudioListener;
import br.com.safety.audio_recorder.AudioRecordButton;
import br.com.safety.audio_recorder.AudioRecording;
import br.com.safety.audio_recorder.RecordingItem;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xin.banghua.moyuan.ParseJSON.ParseJSONObject;
import xin.banghua.moyuan.R;
import xin.banghua.moyuan.SharedPreferences.SharedHelper;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IntroductionVoiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntroductionVoiceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    VoiceButton voicebutton;

    private AudioRecordButton mAudioRecordButton;
    private AudioRecording audioRecording;

    String postvoice;

    private Context mContext;

    private static final String TAG = "IntroductionVoice";


    View mView;

    public IntroductionVoiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IntroductionVoiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IntroductionVoiceFragment newInstance(String param1, String param2) {
        IntroductionVoiceFragment fragment = new IntroductionVoiceFragment();
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
        mView = inflater.inflate(R.layout.fragment_introduction_voice, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("我的语音介绍");
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
                    postVoice("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=Introductionvoice&m=moyuan");
                }
                return true;
            }
        });

        postvoice= "";
        voicebutton= (VoiceButton) view.findViewById(R.id.voicebutton);


        mAudioRecordButton = (AudioRecordButton) view.findViewById(R.id.audio_record_button);
        audioRecording = new AudioRecording(getActivity().getBaseContext());
        //权限
        ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, READ_EXTERNAL_STORAGE},0);
        ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE}, 0);

        this.mAudioRecordButton.setOnAudioListener(new AudioListener() {
            @Override
            public void onStop(RecordingItem recordingItem) {
                Toast.makeText(getActivity().getBaseContext(), "录音中...", Toast.LENGTH_SHORT).show();
                Log.d("测试",":"+recordingItem.getFilePath());
                postvoice = recordingItem.getFilePath();
                voicebutton.setPlayPath(recordingItem.getFilePath());
                audioRecording.play(recordingItem);

            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity().getBaseContext(), "取消", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                Log.d("MainActivity", "Error: " + e.getMessage());
            }
        });

        getVoice("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=Getintroductionvoice&m=moyuan");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                Navigation.findNavController(mView).navigate(R.id.action_introductionVoiceFragment_to_introductionMainFragment);
            }
            if (msg.arg1==2) {
                try {
                    Log.d(TAG, "getVoice");
                    JSONObject jsonObject = new ParseJSONObject(msg.obj.toString()).getParseJSON();//自定义的
                    String resultJson = jsonObject.getString("voice");
                    Log.d(TAG, resultJson);
                    if (resultJson == "null" || resultJson.isEmpty())return;

                    voicebutton.setPlayPath(resultJson);
                    postvoice = resultJson;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    };
    //TODO 注册 form形式的post
    public void postVoice(final String url){
        new Thread(new Runnable() {
            @Override
            public void run(){
                //获取文件名
                Log.d("进入run","run");
                String fileName = "postvoice.m4a";
                SharedHelper shuserinfo = new SharedHelper(getActivity().getApplicationContext());
                String myid = shuserinfo.readUserInfo().get("userID");
                //开始网络传输
                OkHttpClient client = new OkHttpClient();
                MediaType MEDIA_TYPE_M4A = MediaType.parse("audio");
                MultipartBody.Builder multipartBody = new MultipartBody.Builder();
                multipartBody.setType(MultipartBody.FORM);
                multipartBody.addFormDataPart("authid", myid);
                Log.d(TAG,"开始提交");
                if (!postvoice.isEmpty()) {
                    Log.d(TAG,"不为空");
                    if (!postvoice.contains("https")) {
                        Log.d(TAG,postvoice);
                        multipartBody.addFormDataPart("postvoice", fileName, RequestBody.create(new File(postvoice), MEDIA_TYPE_M4A));
                    }else {
                        multipartBody.addFormDataPart("postvoice", postvoice);
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
    public void getVoice(final String url){
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
                    Log.d(TAG, "获得的语音值"+message.obj.toString());
                    handler.sendMessageDelayed(message,10);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
