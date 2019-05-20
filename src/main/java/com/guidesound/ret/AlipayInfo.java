package com.guidesound.ret;

public class AlipayInfo {
    String appId;
    String pId;
    String payRatio;
    String sign;
    String sign_type;

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getPayRatio() {
        return payRatio;
    }

    public void setPayRatio(String payRatio) {
        this.payRatio = payRatio;
    }
}
