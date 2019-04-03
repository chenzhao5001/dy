package com.guidesound.models;

public class OrderInfo {
    int id;
    int course_owner_id;
    int course_id;
    String way;
    String tutor_content;
    String refund_rule;
    int order_status;
    int type;
    int all_hours;
    int student_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getAll_hours() {
        return all_hours;
    }

    public void setAll_hours(int all_hours) {
        this.all_hours = all_hours;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public String getTutor_content() {
        return tutor_content;
    }

    public void setTutor_content(String tutor_content) {
        this.tutor_content = tutor_content;
    }

    public String getRefund_rule() {
        return refund_rule;
    }

    public void setRefund_rule(String refund_rule) {
        this.refund_rule = refund_rule;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public int getCourse_owner_id() {
        return course_owner_id;
    }

    public void setCourse_owner_id(int course_owner_id) {
        this.course_owner_id = course_owner_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }
}
