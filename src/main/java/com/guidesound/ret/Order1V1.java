package com.guidesound.ret;

import com.guidesound.TempStruct.ClassUseInfo;
import com.guidesound.TempStruct.RefundInfo;

public class Order1V1 {
    int id;
    int order_status;
    RefundInfo refund_info;
    int course_owner_id;
    int course_id;
    String course_pic;
    String course_owner_pic;
    String course_owner_name;
    String course_name;
    String teacher_name;
    String student_name;
    String subject;
    String grade;
    String form;
    String way;
    int all_hours;
    int price_one_hour;
    int all_charge;
    int create_time;
    int student_id;
    String refund_rule;
    String tutor_content;
    ClassUseInfo class_use_info;
    Object outline;

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getCourse_pic() {
        return course_pic;
    }

    public void setCourse_pic(String course_pic) {
        this.course_pic = course_pic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public RefundInfo getRefund_info() {
        return refund_info;
    }

    public int getCourse_owner_id() {
        return course_owner_id;
    }

    public void setCourse_owner_id(int course_owner_id) {
        this.course_owner_id = course_owner_id;
    }

    public String getCourse_owner_pic() {
        return course_owner_pic;
    }

    public void setCourse_owner_pic(String course_owner_pic) {
        this.course_owner_pic = course_owner_pic;
    }

    public String getCourse_owner_name() {
        return course_owner_name;
    }

    public void setCourse_owner_name(String course_owner_name) {
        this.course_owner_name = course_owner_name;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public int getAll_hours() {
        return all_hours;
    }

    public void setAll_hours(int all_hours) {
        this.all_hours = all_hours;
    }

    public int getPrice_one_hour() {
        return price_one_hour;
    }

    public void setPrice_one_hour(int price_one_hour) {
        this.price_one_hour = price_one_hour;
    }

    public int getAll_charge() {
        return all_charge;
    }

    public void setAll_charge(int all_charge) {
        this.all_charge = all_charge;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public String getRefund_rule() {
        return refund_rule;
    }

    public void setRefund_rule(String refund_rule) {
        this.refund_rule = refund_rule;
    }

    public String getTutor_content() {
        return tutor_content;
    }

    public void setTutor_content(String tutor_content) {
        this.tutor_content = tutor_content;
    }

    public void setRefund_info(RefundInfo refund_info) {
        this.refund_info = refund_info;
    }

    public ClassUseInfo getClass_use_info() {
        return class_use_info;
    }

    public void setClass_use_info(ClassUseInfo class_use_info) {
        this.class_use_info = class_use_info;
    }

    public Object getOutline() {
        return outline;
    }

    public void setOutline(Object outline) {
        this.outline = outline;
    }
}
