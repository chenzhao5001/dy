package com.guidesound.models;

import javax.validation.constraints.NotNull;

public class Course {

    int id;
    int user_id;
    String course_pic;
    String course_name;
    int subject;
    int grade;
    int form;
    int price_one_hour;
    String area_service;
    int test_form;
    int test_duration;
    int test_charge;
    int test_time;
    String introduction_teacher;
    int teacher_id;
    String teacher_name;
    String way;
    String refund_rule;

    int type;
    int course_status;
    int create_time;
    int update_time;


    int max_person;
    int all_hours;
    int all_charge;
    String course_content;
    String outline;

    String tutor_content;

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

    public int getTest_time() {
        return test_time;
    }

    public void setTest_time(int test_time) {
        this.test_time = test_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourse_pic() {
        return course_pic;
    }

    public void setCourse_pic(String course_pic) {
        this.course_pic = course_pic;
    }

    public int getCourse_status() {
        return course_status;
    }

    public void setCourse_status(int course_status) {
        this.course_status = course_status;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getForm() {
        return form;
    }

    public void setForm(int form) {
        this.form = form;
    }

    public int getPrice_one_hour() {
        return price_one_hour;
    }

    public void setPrice_one_hour(int price_one_hour) {
        this.price_one_hour = price_one_hour;
    }

    public String getArea_service() {
        return area_service;
    }

    public void setArea_service(String area_service) {
        this.area_service = area_service;
    }

    public int getTest_form() {
        return test_form;
    }

    public void setTest_form(int test_form) {
        this.test_form = test_form;
    }

    public int getTest_duration() {
        return test_duration;
    }

    public void setTest_duration(int test_duration) {
        this.test_duration = test_duration;
    }

    public int getTest_charge() {
        return test_charge;
    }

    public void setTest_charge(int test_charge) {
        this.test_charge = test_charge;
    }

    public String getIntroduction_teacher() {
        return introduction_teacher;
    }

    public void setIntroduction_teacher(String introduction_teacher) {
        this.introduction_teacher = introduction_teacher;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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

    public int getMax_person() {
        return max_person;
    }

    public void setMax_person(int max_person) {
        this.max_person = max_person;
    }

    public int getAll_hours() {
        return all_hours;
    }

    public void setAll_hours(int all_hours) {
        this.all_hours = all_hours;
    }

    public int getAll_charge() {
        return all_charge;
    }

    public void setAll_charge(int all_charge) {
        this.all_charge = all_charge;
    }

    public String getCourse_content() {
        return course_content;
    }

    public void setCourse_content(String course_content) {
        this.course_content = course_content;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }
}
