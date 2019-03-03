package com.guidesound.dto;
import javax.validation.constraints.NotNull;

public class CourseClassDTO {
    @NotNull(message = "course_id:不能为空;")
    String course_id = "0";
    @NotNull(message = "course_pic:不能为空;")
    String course_pic = "";
    @NotNull(message = "course_name:不能为空;")
    String course_name = "";
    @NotNull(message = "subject:不能为空;")
    String subject = "";
    @NotNull(message = "grade:不能为空;")
    String grade = "0";
    @NotNull(message = "form:不能为空;")
    String form = "0";
    @NotNull(message = "max_person:不能为空;")
    String max_person = "0";
    @NotNull(message = "all_hours:不能为空;")
    String all_hours = "0";
    @NotNull(message = "all_charge:不能为空;")
    String all_charge = "0";
    @NotNull(message = "test_form:不能为空;")
    String test_form = "0";
    @NotNull(message = "test_duration:不能为空;")
    String test_duration = "0";
    @NotNull(message = "test_charge:不能为空;")
    String test_charge = "0";
    @NotNull(message = "course_content:不能为空;")
    String course_content = "";
    @NotNull(message = "outline:不能为空;")
    String outline = "";
    @NotNull(message = "introduction_teacher:不能为空;")
    String introduction_teacher = "";
    @NotNull(message = "teacher_id:不能为空;")
    String teacher_id = "0";
    @NotNull(message = "teacher_name:不能为空;")
    String teacher_name = "";

    @NotNull(message = "save:不能为空;")
    String save = "0";

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
