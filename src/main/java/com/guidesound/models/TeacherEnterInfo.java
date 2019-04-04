package com.guidesound.models;

public class TeacherEnterInfo {
    int id;
    int teacher_id;
    int class_id;
    int class_nunber;
    int state;
    int create_time;

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public int getClass_nunber() {
        return class_nunber;
    }

    public void setClass_nunber(int class_nunber) {
        this.class_nunber = class_nunber;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }
}
