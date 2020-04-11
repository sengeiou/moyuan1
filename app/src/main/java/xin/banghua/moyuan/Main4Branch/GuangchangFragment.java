package xin.banghua.moyuan.Main4Branch;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.navigation.Navigation;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xin.banghua.moyuan.Adapter.DongtaiList;
import xin.banghua.moyuan.Adapter.DongtaiSliderAdapter;
import xin.banghua.moyuan.ParseJSON.ParseJSONArray;
import xin.banghua.moyuan.R;
import xin.banghua.moyuan.SharedPreferences.SharedHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class GuangchangFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private static final String TAG = "GuangchangFragment";
    JSONArray sliderJsonArray;
    public Map<String,String> userInfo;
    private SharedHelper sh;

    private View mView;

    private SliderLayout mDemoSlider;
    //var
    private List<DongtaiList> dongtaiLists = new ArrayList<>();

    public GuangchangFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_guangchang, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //使用okhttp获取全部用户信息
        //getDataDongtai("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=guangchang&m=moyuan");
        //使用okhttp获取推荐的幻灯片
        getDataSlide("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=guangchang&m=moyuan");

        initNavigateButton(view);

        initDongtaiRelease(view);
    }

    private void initDongtaiRelease(View view) {
        FloatingActionButton floatingActionButton = view.findViewById(R.id.dongtaiRelease);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.guangchang_fabudongtai_action);
            }
        });
    }

    //三个按钮初始化
    private void initNavigateButton(View view){
        //Button guangchang = view.findViewById(R.id.guangchang_btn);
        //Button guanzhu = view.findViewById(R.id.guanzhu_btn);
        Button luntan = view.findViewById(R.id.luntan_btn);


        //guanzhu.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.guangchang_guanzhu_action));
        luntan.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.guangchang_luntan_action));

    }
    //TODO 幻灯片相关
    private void initSlider(View view,JSONArray jsonArray) throws JSONException {
//        mDemoSlider = view.findViewById(R.id.guangchang_slider);
//
//        HashMap<String,String> url_maps = new HashMap<String, String>();
//        if (jsonArray.length()>0){
//            for (int i=0;i<jsonArray.length();i++){
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                url_maps.put(jsonObject.getString("slidename"), jsonObject.getString("slidepicture"));
//            }
//        }
//
//        for(String name : url_maps.keySet()){
//            TextSliderView textSliderView = new TextSliderView(getActivity());
//            // initialize a SliderLayout
//            textSliderView
//                    .description(name)
//                    .image(url_maps.get(name))
//                    .setScaleType(BaseSliderView.ScaleType.Fit)
//                    .setOnSliderClickListener(this);
//
//            //add your extra information
//            textSliderView.bundle(new Bundle());
//            textSliderView.getBundle()
//                    .putString("extra",name);
//
//            mDemoSlider.addSlider(textSliderView);
//        }
//        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
//        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
//        mDemoSlider.setDuration(4000);
//        mDemoSlider.addOnPageChangeListener(this);
    }
    @Override
    public void onSliderClick(BaseSliderView slider) {
        Log.d(TAG, "onSliderClick: clicked");

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d(TAG, "onPageScrolled: scrolled");

    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected: selected");

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d(TAG, "onPageScrollStateChanged: changed");

    }

    //TODO 用户列表
    private void initDongtai(View view,JSONArray jsonArray) throws JSONException {
        Log.d(TAG, "initUserInfo: preparing userinfo");

        if (jsonArray.length()>0){
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String[] picture = jsonObject.getString("picture").split(",");

                String circlePicture1 = "";
                String circlePicture2 = "";
                String circlePicture3 = "";

                switch (picture.length){
                    case 1:
                        circlePicture1 = picture[0];
                        break;
                    case 2:
                        circlePicture1 = picture[0];
                        circlePicture2 = picture[1];
                        break;
                    case 3:
                        circlePicture1 = picture[0];
                        circlePicture2 = picture[1];
                        circlePicture3 = picture[2];
                        break;
                }
                DongtaiList dongtaiList = new DongtaiList(jsonObject.getString("age"),jsonObject.getString("gender"),jsonObject.getString("region"),jsonObject.getString("property"),jsonObject.getString("id"),jsonObject.getString("myid"),jsonObject.getString("mynickname"),jsonObject.getString("myportrait"),jsonObject.getString("context"),circlePicture1,circlePicture2,circlePicture3,jsonObject.getString("video"),jsonObject.getString("share"),jsonObject.getString("like"),jsonObject.getString("time"));
                dongtaiLists.add(dongtaiList);
            }
        }
        initRecyclerView(view);
    }

    //TODO 初始化动态recyclerview
    private void initRecyclerView(View view){
        Log.d(TAG, "initRecyclerView: init recyclerview");

        final PullLoadMoreRecyclerView recyclerView = view.findViewById(R.id.dongtai_RecyclerView);
        DongtaiSliderAdapter adapter = new DongtaiSliderAdapter(view.getContext(),sliderJsonArray,dongtaiLists);
        recyclerView.setAdapter(adapter);
        recyclerView.setLinearLayout();;
        recyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {

                recyclerView.setPullLoadMoreCompleted();

            }

            @Override
            public void onLoadMore() {

                  recyclerView.setPullLoadMoreCompleted();
            }
        });
    }

    //网络数据部分
    //TODO okhttp获取动态列表
    public void getDataDongtai(final String url){
        new Thread(new Runnable() {
            @Override
            public void run(){
                sh = new SharedHelper(mView.getContext());
                userInfo = sh.readUserInfo();
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("type", "getDongtai")
                        .add("userID", userInfo.get("userID"))
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

    //TODO okhttp获取幻灯片
    public void getDataSlide(final String url){
        new Thread(new Runnable() {
            @Override
            public void run(){
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("type", "getSlide")
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
                    Log.d(TAG, "run: Slide发送的值"+message.obj.toString());
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
            //1是动态数据，2是幻灯片
            switch (msg.what){
                case 1:
                    try {
                        String resultJson1 = msg.obj.toString();
                        Log.d(TAG, "handleMessage: 动态信息接收的值"+msg.obj.toString());

                        JSONArray jsonArray = new ParseJSONArray(msg.obj.toString()).getParseJSON();
                        initDongtai(mView,jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        String resultJson2 = msg.obj.toString();
                        Log.d(TAG, "handleMessage: 幻灯片接收的值"+msg.obj.toString());
                        JSONArray jsonArray = new ParseJSONArray(msg.obj.toString()).getParseJSON();
                        sliderJsonArray = jsonArray;
                        getDataDongtai("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=guangchang&m=moyuan");
                        //initSlider(mView,jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

}
