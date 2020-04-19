package xin.banghua.moyuan.Main4Branch;


import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.Toast;
import android.widget.ToggleButton;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.jzvd.Jzvd;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xin.banghua.moyuan.Adapter.LuntanAdapter;
import xin.banghua.moyuan.Adapter.LuntanList;
import xin.banghua.moyuan.Adapter.LuntanSliderAdapter;
import xin.banghua.moyuan.MarqueeTextView;
import xin.banghua.moyuan.MarqueeTextViewClickListener;
import xin.banghua.moyuan.ParseJSON.ParseJSONArray;
import xin.banghua.moyuan.R;
import xin.banghua.moyuan.SharedPreferences.SharedHelper;
import xin.banghua.moyuan.utils.AutoPlayUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class LuntanFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    private static final String TAG = "LuntanFragment";
    JSONArray sliderJsonArray;
    //公告
    String[] strs;
    MarqueeTextView marqueeTv;
    //幻灯
    private SliderLayout mDemoSlider;
    //帖子列表
    private List<LuntanList> luntanLists = new ArrayList<>();
    private LuntanSliderAdapter adapter;
    PullLoadMoreRecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    //页码
    private Integer pageindex = 1;
    //二级菜单
    ToggleButton toggleButton1,toggleButton2,toggleButton3,toggleButton4,toggleButton5;

    private View mView;

    private String subtitle;

    public LuntanFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_luntan, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //首页初始化
        getDataGonggao("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=luntan&m=moyuan");
        getDataSlider("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=luntan&m=moyuan","首页");
        //getDataPostlist("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=luntan&m=moyuan","首页");

        initNavigateButton(view);
        initSubnavigationButton(view);
        initTieziRelease(view);
    }

    //三个按钮初始化
    private void initNavigateButton(View view){
        Button guangchang = view.findViewById(R.id.guangchang_btn);
        //Button guanzhu = view.findViewById(R.id.guanzhu_btn);
        //Button luntan = view.findViewById(R.id.luntan_btn);

        guangchang.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.luntan_guangchang_action));
        //guanzhu.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.guangchang_guanzhu_action));
        //luntan.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.guangchang_luntan_action));

    }

    //二级菜单
    private void initSubnavigationButton(View view){
        toggleButton1 = view.findViewById(R.id.toggleButton1);
        toggleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageindex = 1;
                toggleButton2.setChecked(false);
                toggleButton3.setChecked(false);
                toggleButton4.setChecked(false);
                toggleButton5.setChecked(false);
                getDataGonggao("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=luntan&m=moyuan");
                getDataSlider("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=luntan&m=moyuan","首页");
                //getDataPostlist("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=luntan&m=moyuan","首页");
            }
        });
        toggleButton2 = view.findViewById(R.id.toggleButton2);
        toggleButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageindex = 1;
                toggleButton1.setChecked(false);
                toggleButton3.setChecked(false);
                toggleButton4.setChecked(false);
                toggleButton5.setChecked(false);
                marqueeTv.setVisibility(View.GONE);
                getDataSlider("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=luntan&m=moyuan","自拍");
                //getDataPostlist("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=luntan&m=moyuan","自拍");
            }
        });
        toggleButton3 = view.findViewById(R.id.toggleButton3);
        toggleButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageindex = 1;
                toggleButton1.setChecked(false);
                toggleButton2.setChecked(false);
                toggleButton4.setChecked(false);
                toggleButton5.setChecked(false);
                marqueeTv.setVisibility(View.GONE);
                getDataSlider("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=luntan&m=moyuan","真实");
                //getDataPostlist("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=luntan&m=moyuan","真实");
            }
        });
        toggleButton4 = view.findViewById(R.id.toggleButton4);
        toggleButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageindex = 1;
                toggleButton1.setChecked(false);
                toggleButton2.setChecked(false);
                toggleButton3.setChecked(false);
                toggleButton5.setChecked(false);
                marqueeTv.setVisibility(View.GONE);
                getDataSlider("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=luntan&m=moyuan","情感");
                //getDataPostlist("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=luntan&m=moyuan","情感");
            }
        });
        toggleButton5 = view.findViewById(R.id.toggleButton5);
        toggleButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageindex = 1;
                toggleButton1.setChecked(false);
                toggleButton2.setChecked(false);
                toggleButton3.setChecked(false);
                toggleButton4.setChecked(false);
                marqueeTv.setVisibility(View.GONE);
                getVipinfo("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=viptimeinsousuo&m=moyuan");
                //getDataSlider("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=luntan&m=moyuan","大圈");
                //getDataPostlist("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=luntan&m=moyuan","大圈");
            }
        });
    }
    //发布帖子
    private void initTieziRelease(View view) {
        FloatingActionButton floatingActionButton = view.findViewById(R.id.luntanRelease);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigation.findNavController(v).navigate(R.id.luntan_fabutiezi_action);
                Intent intent = new Intent(getActivity() ,FabutieziActivity.class);
                intent.putExtra("logtype","1");
                startActivity(intent);
            }
        });
    }
    private void initGonggao(View view,JSONArray jsonArray) throws JSONException {
        if (jsonArray.length()>0){
            strs = new String[jsonArray.length()];
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                strs[i]=jsonObject.getString("noticeinfo");
            }
            marqueeTv =  view.findViewById(R.id.marquee);
            marqueeTv.setTextArraysAndClickListener(strs, new MarqueeTextViewClickListener() {
                @Override
                public void onClick(View view) {
                    //startActivity(new Intent(MainActivity.this,AnotherActivity.class));
                }
            });
        }else {
            marqueeTv.setVisibility(View.GONE);
        }

    }
    //TODO 幻灯片相关
    private void initSlider(View view,JSONArray jsonArray) throws JSONException {
//        mDemoSlider = view.findViewById(R.id.luntan_slider);
//        mDemoSlider.removeAllSliders();
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
//
//            mDemoSlider.addSlider(textSliderView);
//        }
//
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

    @SuppressLint("ClickableViewAccessibility")
    private void initPostList(View view, JSONArray jsonArray) throws JSONException {
        Log.d(TAG, "initPostList: start");
        if (pageindex>1){
            //不是第一页，不用清零，直接更新
            if (jsonArray.length()>0){
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String[] postPicture = jsonObject.getString("postpicture").split(",");
                    Map map = new HashMap();
                    map.put("age",jsonObject.getString("age"));
                    map.put("gender",jsonObject.getString("gender"));
                    map.put("region",jsonObject.getString("region"));
                    map.put("property",jsonObject.getString("property"));
                    map.put("id",jsonObject.getString("id"));
                    map.put("plateid",jsonObject.getString("plateid"));
                    map.put("platename",jsonObject.getString("platename"));
                    map.put("authid",jsonObject.getString("authid"));
                    map.put("authnickname",jsonObject.getString("authnickname"));
                    map.put("authportrait",jsonObject.getString("authportrait"));
                    map.put("poi",jsonObject.getString("poi"));
                    map.put("posttitle",jsonObject.getString("posttitle"));
                    map.put("posttext",jsonObject.getString("posttext"));
                    map.put("like",jsonObject.getString("like"));
                    map.put("favorite",jsonObject.getString("favorite"));
                    map.put("time",jsonObject.getString("time"));
                    map.put("videourl",jsonObject.getString("videourl"));
                    map.put("videocover",jsonObject.getString("videocover"));
                    map.put("videowidth",jsonObject.getString("videowidth"));
                    map.put("videoheight",jsonObject.getString("videoheight"));
                    LuntanList posts = new LuntanList(map,postPicture);
                    luntanLists.add(posts);
                }
            }
            adapter.swapData(luntanLists);
        }else {
            //不同板块，需要先清零
            luntanLists.clear();
            if (jsonArray.length()>0){
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String[] postPicture = jsonObject.getString("postpicture").split(",");
                    Map map = new HashMap();
                    map.put("age",jsonObject.getString("age"));
                    map.put("gender",jsonObject.getString("gender"));
                    map.put("region",jsonObject.getString("region"));
                    map.put("property",jsonObject.getString("property"));
                    map.put("id",jsonObject.getString("id"));
                    map.put("plateid",jsonObject.getString("plateid"));
                    map.put("platename",jsonObject.getString("platename"));
                    map.put("authid",jsonObject.getString("authid"));
                    map.put("authnickname",jsonObject.getString("authnickname"));
                    map.put("authportrait",jsonObject.getString("authportrait"));
                    map.put("poi",jsonObject.getString("poi"));
                    map.put("posttitle",jsonObject.getString("posttitle"));
                    map.put("posttext",jsonObject.getString("posttext"));
                    map.put("like",jsonObject.getString("like"));
                    map.put("favorite",jsonObject.getString("favorite"));
                    map.put("time",jsonObject.getString("time"));
                    map.put("videourl",jsonObject.getString("videourl"));
                    map.put("videocover",jsonObject.getString("videocover"));
                    map.put("videowidth",jsonObject.getString("videowidth"));
                    map.put("videoheight",jsonObject.getString("videoheight"));
                    LuntanList posts = new LuntanList(map,postPicture);
                    luntanLists.add(posts);
                }
            }

            recyclerView = view.findViewById(R.id.luntan_RecyclerView);
            mLayoutManager = new LinearLayoutManager(getActivity());

            adapter = new LuntanSliderAdapter(luntanLists,sliderJsonArray,view.getContext());
            mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.getRecyclerView().setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
                @Override
                public void onRefresh() {
                    Log.d(TAG, "onLoadMore: start");

                    recyclerView.setPullLoadMoreCompleted();

                }

                @Override
                public void onLoadMore() {

                    pageindex = pageindex+1;

                    getDataPostlist("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=luntan&m=moyuan",subtitle,pageindex+"");
                    Log.d(TAG, "论坛页码："+pageindex);
                    recyclerView.setPullLoadMoreCompleted();
                }

            });
            recyclerView.getRecyclerView().addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
                @Override
                public void onChildViewAttachedToWindow(View view) {

                }

                @Override
                public void onChildViewDetachedFromWindow(View view) {
                    Jzvd jzvd = view.findViewById(R.id.jz_video);
                    if (jzvd != null && Jzvd.CURRENT_JZVD != null &&
                            jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
                        if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN ) {
                            Jzvd.releaseAllVideos();
                        }
                    }
                }
            });

            recyclerView.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        AutoPlayUtils.onScrollPlayVideo(recyclerView, R.id.jz_video, mLayoutManager.findFirstVisibleItemPosition(), mLayoutManager.findLastVisibleItemPosition());
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy != 0) {
                        AutoPlayUtils.onScrollReleaseAllVideos(mLayoutManager.findFirstVisibleItemPosition(), mLayoutManager.findLastVisibleItemPosition(), 0.2f);
                    }
                }
            });
        }
    }
    //网络数据部分
    //处理返回的数据
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //1是公告，2是幻灯片，3是帖子
            switch (msg.what){
                case 1:
                    try {
                        Log.d(TAG, "handleMessage: 公告接收的值"+msg.obj.toString());
                        JSONArray jsonArray = new ParseJSONArray(msg.obj.toString()).getParseJSON();
                        initGonggao(mView,jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        Log.d(TAG, "handleMessage: 幻灯片接收的值"+msg.obj.toString());
                        JSONArray jsonArray = new ParseJSONArray(msg.obj.toString()).getParseJSON();
                        sliderJsonArray = jsonArray;
                        Log.d(TAG, "handleMessage: subtitle"+subtitle);
                        getDataPostlist("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=luntan&m=moyuan",subtitle,"1");
                        //initSlider(mView,jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {

                        Log.d(TAG, "handleMessage: 帖子接收的值"+msg.obj.toString());

                        JSONArray jsonArray = new ParseJSONArray(msg.obj.toString()).getParseJSON();
                        initPostList(mView,jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    if (msg.obj.toString().equals("会员已到期")){
                        Toast.makeText(getActivity(), "此版块需要开通会员", Toast.LENGTH_LONG).show();
                    }else {
                        getDataSlider("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=luntan&m=moyuan","精华");
                    }
                    break;
            }
        }
    };
    //TODO okhttp获取论坛信息  1.公告，2.幻灯片，3.帖子
    public void getDataGonggao(final String url){
        new Thread(new Runnable() {
            @Override
            public void run(){
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("type", "getGonggao")
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
    public void getDataSlider(final String url, final String subNav){
        new Thread(new Runnable() {
            @Override
            public void run(){
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("type", "getSlide")
                        .add("slidesort", subNav)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();
                subtitle = subNav;
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
    public void getDataPostlist(final String url,final String subNav,final String pageindex){
        new Thread(new Runnable() {
            @Override
            public void run(){
                SharedHelper sh = new SharedHelper(getActivity());
                String myid = sh.readUserInfo().get("userID");

                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("type", "getPostlist")
                        .add("myid", myid)
                        .add("platename", subNav)
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
                    message.what=3;
                    Log.d(TAG, "run: Userinfo发送的值"+message.obj.toString());
                    handler.sendMessageDelayed(message,10);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
                    message.what=4;
                    Log.d(TAG, "run: Userinfo发送的值"+message.obj.toString());
                    handler.sendMessageDelayed(message,10);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
