package com.guidesound.models;

public class ClassRoom {
    int class_id;
    int user_id;
    int course_id;
    String new_class_time;

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getNew_class_time() {
        return new_class_time;
    }

    public void setNew_class_time(String new_class_time) {
        this.new_class_time = new_class_time;
    }
}
