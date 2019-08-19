package xin.banghua.beiyuan.Personage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import xin.banghua.beiyuan.R;

public class PersonageActivity extends AppCompatActivity {

    private static final String TAG = "PersonageActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personage);


        Log.d(TAG, "onCreate: "+getIntent().getIntExtra("userid",1));
    }
}
