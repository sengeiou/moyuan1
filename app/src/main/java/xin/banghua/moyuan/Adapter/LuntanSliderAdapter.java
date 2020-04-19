package xin.banghua.moyuan.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.orhanobut.dialogplus.DialogPlus;
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
import xin.banghua.moyuan.Main4Branch.PostListActivity;
import xin.banghua.moyuan.Personage.PersonageActivity;
import xin.banghua.moyuan.R;
import xin.banghua.moyuan.SliderWebViewActivity;
import xin.banghua.moyuan.introduction.MyJzvdStd;

public class LuntanSliderAdapter extends RecyclerView.Adapter  implements  ViewPagerEx.OnPageChangeListener {
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
    //替换数据，并更新
    public void swapData(List<LuntanList> luntanLists){
        this.luntanLists = luntanLists;
        notifyDataSetChanged();
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
                    try {
                       final JSONObject jsonObject = jsonArray.getJSONObject(j);
                        //url_maps.put(jsonObject.getString("slidename"), jsonObject.getString("slidepicture"));
                        TextSliderView textSliderView = new TextSliderView(mContext);
                        // initialize a SliderLayout
                        textSliderView
                                .description(jsonObject.getString("slidename"))
                                .image(jsonObject.getString("slidepicture"))
                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(BaseSliderView slider) {
                                        try {
                                            if (!jsonObject.getString("slideurl").isEmpty()){
                                                Log.d(TAG, "onSliderClick: 链接是"+jsonObject.getString("slideurl"));
                                                Intent intent = new Intent(mContext, SliderWebViewActivity.class);
                                                intent.putExtra("slidername",jsonObject.getString("slidename"));
                                                intent.putExtra("sliderurl",jsonObject.getString("slideurl"));
                                                mContext.startActivity(intent);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                        //add your extra information
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle()
                                .putString("extra",jsonObject.getString("slidename"));

                        mDemoSlider.addSlider(textSliderView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                mDemoSlider.setMinimumHeight(100);
                mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                mDemoSlider.setDuration(4000);
                mDemoSlider.addOnPageChangeListener(this);
            }else {
                mDemoSlider.setVisibility(View.GONE);
            }



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

            if (currentItem.getPoi().isEmpty() || currentItem.getPoi().equals("null")){
                ((ViewHolder) viewHolder).Poi.setText("");
            }else {
                ((ViewHolder) viewHolder).Poi.setText(currentItem.getPoi());
            }


            ((ViewHolder) viewHolder).posttitle.setText(currentItem.getPosttitle());
            if (currentItem.getPosttext().length()>50) {
                ((ViewHolder) viewHolder).posttext.setText(currentItem.getPosttext().substring(0, 50)+"......");
                ((ViewHolder) viewHolder).detail_content.setVisibility(View.VISIBLE);
                ((ViewHolder) viewHolder).detail_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ViewHolder) viewHolder).posttext.setText(currentItem.getPosttext());
                        ((ViewHolder) viewHolder).detail_content.setVisibility(View.GONE);
                    }
                });
            }else {
                ((ViewHolder) viewHolder).posttext.setText(currentItem.getPosttext());
                ((ViewHolder) viewHolder).detail_content.setVisibility(View.GONE);
            }

