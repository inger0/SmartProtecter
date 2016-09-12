package com.example.smartprotector.Net;

import com.example.smartprotector.Config.Config;

import org.json.JSONException;

/**
 * Created by Huhu on 8/24/15.
 */
public class GetLoginIcon {
    public GetLoginIcon(String phone, final SuccessCallback successCallback) {
        new NetGetConnection(Config.URL_GETLOGINICON, new NetGetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) throws JSONException {
                successCallback.onSuccess(result);
            }
        }, new NetGetConnection.FailCallback() {
            @Override
            public void onFail() {

            }
        }, Config.USER_PHONE, phone);

    }

    public static interface SuccessCallback {
        public void onSuccess(String result) throws JSONException;
    }

}
