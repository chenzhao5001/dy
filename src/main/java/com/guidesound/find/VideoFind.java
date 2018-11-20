package com.guidesound.find;

import java.util.List;

public class VideoFind {
    String title;
    int sType;
    String status;
    int begin;
    int end;

    List<String> subject_list;
    List<String> grade_class_list;

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
