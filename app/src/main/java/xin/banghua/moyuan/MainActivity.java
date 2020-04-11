package xin.banghua.moyuan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
//import android.support.design.widget.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import java.io.IOException;
import java.util.List;
import java.util.Map;


import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.model.Conversation;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xin.banghua.moyuan.SharedPreferences.SharedHelper;
import xin.banghua.moyuan.Signin.SigninActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    Uniquelogin uniquelogin;

    //super果 18673668974
    private static final String token1 = "FnqsejuE+d830KfLY+PQ/r2N7KVbcvzutnIpZ+Sh9wDFjYfKqXxBctd4ec0OeFSRUAuX4eO3BltHQ60BVeZ2HA==";
    //贝吉塔 18673668975
    private String token2 = "";
    //希特 18673668976
    private static final String token3 = "PxvEuLbavYH5sNKqsxX7lb2N7KVbcvzutnIpZ+Sh9wDFjYfKqXxBcvx+1j1Oq44Au22Gnok3QTNHQ60BVeZ2HA==";


    private Context mContext = this;


    private TextView mTextMessage;

    //未读信息监听相关
    private BottomNavigationView bottomNavigationView;
    private IUnReadMessageObserver iUnReadMessageObserver;
    private TextView unreadNumber;

    //初始化底部导航按钮监听
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_shenbian:

                    //Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
                    //startActivity(intent1);
                    return true;
                case R.id.navigation_haoyou:

                    Intent intent2 = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(intent2);
                    return true;
                case R.id.navigation_xiaoxi:

                    //Intent intent3 = new Intent(MainActivity.this, Main3Activity.class);
                    //startActivity(intent3);
                    //启动会话列表
                    startActivity(new Intent(MainActivity.this, Main3Activity.class));
                    return true;
                case R.id.navigation_dongtai:

                    Intent intent4 = new Intent(MainActivity.this, Main4Activity.class);
                    startActivity(intent4);
                    return true;
                case R.id.navigation_wode:

                    Intent intent5 = new Intent(MainActivity.this, Main5Activity.class);
                    startActivity(intent5);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //判断是否登录
        ifSignin();



        //定位问题
        localization();

        //底部导航初始化和配置监听，默认选项
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.navigation_shenbian);
        //好友申请数
        BadgeBottomNav badgeBottomNav = new BadgeBottomNav(this,handler);
        badgeBottomNav.getDataFriendsapply("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=friendsapplynumber&m=moyuan");
        //未读信息监听
        iUnReadMessageObserver = new IUnReadMessageObserver() {
            @Override
            public void onCountChanged(int i) {
                BadgeBottomNav.unreadMessageBadge(bottomNavigationView,i,getApplicationContext());
                //initUnreadBadge(bottomNavigationView,i);
            }
        };
        RongIM.getInstance().addUnReadMessageCountChangedObserver(iUnReadMessageObserver,Conversation.ConversationType.PRIVATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(iUnReadMessageObserver);
    }

    public void initUnreadBadge(BottomNavigationView navigation, Integer integer){
        //底部导航栏角标
        //获取整个的NavigationView
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        //这里就是获取所添加的每一个Tab(或者叫menu)，
        View tab = menuView.getChildAt(1);
        BottomNavigationItemView itemView = (BottomNavigationItemView) tab;
        //加载我们的角标View，新创建的一个布局
        View badge = LayoutInflater.from(this).inflate(R.layout.badge_unreadmessage, menuView, false);
        //添加到Tab上
        itemView.addView(badge);
        unreadNumber = badge.findViewById(R.id.badge_text);
        unreadNumber.setText("");
        unreadNumber.setText(String.valueOf(integer));
        //无消息时可以将它隐藏即可
        if (integer>0){
            unreadNumber.setVisibility(View.VISIBLE);
        }else {
            unreadNumber.setVisibility(View.GONE);
        }
    }
    public void ifSignin(){
        Map<String,String> userInfo;
        SharedHelper sh;
        sh = new SharedHelper(getApplicationContext());
        userInfo = sh.readUserInfo();
        //Toast.makeText(mContext, userInfo.toString(), Toast.LENGTH_SHORT).show();
        if(userInfo.get("userID")==""){
            Intent intentSignin = new Intent(MainActivity.this, SigninActivity.class);
            startActivity(intentSignin);
        }else{
            //唯一登录验证
            uniquelogin = new Uniquelogin(this,handler);
            uniquelogin.compareUniqueLoginToken("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=uniquelogin&m=moyuan");
            //登录后，更新定位信息，包括经纬度和更新时间
            //获取用户id和定位值
            SharedHelper shlocation = new SharedHelper(getApplicationContext());
            Map<String,String> locationInfo = shlocation.readLocation();
            postLocationInfo(userInfo.get("userID"),locationInfo.get("latitude"),locationInfo.get("longitude"),"https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=updatelocation&m=moyuan");
        }
    }

    //定位
    private void localization() {
        Location location = getLastKnownLocation();

        if (location!=null){
            Log.d(TAG, "localization: latitude:"+location.getLatitude());
            Log.d(TAG, "localization: longitude:"+location.getLongitude());
            //保存定位值
            SharedHelper shlocation = new SharedHelper(getApplicationContext());
            //Float latitude = (float)(Math.round(location.getLatitude()*10))/10;
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
            shlocation.saveLocation(latitude+"",longitude+"");
        }
    }

    private Location getLastKnownLocation() {
        Location l=null;
        LocationManager mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                l = mLocationManager.getLastKnownLocation(provider);
            }
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }


    //更新定位信息
    public void postLocationInfo(final String userID,final String latitude, final String longitude,final String url){
        new Thread(new Runnable() {
            @Override
            public void run(){
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("userID", userID)
                        .add("latitude", latitude)
                        .add("longitude",longitude)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    //Log.d("form形式的post",response.body().string());
                    //不需要返回值
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
                case 11:
                    String resultJson1 = msg.obj.toString();
                    //Log.d(TAG, "handleMessage: 用户数据接收的值"+msg.obj.toString());
                    SharedHelper shvalue = new SharedHelper(getApplicationContext());
                    String newFriendApplyNumber = shvalue.readNewFriendApplyNumber();
                    Log.d(TAG, "对比值"+newFriendApplyNumber+":"+msg.obj.toString());
                    if (newFriendApplyNumber.equals(msg.obj.toString())){
                        //BadgeBottomNav.newFriendApplicationBadge(bottomNavigationView,msg.obj.toString(),getApplicationContext());
                    }else {
                        BadgeBottomNav.newFriendApplicationBadge(bottomNavigationView,msg.obj.toString(),getApplicationContext());
                    }

                    break;
                case 10:
                    if (msg.obj.toString().equals("false")){
                        //通知
                        uniquelogin.uniqueNotification();
                        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("userID", "");
                        editor.commit();
                        Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                        intent.putExtra("uniquelogin","强制退出");
                        startActivity(intent);
                    }
                    break;
            }
        }
    };

}
