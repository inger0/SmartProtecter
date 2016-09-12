package com.example.smartprotector.Net;

import android.content.Context;
import android.widget.Toast;

import com.example.smartprotector.Config.Config;

/**
 * Created by Huhu on 8/13/15.
 */
public class Register {
    public Register(final Context c, String str_phone, String str_password, String str_nickname, String str_iconPath, final SuccessCallBack successCallBack, final FailCallBack failCallBack) {
        new NetPostConnection(Config.URL_REGISTER, new NetPostConnection.SuccessCallback() {

            @Override
            public void onSuccess(String result) {
                if (result.equals("-3")) {
                    Toast.makeText(c, "手机号已经被注册！", Toast.LENGTH_SHORT).show();
                }
                if (result.equals("1")) {
                    Toast.makeText(c, "注册成功！", Toast.LENGTH_SHORT).show();

                }

            }
        }, new NetPostConnection.FailCallback() {

            @Override
            public void onFail() {

            }
        }, Config.USER_PHONE, str_phone, Config.USER_PASSWORD, str_password, Config.USER_NICKNAME, str_nickname, Config.USER_ICONPATH, str_iconPath);

    }

    public static interface SuccessCallBack {
        void onSuccess(String result);
    }

    public static interface FailCallBack {
        void onFail();
    }
}
