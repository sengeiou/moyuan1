package xin.banghua.moyuan.Main5Branch;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.GsonBuilder;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

import cn.addapp.pickers.entity.City;
import cn.addapp.pickers.entity.County;
import cn.addapp.pickers.entity.Province;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xin.banghua.moyuan.AddressPickTask;
import xin.banghua.moyuan.Main5Activity;
import xin.banghua.moyuan.ParseJSON.ParseJSONObject;
import xin.banghua.moyuan.R;
import xin.banghua.moyuan.SharedPreferences.SharedHelper;
import xin.banghua.moyuan.Signin.CityAdapter;
import xin.banghua.moyuan.Signin.ProvinceAdapter;
import xin.banghua.moyuan.bean.AddrBean;

public class ResetActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private static final String TAG = "ResetActivity";
    //年龄选择
    //地区选择
    Spinner spProvince, spCity;
    private AddrBean addrBean;
    private ProvinceAdapter adpProvince;
    private CityAdapter adpCity;
    private List<AddrBean.ProvinceBean.CityBean> cityBeanList;
    private AddrBean.ProvinceBean provinceBean;
    //var
    Button submit_btn;
    EditText value_et;
    TextView title_tv;
    TextView current_address;
    CircleImageView portrait;
    RadioGroup userGender;
    RadioGroup userProperty;
    Button age_btn;
    Button current_address_btn;

    String title,userPortrait,value;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("个人信息设置");
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //setHasOptionsMenu(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView back_btn = findViewById(R.id.iv_back_left);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        current_address = findViewById(R.id.current_address);
        current_address.setVisibility(View.GONE);

        getDataPersonage("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=personage&m=moyuan");
    }


    private void initView() {
        spCity = findViewById(R.id.spinner_city);
        spProvince = findViewById(R.id.spinner_province);

        adpProvince = new ProvinceAdapter(this);
        adpCity = new CityAdapter(this);
    }
    private void loadData() {
        try {
            InputStream inputStream = getApplicationContext().getAssets().open("addr_china.json");

            addrBean = new GsonBuilder().create().fromJson(new InputStreamReader(inputStream), AddrBean.class);
            spProvince.setAdapter(adpProvince);

            adpProvince.setProvinceBeanList(addrBean.getProvinceList());

            spCity.setAdapter(adpCity);
            adpCity.setCityBeanList(addrBean.getProvinceList().get(0).getCitylist());

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    private void register() {
        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spCity.setVisibility(View.VISIBLE);
                cityBeanList = addrBean.getProvinceList().get(position).getCitylist();
                provinceBean = addrBean.getProvinceList().get(position);
                adpCity.setCityBeanList(addrBean.getProvinceList().get(position).getCitylist());
                //选择省份后，城市默认第一个
                String selected_province = provinceBean.getProvince();
                String selected_city = cityBeanList.get(0).getCityName();
                value_et.setText(selected_province+"-"+selected_city);
                spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //选择城市后
                        String selected_province = provinceBean.getProvince();
                        String selected_city = cityBeanList.get(position).getCityName();
                        value_et.setText(selected_province+"-"+selected_city);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //TODO okhttp获取用户信息
    public void getDataPersonage(final String url){
        new Thread(new Runnable() {
            @Override
            public void run(){
                SharedHelper shuserinfo = new SharedHelper(getBaseContext());
                String myid = shuserinfo.readUserInfo().get("userID");
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("userid", myid)
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
                    Log.d(TAG, "run: getDataPersonage"+message.obj.toString());
                    handler.sendMessageDelayed(message,10);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void initPersonage(JSONObject jsonObject) throws JSONException {

        title = getIntent().getStringExtra("title");

        toolbar.setTitle(title);

        submit_btn = findViewById(R.id.submit_btn);
        value_et = findViewById(R.id.value_et);
        value_et.setHint(title);
        title_tv = findViewById(R.id.title_tv);
        title_tv.setText(title);
        portrait = findViewById(R.id.portrait);
        userGender = findViewById(R.id.userGender);
        userProperty = findViewById(R.id.userProperty);
        age_btn = findViewById(R.id.age_btn);
        current_address_btn = findViewById(R.id.current_address_btn);


        if (title.equals("头像设置")){
            SharedHelper shuserinfo = new SharedHelper(getApplicationContext());
            String myportrait = shuserinfo.readUserInfo().get("userPortrait");
            value_et.setVisibility(View.GONE);
            portrait.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .asBitmap()
                    .load(myportrait)
                    .into(portrait);
        }
        if (title.equals("昵称设置")){
            value_et.setVisibility(View.VISIBLE);
            value_et.setText(jsonObject.getString("nickname"));
        }
        if (title.equals("年龄设置")){
            value_et.setVisibility(View.GONE);
            value_et.setText(jsonObject.getString("age"));
            age_btn.setText("年龄："+jsonObject.getString("age"));
            age_btn.setVisibility(View.VISIBLE);
            //新年龄选择器
//            value_et.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    showDataPickerDialog();
//                }
//            });
            //年龄选择器
//            Spinner spinner_age = findViewById(R.id.spinner_age);
//            spinner_age.setVisibility(View.VISIBLE);
//            ArrayAdapter<CharSequence> adapter_age = ArrayAdapter.createFromResource(this,R.array.age,android.R.layout.simple_spinner_item);
//            adapter_age.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinner_age.setAdapter(adapter_age);
//            spinner_age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    String selected_age = parent.getItemAtPosition(position).toString();
//                    value_et.setText(selected_age);
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//            spinner_age.setSelection(adapter_age.getPosition(jsonObject.getString("age")));
        }
        if (title.equals("性别设置")){
            value_et.setVisibility(View.GONE);
            userGender.setVisibility(View.VISIBLE);
            if (jsonObject.getString("gender").equals("男")){
                userGender.check(R.id.male);
            }else {
                userGender.check(R.id.female);
            }
        }
        if (title.equals("属性设置")){
            value_et.setVisibility(View.GONE);
            userProperty.setVisibility(View.VISIBLE);
            if (jsonObject.getString("property").equals("双")){
                userProperty.check(R.id.dProperty);
            }else if(jsonObject.getString("property").equals("Z")){
                userProperty.check(R.id.zProperty);
            }else if(jsonObject.getString("property").equals("B")){
                userProperty.check(R.id.bProperty);
            }
        }

        if (title.equals("地区设置")){
            value_et.setVisibility(View.GONE);
            //current_address.setVisibility(View.VISIBLE);
            //current_address.setText("选择地址："+jsonObject.getString("region"));
            current_address_btn.setVisibility(View.VISIBLE);
            current_address_btn.setText("选择地址："+jsonObject.getString("region"));
            //spCity = findViewById(R.id.spinner_city);
            //spProvince = findViewById(R.id.spinner_province);
            //spCity.setVisibility(View.VISIBLE);
            //spProvince.setVisibility(View.VISIBLE);
//            initView();
//            loadData();
//            register();

        }
        if (title.equals("签名设置")){
            value_et.setVisibility(View.VISIBLE);
            value_et.setText(jsonObject.getString("signature"));
        }
        portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImagePicker.Builder(ResetActivity.this)
                        .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                        .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                        .directory(ImagePicker.Directory.DEFAULT)
                        .extension(ImagePicker.Extension.PNG)
                        .scale(600, 600)
                        .allowMultipleImages(false)
                        .enableDebuggingMode(true)
                        .build();
            }
        });



        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (title){
                    case "头像设置":
                        setUserPortrait("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=reset&m=moyuan");
                        break;
                    case "性别设置":
                        submitValue("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=reset&m=moyuan","性别");
                        break;
                    case "属性设置":
                        submitValue("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=reset&m=moyuan","属性");
                        break;
                    case "意见反馈":
                        submitValue("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=feedback&m=moyuan","意见");
                        break;
                    default:
                        submitValue("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=reset&m=moyuan","其他");
                        break;
                }
                Toast.makeText(v.getContext(), "提交成功", Toast.LENGTH_LONG).show();

            }
        });
    }
    /*
     * 年月日选择
     * */
    public void onYearMonthDayPicker(View view) {
        final cn.addapp.pickers.picker.DatePicker picker = new cn.addapp.pickers.picker.DatePicker(this);
        picker.setTopPadding(15);
        picker.setRangeStart(1950, 1, 1);
        picker.setRangeEnd(2005, 1, 1);
        picker.setSelectedItem(1990, 1, 1);
        picker.setWeightEnable(true);
        picker.setLineColor(Color.BLACK);
        picker.setOnDatePickListener(new cn.addapp.pickers.picker.DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {

                //ToastUtils.showShort(year + "-" + month + "-" + day);
                value_et.setText(year + month + day);
                age_btn.setText("生日："+year + "-" + month + "-" + day);
            }
        });
        picker.setOnWheelListener(new cn.addapp.pickers.picker.DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }
    //新城市选择器
    public void onAddressPicker(View view) {
        final AddressPickTask task = new AddressPickTask(this);
        task.setHideCounty(true);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                //ToastUtils.showShort("数据初始化失败");
                Log.d(TAG,"数据初始化失败");
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                //ToastUtils.showShort(province.getAreaName() + " " + city.getAreaName());
                Log.d(TAG,province.getAreaName() + " " + city.getAreaName());
                //current_address.setText("选择地址："+province.getAreaName()+"-"+city.getAreaName());
                current_address_btn.setText("选择地址："+province.getAreaName()+"-"+city.getAreaName());
                value_et.setText(province.getAreaName()+"-"+city.getAreaName());

            }
        });
        task.execute("北京", "北京");
    }
    //网络数据部分
    private void showDataPickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String date = i+"/"+i1+"/"+i2;
        value_et.setText(date);
    }
