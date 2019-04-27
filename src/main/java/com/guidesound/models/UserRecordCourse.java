package com.guidesound.models;

public class UserRecordCourse {
    int id;
    int user_id;
    int user_record_course_id;
    int last_class_NO;
    String last_class_name;
    int last_class_pos;
    int create_time;
    int update_time;

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public int getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(int update_time) {
        this.update_time = update_time;
    }

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

    public int getUser_record_course_id() {
        return user_record_course_id;
    }

    public void setUser_record_course_id(int user_record_course_id) {
        this.user_record_course_id = user_record_course_id;
    }

    public int getLast_class_NO() {
        return last_class_NO;
    }

    public void setLast_class_NO(int last_class_NO) {
        this.last_class_NO = last_class_NO;
    }

    public String getLast_class_name() {
        return last_class_name;
    }

    public void setLast_class_name(String last_class_name) {
        this.last_class_name = last_class_name;
    }

    public int getLast_class_pos() {
        return last_class_pos;
    }

    public void setLast_class_pos(int last_class_pos) {
        this.last_class_pos = last_class_pos;
    }
}
