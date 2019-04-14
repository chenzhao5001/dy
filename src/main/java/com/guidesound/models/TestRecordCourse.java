package com.guidesound.models;

public class TestRecordCourse {
    int id;
    int user_id;
    int record_course_id;
    int class_NO;
    String class_url;
    String class_name;
    int time_start;
    int time_end;
    String picture;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRecord_course_id() {
        return record_course_id;
    }

    public void setRecord_course_id(int record_course_id) {
        this.record_course_id = record_course_id;
    }

    public int getClass_NO() {
        return class_NO;
    }

    public void setClass_NO(int class_NO) {
        this.class_NO = class_NO;
    }

    public String getClass_url() {
        return class_url;
    }

    public void setClass_url(String class_url) {
        this.class_url = class_url;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

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
