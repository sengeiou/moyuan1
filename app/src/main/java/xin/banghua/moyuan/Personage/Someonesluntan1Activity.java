package xin.banghua.moyuan.Personage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xin.banghua.moyuan.Adapter.LuntanAdapter;
import xin.banghua.moyuan.Adapter.LuntanList;
import xin.banghua.moyuan.ParseJSON.ParseJSONArray;
import xin.banghua.moyuan.R;

public class Someonesluntan1Activity extends AppCompatActivity {
    private static final String TAG = "SomeonesluntanActivity";
    //接收用户id，查看用户发过的帖子，回帖暂时不做
    //帖子列表
    private List<LuntanList> luntanLists = new ArrayList<>();
    private LuntanAdapter adapter;
    //页码
    private Integer pageindex = 1;
    private String authid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_someonesluntan);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("个人发帖");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

//        SharedHelper shuserinfo = new SharedHelper(getApplicationContext());
//        authid = shuserinfo.readUserInfo().get("userID");
        Intent intent = getIntent();
        authid = intent.getStringExtra("authid");
        Log.d(TAG, "onCreate: authid"+authid);
        getDataPostlist("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=someonesluntan&m=moyuan",authid,"1");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initPostList(JSONArray jsonArray) throws JSONException {
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

            final PullLoadMoreRecyclerView recyclerView = findViewById(R.id.luntan_RecyclerView);
            adapter = new LuntanAdapter(luntanLists,this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLinearLayout();
            recyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
                @Override
                public void onRefresh() {
                    Log.d(TAG, "onLoadMore: start");

                    recyclerView.setPullLoadMoreCompleted();

                }

                @Override
                public void onLoadMore() {
                    pageindex = pageindex+1;
                    getDataPostlist("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=someonesluntan&m=moyuan",authid,pageindex+"");
                    Log.d(TAG, "个人帖子页码："+pageindex);
                    recyclerView.setPullLoadMoreCompleted();
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
                        Log.d(TAG, "handleMessage: 帖子接收的值"+msg.obj.toString());
                        JSONArray jsonArray = new ParseJSONArray(msg.obj.toString()).getParseJSON();
                        initPostList(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    public void getDataPostlist(final String url,final String authid,final String pageindex){
        new Thread(new Runnable() {
            @Override
            public void run(){
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("authid", authid)
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
}
