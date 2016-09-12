package com.example.smartprotector.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartprotector.Config.MyApplication;
import com.example.smartprotector.Net.GetCompanyInfo;
import com.example.smartprotector.Net.PushAdvice;
import com.example.smartprotector.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Huhu on 7/13/15.
 */
public class MoreFragment extends Fragment {

    private EditText edt_advice;
    private Button btn_commit;
    private View view;
    private String str_advice;
    private TextView tv_company, tv_cphone, tv_originzation;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_more, null);
        edt_advice = (EditText) view.findViewById(R.id.edt_adviceToCompany);
        btn_commit = (Button) view.findViewById(R.id.btn_pushAdviceToCompany);
        tv_company = (TextView) view.findViewById(R.id.tv_company);
        tv_cphone = (TextView) view.findViewById(R.id.tv_cphone);
        tv_originzation = (TextView) view.findViewById(R.id.tv_originzation);

        new GetCompanyInfo(new GetCompanyInfo.SuccessCallBack() {
            @Override
            public void onSuccess(String result) throws JSONException {
                JSONObject jo = new JSONObject(result);

                tv_company.setText(jo.get("name").toString());
                tv_cphone.setText(jo.get("phone").toString());
                tv_originzation.setText(jo.get("organization").toString());

            }
        });
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_advice = edt_advice.getText().toString();
                new PushAdvice(MyApplication.getInstance().getCurrentUser().getUser_phone(), str_advice, new PushAdvice.SuccessCallBack() {
                    @Override
                    public void onSuccess(String result) throws JSONException {
                        Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_SHORT).show();
                        edt_advice.setText("");

                    }
                });
            }
        });


        return view;
    }
}
