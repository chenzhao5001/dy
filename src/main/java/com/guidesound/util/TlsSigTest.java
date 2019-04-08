package com.guidesound.util;

import okhttp3.*;
import org.json.JSONObject;
import org.junit.Assert;
import com.tls.tls_sigature.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
public class TlsSigTest {

    private static String  privStr =  "-----BEGIN PRIVATE KEY-----\n" +
            "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgOxbmS/H3s7/mFVk7\n" +
            "cApjvwy0jqZdM2hTyo184i3PxbyhRANCAARtMrE+LaHFjagDuSReovTn/rHKaCvm\n" +
            "Jb25OZQhuF7om6QK7ED372xqwImKbYOwUNxGd/GW/mRCYoD4C9osfdL6\n" +
            "-----END PRIVATE KEY-----\n";
    private static String pubStr = "-----BEGIN PUBLIC KEY-----\n" +
            "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEbTKxPi2hxY2oA7kkXqL05/6xymgr\n" +
            "5iW9uTmUIbhe6JukCuxA9+9sasCJim2DsFDcRnfxlv5kQmKA+AvaLH3S+g==\n" +
            "-----END PUBLIC KEY-----";

    static String user_control = "control";
    static String user_message = "message";

    static String control_usersig = "eJxlj11PgzAYhe-5FaS3GimFTjDZRZnDbXGLfLipN4SVDipQCBTDNPvvU1wiie-t8*Sc834pqqqC8DG4iSmtOiEjeawZUO9UAMH1H6xrnkSxjIwm*QdZX-OGRfFBsmaAOsYYQTh2eMKE5Ad*MWglZFMVI6FN8mho*U0wIdSxhQ1zrPB0gOu5N1vO89h9X5F0tkHh9krjATJLuptse*GXZKkRzddX7nO2ePFfCXecYvNW7h-ce68-eqSd7HcWDZ0gsBfrgthdln0mmHa5ZT6l0*moUvKSXV4yb3UELWiP6AdrWl6JQUDfe3VkwJ8Dykk5A545XPA_";
    static String message_usersig = "eJxlj1FPgzAUhd-5FQ2vM1IKTcHEB4JsSnA6xyLbS9NAITdmgLQyNuN-d*ISSbyv35dzzv00EEJmmqyvRZ43H7Xm*thKE90gE5tXf7BtoeBCc6cr-kE5tNBJLkotuxHalFKC8dSBQtYaSrgYe6mUqOREUMUbH1t*E1yMbepRx50qUI3wMdqED1Eyw5Cn70PJNi8eTe7TDk4WEbvUz*qBET-CeayerWXYBhAFnnt3YGKbhjPmqPn6dWvF-ZO3yrLqGJP5ovEPwWLZ*7uKqNtJpYa9vLzkMptgz2UT2stOQVOPAjnvtYmDf840voxvRdJcHw__";


    private static String importUrl = "https://console.tim.qq.com/v4/im_open_login_svc/account_import?usersig="  +  control_usersig + "&apn=1&identifier=" + user_control + "&sdkappid=1400158534&contenttype=json";
    private static String controlUrl = "https://console.tim.qq.com/v4/openim/sendmsg?usersig=" + control_usersig + "&identifier=" + user_control + "&sdkappid=1400158534&random=99999999&contenttype=json";
    public static String createGroup(String im_id,String GroupName) throws IOException {
        Random random = new Random();
        int randomInt = random.nextInt();
//        String strRandomInt = String.valueOf(randomInt);
        String addGroupUrl = "https://console.tim.qq.com/v4/group_open_http_svc/create_group?usersig=" + control_usersig + "&identifier=" + user_control + "&sdkappid=1400158534&random=" + randomInt+ "&contenttype=json";

        String reqJson = "{\"Name\":\"" +GroupName + "\",\"Type\":\"Public\",\"Owner_Account\": \"" + im_id + "\",\"FaceUrl\":\"https://background-1257964795.cos.ap-beijing.myqcloud.com/main_background.jpg\"}";
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, reqJson);
        Request req = new Request.Builder()
                .url(addGroupUrl)
                .post(body)
                .build();
        Response resp;
        OkHttpClient client_temp = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        resp = client_temp.newCall(req).execute();
        String jsonString = resp.body().string();

