package xin.banghua.beiyuan.Main5Branch;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import xin.banghua.beiyuan.R;
import xin.banghua.beiyuan.Signin.SigninActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    Button phone_reset;
    Button email_reset;
    Button password_reset;
    Button private_set;
    Button common_set;
    Button feedback_btn;
    Button help_btn;
    Button version_btn;
    Button logout_btn;

    private Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();


    }
    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("设置");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        super.onViewCreated(view, savedInstanceState);
        phone_reset = view.findViewById(R.id.phone_reset);
        email_reset = view.findViewById(R.id.email_reset);
        password_reset = view.findViewById(R.id.password_reset);
        private_set = view.findViewById(R.id.private_set);
        common_set = view.findViewById(R.id.common_set);
        feedback_btn = view.findViewById(R.id.feedback_btn);
        help_btn = view.findViewById(R.id.help_btn);
        version_btn = view.findViewById(R.id.version_btn);
        logout_btn = view.findViewById(R.id.logout_btn);

        phone_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ResetActivity.class);
                intent.putExtra("title","手机绑定");
                startActivity(intent);
            }
        });
        email_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ResetActivity.class);
                intent.putExtra("title","邮箱绑定");
                startActivity(intent);
            }
        });
        password_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ResetActivity.class);
                intent.putExtra("title","密码重置");
                startActivity(intent);
            }
        });
        private_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,PrivateSettingActivity.class);
                intent.putExtra("title","隐私设置");
                startActivity(intent);
            }
        });
        common_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,CommonSettingActivity.class);
                intent.putExtra("title","通用设置");
                startActivity(intent);
            }
        });
        feedback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ResetActivity.class);
                intent.putExtra("title","意见反馈");
                startActivity(intent);
            }
        });
        help_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "功能维护中", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mContext, HelpCenter.class);
                startActivity(intent);
            }
        });
        version_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "当前版本：1.0.0", Toast.LENGTH_LONG).show();
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = mContext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("userID", "");
                editor.commit();
                Intent intent = new Intent(mContext, SigninActivity.class);
                startActivity(intent);
            }
        });


    }


    @Override  //菜单的点击，其中返回键的id是android.R.id.home
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
