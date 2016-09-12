package com.example.smartprotector.Net;

import com.example.smartprotector.Config.Config;

import org.json.JSONException;

/**
 * Created by Huhu on 8/22/15.
 */
public class GetServiceReport {
    public GetServiceReport(String s_id,final SuccessCallBack successCallBack) {
        new NetGetConnection(Config.URL_GETSERVICE, new NetGetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) throws JSONException {
                successCallBack.onSuccess(result);
            }
        }, new NetGetConnection.FailCallback() {
            @Override
            public void onFail() {

            }
        }, Config.SERVICE_ID, s_id);
    }

    public static interface SuccessCallBack {
        void onSuccess(String result) throws JSONException;
    }

    public static interface FailCallBack {
        void onFail();
    }
}
