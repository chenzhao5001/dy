package com.guidesound.dto;

import javax.validation.constraints.NotNull;

public class Course1V1DTO
{
    @NotNull(message = "course_name:不能为空;")
    String course_name;
    @NotNull(message = "grade_level:不能为空;")
    String grade_level;
    @NotNull(message = "method:不能为空;")
    String method;
    @NotNull(message = "price:不能为空;")
    String price;
    @NotNull(message = "area:不能为空;")
    String area;
    @NotNull(message = "course_pic:不能为空;")
    String course_pic;
    @NotNull(message = "test_method:不能为空;")
    String test_method;
    @NotNull(message = "test_duration:不能为空;")
    String test_duration;
    @NotNull(message = "test_price:不能为空;")
    String test_price;

    int create_time;
    int user_id;

    public String getGrade_level() {
        return grade_level;
    }

    public void setGrade_level(String grade_level) {
        this.grade_level = grade_level;
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

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getGrade() {
        return grade_level;
    }

    public void setGrade(String grade) {
        this.grade_level = grade;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTest_method() {
        return test_method;
    }

    public void setTest_method(String test_method) {
        this.test_method = test_method;
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
