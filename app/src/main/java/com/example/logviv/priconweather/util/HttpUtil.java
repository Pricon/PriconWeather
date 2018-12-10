package com.example.logviv.priconweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by logviv on 2018/12/3.
 */

public class HttpUtil {
    //与服务器进行交互
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
