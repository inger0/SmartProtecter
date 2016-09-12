package com.example.smartprotector.Net;

import com.example.smartprotector.Config.Config;

import org.json.JSONException;

/**
 * Created by Huhu on 8/23/15.
 */
public class GetCompanyInfo {
    public GetCompanyInfo(final SuccessCallBack successCallBack) {
        new NetGetConnection(Config.URL_GETCOMPANY, new NetGetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) throws JSONException {
                successCallBack.onSuccess(result);
            }
        }, new NetGetConnection.FailCallback() {
            @Override
            public void onFail() {

            }
        });

    }

    public static interface SuccessCallBack {
        void onSuccess(String result) throws JSONException;
    }

    public static interface FailCallBack {
        void onFail();
    }
}
