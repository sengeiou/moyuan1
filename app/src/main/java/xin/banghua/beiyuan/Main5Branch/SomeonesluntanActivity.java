package xin.banghua.beiyuan.Main5Branch;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xin.banghua.beiyuan.Adapter.LuntanAdapter;
import xin.banghua.beiyuan.Adapter.LuntanList;
import xin.banghua.beiyuan.ParseJSON.ParseJSONArray;
import xin.banghua.beiyuan.R;

public class SomeonesluntanActivity extends AppCompatActivity {
    private static final String TAG = "SomeonesluntanActivity";
    //接收用户id，查看用户发过的帖子，回帖暂时不做
    //帖子列表
    private List<LuntanList> luntanLists = new ArrayList<>();
    private LuntanAdapter adapter;

    private String authid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_someonesluntan);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("个人发帖和跟帖");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        authid = getIntent().getStringExtra("authid");
        Log.d(TAG, "onCreate: authid"+authid);
        getDataPostlist("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=someonesluntan&m=socialchat",authid);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initPostList(JSONArray jsonArray) throws JSONException {
        Log.d(TAG, "initPostList: start");
        luntanLists.clear();
        if (jsonArray.length()>0){
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String[] postPicture = jsonObject.getString("postpicture").split(",");
                LuntanList posts = new LuntanList(jsonObject.getString("id"),jsonObject.getString("plateid"),jsonObject.getString("platename"),jsonObject.getString("authid"),jsonObject.getString("authnickname"),jsonObject.getString("authportrait"),jsonObject.getString("posttip"),jsonObject.getString("posttitle"),jsonObject.getString("posttext"),postPicture,jsonObject.getString("like"),jsonObject.getString("favorite"),jsonObject.getString("time"));
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

                recyclerView.setPullLoadMoreCompleted();
            }

        });




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
    public void getDataPostlist(final String url,final String authid){
        new Thread(new Runnable() {
            @Override
            public void run(){
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("authid", authid)
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
