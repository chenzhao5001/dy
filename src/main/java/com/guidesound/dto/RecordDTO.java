package com.guidesound.dto;

import javax.validation.constraints.NotNull;

public class RecordDTO {
    @NotNull(message = "record_course_id:不能为空;")
    String record_course_id;
    @NotNull(message = "record_course_pic:不能为空;")
    String record_course_pic;
    @NotNull(message = "record_course_name:不能为空;")
    String record_course_name;
    @NotNull(message = "subject:不能为空;")
    String subject;
    @NotNull(message = "grade:不能为空;")
    String grade;
    @NotNull(message = "video_count:不能为空;")
    String video_count;
    @NotNull(message = "price:不能为空;")
    String price;
    @NotNull(message = "intro_teacher_text:不能为空;")
    String intro_teacher_text;
    @NotNull(message = "intro_teacher_pic:不能为空;")
    String intro_teacher_pic;
    @NotNull(message = "intro_course_text:不能为空;")
    String intro_course_text;
    @NotNull(message = "intro_course_pic:不能为空;")
    String intro_course_pic;
    @NotNull(message = "videos:不能为空;")
    String videos;
    @NotNull(message = "save:不能为空;")
    String save;

    public String getRecord_course_id() {
        return record_course_id;
    }

    public void setRecord_course_id(String record_course_id) {
        this.record_course_id = record_course_id;
    }

    public String getRecord_course_pic() {
        return record_course_pic;
    }

    public void setRecord_course_pic(String record_course_pic) {
        this.record_course_pic = record_course_pic;
    }

    public String getRecord_course_name() {
        return record_course_name;
    }

    public void setRecord_course_name(String record_course_name) {
        this.record_course_name = record_course_name;
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

    public String getVideo_count() {
        return video_count;
    }

    public void setVideo_count(String video_count) {
        this.video_count = video_count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIntro_teacher_text() {
        return intro_teacher_text;
    }

    public void setIntro_teacher_text(String intro_teacher_text) {
        this.intro_teacher_text = intro_teacher_text;
    }

    public String getIntro_teacher_pic() {
        return intro_teacher_pic;
    }

    public void setIntro_teacher_pic(String intro_teacher_pic) {
        this.intro_teacher_pic = intro_teacher_pic;
    }

    public String getIntro_course_text() {
        return intro_course_text;
    }

    public void setIntro_course_text(String intro_course_text) {
        this.intro_course_text = intro_course_text;
    }

    public String getIntro_course_pic() {
        return intro_course_pic;
    }

    public void setIntro_course_pic(String intro_course_pic) {
        this.intro_course_pic = intro_course_pic;
    }

    public String getVideos() {
        return videos;
    }

    public void setVideos(String videos) {
        this.videos = videos;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }
}
