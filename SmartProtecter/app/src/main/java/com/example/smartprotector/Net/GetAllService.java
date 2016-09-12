package com.example.smartprotector.Net;

import com.example.smartprotector.Bean.HistoryListBean;
import com.example.smartprotector.Config.Config;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Huhu on 8/20/15.
 */
public class GetAllService {
    private static List<HistoryListBean> list;


    public GetAllService(String register_phone, final SuccessCallBack successCallBack) {
        new NetGetConnection(Config.URL_GETALLSERVICE, new NetGetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) throws JSONException {
                successCallBack.onSuccess(result);
            }
        }, new NetGetConnection.FailCallback() {
            @Override
            public void onFail() {

            }
        }, Config.USER_PHONE, register_phone);

    }

    public static interface SuccessCallBack {
        void onSuccess(String result) throws JSONException;
    }

    public static interface FailCallBack {
        void onFail();
    }

}
