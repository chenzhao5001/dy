package com.guidesound.models;

public class ArticleInfo {

    int id;
    int user_id;
    String head;
    String head_pic1;
    String head_pic2;
    String head_pic3;
    int subject;
    String subject_name;
    int type;


    String user_head;
    String user_name;
    int user_type;

    private int user_subject;
    private String user_subject_name;
    private int user_grade;
    private String user_grade_name;
    private int user_grade_level;
    private String user_grade_level_name;
    private String content_url;


    int comment_count;
//    int collection_count;
//    int priase_count;

    int create_time;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

    public int getUser_subject() {
        return user_subject;
    }

    public void setUser_subject(int user_subject) {
        this.user_subject = user_subject;
    }

    public String getUser_subject_name() {
        return user_subject_name;
    }

    public void setUser_subject_name(String user_subject_name) {
        this.user_subject_name = user_subject_name;
    }

    public int getUser_grade() {
        return user_grade;
    }

    public void setUser_grade(int user_grade) {
        this.user_grade = user_grade;
    }

    public String getUser_grade_name() {
        return user_grade_name;
    }

    public void setUser_grade_name(String user_grade_name) {
        this.user_grade_name = user_grade_name;
    }

    public int getUser_grade_level() {
        return user_grade_level;
    }

    public void setUser_grade_level(int user_grade_level) {
        this.user_grade_level = user_grade_level;
    }

    public String getUser_grade_level_name() {
        return user_grade_level_name;
    }

    public void setUser_grade_level_name(String user_grade_level_name) {
        this.user_grade_level_name = user_grade_level_name;
    }

    public String getUser_head() {
        return user_head;
    }

    public void setUser_head(String user_head) {
        this.user_head = user_head;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getHead_pic1() {
        return head_pic1;
    }

    public void setHead_pic1(String head_pic1) {
        this.head_pic1 = head_pic1;
    }

    public String getHead_pic2() {
        return head_pic2;
    }

    public void setHead_pic2(String head_pic2) {
        this.head_pic2 = head_pic2;
    }

    public String getHead_pic3() {
        return head_pic3;
    }

    public void setHead_pic3(String head_pic3) {
        this.head_pic3 = head_pic3;
    }

//    public int getCollection_count() {
//        return collection_count;
//    }
//
//    public void setCollection_count(int collection_count) {
//        this.collection_count = collection_count;
//    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

//    public int getPriase_count() {
//        return priase_count;
//    }
//
//    public void setPriase_count(int priase_count) {
//        this.priase_count = priase_count;
//    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

}
