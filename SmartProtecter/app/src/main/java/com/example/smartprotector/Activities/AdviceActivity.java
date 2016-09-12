package com.example.smartprotector.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smartprotector.Net.PushAdviceToPerson;
import com.example.smartprotector.R;

import org.json.JSONException;

public class AdviceActivity extends Activity {
    private EditText edt_score;
    private EditText edt_advice;
    private Button btn_push;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        Intent i = getIntent();
        Bundle bundle=i.getExtras();
        String sid = bundle.get("s_id").toString();
        final int s_id= Integer.parseInt(sid);
        edt_advice = (EditText) findViewById(R.id.edt_adviceToManager);
        edt_score = (EditText) findViewById(R.id.edt_score);
        btn_push = (Button) findViewById(R.id.btn_pushAdviceToManager);

        btn_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String score = edt_score.getText().toString();
                final int s = Integer.parseInt(score);
                final String advice = edt_advice.getText().toString();
                new PushAdviceToPerson(s_id, s, advice, new PushAdviceToPerson.SuccessCallBack() {
                    @Override
                    public void onSuccess(String result) throws JSONException {
                        Toast.makeText(AdviceActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    }
                }) {

                };
                AdviceActivity.this.finish();
            }
        });
    }


}
