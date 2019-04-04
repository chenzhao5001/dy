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
    int teacher_id;
    int price_one_hour;
    int refund_amount;
    int submit_time;

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public int getSubmit_time() {
        return submit_time;
    }

    public void setSubmit_time(int submit_time) {
        this.submit_time = submit_time;
    }

    public int getRefund_amount() {
        return refund_amount;
    }

    public void setRefund_amount(int refund_amount) {
        this.refund_amount = refund_amount;
    }


    public int getPrice_one_hour() {
        return price_one_hour;
    }

    public void setPrice_one_hour(int price_one_hour) {
        this.price_one_hour = price_one_hour;
    }

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
