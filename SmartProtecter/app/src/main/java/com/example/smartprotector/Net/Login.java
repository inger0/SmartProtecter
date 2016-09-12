package com.example.smartprotector.Net;

import android.content.Context;

import com.example.smartprotector.Config.Config;

import org.json.JSONException;

/**
 * Created by Huhu on 8/10/15.
 */
public class Login {
    public Login(final Context c, String account, String MD5password, final FailCallBack failCallBack, final SuccessCallBack successsCallBack) {
        new NetPostConnection(Config.URL_LOGIN, new NetPostConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) throws JSONException {
                successsCallBack.onSuccess(result);

            }
        }, new NetPostConnection.FailCallback() {
            //
            @Override
            public void onFail() {
            }
        }, Config.USER_ACCOUNT, account, Config.USER_PASSWORD, MD5password);

    }

    public static interface SuccessCallBack {
        void onSuccess(String result) throws JSONException;
    }

    public static interface FailCallBack {
        void onFail();
    }
}
