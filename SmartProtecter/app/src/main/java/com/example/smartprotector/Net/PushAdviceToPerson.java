package com.example.smartprotector.Net;

import android.util.Log;

import com.example.smartprotector.Config.Config;
import com.example.smartprotector.Config.MyApplication;

import org.json.JSONException;

/**
 * Created by Huhu on 9/17/15.
 */
public class PushAdviceToPerson {
    public PushAdviceToPerson(int sid, int score,String advice, final SuccessCallBack successCallBack) {
        new NetPostConnection(Config.URL_ADVICE_TO, new NetPostConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) throws JSONException {
                successCallBack.onSuccess(result);
                Log.e("success", result);

            }
        }, new NetPostConnection.FailCallback() {
            @Override
            public void onFail() {

            }
        }, Config.USER_PHONE, MyApplication.getInstance().getCurrentUser().getUser_phone(),"advice", advice,"score",score,"s_id",sid);
    }

    public static interface SuccessCallBack {
        void onSuccess(String result) throws JSONException;
    }

    public static interface FailCallBack {
        void onFail();
    }
}
