package com.example.smartprotector.Net;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 用来获取七牛token的一个工具类
 */
public class NetCore {

    public static String getResultFromNet(String url) throws Exception {
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
        String result = "";

        HttpGet get = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                BufferedReader bin = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String s;
                StringBuilder sBuilder = new StringBuilder();
                while (((s = bin.readLine()) != null)) {
                    sBuilder.append(s);
                }
                bin.close();
                result = sBuilder.toString();
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        return result;
    }

}
