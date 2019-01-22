package com.guidesound.dto;

import javax.validation.constraints.NotNull;

public class CourseClassDTO {
    @NotNull(message = "course_name:不能为空;")
    String course_name;
    @NotNull(message = "grade_level:不能为空;")
    String grade_level;

    @NotNull(message = "course_pic:不能为空;")
    String course_pic;
    @NotNull(message = "max_person:不能为空;")
    String max_person;
    @NotNull(message = "all_duration:不能为空;")
    String all_duration;
    @NotNull(message = "all_price:不能为空;")
    String all_price;

    @NotNull(message = "test_time:不能为空;")
    String test_time;
    @NotNull(message = "test_duration:不能为空;")
    String test_duration;
    @NotNull(message = "test_price:不能为空;")
    String test_price;
    @NotNull(message = "course_introduce:不能为空;")
    String course_introduce;

    int create_time;
    int user_id;

    public String getCourse_introduce() {
        return course_introduce;
    }

    public void setCourse_introduce(String course_introduce) {
        this.course_introduce = course_introduce;
    }

    public String getCourse_pic() {
        return course_pic;
    }

    public void setCourse_pic(String course_pic) {
        this.course_pic = course_pic;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getGrade_level() {
        return grade_level;
    }

    public void setGrade_level(String grade_level) {
        this.grade_level = grade_level;
    }

    public String getMax_person() {
        return max_person;
    }

    public void setMax_person(String max_person) {
        this.max_person = max_person;
    }

    public String getAll_duration() {
        return all_duration;
    }

    public void setAll_duration(String all_duration) {
        this.all_duration = all_duration;
    }

    public String getAll_price() {
        return all_price;
    }

    public void setAll_price(String all_price) {
        this.all_price = all_price;
    }

    public String getTest_time() {
        return test_time;
    }

    public void setTest_time(String test_time) {
        this.test_time = test_time;
    }

    public String getTest_duration() {
        return test_duration;
    }

    public void setTest_duration(String test_duration) {
        this.test_duration = test_duration;
    }

    public String getTest_price() {
        return test_price;
    }

    public void setTest_price(String test_price) {
        this.test_price = test_price;
    }
}
