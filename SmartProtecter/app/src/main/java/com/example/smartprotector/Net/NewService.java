package com.example.smartprotector.Net;

import android.content.Context;
import android.widget.Toast;

import com.example.smartprotector.Config.Config;

/**
 * Created by Huhu on 8/14/15.
 * 提交新保修单的工具类。
 */
public class NewService {
    public NewService(final Context c, String image_url, String register_phone, String address, String phone, int types, String origin, String time, String reason, String memo, final FailedCallback failedCallback, final SuccessfulCallback successCallBack) {
        new NetPostConnection(Config.URL_NEWService, new NetPostConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if (result.equals("1")) {
                    Toast.makeText(c, "提交成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(c, "提交失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, new NetPostConnection.FailCallback() {
            @Override
            public void onFail() {

            }
        }, Config.SERVICE_IMAGE, image_url, Config.USER_PHONE, register_phone, Config.SERVICE_ADDRESS, address, Config.SERVICE_PHONE, phone, Config.SERVICE_ORIGIN, origin, Config.SERVICE_TIME, time, Config.SERVICE_REASON, reason, Config.SERVICE_MEMO, memo, Config.SERVICE_TYPES, types);
    }

    public static interface SuccessfulCallback {
        void onSuccess(String result);
    }

    public static interface FailedCallback {
        void onFailure();
    }
}
