package xin.banghua.moyuan.MainBranch;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import androidx.navigation.Navigation;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xin.banghua.moyuan.Adapter.UserInfoSliderAdapter;
import xin.banghua.moyuan.ParseJSON.ParseJSONArray;
import xin.banghua.moyuan.R;
import xin.banghua.moyuan.SharedPreferences.SharedHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class FujinFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    private static final String TAG = "TuijianFragment";
    JSONArray sliderJsonArray;
    private View mView;
    private Integer pageindex = 1;
    private SliderLayout mDemoSlider;
    UserInfoSliderAdapter adapter;
    PullLoadMoreRecyclerView recyclerView;
    //button
    TableRow seewho_tablerow;
    Button seewho_btn,seeall_btn,seefemale_btn,seemale_btn;
    //vars
    private ArrayList<String> mUserID = new ArrayList<>();
    private ArrayList<String> mUserPortrait = new ArrayList<>();
    private ArrayList<String> mUserNickName = new ArrayList<>();
    private ArrayList<String> mUserAge = new ArrayList<>();
    private ArrayList<String> mUserGender = new ArrayList<>();
    private ArrayList<String> mUserProperty = new ArrayList<>();
    private ArrayList<String> mUserLocation = new ArrayList<>();
    private ArrayList<String> mUserRegion = new ArrayList<>();
    private ArrayList<String> mUserVIP = new ArrayList<>();
    private ArrayList<String> mAllowLocation = new ArrayList<>();

    //vars用来存放快捷键后的值
    private ArrayList<String> mUserID1 = new ArrayList<>();
    private ArrayList<String> mUserPortrait1 = new ArrayList<>();
    private ArrayList<String> mUserNickName1 = new ArrayList<>();
    private ArrayList<String> mUserAge1 = new ArrayList<>();
    private ArrayList<String> mUserGender1 = new ArrayList<>();
    private ArrayList<String> mUserProperty1 = new ArrayList<>();
    private ArrayList<String> mUserLocation1 = new ArrayList<>();
    private ArrayList<String> mUserRegion1 = new ArrayList<>();
    private ArrayList<String> mUserVIP1 = new ArrayList<>();
    private ArrayList<String> mAllowLocation1 = new ArrayList<>();
    public FujinFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_fujin, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //使用okhttp获取全部用户信息
        //getDataUserinfo("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=fujin&m=moyuan");
        //使用okhttp获取推荐的幻灯片
        getDataSlide("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=fujin&m=moyuan");

        initNavigateButton(view);

        recyclerView = view.findViewById(R.id.tuijian_RecyclerView);
        //快捷按钮
        seewho_tablerow = mView.findViewById(R.id.seewho_tablerow);
        seewho_btn = view.findViewById(R.id.seewho_btn);
        seewho_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (seewho_tablerow.getVisibility()==View.VISIBLE){
                    seewho_tablerow.setVisibility(View.GONE);
                }else {
                    seewho_tablerow.setVisibility(View.VISIBLE);
                }
            }
        });
        seeall_btn = view.findViewById(R.id.seeall_btn);
        seeall_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示全部
                adapter.swapData(mUserID,mUserPortrait,mUserNickName,mUserAge,mUserGender,mUserProperty,mUserLocation,mUserRegion,mUserVIP,mAllowLocation);
            }
        });
        seefemale_btn = view.findViewById(R.id.seefemale_btn);
        seefemale_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserID1.clear();
                mUserPortrait1.clear();
                mUserNickName1.clear();
                mUserAge1.clear();
                mUserGender1.clear();
                mUserProperty1.clear();
                mUserLocation1.clear();
                mUserRegion1.clear();
                mUserVIP1.clear();
                mAllowLocation1.clear();
                //剔除男的
                for (int i=0;i<mUserID.size();i++){
                    if (mUserGender.get(i).equals("女")){
                        mUserID1.add(mUserID.get(i));
                        mUserPortrait1.add(mUserPortrait.get(i));
                        mUserNickName1.add(mUserNickName.get(i));
                        mUserAge1.add(mUserAge.get(i));
                        mUserGender1.add(mUserGender.get(i));
                        mUserProperty1.add(mUserProperty.get(i));
                        mUserLocation1.add(mUserLocation.get(i));
                        mUserRegion1.add(mUserRegion.get(i));
                        mUserVIP1.add(mUserVIP.get(i));
                        mAllowLocation1.add(mAllowLocation.get(i));
                    }
                }
                //显示女
                adapter.swapData(mUserID1,mUserPortrait1,mUserNickName1,mUserAge1,mUserGender1,mUserProperty1,mUserLocation1,mUserRegion1,mUserVIP1,mAllowLocation1);
            }
        });
        seemale_btn = view.findViewById(R.id.seemale_btn);
        seemale_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserID1.clear();
                mUserPortrait1.clear();
                mUserNickName1.clear();
                mUserAge1.clear();
                mUserGender1.clear();
                mUserProperty1.clear();
                mUserLocation1.clear();
                mUserRegion1.clear();
                mUserVIP1.clear();
                mAllowLocation1.clear();
                //剔除男的
                for (int i=0;i<mUserID.size();i++){
                    if (mUserGender.get(i).equals("男")){
                        mUserID1.add(mUserID.get(i));
                        mUserPortrait1.add(mUserPortrait.get(i));
                        mUserNickName1.add(mUserNickName.get(i));
                        mUserAge1.add(mUserAge.get(i));
                        mUserGender1.add(mUserGender.get(i));
                        mUserProperty1.add(mUserProperty.get(i));
                        mUserLocation1.add(mUserLocation.get(i));
                        mUserRegion1.add(mUserRegion.get(i));
                        mUserVIP1.add(mUserVIP.get(i));
                        mAllowLocation1.add(mAllowLocation.get(i));
                    }
                }
                //显示男
                adapter.swapData(mUserID1,mUserPortrait1,mUserNickName1,mUserAge1,mUserGender1,mUserProperty1,mUserLocation1,mUserRegion1,mUserVIP1,mAllowLocation1);
            }
        });
    }

    //三个按钮初始化
    private void initNavigateButton(View view){
        Button tuijian = view.findViewById(R.id.tuijian_btn);
        //Button fujin = view.findViewById(R.id.fujin_btn);
        Button sousuo = view.findViewById(R.id.sousuo_btn);


        tuijian.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.fujin_tuijian_action));
        sousuo.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.fujin_sousuo_action));

    }

    //TODO 幻灯片相关
    private void initSlider(View view,JSONArray jsonArray) throws JSONException {
//        mDemoSlider = view.findViewById(R.id.tuijian_slider);
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
    private void initUserInfo(View view,JSONArray jsonArray) throws JSONException {
        Log.d(TAG, "initUserInfo: preparing userinfo");

        if (jsonArray.length()>0){
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mUserID.add(jsonObject.getString("id"));
                mUserPortrait.add(jsonObject.getString("portrait"));
                mUserNickName.add(jsonObject.getString("nickname"));
                mUserAge.add(jsonObject.getString("age"));
                mUserGender.add(jsonObject.getString("gender"));
                mUserProperty.add(jsonObject.getString("property"));
                mUserLocation.add(jsonObject.getString("location"));
                mUserRegion.add(jsonObject.getString("region"));
                mUserVIP.add(jsonObject.getString("vip"));
                mAllowLocation.add(jsonObject.getString("allowlocation"));
            }
        }

        if (pageindex>1){
            //第二页以上，只加载刷新，不新建recyclerView
            adapter.swapData(mUserID,mUserPortrait,mUserNickName,mUserAge,mUserGender,mUserProperty,mUserLocation,mUserRegion,mUserVIP,mAllowLocation);
        }else {
            //初次加载
            initRecyclerView(view,mUserID,mUserPortrait,mUserNickName,mUserAge,mUserGender,mUserProperty,mUserLocation,mUserRegion,mUserVIP,mAllowLocation);
        }
    }

    private void initRecyclerView(View view,ArrayList<String> mUserID,ArrayList<String> mUserPortrait,ArrayList<String> mUserNickName,ArrayList<String> mUserAge,ArrayList<String> mUserGender,ArrayList<String> mUserProperty,ArrayList<String> mUserLocation,ArrayList<String> mUserRegion,ArrayList<String> mUserVIP,ArrayList<String> mAllowLocation){
        Log.d(TAG, "initRecyclerView: init recyclerview");

        adapter = new UserInfoSliderAdapter(view.getContext(),sliderJsonArray,mUserID,mUserPortrait,mUserNickName,mUserAge,mUserGender,mUserProperty,mUserLocation,mUserRegion,mUserVIP,mAllowLocation);
        recyclerView.setAdapter(adapter);
        recyclerView.setLinearLayout();
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {

                Log.d(TAG, "onRefresh: start");
                recyclerView.setPullLoadMoreCompleted();
            }

            @Override
            public void onLoadMore() {

                pageindex = pageindex+1;

                getDataUserinfo("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=fujin&m=moyuan",pageindex+"");
                Log.d(TAG, "附近页码："+pageindex);
                recyclerView.setPullLoadMoreCompleted();
            }
        });
    }


    //TODO okhttp获取用户信息
    public void getDataUserinfo(final String url,final String pageindex){
        new Thread(new Runnable() {
            @Override
            public void run(){
                SharedHelper sh = new SharedHelper(getActivity());
                Map<String,String> locationInfo = sh.readLocation();
                String myid = sh.readUserInfo().get("userID");

                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("type", "getUserInfo")
                        .add("myid", myid)
                        .add("latitude",locationInfo.get("latitude"))
                        .add("longitude",locationInfo.get("longitude"))
                        .add("pageindex",pageindex)
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
            //1是用户数据，2是幻灯片
            switch (msg.what){
                case 1:
                    try {
                        String resultJson1 = msg.obj.toString();
                        Log.d(TAG, "handleMessage: 用户数据接收的值"+msg.obj.toString());

                        JSONArray jsonArray = new ParseJSONArray(msg.obj.toString()).getParseJSON();
                        initUserInfo(mView,jsonArray);
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
                        getDataUserinfo("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=fujin&m=moyuan","1");
                        //initSlider(mView,jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

}
