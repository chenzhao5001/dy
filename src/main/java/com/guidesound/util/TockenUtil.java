package com.guidesound.util;

import org.apache.commons.codec.binary.Base64;

public class TockenUtil {
    private String sign = "guide_sound";

    static public String makeTocken(int id) {
        String temp = "guide_sound" + ":" + id;
        byte[] b=temp.getBytes();
        Base64 base64=new Base64();
        return new String(base64.encode(b));
    }

    static public int getUserIdByTocket(String token) {

        byte[] b = token.getBytes();
        Base64 base64 = new Base64();
        String strBase = new String(base64.decode(b));
        String [] line = strBase.split(":");
        int userId = -1;
        if(line.length == 2) {
            userId = Integer.parseInt(line[1]);
        }
        return userId;
    }
}
