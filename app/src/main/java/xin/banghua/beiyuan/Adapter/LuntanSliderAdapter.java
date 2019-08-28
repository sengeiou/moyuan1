package xin.banghua.beiyuan.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

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
import xin.banghua.beiyuan.Main4Branch.PostListActivity;
import xin.banghua.beiyuan.Personage.PersonageActivity;
import xin.banghua.beiyuan.R;

public class LuntanSliderAdapter extends RecyclerView.Adapter  implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private static final String TAG = "LuntanAdapter";
    //幻灯片
    SliderLayout mDemoSlider;
    JSONArray jsonArray;
    ViewHolder viewHolder_btn;
    private List<LuntanList> luntanLists;
    private Context mContext;
    private final static int TYPE_HEAD = 0;
    private final static int TYPE_CONTENT = 1;
    public LuntanSliderAdapter(List<LuntanList> luntanLists,JSONArray jsonArray, Context mContext) {
        this.jsonArray = jsonArray;
        Log.d(TAG, "LuntanAdapter: start");
        this.luntanLists = luntanLists;
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,final int i) {
        Log.d(TAG, "onCreateViewHolder: 进入");
        if (i == TYPE_HEAD){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_slider,viewGroup,false);
            LuntanSliderAdapter.SliderHolder viewHolder = new LuntanSliderAdapter.SliderHolder(view);
            return viewHolder;
        }else if(i == TYPE_CONTENT){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_luntan,viewGroup,false);
            LuntanSliderAdapter.ViewHolder viewHolder = new LuntanSliderAdapter.ViewHolder(view);
            return viewHolder;
        }
        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder,final int i) {
        if (viewHolder instanceof LuntanSliderAdapter.SliderHolder){
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
        }else if (viewHolder instanceof LuntanSliderAdapter.ViewHolder){
            final LuntanList currentItem = luntanLists.get(i-1);

            String authattributes_string = currentItem.getAuthage()+"岁 | "+currentItem.getAuthgender()+" | "+currentItem.getAuthregion()+" | "+currentItem.getAuthproperty();
            ((ViewHolder) viewHolder).authattributes.setText(authattributes_string);

            ((ViewHolder) viewHolder).id.setText(currentItem.getId());
            ((ViewHolder) viewHolder).plateid.setText(currentItem.getPlateid());
            ((ViewHolder) viewHolder).platename.setText(currentItem.getPlatename());
            ((ViewHolder) viewHolder).authid.setText(currentItem.getAuthid());
            ((ViewHolder) viewHolder).authnickname.setText(currentItem.getAuthnickname());
            Glide.with(mContext)
                    .asBitmap()
                    .load(currentItem.getAuthportrait())
                    .into(((ViewHolder) viewHolder).authportrait);
            ((ViewHolder) viewHolder).posttip.setText(currentItem.getPosttip().isEmpty()?"":currentItem.getPosttip());
            if (currentItem.getPosttip().equals("加精")){
                Resources resources = mContext.getResources();
                Drawable drawable = resources.getDrawable(R.drawable.ic_essence,null);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                ((ViewHolder) viewHolder).posttip.setCompoundDrawables(drawable,null,null,null);
            }else if(currentItem.getPosttip().equals("置顶")){
                Resources resources = mContext.getResources();
                Drawable drawable = resources.getDrawable(R.drawable.ic_ontop,null);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                ((ViewHolder) viewHolder).posttip.setCompoundDrawables(drawable,null,null,null);
            }else if (currentItem.getPosttip().equals("置顶,加精")){
                Resources resources = mContext.getResources();
                Drawable drawable1 = resources.getDrawable(R.drawable.ic_essence,null);
                drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                Drawable drawable2 = resources.getDrawable(R.drawable.ic_ontop,null);
                drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
                ((ViewHolder) viewHolder).posttip.setCompoundDrawables(drawable1,null,drawable2,null);
            }else {
                //必须加上，否则会错乱
                ((ViewHolder) viewHolder).posttip.setCompoundDrawables(null,null,null,null);
            }
            ((ViewHolder) viewHolder).posttitle.setText(currentItem.getPosttitle());
            ((ViewHolder) viewHolder).posttext.setText(currentItem.getPosttext());
            if (currentItem.getPostpicture().length==0){
                ((ViewHolder) viewHolder).postpicture.setVisibility(View.GONE);
            }else {
                Glide.with(mContext)
                        .asBitmap()
                        .load(currentItem.getPostpicture()[0])
                        .into(((ViewHolder) viewHolder).postpicture);
            }
            ((ViewHolder) viewHolder).like.setText("赞"+currentItem.getLike());
            ((ViewHolder) viewHolder).like.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: clicked on: " + currentItem.getId());
                    //Toast.makeText(mContext, mUserID.get(i) + mUserNickName.get(i), Toast.LENGTH_LONG).show();
                    viewHolder_btn = (ViewHolder) viewHolder;
                    like("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=luntanlike&m=socialchat",currentItem.getId());
                }
            });
            ((ViewHolder) viewHolder).favorite.setText(currentItem.getFavorite());
            ((ViewHolder) viewHolder).time.setText(currentItem.getTime());


            ((ViewHolder) viewHolder).authnickname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), PersonageActivity.class);
                    intent.putExtra("userID",currentItem.getAuthid());
                    v.getContext().startActivity(intent);
                }
            });
            ((ViewHolder) viewHolder).authportrait.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), PersonageActivity.class);
                    intent.putExtra("userID",currentItem.getAuthid());
                    v.getContext().startActivity(intent);
                }
            });

            ((ViewHolder) viewHolder).posttitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentPostlist(v,currentItem);
                }
            });
            ((ViewHolder) viewHolder).posttext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentPostlist(v,currentItem);
                }
            });
            ((ViewHolder) viewHolder).postpicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentPostlist(v,currentItem);
                }
            });

            ((ViewHolder) viewHolder).luntanLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentPostlist(v,currentItem);
                }
            });

        }
    }


    public void intentPostlist(View v,LuntanList currentItem){
        Intent intent = new Intent(v.getContext(), PostListActivity.class);
        intent.putExtra("postid",currentItem.getId());
        intent.putExtra("plateid",currentItem.getPlateid());
        intent.putExtra("platename",currentItem.getPlatename());
        intent.putExtra("authid",currentItem.getAuthid());
        intent.putExtra("authnickname",currentItem.getAuthnickname());
        intent.putExtra("authportrait",currentItem.getAuthportrait());
        intent.putExtra("posttip",currentItem.getPosttip());
        intent.putExtra("posttitle",currentItem.getPosttitle());
        intent.putExtra("posttext",currentItem.getPosttext());
        intent.putExtra("postpicture",currentItem.getPostpicture());
        intent.putExtra("like",currentItem.getLike());
        intent.putExtra("favorite",currentItem.getFavorite());
        intent.putExtra("time",currentItem.getTime());
        v.getContext().startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return luntanLists.size()+1;
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
        TextView id;
        TextView plateid;
        TextView platename;
        TextView authid;
        TextView authnickname;
        CircleImageView authportrait;
        TextView posttip;
        TextView posttitle;
        TextView posttext;
        ImageView postpicture;
        TextView like;
        TextView favorite;
        TextView time;

        TextView authattributes;
        Button menu_btn;

        RelativeLayout luntanLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            plateid = itemView.findViewById(R.id.plateid);
            platename = itemView.findViewById(R.id.platename);
            authid = itemView.findViewById(R.id.authid);
            authnickname = itemView.findViewById(R.id.authnickname);
            authportrait = itemView.findViewById(R.id.authportrait);
            posttip = itemView.findViewById(R.id.posttip);
            posttitle = itemView.findViewById(R.id.posttitle);
            posttext = itemView.findViewById(R.id.posttext);
            postpicture = itemView.findViewById(R.id.postpicture);
            like = itemView.findViewById(R.id.like);
            favorite = itemView.findViewById(R.id.favorite);
            time = itemView.findViewById(R.id.time);

            luntanLayout = itemView.findViewById(R.id.luntanLayout);

            authattributes = itemView.findViewById(R.id.authattributes);
            menu_btn = itemView.findViewById(R.id.menu_btn);
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
    public void like(final String url,final String postid){
        new Thread(new Runnable() {
            @Override
            public void run(){
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("postid", postid)
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
