/*已替换为LuntanSliderAdapter*/



package xin.banghua.moyuan.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
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
import com.orhanobut.dialogplus.DialogPlus;
import com.zolad.zoominimageview.ZoomInImageView;

import java.io.IOException;
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
import xin.banghua.moyuan.introduction.MyJzvdStd;


public class LuntanAdapter extends RecyclerView.Adapter<LuntanAdapter.ViewHolder> {
    private static final String TAG = "LuntanAdapter";

    private List<LuntanList> luntanLists;
    private Context mContext;
    ViewHolder viewHolder_btn;
    public LuntanAdapter(List<LuntanList> luntanLists, Context mContext) {
        Log.d(TAG, "LuntanAdapter: start");
        this.luntanLists = luntanLists;
        this.mContext = mContext;
    }
    //替换数据，并更新
    public void swapData(List<LuntanList> luntanLists){
        this.luntanLists = luntanLists;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_luntan,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final LuntanList currentItem = luntanLists.get(i);

        String authattributes_string = currentItem.getAuthage()+"岁 | "+currentItem.getAuthgender()+" | "+currentItem.getAuthregion()+" | "+currentItem.getAuthproperty();
        viewHolder.authattributes.setText(authattributes_string);

        viewHolder.id.setText(currentItem.getId());
        viewHolder.plateid.setText(currentItem.getPlateid());
        viewHolder.platename.setText(currentItem.getPlatename());
        viewHolder.authid.setText(currentItem.getAuthid());
        viewHolder.authnickname.setText(currentItem.getAuthnickname());
        Glide.with(mContext)
                .asBitmap()
                .load(currentItem.getAuthportrait())
                .into(viewHolder.authportrait);

        if (currentItem.getPoi().isEmpty() || currentItem.getPoi().equals("null")){
            viewHolder.poi.setText("");
        }else {
            viewHolder.poi.setText(currentItem.getPoi());
        }

        viewHolder.posttitle.setText(currentItem.getPosttitle());
        if (currentItem.getPosttext().length()>50) {
            viewHolder.posttext.setText(currentItem.getPosttext().substring(0, 50)+"......");
            viewHolder.detail_content.setVisibility(View.VISIBLE);
            viewHolder.detail_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.posttext.setText(currentItem.getPosttext());
                    viewHolder.detail_content.setVisibility(View.GONE);
                }
            });
        }else {
            viewHolder.posttext.setText(currentItem.getPosttext());
            viewHolder.detail_content.setVisibility(View.GONE);
        }
        if (currentItem.getPosttext().length()>50) {
            viewHolder.posttext.setText(currentItem.getPosttext().substring(0, 50)+"  查看全文");
            viewHolder.posttext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.posttext.setText(currentItem.getPosttext());
                }
            });
        }else {
            viewHolder.posttext.setText(currentItem.getPosttext());
        }

        //视频,正方形则200*200，高型则宽150，宽型则高150
        if (!currentItem.getVideourl().isEmpty()){
            if (currentItem.getVideourl().toString()!=""&&currentItem.getVideourl().toString()!="null") {
                if ((Integer.parseInt(currentItem.getVideoheight()) * 100)/ (Integer.parseInt(currentItem.getVideowidth())) == 100) {
                    viewHolder.jzvdStd.getLayoutParams().width = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_150);
                    viewHolder.jzvdStd.widthRatio = 1;
                    viewHolder.jzvdStd.heightRatio = 1;
                }
                if ((Integer.parseInt(currentItem.getVideoheight()) * 100)/ (Integer.parseInt(currentItem.getVideowidth())) == 100) {
                    viewHolder.jzvdStd.getLayoutParams().width = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_100);
                    viewHolder.jzvdStd.widthRatio = 100;
                    viewHolder.jzvdStd.heightRatio = Integer.parseInt(currentItem.getVideoheight()) * 100 / Integer.parseInt(currentItem.getVideowidth());
                }
                if ((Integer.parseInt(currentItem.getVideoheight()) * 100)/ (Integer.parseInt(currentItem.getVideowidth())) == 100) {
                    viewHolder.jzvdStd.getLayoutParams().height = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_100);
                    viewHolder.jzvdStd.widthRatio = 100;
                    viewHolder.jzvdStd.heightRatio = Integer.parseInt(currentItem.getVideoheight()) * 100 / Integer.parseInt(currentItem.getVideowidth());
                }
                viewHolder.video_layout.setVisibility(View.VISIBLE);
                viewHolder.jzvdStd.setUp(currentItem.getVideourl(), "");
                Glide.with(mContext).load(currentItem.getVideocover()).into(viewHolder.jzvdStd.posterImageView);
            }else {
                viewHolder.jzvdStd.setUp("", "");
                viewHolder.video_layout.setVisibility(View.GONE);
            }
        }else {
            viewHolder.jzvdStd.setUp("", "");
        }

        switch (currentItem.getPostpicture().length){
            case 9:
                if (currentItem.getPostpicture()[8] != "") {
                    Glide.with(mContext)
                            .asBitmap()
                            .load(currentItem.getPostpicture()[8])
                            .into(viewHolder.postpicture9);

                }else {
                Glide.with(mContext)
                        .asBitmap()
                        .load("")
                        .into(viewHolder.postpicture9);
               }
            case 8:
                if (currentItem.getPostpicture()[8] != "") {
                    Glide.with(mContext)
                            .asBitmap()
                            .load(currentItem.getPostpicture()[7])
                            .into(viewHolder.postpicture8);

                }else {
                    Glide.with(mContext)
                            .asBitmap()
                            .load("")
                            .into(viewHolder.postpicture8);
                }
            case 7:
                if (currentItem.getPostpicture()[7] != "") {
                    Glide.with(mContext)
                            .asBitmap()
                            .load(currentItem.getPostpicture()[6])
                            .into(viewHolder.postpicture7);
                    viewHolder.postpicture9.setVisibility(View.VISIBLE);
                    viewHolder.postpicture8.setVisibility(View.VISIBLE);
                    viewHolder.postpicture7.setVisibility(View.VISIBLE);
                }else {
                    Glide.with(mContext)
                            .asBitmap()
                            .load("")
                            .into(viewHolder.postpicture7);
                }
            case 6:
                if (currentItem.getPostpicture()[6] != "") {
                    Glide.with(mContext)
                            .asBitmap()
                            .load(currentItem.getPostpicture()[5])
                            .into(viewHolder.postpicture6);

                }else {
                    Glide.with(mContext)
                            .asBitmap()
                            .load("")
                            .into(viewHolder.postpicture6);
                }
            case 5:
                if (currentItem.getPostpicture()[5] != "") {
                    Glide.with(mContext)
                            .asBitmap()
                            .load(currentItem.getPostpicture()[4])
                            .into(viewHolder.postpicture5);

                }else {
                    Glide.with(mContext)
                            .asBitmap()
                            .load("")
                            .into(viewHolder.postpicture5);
                }
            case 4:
                if (currentItem.getPostpicture()[4] != "") {
                    Glide.with(mContext)
                            .asBitmap()
                            .load(currentItem.getPostpicture()[3])
                            .into(viewHolder.postpicture4);
                    viewHolder.postpicture6.setVisibility(View.VISIBLE);
                    viewHolder.postpicture5.setVisibility(View.VISIBLE);
                    viewHolder.postpicture4.setVisibility(View.VISIBLE);
                }else {
                    Glide.with(mContext)
                            .asBitmap()
                            .load("")
                            .into(viewHolder.postpicture4);
                }
            case 3:
                if (currentItem.getPostpicture()[3] != "") {
                    Glide.with(mContext)
                            .asBitmap()
                            .load(currentItem.getPostpicture()[2])
                            .into(viewHolder.postpicture3);
                }else {
                    Glide.with(mContext)
                            .asBitmap()
                            .load("")
                            .into(viewHolder.postpicture3);
                }
            case 2:
                if (currentItem.getPostpicture()[2] != "") {
                    Glide.with(mContext)
                            .asBitmap()
                            .load(currentItem.getPostpicture()[1])
                            .into(viewHolder.postpicture2);
                }else {
                    Glide.with(mContext)
                            .asBitmap()
                            .load("")
                            .into(viewHolder.postpicture2);
                }
            case 1:
                if (currentItem.getPostpicture()[1] != "") {
                    Glide.with(mContext)
                            .asBitmap()
                            .load(currentItem.getPostpicture()[0])
                            .into(viewHolder.postpicture1);
                    viewHolder.postpicture3.setVisibility(View.VISIBLE);
                    viewHolder.postpicture2.setVisibility(View.VISIBLE);
                    viewHolder.postpicture1.setVisibility(View.VISIBLE);
                }else {
                    Glide.with(mContext)
                            .asBitmap()
                            .load("")
                            .into(viewHolder.postpicture1);
                }
        }
        if (currentItem.getPostpicture().length == 1 && currentItem.getPostpicture()[0] != ""){
            viewHolder.postpicture1.getLayoutParams().width = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_150);
        }
        //图片少时设置不同的高度
        if (currentItem.getPostpicture().toString()==""){
            viewHolder.postpicture1.setVisibility(View.GONE);
            viewHolder.postpicture2.setVisibility(View.GONE);
            viewHolder.postpicture3.setVisibility(View.GONE);
            viewHolder.postpicture4.setVisibility(View.GONE);
            viewHolder.postpicture5.setVisibility(View.GONE);
            viewHolder.postpicture6.setVisibility(View.GONE);
            viewHolder.postpicture7.setVisibility(View.GONE);
            viewHolder.postpicture8.setVisibility(View.GONE);
            viewHolder.postpicture9.setVisibility(View.GONE);
        }


        viewHolder.like.setText("赞"+currentItem.getLike());
        viewHolder.like.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "赞onClick: clicked on: " + currentItem.getId());
                //Toast.makeText(mContext, mUserID.get(i) + mUserNickName.get(i), Toast.LENGTH_LONG).show();
                viewHolder_btn = viewHolder;
                like("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=luntanlike&m=moyuan",currentItem.getId());
            }
        });
        viewHolder.favorite.setText(currentItem.getFavorite());
        viewHolder.time.setText(currentItem.getTime());


        viewHolder.authnickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PersonageActivity.class);
                intent.putExtra("userID",currentItem.getAuthid());
                v.getContext().startActivity(intent);
            }
        });
        viewHolder.authportrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PersonageActivity.class);
                intent.putExtra("userID",currentItem.getAuthid());
                v.getContext().startActivity(intent);
            }
        });

//        viewHolder.posttitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                intentPostlist(v,currentItem);
//            }
//        });
//        viewHolder.posttext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                intentPostlist(v,currentItem);
//            }
//        });
//        viewHolder.postpicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                intentPostlist(v,currentItem);
//            }
//        });

//        viewHolder.luntanLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                intentPostlist(v,currentItem);
//            }
//        });
        viewHolder.menu_btn.setOnClickListener(new View.OnClickListener() {
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
        return luntanLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView plateid;
        TextView platename;
        TextView authid;
        TextView authnickname;
        CircleImageView authportrait;
        TextView poi;
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

        LinearLayout video_layout;
        MyJzvdStd jzvdStd;

        RelativeLayout luntanLayout;
        Button detail_content;
        TextView authattributes;
        Button menu_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            plateid = itemView.findViewById(R.id.plateid);
            platename = itemView.findViewById(R.id.platename);
            authid = itemView.findViewById(R.id.authid);
            authnickname = itemView.findViewById(R.id.authnickname);
            authportrait = itemView.findViewById(R.id.authportrait);
            poi = itemView.findViewById(R.id.poi);
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
