package com.guidesound.dto;

import javax.validation.constraints.NotNull;

public class OrderClassDTO {

    @NotNull(message = "course_id:不能为空;")
    String course_id;
    @NotNull(message = "course_name:不能为空;")
    String course_name;
    @NotNull(message = "course_pic:不能为空;")
    String course_pic;
    @NotNull(message = "teacher_id:不能为空;")
    String teacher_id;
    @NotNull(message = "teacher_name:不能为空;")
    String teacher_name;
    @NotNull(message = "student_id:不能为空;")
    String student_id;
    @NotNull(message = "student_name:不能为空;")
    String student_name;
    @NotNull(message = "subject:不能为空;")
    String subject;
    @NotNull(message = "grade:不能为空;")
    String grade;
    @NotNull(message = "form:不能为空;")
    String form;
    @NotNull(message = "way:不能为空;")
    String way;
    @NotNull(message = "max_person:不能为空;")
    String max_person;
    @NotNull(message = "all_hours:不能为空;")
    String all_hours;
    @NotNull(message = "price_one_hour:不能为空;")
    String price_one_hour;
    @NotNull(message = "all_charge:不能为空;")
    String all_charge;
    @NotNull(message = "refund_rule:不能为空;")
    String refund_rule;
    @NotNull(message = "tutor_content:不能为空;")
    String tutor_content;
    @NotNull(message = "outline:不能为空;")
    String outline;

    int type;
    int create_time;
    int user_id;
    int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_pic() {
        return course_pic;
    }

    public void setCourse_pic(String course_pic) {
        this.course_pic = course_pic;
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

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
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

    public String getMax_person() {
        return max_person;
    }

    public void setMax_person(String max_person) {
        this.max_person = max_person;
    }

    public String getAll_hours() {
        return all_hours;
    }

    public void setAll_hours(String all_hours) {
        this.all_hours = all_hours;
    }

    public String getPrice_one_hour() {
        return price_one_hour;
    }

    public void setPrice_one_hour(String price_one_hour) {
        this.price_one_hour = price_one_hour;
    }

    public String getAll_charge() {
        return all_charge;
    }

    public void setAll_charge(String all_charge) {
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

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }
}
