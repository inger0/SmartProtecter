package com.example.smartprotector.Net;

import android.util.Log;

import com.example.smartprotector.Config.Config;
import com.example.smartprotector.Config.MyApplication;

import org.json.JSONException;

/**
 * Created by Huhu on 8/22/15.
 */
public class PushAdvice {
    public PushAdvice(String phone, String advice, final SuccessCallBack successCallBack) {
        new NetPostConnection(Config.URL_ADVICE, new NetPostConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) throws JSONException {
                successCallBack.onSuccess(result);
                Log.e("success", result);

            }
        }, new NetPostConnection.FailCallback() {
            @Override
            public void onFail() {

            }
        }, Config.USER_PHONE, MyApplication.getInstance().getCurrentUser().getUser_phone(), Config.ADVICE, advice);
    }

    public static interface SuccessCallBack {
        void onSuccess(String result) throws JSONException;
    }

    public static interface FailCallBack {
        void onFail();
    }

}
