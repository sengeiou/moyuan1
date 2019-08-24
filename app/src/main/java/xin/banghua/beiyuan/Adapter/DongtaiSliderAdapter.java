package xin.banghua.beiyuan.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.zolad.zoominimageview.ZoomInImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xin.banghua.beiyuan.R;

public class DongtaiSliderAdapter extends RecyclerView.Adapter implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    private static final String TAG = "DongtaiAdapter";
    //幻灯片
    SliderLayout mDemoSlider;
    JSONArray jsonArray;
    private Context mContext;
    private final static int TYPE_HEAD = 0;
    private final static int TYPE_CONTENT = 1;
    DongtaiSliderAdapter.ViewHolder viewHolder_btn;


    private List<DongtaiList> dongtaiLists;

    public DongtaiSliderAdapter(Context mContext, JSONArray jsonArray,List<DongtaiList> dongtaiList) {
        this.jsonArray = jsonArray;
        this.dongtaiLists = dongtaiList;
        this.mContext = mContext;
    }
    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType: position"+position);
        if ( position == 0){ // 头部
            Log.d(TAG, "getItemViewType: 头");
            return TYPE_HEAD;
        }else{
            Log.d(TAG, "getItemViewType: 身");
            return TYPE_CONTENT;
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder: 进入");
        if (i == TYPE_HEAD){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_slider,viewGroup,false);
            DongtaiSliderAdapter.SliderHolder viewHolder = new DongtaiSliderAdapter.SliderHolder(view);
            return viewHolder;
        }else if(i == TYPE_CONTENT){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_dongtai,viewGroup,false);
            DongtaiSliderAdapter.ViewHolder viewHolder = new DongtaiSliderAdapter.ViewHolder(view);
            return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder,final int i) {
        Log.d(TAG, "onBindViewHolder: called");
        if (viewHolder instanceof SliderHolder){
            HashMap<String,String> url_maps = new HashMap<String, String>();
            if (jsonArray.length()>0){
                for (int j=0;j<jsonArray.length();j++){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = jsonArray.getJSONObject(j);
                        url_maps.put(jsonObject.getString("slidename"), jsonObject.getString("slidepicture"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            for(String name : url_maps.keySet()){
                TextSliderView textSliderView = new TextSliderView(mContext);
                // initialize a SliderLayout
                textSliderView
                        .description(name)
                        .image(url_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra",name);

                mDemoSlider.addSlider(textSliderView);
            }
            mDemoSlider.setMinimumHeight(100);
            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(4000);
            mDemoSlider.addOnPageChangeListener(this);
        }else if (viewHolder instanceof ViewHolder){
            Log.d(TAG, "onBindViewHolder: i="+i);
            final DongtaiList currentItem = dongtaiLists.get(i-1);

            ((ViewHolder) viewHolder).userID.setText(currentItem.getMyid());
            Glide.with(mContext)
                    .asBitmap()
                    .load(currentItem.getMyportrait())
                    .into(((ViewHolder) viewHolder).userPortrait);
            ((ViewHolder) viewHolder).userNickName.setText(currentItem.getMynickname());
            ((ViewHolder) viewHolder).dongtaiWord.setText(currentItem.getContext());
            Glide.with(mContext)
                    .asBitmap()
                    .load(currentItem.getPicture())
                    .into(((ViewHolder) viewHolder).dongtaiImage);
            ((ViewHolder) viewHolder).dongtaiTime.setText(currentItem.getTime());
            ((ViewHolder) viewHolder).like.setText("赞"+currentItem.getLike());
            ((ViewHolder) viewHolder).like.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: clicked on: " + currentItem.getId());
                    //Toast.makeText(mContext, mUserID.get(i) + mUserNickName.get(i), Toast.LENGTH_LONG).show();
                    viewHolder_btn = ((ViewHolder) viewHolder);
                    like("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=guangchanglike&m=socialchat",currentItem.getId());
                }
            });
            ((ViewHolder) viewHolder).dongtaiLayout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: clicked on: "+currentItem.getId());
                    //Toast.makeText(mContext,mUserID.get(i)+mUserNickName.get(i),Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return dongtaiLists.size()+1;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class SliderHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "SliderHolder";

        public SliderHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "SliderHolder: 进入");
            mDemoSlider = itemView.findViewById(R.id.recyclerview_slider);
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userID;
        CircleImageView userPortrait;
        TextView userNickName;
        TextView dongtaiWord;
        ZoomInImageView dongtaiImage;
        TextView dongtaiTime;
        TextView like;
        RelativeLayout dongtaiLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userID = itemView.findViewById(R.id.userID);
            userPortrait = itemView.findViewById(R.id.authportrait);
            userNickName = itemView.findViewById(R.id.userNickName);
            dongtaiWord = itemView.findViewById(R.id.dongtaiWord);
            dongtaiImage = itemView.findViewById(R.id.dongtaiImage);
            dongtaiTime = itemView.findViewById(R.id.dongtaiTime);
            like = itemView.findViewById(R.id.like);

            dongtaiLayout = itemView.findViewById(R.id.dongtai_layout);
        }
    }


    //网络数据部分
//处理返回的数据
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //1是用户数据，2是幻灯片
            switch (msg.what){
                case 1:
                    viewHolder_btn.like.setText("赞"+msg.obj.toString());
                    break;
            }
        }
    };
    //TODO okhttp点赞
    public void like(final String url,final String circleid){
        new Thread(new Runnable() {
            @Override
            public void run(){
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("circleid", circleid)
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
