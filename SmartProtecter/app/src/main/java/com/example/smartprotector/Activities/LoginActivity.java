package com.example.smartprotector.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.smartprotector.Bean.CurrentUser;
import com.example.smartprotector.Config.MyApplication;
import com.example.smartprotector.Net.GetLoginIcon;
import com.example.smartprotector.Net.Login;
import com.example.smartprotector.Net.MainImageConnection;
import com.example.smartprotector.R;
import com.example.smartprotector.Utils.DoubleClickUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 登陆的activity
 */
public class LoginActivity extends Activity {
    //注册按钮
    private Button btn_register;
    //登陆按钮
    private Button btn_login;
    //输入框的内容
    private EditText edt_loginAccount, edt_loginPassword;
    //全局变量Application
    private MyApplication application;
    //头像布局
    private ImageView iv_icon;
    //判断线程是否该结束的标志
    boolean stopThread = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    JSONObject jo = null;
                    try {
                        jo = new JSONObject(msg.obj.toString());
                        new MainImageConnection(iv_icon).execute(jo.get("icon_u").toString().replace(" ", "%20"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        setListener();
        startThread();
    }

    private void startThread() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!stopThread) {
                    if (edt_loginAccount.length() == 11) {
                        new GetLoginIcon(edt_loginAccount.getText().toString(), new GetLoginIcon.SuccessCallback() {
                            @Override
                            public void onSuccess(String result) throws JSONException {
                                Message msg = new Message();
                                msg.what = 0;
                                msg.obj = result;
                                handler.sendMessage(msg);
                            }
                        });
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        t.start();
    }

    /**
     * 初始化场景
     */
    void init() {
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_login = (Button) findViewById(R.id.btn_login);
        edt_loginPassword = (EditText) findViewById(R.id.edt_loginPassword);
        edt_loginAccount = (EditText) findViewById(R.id.edt_loginAccount);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
    }

    /**
     * 设置监听器
     */
    void setListener() {
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(i);
                LoginActivity.this.finish();

            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DoubleClickUtil.isFastDoubleClick()) {
                    return;
                }
                testIsLegal();
            }
        });

    }


    /**
     * 检测用户名密码是否正确。
     */
    void testIsLegal() {
        String account = edt_loginAccount.getText().toString();
        String password = edt_loginPassword.getText().toString();
        String MD5password = md5(password);
        new Login(this, account, MD5password, new Login.FailCallBack() {
            @Override
            public void onFail() {
            }
        }, new Login.SuccessCallBack() {
            @Override
            public void onSuccess(String result) throws JSONException {
                switch (result) {
                    case "-1":
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        // 登录初始化,记录必要信息
                        application = MyApplication.getInstance();
                        JSONObject jo = new JSONObject(result);
                        CurrentUser user = new CurrentUser(edt_loginAccount.getText().toString(), jo.getString("nickname"), jo.getString("icon_u"));
                        application.setCurrentUser(user);
                        //跳转
                        Intent i = new Intent();
                        i.setClass(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.startActivity(i);
                        LoginActivity.this.finish();
                }
            }
        });

    }

    /**
     * MD5加密
     *
     * @param string
     * @return
     */

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    @Override
    protected void onDestroy() {
        stopThread = true;
        super.onDestroy();
    }
}
