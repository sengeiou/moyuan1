package xin.banghua.moyuan.MainBranch;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import androidx.navigation.Navigation;

import com.google.gson.GsonBuilder;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xin.banghua.moyuan.R;
import xin.banghua.moyuan.SharedPreferences.SharedHelper;
import xin.banghua.moyuan.Signin.CityAdapter;
import xin.banghua.moyuan.Signin.ProvinceAdapter;
import xin.banghua.moyuan.bean.AddrBean;


/**
 * A simple {@link Fragment} subclass.
 */
public class SousuoFragment extends Fragment {

    private static final String TAG = "SousuoFragment";
    //
    Spinner spProvince, spCity;
    private AddrBean addrBean;
    private ProvinceAdapter adpProvince;
    private CityAdapter adpCity;
    private List<AddrBean.ProvinceBean.CityBean> cityBeanList;
    private AddrBean.ProvinceBean provinceBean;
    //昵称，年龄，地区
    EditText userNickname_et,userAge_et,userRegion_et,direct_et;
    //性别，标签
    RadioGroup userGender_rg,userProperty_rg;
    RadioButton male_rb,female_rb,zProperty_rb,bProperty_rb,dProperty_rb;

    //
    String logtype,userAccount,userPassword,userNickname,userAge,userRegion,userGender,userProperty,userPortrait;
    Button direct_btn,condition_btn;


    TextView viptime_tv;
    public SousuoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sousuo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userRegion_et = view.findViewById(R.id.userRegion);
        //默认不限
        userRegion_et.setText("不限");
        userProperty_rg = view.findViewById(R.id.userProperty);//直接搜索
        direct_et = view.findViewById(R.id.direct_et);
        direct_btn = view.findViewById(R.id.direct_btn);
        //条件搜索
        userAge_et = view.findViewById(R.id.userAge);
        userGender_rg = view.findViewById(R.id.userGender);
        condition_btn = view.findViewById(R.id.submit_btn);
        viptime_tv = view.findViewById(R.id.viptime_tv);
        zProperty_rb = view.findViewById(R.id.zProperty);
        bProperty_rb = view.findViewById(R.id.bProperty);
        dProperty_rb = view.findViewById(R.id.dProperty);

        spCity = view.findViewById(R.id.spinner_city);
        spProvince = view.findViewById(R.id.spinner_province);
        //vip
        getVipinfo("https://applet.banghua.xin/app/index.php?i=99999&c=entry&a=webapp&do=viptimeinsousuo&m=moyuan");
        //初始化导航按钮
        initNavigateButton(view);

        //监听提交按钮
        submitValue(view);

        //年龄选择
        Spinner spinner_age = view.findViewById(R.id.spinner_age);
        ArrayAdapter<CharSequence> adapter_age = ArrayAdapter.createFromResource(getActivity(),R.array.sousuo_age,android.R.layout.simple_spinner_item);
        adapter_age.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_age.setAdapter(adapter_age);
        spinner_age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected_age = parent.getItemAtPosition(position).toString();
                if (selected_age.equals("不限")){
                    userAge_et.setText("");
                }else {
                    userAge_et.setText(selected_age);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        initView();
        loadData();
        register();
    }
    private void initView() {


        adpProvince = new ProvinceAdapter(getActivity());
        adpCity = new CityAdapter(getActivity());
    }
    private void loadData() {
        try {
            InputStream inputStream = getActivity().getAssets().open("addr_china_selected.json");

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
                //选择省份后，只传递省份
                String selected_province = provinceBean.getProvince();
                userRegion_et.setText(selected_province);
                spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //选择城市后，只传递城市
                        String selected_province = provinceBean.getProvince();
                        String selected_city = cityBeanList.get(position).getCityName();
                        if (selected_city.equals("不限")){
                            userRegion_et.setText(selected_province);
                        }else {
                            userRegion_et.setText(selected_city);
                        }

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

    //三个按钮初始化
    private void initNavigateButton(View view){
        Button tuijian = view.findViewById(R.id.tuijian_btn);
        Button fujin = view.findViewById(R.id.fujin_btn);
        //Button sousuo = view.findViewById(R.id.sousuo_btn);

        tuijian.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.sousuo_tuijian_action));
        fujin.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.sousuo_fujin_action));
    }

    //搜索提交的传值
    private void submitValue(final View view){



        //会直接运行getDirectBundle，所以没法动态传值
        //direct_btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.sousuo_result_action,getDirectBundle()));
        //condition_btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.sousuo_result_action,getConditionBundle(view)));
        SearchView searchView = view.findViewById(R.id.direct_sv);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bundle = new Bundle();
                bundle.putString("type","direct");
                bundle.putString("nameOrPhone",query);
                Navigation.findNavController(view).navigate(R.id.sousuo_result_action, bundle);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        direct_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 直接搜索"+getDirectBundle().getString("nameOrPhone"));
                Navigation.findNavController(v).navigate(R.id.sousuo_result_action, getDirectBundle());
            }
        });
        condition_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 条件搜索");
                Navigation.findNavController(v).navigate(R.id.sousuo_result_action, getConditionBundle(view));
            }
        });
    }

    //获取值作为监听参数
    private Bundle getDirectBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("type","direct");
        bundle.putString("nameOrPhone",direct_et.getText().toString());
        return bundle;
    }
    //获取值作为监听参数
    private Bundle getConditionBundle(View view){
        Bundle bundle = new Bundle();
        bundle.putString("type","condition");
        bundle.putString("userAge",userAge_et.getText().toString());
        bundle.putString("userRegion",userRegion_et.getText().toString());
        userGender = ((RadioButton) view.findViewById(userGender_rg.getCheckedRadioButtonId())).getText().toString();
        userProperty = ((RadioButton) view.findViewById(userProperty_rg.getCheckedRadioButtonId())).getText().toString();
        bundle.putString("userGender",userGender);
        bundle.putString("userProperty",userProperty);
        return bundle;
    }



    //网络数据部分
//处理返回的数据
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //1是用户数据
            switch (msg.what){
                case 1:

                        String resultJson1 = msg.obj.toString();
                        Log.d(TAG, "handleMessage: 用户数据接收的值"+msg.obj.toString());

                        viptime_tv.setText(msg.obj.toString());
                        if (msg.obj.toString().equals("会员已到期")){
                            zProperty_rb.setEnabled(false);
                            bProperty_rb.setEnabled(false);
                            dProperty_rb.setEnabled(false);
                            userRegion_et.setEnabled(false);
                            spCity.setEnabled(false);
                            spProvince.setEnabled(false);

                        }

                    break;
            }
        }
    };
    //TODO okhttp获取用户信息
    public void getVipinfo(final String url){
        new Thread(new Runnable() {
            @Override
            public void run(){
                SharedHelper shuserinfo = new SharedHelper(getActivity().getApplicationContext());
                String myid = shuserinfo.readUserInfo().get("userID");

                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("id", myid)
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
