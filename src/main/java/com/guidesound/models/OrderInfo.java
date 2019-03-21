package com.guidesound.models;

public class OrderInfo {
    int course_owner_id;
    int course_id;
    String way;
    String tutor_content;
    String refund_rule;

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
