package com.guidesound.util;


import com.fasterxml.jackson.databind.util.JSONPObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class AuthUtil {

    public static final String APPID = "";
    public static final String APPSECRET = "";


    public static JSONPObject doGetJson(String url) {
        OkHttpClient client = new OkHttpClient();
        Request requset =  new Request.Builder().url(url).build();
        Response response = null;

        try {
            response = client.newCall(requset).execute();
            String result = response.body().string();
            Object o = new Object();
            new JSONPObject(result,o);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
