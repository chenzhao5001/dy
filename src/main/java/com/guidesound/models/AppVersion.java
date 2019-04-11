package com.guidesound.models;

public class AppVersion {
    int id;
    String android_version;
    String android_download_url;
    int android_type;
    String android_message;
    String ios_version;
    String ios_download_url;
    int ios_type;
    String ios_message;

    public String getIos_message() {
        return ios_message;
    }

    public void setIos_message(String ios_message) {
        this.ios_message = ios_message;
    }

    public String getAndroid_message() {
        return android_message;
    }

    public void setAndroid_message(String android_message) {
        this.android_message = android_message;
    }

    public int getAndroid_type() {
        return android_type;
    }

    public void setAndroid_type(int android_type) {
        this.android_type = android_type;
    }

    public int getIos_type() {
        return ios_type;
    }

    public void setIos_type(int ios_type) {
        this.ios_type = ios_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAndroid_version() {
        return android_version;
    }

    public void setAndroid_version(String android_version) {
        this.android_version = android_version;
    }

    public String getAndroid_download_url() {
        return android_download_url;
    }

    public void setAndroid_download_url(String android_download_url) {
        this.android_download_url = android_download_url;
    }

    public String getIos_version() {
        return ios_version;
    }

    public void setIos_version(String ios_version) {
        this.ios_version = ios_version;
    }

    public String getIos_download_url() {
        return ios_download_url;
    }

    public void setIos_download_url(String ios_download_url) {
        this.ios_download_url = ios_download_url;
    }
}
