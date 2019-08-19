package xin.banghua.beiyuan.Main5Branch;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import xin.banghua.beiyuan.R;

public class PrivateSettingActivity extends AppCompatActivity {


    Switch switch1,switch2,switch3,switch4;

    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_setting);
        mContext = this;
        ImageView back_btn = findViewById(R.id.iv_back_left);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initSwitch();

    }

    private void initSwitch() {
        switch1 = findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(mContext, "开启", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext, "关闭", Toast.LENGTH_LONG).show();
                }
            }
        });
        switch2 = findViewById(R.id.switch2);
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(mContext, "开启", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext, "关闭", Toast.LENGTH_LONG).show();
                }
            }
        });
        switch3 = findViewById(R.id.switch3);
        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(mContext, "开启", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext, "关闭", Toast.LENGTH_LONG).show();
                }
            }
        });
        switch4 = findViewById(R.id.switch4);
        switch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(mContext, "开启", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext, "关闭", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
