package xin.banghua.moyuan.introduction;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import xin.banghua.moyuan.Main5Activity;
import xin.banghua.moyuan.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IntroductionMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntroductionMainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    Button introduction_album;
    Button introduction_voice;
    Button introduction_video;
    Button introduction_text;


    public IntroductionMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IntroductionMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IntroductionMainFragment newInstance(String param1, String param2) {
        IntroductionMainFragment fragment = new IntroductionMainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_introduction_main, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("个人介绍设置");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        introduction_album = view.findViewById(R.id.introduction_album);
        introduction_voice = view.findViewById(R.id.introduction_voice);
        introduction_video = view.findViewById(R.id.introduction_video);
        introduction_text = view.findViewById(R.id.introduction_text);
        introduction_album.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_introductionMainFragment_to_introductionAlbumFragment));
        introduction_voice.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_introductionMainFragment_to_introductionVoiceFragment));
        introduction_video.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_introductionMainFragment_to_introductionVideoFragment));
        introduction_text.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_introductionMainFragment_to_introductionTextFragment));
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
                //getActivity().onBackPressed(); // back button
                Intent intent = new Intent(getActivity(), Main5Activity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