            //视频,正方形则200*200，高型则宽150，宽型则高150
            if (!currentItem.getVideourl().isEmpty()){
                if (currentItem.getVideourl().toString() != "" && currentItem.getVideourl().toString() != "null") {
                    if ((Integer.parseInt(currentItem.getVideoheight()) * 100)/ (Integer.parseInt(currentItem.getVideowidth())) == 100) {
                        ((ViewHolder) viewHolder).jzvdStd.getLayoutParams().width = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_150);
                        ((ViewHolder) viewHolder).jzvdStd.getLayoutParams().height = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_150);
                        ((ViewHolder) viewHolder).jzvdStd.widthRatio = 1;
                        ((ViewHolder) viewHolder).jzvdStd.heightRatio = 1;
                    }
                    if ((Integer.parseInt(currentItem.getVideoheight()) * 100)/ (Integer.parseInt(currentItem.getVideowidth())) > 100) {
                        ((ViewHolder) viewHolder).jzvdStd.getLayoutParams().width = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_100);
                        ((ViewHolder) viewHolder).jzvdStd.widthRatio = 100;
                        ((ViewHolder) viewHolder).jzvdStd.heightRatio = Integer.parseInt(currentItem.getVideoheight()) * 100 / Integer.parseInt(currentItem.getVideowidth());
                    }
                    if ((Integer.parseInt(currentItem.getVideoheight()) * 100)/ (Integer.parseInt(currentItem.getVideowidth())) < 100) {
                        ((ViewHolder) viewHolder).jzvdStd.getLayoutParams().height = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_100);
                        ((ViewHolder) viewHolder).jzvdStd.widthRatio = 100;
                        ((ViewHolder) viewHolder).jzvdStd.heightRatio = Integer.parseInt(currentItem.getVideoheight()) * 100 / Integer.parseInt(currentItem.getVideowidth());
                    }

                    ((ViewHolder) viewHolder).video_layout.setVisibility(View.VISIBLE);
                    ((ViewHolder) viewHolder).jzvdStd.setUp(currentItem.getVideourl(), "");
                    Glide.with(mContext).load(currentItem.getVideocover()).into(((ViewHolder) viewHolder).jzvdStd.posterImageView);
                }else {
                    ((ViewHolder) viewHolder).jzvdStd.setUp("", "");//没有视频也加个空url,防止自动播放报空指针
                    ((ViewHolder) viewHolder).video_layout.setVisibility(View.GONE);
                }
            }else {
                ((ViewHolder) viewHolder).jzvdStd.setUp("", "");
            }

            switch (currentItem.getPostpicture().length){
                case 9:
                    if (currentItem.getPostpicture()[8] != ""){
                    Glide.with(mContext)
                            .asBitmap()
                            .load(currentItem.getPostpicture()[8])
                            .into(((ViewHolder) viewHolder).postpicture9);
                    }else {
                        Glide.with(mContext)
                                .asBitmap()
                                .load("")
                                .into(((ViewHolder) viewHolder).postpicture9);
                    }
                case 8:
                    if (currentItem.getPostpicture()[7] != "") {
                        Glide.with(mContext)
                                .asBitmap()
                                .load(currentItem.getPostpicture()[7])
                                .into(((ViewHolder) viewHolder).postpicture8);
                    }else {
                        Glide.with(mContext)
                                .asBitmap()
                                .load("")
                                .into(((ViewHolder) viewHolder).postpicture8);
                    }
                case 7:
                    if (currentItem.getPostpicture()[6] != "") {
                        Glide.with(mContext)
                                .asBitmap()
                                .load(currentItem.getPostpicture()[6])
                                .into(((ViewHolder) viewHolder).postpicture7);
                        ((ViewHolder) viewHolder).postpicture9.setVisibility(View.VISIBLE);
                        ((ViewHolder) viewHolder).postpicture8.setVisibility(View.VISIBLE);
                        ((ViewHolder) viewHolder).postpicture7.setVisibility(View.VISIBLE);
                    }else {
                        Glide.with(mContext)
                                .asBitmap()
                                .load("")
                                .into(((ViewHolder) viewHolder).postpicture7);
                    }
                case 6:
                    if (currentItem.getPostpicture()[5] != "") {
                        Glide.with(mContext)
                                .asBitmap()
                                .load(currentItem.getPostpicture()[5])
                                .into(((ViewHolder) viewHolder).postpicture6);

                    }else {
                        Glide.with(mContext)
                                .asBitmap()
                                .load("")
                                .into(((ViewHolder) viewHolder).postpicture6);
                    }
                case 5:
                    if (currentItem.getPostpicture()[4] != "") {
                        Glide.with(mContext)
                                .asBitmap()
                                .load(currentItem.getPostpicture()[4])
                                .into(((ViewHolder) viewHolder).postpicture5);

                    }else {
                        Glide.with(mContext)
                                .asBitmap()
                                .load("")
                                .into(((ViewHolder) viewHolder).postpicture5);
                    }
                case 4:
                    if (currentItem.getPostpicture()[3] != "") {
                        Glide.with(mContext)
                                .asBitmap()
                                .load(currentItem.getPostpicture()[3])
                                .into(((ViewHolder) viewHolder).postpicture4);
                        ((ViewHolder) viewHolder).postpicture6.setVisibility(View.VISIBLE);
                        ((ViewHolder) viewHolder).postpicture5.setVisibility(View.VISIBLE);
                        ((ViewHolder) viewHolder).postpicture4.setVisibility(View.VISIBLE);
                    }else {
                        Glide.with(mContext)
                                .asBitmap()
                                .load("")
                                .into(((ViewHolder) viewHolder).postpicture4);
                    }
                case 3:
                    if (currentItem.getPostpicture()[2] != "") {
                        Glide.with(mContext)
                                .asBitmap()
                                .load(currentItem.getPostpicture()[2])
                                .into(((ViewHolder) viewHolder).postpicture3);

                    }else {
                        Glide.with(mContext)
                                .asBitmap()
                                .load("")
                                .into(((ViewHolder) viewHolder).postpicture3);
                    }
                case 2:
                    if (currentItem.getPostpicture()[1] != "") {
                        Glide.with(mContext)
                                .asBitmap()
                                .load(currentItem.getPostpicture()[1])
                                .into(((ViewHolder) viewHolder).postpicture2);

                    }else {
                        Glide.with(mContext)
                                .asBitmap()
                                .load("")
                                .into(((ViewHolder) viewHolder).postpicture2);
                    }
                case 1:
                    if (currentItem.getPostpicture()[0] != "") {
                        Glide.with(mContext)
                                .asBitmap()
                                .load(currentItem.getPostpicture()[0])
                                .into(((ViewHolder) viewHolder).postpicture1);
                        ((ViewHolder) viewHolder).postpicture3.setVisibility(View.VISIBLE);
                        ((ViewHolder) viewHolder).postpicture2.setVisibility(View.VISIBLE);
                        ((ViewHolder) viewHolder).postpicture1.setVisibility(View.VISIBLE);
                    }
            }
            if (currentItem.getPostpicture().length == 1 && currentItem.getPostpicture()[0] != ""){
                ((ViewHolder) viewHolder).postpicture1.getLayoutParams().width = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_150);
            }
            if (currentItem.getPostpicture().toString()==""){
                ((ViewHolder) viewHolder).postpicture1.setVisibility(View.GONE);
                ((ViewHolder) viewHolder).postpicture2.setVisibility(View.GONE);
                ((ViewHolder) viewHolder).postpicture3.setVisibility(View.GONE);
                ((ViewHolder) viewHolder).postpicture4.setVisibility(View.GONE);
                ((ViewHolder) viewHolder).postpicture5.setVisibility(View.GONE);
                ((ViewHolder) viewHolder).postpicture6.setVisibility(View.GONE);
                ((ViewHolder) viewHolder).postpicture7.setVisibility(View.GONE);
                ((ViewHolder) viewHolder).postpicture8.setVisibility(View.GONE);
                ((ViewHolder) viewHolder).postpicture9.setVisibility(View.GONE);
            }

            ((ViewHolder) viewHolder).like.setText("赞"+currentItem.getLike());
            ((ViewHolder) viewHolder).like.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: clicked on: " + currentItem.getId());
                    //Toast.makeText(mContext, mUserID.get(i) + mUserNickName.get(i), Toast.LENGTH_LONG).show();
                    viewHolder_btn = (ViewHolder) viewHolder;
                    like("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=luntanlike&m=moyuan",currentItem.getId());
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

