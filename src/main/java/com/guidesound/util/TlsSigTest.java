package com.guidesound.util;

import okhttp3.*;
import org.json.JSONObject;
import org.junit.Assert;
import com.tls.tls_sigature.*;

import java.io.IOException;
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
    private static String importUrl = "https://console.tim.qq.com/v4/im_open_login_svc/account_import?usersig=eJw1j11vgjAUhv8Lty5SCpWyZBcGMcGxmSn7yG6aYgucbdCu1vmx7L-rGrx9npO85-n1ymI95lqDYNyy0Ajv1kPejcPyoMFIxmsrzQUHhBCM0NWCkL2FGpzjooMettZwq8xwsIXmYh6y5zRPJcUfryf-PhCqw6v3NjHqB1XF8uttnuglfVqUsxZWB1X7U8imXYX3qmyzojE0R-moycPZN6j18WW3ECJu4jSp-YKQx2p-dx0Tn8xl-D8aIRQQSsJokBY66QIigiaTGNOB881G7XrL7FFL1-13BnijU2M_&apn=1&identifier=administrator&sdkappid=1400158534&contenttype=json";
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