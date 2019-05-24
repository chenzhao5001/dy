package com.guidesound.TempStruct;

public class CourseOrder {
    int course_type;
    String course_name;
    String order_id;
    String teacher_name;
    int teacher_id;

    public int getCourse_type() {
        return course_type;
    }

    public void setCourse_type(int course_type) {
        this.course_type = course_type;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }
}
