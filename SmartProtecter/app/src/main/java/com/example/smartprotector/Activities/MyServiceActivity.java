package com.example.smartprotector.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smartprotector.Net.MainImageConnection;
import com.example.smartprotector.R;

/**
 * 报修单详细页面
 */
public class MyServiceActivity extends Activity {
    private TextView tv_showAddress, tv_showPhone, tv_showOrigin, tv_showReason, tv_showMemo, tv_showStatus, tv_showManagerPhone, tv_showTime;
    private Button btn_showType, btn_addAdvice;
    private ImageButton btn_showBack, btn_call;
    private ImageView iv_photo;
    private String s_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service);
        init();
        setData();
        setListener();
    }

    void init() {
        tv_showAddress = (TextView) findViewById(R.id.tv_showAddress);
        tv_showPhone = (TextView) findViewById(R.id.tv_showPhone);
        tv_showOrigin = (TextView) findViewById(R.id.tv_showServiceOrigin);
        tv_showReason = (TextView) findViewById(R.id.tv_showServiceReason);
        tv_showMemo = (TextView) findViewById(R.id.tv_showMemo);
        tv_showStatus = (TextView) findViewById(R.id.tv_showStatus);
        tv_showManagerPhone = (TextView) findViewById(R.id.tv_showManagerphone);
        tv_showTime = (TextView) findViewById(R.id.tv_showServiceTime);
        btn_showType = (Button) findViewById(R.id.btn_showServiceType);
        btn_showBack = (ImageButton) findViewById(R.id.btn_show_service_back);
        iv_photo = (ImageView) findViewById(R.id.iv_photo);
        btn_showBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_addAdvice = (Button) findViewById(R.id.btn_addAdvice);
        btn_call = (ImageButton) findViewById(R.id.btn_callTheManager);
    }

    /**
     * 装填数据的方法
     */
    void setData() {
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        s_id = bundle.get("service_id").toString();
        String address = bundle.get("address").toString();
        String phone = bundle.get("reportphone").toString();
        String type = bundle.get("type").toString();
        String origin = bundle.get("origin").toString();
        String reason = bundle.get("reason").toString();
        String memo = bundle.get("memo").toString();
        String time = bundle.get("time").toString();
        String status = bundle.get("status").toString();
        String mphone = bundle.get("mphone").toString();

        tv_showAddress.setText(address);
        tv_showPhone.setText(phone);
        tv_showTime.setText(time);
        tv_showMemo.setText(memo);
        tv_showOrigin.setText(origin);
        tv_showReason.setText(reason);

        tv_showManagerPhone.setText(mphone);
        if (type.equals("1")) {
            btn_showType.setText("宿舍楼");
        }
        if (type.equals("2")) {
            btn_showType.setText("教学楼");
        }
        if (type.equals("3")) {
            btn_showType.setText("校园设施");
        }
        if (status.equals("1")) {
            tv_showStatus.setText("未维修");
        }
        if (status.equals("2")) {
            tv_showStatus.setText("正在维修中");
            btn_call.setVisibility(View.VISIBLE);
        }
        if (status.equals("3")) {
            tv_showStatus.setText("已维修");
            btn_addAdvice.setVisibility(View.VISIBLE);
            btn_call.setVisibility(View.VISIBLE);
        }
        new MainImageConnection(iv_photo).execute(bundle.get("image_path"));
        new MainImageConnection(iv_photo).execute(bundle.get("image_path"));

    }

    //为评价按钮设置监听器
    void setListener() {
        btn_addAdvice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("s_id", s_id);
                i.putExtras(bundle);
                i.setClass(MyServiceActivity.this, AdviceActivity.class);
                MyServiceActivity.this.startActivity(i);
                MyServiceActivity.this.finish();
            }
        });
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=tv_showManagerPhone.getText().toString();
                String call="tel:"+phone;
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(call));
                startActivity(intent);
            }
        });
    }

}
