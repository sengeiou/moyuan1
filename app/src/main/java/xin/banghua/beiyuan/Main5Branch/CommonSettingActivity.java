package xin.banghua.beiyuan.Main5Branch;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import xin.banghua.beiyuan.R;

public class CommonSettingActivity extends AppCompatActivity {

    Button clearCache,clearChat;

    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_setting);
        mContext = this;
        ImageView back_btn = findViewById(R.id.iv_back_left);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        initButton();
    }

    private void initButton() {
        clearCache = findViewById(R.id.clearCache);
        clearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "清除缓存", Toast.LENGTH_LONG).show();
            }
        });
        clearChat = findViewById(R.id.clearChat);
        clearChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "清除聊天记录", Toast.LENGTH_LONG).show();
            }
        });
    }
}
