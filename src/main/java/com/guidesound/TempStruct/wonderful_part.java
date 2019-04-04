package com.guidesound.TempStruct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class wonderful_part {
    int time_start;
    int time_end;
    String picture;

    public int getTime_start() {
        return time_start;
    }

    public void setTime_start(int time_start) {
        this.time_start = time_start;
    }

    public int getTime_end() {
        return time_end;
    }

    public void setTime_end(int time_end) {
        this.time_end = time_end;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
