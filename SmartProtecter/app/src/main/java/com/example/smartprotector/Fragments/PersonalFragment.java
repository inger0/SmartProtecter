package com.example.smartprotector.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smartprotector.Activities.LoginActivity;
import com.example.smartprotector.Config.MyApplication;
import com.example.smartprotector.Net.MainImageConnection;
import com.example.smartprotector.R;

/**
 * Created by Huhu on 7/13/15.
 */
public class PersonalFragment extends Fragment {
    private View view;
    private TextView text_nickname;
    private ImageView iv_image_icon;
    private Button btn_logout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal, null);
        text_nickname = (TextView) view.findViewById(R.id.text_nickname);
        iv_image_icon = (ImageView) view.findViewById(R.id.image_icon);
        btn_logout = (Button) view.findViewById(R.id.btn_logout);
        text_nickname.setText(MyApplication.getInstance().getCurrentUser().getUser_nickname());
        new MainImageConnection(iv_image_icon).execute(MyApplication.getInstance().getCurrentUser().getImage_path().replace(" ", "%20"));
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(getActivity(), LoginActivity.class);
                getActivity().startActivity(i);
                getActivity().finish();
            }
        });

        return view;
    }
}
