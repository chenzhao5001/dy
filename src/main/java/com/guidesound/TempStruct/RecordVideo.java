package com.guidesound.TempStruct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordVideo   {
    int class_number;
    String class_title;
    int class_length;
    String class_url;
    int charge_type;
    Object wonderful_part;


    public Object getWonderful_part() {
        return wonderful_part;
    }

    public void setWonderful_part(Object wonderful_part) {
        this.wonderful_part = wonderful_part;
    }


    public int getClass_number() {
        return class_number;
    }

    public void setClass_number(int class_number) {
        this.class_number = class_number;
    }

    public String getClass_title() {
        return class_title;
    }

    public void setClass_title(String class_title) {
        this.class_title = class_title;
    }

    public int getClass_length() {
        return class_length;
    }

    public void setClass_length(int class_length) {
        this.class_length = class_length;
    }

    public String getClass_url() {
        return class_url;
    }

    public void setClass_url(String class_url) {
        this.class_url = class_url;
    }

    public int getCharge_type() {
        return charge_type;
    }

    public void setCharge_type(int charge_type) {
        this.charge_type = charge_type;
    }

}