//处理返回的数据
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(ResetActivity.this, Main5Activity.class);
            switch (msg.what){
                case 1:
                    if (title.equals("昵称设置")){
                        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("userNickName", value);
                        editor.commit();
                        //融云更新缓存
                        SharedHelper shuserinfo = new SharedHelper(getApplicationContext());
                        String myid = shuserinfo.readUserInfo().get("userID");
                        String mynickname = shuserinfo.readUserInfo().get("userNickName");
                        String myportrait = shuserinfo.readUserInfo().get("userPortrait");
                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(myid, mynickname, Uri.parse(myportrait)));
                    }

                    startActivity(intent);
                    break;
                case 2:
                    SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    Log.d(TAG, "handleMessage: 修改头像"+msg.obj.toString());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("userPortrait", msg.obj.toString());
                    editor.commit();
                    //融云更新缓存
                    SharedHelper shuserinfo = new SharedHelper(getApplicationContext());
                    String myid = shuserinfo.readUserInfo().get("userID");
                    String mynickname = shuserinfo.readUserInfo().get("userNickName");
                    String myportrait = shuserinfo.readUserInfo().get("userPortrait");
                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(myid, mynickname, Uri.parse(myportrait)));

                    startActivity(intent);
                    break;
                case 3:
                    try {
                        String resultJson1 = msg.obj.toString();
                        Log.d(TAG, "handleMessage: 用户数据接收的值"+msg.obj.toString());

                        JSONObject jsonObject = new ParseJSONObject(msg.obj.toString()).getParseJSON();
                        initPersonage(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    //TODO okhttp设置手机，密码，邮箱,昵称,年龄，地区。
    public void submitValue(final String url, final String type){
        new Thread(new Runnable() {
            @Override
            public void run(){
                SharedHelper shuserinfo = new SharedHelper(getApplicationContext());
                String myid = shuserinfo.readUserInfo().get("userID");
                String mynickname = shuserinfo.readUserInfo().get("userNickName");
                String myportrait = shuserinfo.readUserInfo().get("userPortrait");
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody;
                switch (type){
                    case "性别":
                        value = ((RadioButton) findViewById(userGender.getCheckedRadioButtonId())).getText().toString();
                         formBody = new FormBody.Builder()
                                .add("type",title)
                                .add("userID", myid)
                                .add("value",value)
                                .build();
                    break;
                    case "属性":
                        value = ((RadioButton) findViewById(userProperty.getCheckedRadioButtonId())).getText().toString();
                         formBody = new FormBody.Builder()
                                .add("type",title)
                                .add("userID", myid)
                                .add("value",value)
                                .build();
                        break;
                    case "意见":
                        value = value_et.getText().toString();
                        formBody = new FormBody.Builder()
                                .add("type",title)
                                .add("userID", myid)
                                .add("nickname", mynickname)
                                .add("portrait", myportrait)
                                .add("content",value)
                                .build();
                        break;
                    default:
                        value = value_et.getText().toString();
                         formBody = new FormBody.Builder()
                                .add("type",title)
                                .add("userID", myid)
                                .add("value",value)
                                .build();
                        break;
                }

                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Message message=handler.obtainMessage();
                    message.what=1;

                    handler.sendMessageDelayed(message,10);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //TODO 注册 form形式的post
    public void setUserPortrait(final String url){
        new Thread(new Runnable() {
            @Override
            public void run(){
                //获取文件名
                Log.d("进入run","run");
                File tempFile =new File(userPortrait.trim());
                String fileName = tempFile.getName();
                //用户id
                SharedHelper shuserinfo = new SharedHelper(getApplicationContext());
                String myid = shuserinfo.readUserInfo().get("userID");
                //开始网络传输
                OkHttpClient client = new OkHttpClient();
                MediaType MEDIA_TYPE_PNG = MediaType.parse("image");
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("type", title)
                        .addFormDataPart("userID", myid)
                        .addFormDataPart("userPortrait",fileName,RequestBody.create(new File(userPortrait),MEDIA_TYPE_PNG))
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                Log.d("进入try","try");
                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    //Log.d("form形式的post",response.body().string());
                    //格式：{"error":"0","info":"登陆成功"}
                    Message message=handler.obtainMessage();
                    message.what=2;
                    message.obj=response.body().string();
                    handler.sendMessageDelayed(message,10);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            //Your Code
            ListIterator<String> listIterator = mPaths.listIterator();
            while (listIterator.hasNext()){
                String mPath = listIterator.next();
                Log.d("path", mPath);
                portrait.setImageURI(Uri.parse(mPath));
                userPortrait = mPath;
            }
        }
    }
}
