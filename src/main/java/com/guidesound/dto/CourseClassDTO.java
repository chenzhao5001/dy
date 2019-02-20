package com.guidesound.dto;
import javax.validation.constraints.NotNull;

public class CourseClassDTO {
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
    @NotNull(message = "max_person:不能为空;")
    String max_person;
    @NotNull(message = "all_hours:不能为空;")
    String all_hours;
    @NotNull(message = "all_charge:不能为空;")
    String all_charge;
    @NotNull(message = "test_form:不能为空;")
    String test_form;
    @NotNull(message = "test_duration:不能为空;")
    String test_duration;
    @NotNull(message = "test_charge:不能为空;")
    String test_charge;
    @NotNull(message = "course_content:不能为空;")
    String course_content;
    @NotNull(message = "outline:不能为空;")
    String outline;
    @NotNull(message = "introduction_teacher:不能为空;")
    String introduction_teacher;

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

    public String getAll_charge() {
        return all_charge;
    }

    public void setAll_charge(String all_charge) {
        this.all_charge = all_charge;
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

    public String getIntroduction_teacher() {
        return introduction_teacher;
    }

    public void setIntroduction_teacher(String introduction_teacher) {
        this.introduction_teacher = introduction_teacher;
    }
}