//            ((ViewHolder) viewHolder).posttitle.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    intentPostlist(v,currentItem);
//                }
//            });
//            ((ViewHolder) viewHolder).posttext.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    intentPostlist(v,currentItem);
//                }
//            });
//            ((ViewHolder) viewHolder).postpicture.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    intentPostlist(v,currentItem);
//                }
//            });
//
            ((ViewHolder) viewHolder).luntanLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentPostlist(v,currentItem);
                }
            });

            ((ViewHolder) viewHolder).menu_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InformBlacklistAdapter adapter = new InformBlacklistAdapter(mContext,"post",currentItem.getId(),currentItem.getAuthid());
                    final DialogPlus dialog = DialogPlus.newDialog(mContext)
                            .setAdapter(adapter)
                            .setFooter(R.layout.inform_blacklist_foot)
                            .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                            .create();
                    dialog.show();
                    View view = dialog.getFooterView();
                    Button cancel = view.findViewById(R.id.inform_blacklist_cancel_btn);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
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
        intent.putExtra("poi",currentItem.getPoi());
        intent.putExtra("posttitle",currentItem.getPosttitle());
        intent.putExtra("posttext",currentItem.getPosttext());
        intent.putExtra("postpicture",currentItem.getPostpicture());
        intent.putExtra("like",currentItem.getLike());
        intent.putExtra("favorite",currentItem.getFavorite());
        intent.putExtra("time",currentItem.getTime());
        intent.putExtra("videourl",currentItem.getVideourl());
        intent.putExtra("videocover",currentItem.getVideocover());
        v.getContext().startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return luntanLists.size()+1;
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
        TextView Poi;
        TextView posttitle;
        TextView posttext;
        ImageView postpicture1;
        ImageView postpicture2;
        ImageView postpicture3;
        ImageView postpicture4;
        ImageView postpicture5;
        ImageView postpicture6;
        ImageView postpicture7;
        ImageView postpicture8;
        ImageView postpicture9;
        TextView like;
        TextView favorite;
        TextView time;
        Button detail_content;

        LinearLayout video_layout;
        MyJzvdStd jzvdStd;

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
            Poi = itemView.findViewById(R.id.poi);
            posttitle = itemView.findViewById(R.id.posttitle);
            posttext = itemView.findViewById(R.id.posttext);
            postpicture1 = itemView.findViewById(R.id.postpicture1);
            postpicture2 = itemView.findViewById(R.id.postpicture2);
            postpicture3 = itemView.findViewById(R.id.postpicture3);
            postpicture4 = itemView.findViewById(R.id.postpicture4);
            postpicture5 = itemView.findViewById(R.id.postpicture5);
            postpicture6 = itemView.findViewById(R.id.postpicture6);
            postpicture7 = itemView.findViewById(R.id.postpicture7);
            postpicture8 = itemView.findViewById(R.id.postpicture8);
            postpicture9 = itemView.findViewById(R.id.postpicture9);
            like = itemView.findViewById(R.id.like);
            favorite = itemView.findViewById(R.id.favorite);
            time = itemView.findViewById(R.id.time);

            luntanLayout = itemView.findViewById(R.id.luntanLayout);

            authattributes = itemView.findViewById(R.id.authattributes);
            menu_btn = itemView.findViewById(R.id.menu_btn);

            detail_content = itemView.findViewById(R.id.detail_content);

            video_layout = itemView.findViewById(R.id.video_layout);
            jzvdStd = itemView.findViewById(R.id.jz_video);
            jzvdStd.widthRatio = 1;
            jzvdStd.heightRatio = 1;
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
