package xin.banghua.beiyuan.Main5Branch;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.navigation.Navigation;
import xin.banghua.beiyuan.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPersonalInfoFragment extends Fragment {
    private static final String TAG = "ResetPersonalInfoFragme";

    //button
    Button reset_portrait_btn;
    Button reset_nickname_btn;
    Button reset_age_btn;
    Button reset_gender_btn;
    Button reset_property_btn;
    Button reset_region_btn;

    public ResetPersonalInfoFragment() {
        // Required empty public constructor
    }
    private Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_reset_personal_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reset_portrait_btn = view.findViewById(R.id.reset_portrait_btn);
        reset_portrait_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ResetActivity.class);
                intent.putExtra("title","头像设置");
                startActivity(intent);
            }
        });
        reset_nickname_btn = view.findViewById(R.id.reset_nickname_btn);
        reset_nickname_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ResetActivity.class);
                intent.putExtra("title","昵称设置");
                startActivity(intent);
            }
        });
        reset_age_btn = view.findViewById(R.id.reset_age_btn);
        reset_age_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ResetActivity.class);
                intent.putExtra("title","年龄设置");
                startActivity(intent);
            }
        });
        reset_gender_btn = view.findViewById(R.id.reset_gender_btn);
        reset_gender_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ResetActivity.class);
                intent.putExtra("title","性别设置");
                startActivity(intent);
            }
        });
        reset_property_btn = view.findViewById(R.id.reset_property_btn);
        reset_property_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ResetActivity.class);
                intent.putExtra("title","属性设置");
                startActivity(intent);
            }
        });
        reset_region_btn = view.findViewById(R.id.reset_region_btn);
        reset_region_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ResetActivity.class);
                intent.putExtra("title","地区设置");
                startActivity(intent);
            }
        });


        ImageView back_btn = view.findViewById(R.id.iv_back_left);
        back_btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.reset_me_action));
    }
}