        JSONObject ret = new JSONObject(jsonString);
        int code = ret.getInt("ErrorCode");
        if(code == 0 ) {
            return ret.getString("GroupId");
        }
        return "";
    }
    public static String addGroupPerson(String group_id,String new_person ) throws IOException {

        Random random = new Random();
        int randomInt = random.nextInt();
        String addGroupUrl = "https://console.tim.qq.com/v4/group_open_http_svc/add_group_member?usersig=" + control_usersig + "&identifier=" + user_control + "&sdkappid=1400158534&random=" + randomInt+ "&contenttype=json";
        String info = "{" +
                "\"GroupId\": \""+ group_id+"\"," +
                "\"MemberList\": [{" +
                "\"Member_Account\": \""+ new_person+"\"" +
                "}]" +
                "}";
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, info);
        Request req = new Request.Builder()
                .url(addGroupUrl)
                .post(body)
                .build();
        Response resp;
        OkHttpClient client_temp = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        resp = client_temp.newCall(req).execute();
        String jsonString = resp.body().string();

        return jsonString;
    }
    public static String getUrlSig(String im_id) throws IOException {
        tls_sigature.GenTLSSignatureResult result = tls_sigature.GenTLSSignatureEx(1400158534, im_id, privStr);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String json = "{\"Identifier\":\"" + im_id + "\"}";
        RequestBody body = RequestBody.create(JSON, json);
        Request req = new Request.Builder()
                .url(importUrl)
                .post(body)
                .build();
        Response resp;
        OkHttpClient client_temp = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        resp = client_temp.newCall(req).execute();
        String jsonString = resp.body().string();
        JSONObject ret = new JSONObject(jsonString);
        int code = ret.getInt("ErrorCode");
        if(code != 0 ) {
            return "";
        }
        return result.urlSig;
    }


    //发送控制消息
    public static String PushMessage(String user_id,String info) throws IOException {
        String rand = ToolsFunction.getNumRandomString(10);
        String controlUrl = "https://console.tim.qq.com/v4/openim/sendmsg?usersig=" + control_usersig + "&identifier=" + user_control + "&sdkappid=1400158534&random=" + rand + "&contenttype=json";

        JSONObject Info = new JSONObject();
        Info.put("Data",info);
        JSONObject cell = new JSONObject();
        cell.put("MsgType","TIMCustomElem");
        cell.put("MsgContent",Info);
        List<JSONObject> arr = new ArrayList<>();
        arr.add(cell);

        JSONObject jsonSend = new JSONObject();
        jsonSend.put("SyncOtherMachine", 2);
        jsonSend.put("To_Account", user_id);
        jsonSend.put("MsgLifeTime", 60);
        jsonSend.put("MsgRandom", 1234);
        jsonSend.put("MsgTimeStamp", new Date().getTime() / 1000);
        jsonSend.put("MsgBody",arr);
        String send = jsonSend.toString();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, send);
        Request req = new Request.Builder()
                .url(controlUrl)
                .post(body)
                .build();
        Response resp;
        OkHttpClient client_temp = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        resp = client_temp.newCall(req).execute();
        String jsonString = resp.body().string();
        JSONObject ret = new JSONObject(jsonString);
        int code = ret.getInt("ErrorCode");
        if(code != 0 ) {
            return ret.toString();
        }
        return "";
    }


    public static String SendMessage(String user_id,String info) throws IOException {
        String rand = ToolsFunction.getNumRandomString(10);
        String controlUrl = "https://console.tim.qq.com/v4/openim/sendmsg?usersig=" + control_usersig + "&identifier=" + user_control + "&sdkappid=1400158534&random=" + rand + "&contenttype=json";

        JSONObject Info = new JSONObject();
        Info.put("Data",info);
        JSONObject cell = new JSONObject();
        cell.put("MsgType","TIMCustomElem");
        cell.put("MsgContent",Info);
        List<JSONObject> arr = new ArrayList<>();
        arr.add(cell);

        JSONObject jsonSend = new JSONObject();
        jsonSend.put("SyncOtherMachine", 2);
        jsonSend.put("From_Account", "397");
        jsonSend.put("To_Account", user_id);
        jsonSend.put("MsgLifeTime", 60);
        jsonSend.put("MsgRandom", 1234);
        jsonSend.put("MsgTimeStamp", new Date().getTime() / 1000);
        jsonSend.put("MsgBody",arr);
        String send = jsonSend.toString();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, send);
        Request req = new Request.Builder()
                .url(controlUrl)
                .post(body)
                .build();
        Response resp;
        OkHttpClient client_temp = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        resp = client_temp.newCall(req).execute();
        String jsonString = resp.body().string();
        JSONObject ret = new JSONObject(jsonString);
        int code = ret.getInt("ErrorCode");
        if(code != 0 ) {
            return ret.toString();
        }
        return "";
    }


    public static void genAndVerify() {
        try {
            // generate signature
            tls_sigature.GenTLSSignatureResult result = tls_sigature.GenTLSSignatureEx(1400000955, "xiaojun", privStr);
            Assert.assertNotEquals(null, result);
            Assert.assertNotEquals(null, result.urlSig);
            Assert.assertNotEquals(0, result.urlSig.length());

            // check signature
            tls_sigature.CheckTLSSignatureResult checkResult = tls_sigature.CheckTLSSignatureEx(result.urlSig, 1400000955, "xiaojun", pubStr);
            Assert.assertNotEquals(null, checkResult);
            Assert.assertTrue(checkResult.verifyResult);

            checkResult = tls_sigature.CheckTLSSignatureEx(result.urlSig, 1400000955, "xiaojun2", pubStr);
            Assert.assertNotEquals(null, checkResult);
            Assert.assertFalse( checkResult.verifyResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}