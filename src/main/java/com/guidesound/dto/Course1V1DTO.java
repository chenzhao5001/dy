package com.guidesound.dto;

import javax.validation.constraints.NotNull;

public class Course1V1DTO
{
    @NotNull(message = "course_id:不能为空;")
    String course_id;
    @NotNull(message = "course_pic:不能为空;")
    String course_pic;
    @NotNull(message = "course_name:不能为空;")
    String course_name;
    @NotNull(message = "subject:不能为空;")
    String subject;
    @NotNull(message = "grade:不能为空;")
    String grade;
    @NotNull(message = "form:不能为空;")
    String form;
    @NotNull(message = "price_one_hour:不能为空;")
    String price_one_hour;
    @NotNull(message = "area_service:不能为空;")
    String area_service;
    @NotNull(message = "test_form:不能为空;")
    String test_form;
    @NotNull(message = "test_duration:不能为空;")
    String test_duration;
    @NotNull(message = "test_charge:不能为空;")
    String test_charge;
    @NotNull(message = "introduction_teacher:不能为空;")
    String introduction_teacher;
    @NotNull(message = "teacher_id:不能为空;")
    String teacher_id;
    @NotNull(message = "teacher_name:不能为空;")
    String teacher_name;
    @NotNull(message = "save:不能为空;")
    String save;
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

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
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

    public String getPrice_one_hour() {
        return price_one_hour;
    }

    public void setPrice_one_hour(String price_one_hour) {
        this.price_one_hour = price_one_hour;
    }

    public String getArea_service() {
        return area_service;
    }

    public void setArea_service(String area_service) {
        this.area_service = area_service;
    }

    public String getTest_form() {
        return test_form;
    }

    public void setTest_form(String test_form) {
        this.test_form = test_form;
    }

    public String getTest_duration() {
        return test_duration;
    }

    public void setTest_duration(String test_duration) {
        this.test_duration = test_duration;
    }

    public String getTest_charge() {
        return test_charge;
    }

    public void setTest_charge(String test_charge) {
        this.test_charge = test_charge;
    }

    public String getIntroduction_teacher() {
        return introduction_teacher;
    }

    public void setIntroduction_teacher(String introduction_teacher) {
        this.introduction_teacher = introduction_teacher;
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

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }
}
