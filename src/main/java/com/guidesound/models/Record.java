package com.guidesound.models;

public class Record {

    int record_course_id;
    int record_course_status;
    String record_course_pic;
    String record_course_name;
    Object subject;
    int subject_id;
    Object grade;
    int grade_id;
    int video_count;
    int price;
    int user_id;
    int student_count;
    int group_id;


    String intro_teacher_text;
    Object intro_teacher_pic;
    String intro_course_text;
    Object intro_course_pic;
    Object videos;
    int save;
    boolean is_pay;

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public boolean isIs_pay() {
        return is_pay;
    }

    public void setIs_pay(boolean is_pay) {
        this.is_pay = is_pay;
    }

    public int getStudent_count() {
        return student_count;
    }

    public void setStudent_count(int student_count) {
        this.student_count = student_count;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRecord_course_id() {
        return record_course_id;
    }

    public void setRecord_course_id(int record_course_id) {
        this.record_course_id = record_course_id;
    }

    public int getRecord_course_status() {
        return record_course_status;
    }

    public void setRecord_course_status(int record_course_status) {
        this.record_course_status = record_course_status;
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

    public Object getSubject() {
        return subject;
    }

    public void setSubject(Object subject) {
        this.subject = subject;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public Object getGrade() {
        return grade;
    }

    public void setGrade(Object grade) {
        this.grade = grade;
    }

    public int getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(int grade_id) {
        this.grade_id = grade_id;
    }

    public int getVideo_count() {
        return video_count;
    }

    public void setVideo_count(int video_count) {
        this.video_count = video_count;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getIntro_teacher_text() {
        return intro_teacher_text;
    }

    public void setIntro_teacher_text(String intro_teacher_text) {
        this.intro_teacher_text = intro_teacher_text;
    }

    public Object getIntro_teacher_pic() {
        return intro_teacher_pic;
    }

    public void setIntro_teacher_pic(Object intro_teacher_pic) {
        this.intro_teacher_pic = intro_teacher_pic;
    }

    public String getIntro_course_text() {
        return intro_course_text;
    }

    public void setIntro_course_text(String intro_course_text) {
        this.intro_course_text = intro_course_text;
    }

    public Object getIntro_course_pic() {
        return intro_course_pic;
    }

    public void setIntro_course_pic(Object intro_course_pic) {
        this.intro_course_pic = intro_course_pic;
    }

    public Object getVideos() {
        return videos;
    }

    public void setVideos(Object videos) {
        this.videos = videos;
    }

    public int getSave() {
        return save;
    }

    public void setSave(int save) {
        this.save = save;
    }
}
