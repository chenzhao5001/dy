package com.guidesound.find;

import java.util.List;

public class VideoFind {
    String title;
    int sType;
    String status;
    int begin;
    int end;
    String user_id;
    String video_id;
    List<String> pools;

    List<String> subject_list;
    List<String> grade_class_list;
    List<Integer> user_ids;
    List<Integer> course_type;

    public List<Integer> getCourse_type() {
        return course_type;
    }

    public void setCourse_type(List<Integer> course_type) {
        this.course_type = course_type;
    }

    public List<String> getPools() {
        return pools;
    }

    public void setPools(List<String> pools) {
        this.pools = pools;
    }

    public List<Integer> getUser_ids() {
        return user_ids;
    }

    public void setUser_ids(List<Integer> user_ids) {
        this.user_ids = user_ids;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public List<String> getSubject_list() {
        return subject_list;
    }

    public void setSubject_list(List<String> subject_list) {
        this.subject_list = subject_list;
    }

    public List<String> getGrade_class_list() {
        return grade_class_list;
    }

    public void setGrade_class_list(List<String> grade_class_list) {
        this.grade_class_list = grade_class_list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String content) {
        this.title = content;
    }

    public int getsType() {
        return sType;
    }

    public void setsType(int sType) {
        this.sType = sType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
