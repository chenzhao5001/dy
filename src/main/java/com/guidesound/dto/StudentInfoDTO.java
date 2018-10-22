package com.guidesound.dto;

import javax.validation.constraints.NotNull;

public class StudentInfoDTO {

    @NotNull(message = "level:不能为空;")
    String level;
    @NotNull(message = "grade:不能为空;")
    String grade;
    @NotNull(message = "sex:不能为空;")
    String sex;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
