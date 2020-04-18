package xin.banghua.moyuan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import xin.banghua.moyuan.Personage.PersonageActivity;
import xin.banghua.moyuan.R;
import xin.banghua.moyuan.SliderWebViewActivity;

public class UserInfoSliderAdapter extends RecyclerView.Adapter implements  ViewPagerEx.OnPageChangeListener{
    private static final String TAG = "UserInfoSliderAdapter";
    private final static int TYPE_HEAD = 0;
    private final static int TYPE_CONTENT = 1;
    //幻灯片
    SliderLayout mDemoSlider;
    JSONArray jsonArray;
    //用户id,头像，昵称，年龄，性别，属性，地区
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
    private Context mContext;

    //替换数据，并更新
    public void swapData(ArrayList<String> mUserID,ArrayList<String> mUserPortrait,ArrayList<String> mUserNickName,ArrayList<String> mUserAge,ArrayList<String> mUserGender,ArrayList<String> mUserProperty,ArrayList<String> mUserLocation,ArrayList<String> mUserRegion,ArrayList<String> mUserVIP,ArrayList<String> mAllowLocation){
        this.mUserID = mUserID;
        this.mUserPortrait = mUserPortrait;
        this.mUserNickName = mUserNickName;
        this.mUserAge = mUserAge;
        this.mUserGender = mUserGender;
        this.mUserProperty = mUserProperty;
        this.mUserLocation = mUserLocation;
        this.mUserRegion = mUserRegion;
        this.mUserVIP = mUserVIP;
        this.mAllowLocation = mAllowLocation;
        notifyDataSetChanged();
    }

    public UserInfoSliderAdapter(Context mContext,JSONArray jsonArray,ArrayList<String> userID, ArrayList<String> userPortrait, ArrayList<String> userNickName,ArrayList<String> userAge,ArrayList<String> userGender,ArrayList<String> userProperty,ArrayList<String> userLocation,ArrayList<String> userRegion,ArrayList<String> userVIP,ArrayList<String> allowLocation) {
        this.jsonArray = jsonArray;

        this.mUserID = userID;
        this.mUserPortrait = userPortrait;
        this.mUserNickName = userNickName;
        this.mUserAge = userAge;
        this.mUserGender = userGender;
        this.mUserProperty = userProperty;
        this.mUserLocation = userLocation;
        this.mUserRegion = userRegion;
        this.mUserVIP = userVIP;
        this.mAllowLocation = allowLocation;
        this.mContext = mContext;

        Log.d(TAG, "UserInfoSliderAdapter: 初始化");
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
            UserInfoSliderAdapter.SliderHolder viewHolder = new UserInfoSliderAdapter.SliderHolder(view);
            return viewHolder;
        }else if(i == TYPE_CONTENT){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_userinfo,viewGroup,false);
            UserInfoSliderAdapter.UserinfoHolder viewHolder = new UserInfoSliderAdapter.UserinfoHolder(view);
            return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder,final int i) {
        Log.d(TAG, "onBindViewHolder: 进入");
        if (viewHolder instanceof SliderHolder){
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
        }else if (viewHolder instanceof UserinfoHolder){
            ((UserinfoHolder) viewHolder).userID.setText(mUserID.get(i-1));
            Glide.with(mContext)
                    .asBitmap()
                    .load(mUserPortrait.get(i-1))
                    .into(((UserinfoHolder) viewHolder).userPortrait);
            ((UserinfoHolder) viewHolder).userNickName.setText(mUserNickName.get(i-1));
            ((UserinfoHolder) viewHolder).userAge.setText(mUserAge.get(i-1));
            ((UserinfoHolder) viewHolder).userGender.setText(mUserGender.get(i-1));
            if (mUserGender.get(i-1).equals("男")){
                Resources resources = mContext.getResources();
                Drawable drawable = resources.getDrawable(R.drawable.ic_male,null);
                ((UserinfoHolder) viewHolder).userGender.setForeground(drawable);
            }else {
                Resources resources = mContext.getResources();
                Drawable drawable = resources.getDrawable(R.drawable.ic_female,null);
                ((UserinfoHolder) viewHolder).userGender.setForeground(drawable);
            }
            ((UserinfoHolder) viewHolder).userProperty.setText(mUserProperty.get(i-1));
            if (mAllowLocation.get(i-1).equals("1")){
                ((UserinfoHolder) viewHolder).userLocation.setText(mUserLocation.get(i-1)+"km");
            }else {
                ((UserinfoHolder) viewHolder).userLocation.setText("? km");
            }
            ((UserinfoHolder) viewHolder).userRegion.setText(mUserRegion.get(i-1));
            ((UserinfoHolder) viewHolder).userVIP.setText(mUserVIP.get(i-1));
            if (mUserVIP.get(i-1).equals("普通")){
                Resources resources = mContext.getResources();
                Drawable drawable = resources.getDrawable(R.drawable.nonmember,null);
                ((UserinfoHolder) viewHolder).userVIP.setForeground(drawable);
            }else {
                Resources resources = mContext.getResources();
                Drawable drawable = resources.getDrawable(R.drawable.vip_image,null);
                ((UserinfoHolder) viewHolder).userVIP.setForeground(drawable);
            }

            ((UserinfoHolder) viewHolder).userinfoLayout.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: clicked on: "+mUserID.get(i-1));
                    //Toast.makeText(mContext,mUserID.get(i)+mUserNickName.get(i),Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(v.getContext(), PersonageActivity.class);
                    intent.putExtra("userID",mUserID.get(i-1));
                    //保存选中的用户id
                    //SharedHelper shvalue = new SharedHelper(mContext);
                    //shvalue.saveValue(mUserID.get(i));
                    Log.d(TAG, "onClick: 保存选中的用户id"+mUserID.get(i-1));
                    v.getContext().startActivity(intent);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mUserID.size()+1;
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


    public class UserinfoHolder extends RecyclerView.ViewHolder{

        private static final String TAG = "UserinfoHolder";
        TextView userID;
        CircleImageView userPortrait;
        TextView userNickName;
        TextView userAge;
        TextView userGender;
        TextView userProperty;
        TextView userLocation;
        TextView userRegion;
        TextView userVIP;
        RelativeLayout userinfoLayout;
        public UserinfoHolder(@NonNull View itemView) {
            super(itemView);
            userID = itemView.findViewById(R.id.userID);
            userPortrait = itemView.findViewById(R.id.authportrait);
            userNickName = itemView.findViewById(R.id.userNickName);
            userAge = itemView.findViewById(R.id.userAge);
            userGender = itemView.findViewById(R.id.userGender);
            userProperty = itemView.findViewById(R.id.userProperty);
            userLocation = itemView.findViewById(R.id.userLocation);
            userRegion = itemView.findViewById(R.id.userRegion);
            userVIP = itemView.findViewById(R.id.userVIP);

            userinfoLayout = itemView.findViewById(R.id.userinfo_layout);
            Log.d(TAG, "UserinfoHolder: 进入");
        }
    }
}
