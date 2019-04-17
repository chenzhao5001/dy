package com.guidesound.models;

public class VideoIndex {
    String user_guid;
    String param;
    int index_count;

    public int getIndex_count() {
        return index_count;
    }

    public void setIndex_count(int index_count) {
        this.index_count = index_count;
    }

    public String getUser_guid() {
        return user_guid;
    }

    public void setUser_guid(String user_guid) {
        this.user_guid = user_guid;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
