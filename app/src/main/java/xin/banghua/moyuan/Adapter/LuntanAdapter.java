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
        viewHolder.posttip.setText(currentItem.getPosttip().isEmpty()?"":currentItem.getPosttip());
        if (currentItem.getPosttip().equals("加精")){
            Resources resources = mContext.getResources();
            Drawable drawable = resources.getDrawable(R.drawable.ic_essence,null);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            viewHolder.posttip.setCompoundDrawables(drawable,null,null,null);
        }else if(currentItem.getPosttip().equals("置顶")){
            Resources resources = mContext.getResources();
            Drawable drawable = resources.getDrawable(R.drawable.ic_ontop,null);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            viewHolder.posttip.setCompoundDrawables(drawable,null,null,null);
        }else if (currentItem.getPosttip().equals("置顶,加精")){
            Resources resources = mContext.getResources();
            Drawable drawable1 = resources.getDrawable(R.drawable.ic_essence,null);
            drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
            Drawable drawable2 = resources.getDrawable(R.drawable.ic_ontop,null);
            drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
            viewHolder.posttip.setCompoundDrawables(drawable1,null,drawable2,null);
        }else {
            //必须加上，否则会错乱
            viewHolder.posttip.setCompoundDrawables(null,null,null,null);
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
        if (currentItem.getPostpicture().length!=1){
            viewHolder.postpicture.setVisibility(View.GONE);
        }else if (currentItem.getPostpicture().length==1){
            Glide.with(mContext)
                    .asBitmap()
                    .load(currentItem.getPostpicture()[0])
                    .into(viewHolder.postpicture);
            viewHolder.postpicture.setVisibility(View.VISIBLE);
            viewHolder.postpicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DialogPlus dialog = DialogPlus.newDialog(mContext)
                            .setAdapter(new BaseAdapter() {
                                @Override
                                public int getCount() {
                                    return 0;
                                }

                                @Override
                                public Object getItem(int position) {
                                    return null;
                                }

                                @Override
                                public long getItemId(int position) {
                                    return 0;
                                }

                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    return null;
                                }
                            })
                            .setFooter(R.layout.dialog_original_image)
                            .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                            .create();
                    dialog.show();
                    View view = dialog.getFooterView();
                    ZoomInImageView originalImage = view.findViewById(R.id.originalImage);
                    Glide.with(mContext)
                            .asBitmap()
                            .load(currentItem.getPostpicture()[0])
                            .into(originalImage);
                    Button dismissdialog_btn = view.findViewById(R.id.cancel);
                    dismissdialog_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        }
        if (currentItem.getPostpicture().length<2){
            viewHolder.postpicture1.setVisibility(View.GONE);
        }else {
            Glide.with(mContext)
                    .asBitmap()
                    .load(currentItem.getPostpicture()[0])
                    .into(viewHolder.postpicture1);
            viewHolder.postpicture1.setVisibility(View.VISIBLE);
            viewHolder.postpicture1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DialogPlus dialog = DialogPlus.newDialog(mContext)
                            .setAdapter(new BaseAdapter() {
                                @Override
                                public int getCount() {
                                    return 0;
                                }

                                @Override
                                public Object getItem(int position) {
                                    return null;
                                }

                                @Override
                                public long getItemId(int position) {
                                    return 0;
                                }

                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    return null;
                                }
                            })
                            .setFooter(R.layout.dialog_original_image)
                            .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                            .create();
                    dialog.show();
                    View view = dialog.getFooterView();
                    ZoomInImageView originalImage = view.findViewById(R.id.originalImage);
                    Glide.with(mContext)
                            .asBitmap()
                            .load(currentItem.getPostpicture()[0])
                            .into(originalImage);
                    Button dismissdialog_btn = view.findViewById(R.id.cancel);
                    dismissdialog_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        }
        if (currentItem.getPostpicture().length<2){
            viewHolder.postpicture2.setVisibility(View.GONE);
        }else {
            Glide.with(mContext)
                    .asBitmap()
                    .load(currentItem.getPostpicture()[1])
                    .into(viewHolder.postpicture2);
            viewHolder.postpicture2.setVisibility(View.VISIBLE);
            viewHolder.postpicture2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DialogPlus dialog = DialogPlus.newDialog(mContext)
                            .setAdapter(new BaseAdapter() {
                                @Override
                                public int getCount() {
                                    return 0;
                                }

                                @Override
                                public Object getItem(int position) {
                                    return null;
                                }

                                @Override
                                public long getItemId(int position) {
                                    return 0;
                                }

                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    return null;
                                }
                            })
                            .setFooter(R.layout.dialog_original_image)
                            .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                            .create();
                    dialog.show();
                    View view = dialog.getFooterView();
                    ZoomInImageView originalImage = view.findViewById(R.id.originalImage);
                    Glide.with(mContext)
                            .asBitmap()
                            .load(currentItem.getPostpicture()[1])
                            .into(originalImage);
                    Button dismissdialog_btn = view.findViewById(R.id.cancel);
                    dismissdialog_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        }
        if (currentItem.getPostpicture().length<3){
            viewHolder.postpicture3.setVisibility(View.GONE);
        }else {
            Glide.with(mContext)
                    .asBitmap()
                    .load(currentItem.getPostpicture()[2])
                    .into(viewHolder.postpicture3);
            viewHolder.postpicture3.setVisibility(View.VISIBLE);
            viewHolder.postpicture3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DialogPlus dialog = DialogPlus.newDialog(mContext)
                            .setAdapter(new BaseAdapter() {
                                @Override
                                public int getCount() {
                                    return 0;
                                }

                                @Override
                                public Object getItem(int position) {
                                    return null;
                                }

                                @Override
                                public long getItemId(int position) {
                                    return 0;
                                }

                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    return null;
                                }
                            })
                            .setFooter(R.layout.dialog_original_image)
                            .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                            .create();
                    dialog.show();
                    View view = dialog.getFooterView();
                    ZoomInImageView originalImage = view.findViewById(R.id.originalImage);
                    Glide.with(mContext)
                            .asBitmap()
                            .load(currentItem.getPostpicture()[2])
                            .into(originalImage);
                    Button dismissdialog_btn = view.findViewById(R.id.cancel);
                    dismissdialog_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });
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
        return luntanLists.size();
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
        ZoomInImageView postpicture;
        ZoomInImageView postpicture1;
        ZoomInImageView postpicture2;
        ZoomInImageView postpicture3;
        TextView like;
        TextView favorite;
        TextView time;

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
            posttip = itemView.findViewById(R.id.posttip);
            posttitle = itemView.findViewById(R.id.posttitle);
            posttext = itemView.findViewById(R.id.posttext);
            postpicture = itemView.findViewById(R.id.postpicture);
            postpicture1 = itemView.findViewById(R.id.postpicture1);
            postpicture2 = itemView.findViewById(R.id.postpicture2);
            postpicture3 = itemView.findViewById(R.id.postpicture3);
            like = itemView.findViewById(R.id.like);
            favorite = itemView.findViewById(R.id.favorite);
            time = itemView.findViewById(R.id.time);

            luntanLayout = itemView.findViewById(R.id.luntanLayout);

            authattributes = itemView.findViewById(R.id.authattributes);
            menu_btn = itemView.findViewById(R.id.menu_btn);

            detail_content = itemView.findViewById(R.id.detail_content);
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
