package com.guidesound.find;

import java.util.List;

public class ArticleFind {

    String head;
    int sType;
    String status;
    int begin;
    int end;
    String user_id;
    String article_id;
    List<String> pools;
    String type;

    List<String> subject_list;
    List<String> grade_class_list;
    List<Integer> user_ids;
    List<Integer> course_type;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public List<String> getPools() {
        return pools;
    }

    public void setPools(List<String> pools) {
        this.pools = pools;
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

    public List<Integer> getUser_ids() {
        return user_ids;
    }

    public void setUser_ids(List<Integer> user_ids) {
        this.user_ids = user_ids;
    }

    public List<Integer> getCourse_type() {
        return course_type;
    }

    public void setCourse_type(List<Integer> course_type) {
        this.course_type = course_type;
    }
}
