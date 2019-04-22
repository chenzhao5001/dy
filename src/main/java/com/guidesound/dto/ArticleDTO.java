package com.guidesound.dto;

import javax.validation.constraints.NotNull;

public class ArticleDTO {

    @NotNull(message = "head:不能为空;")
    String head;

    public String head_pic1;
    public String head_pic2;
    public String head_pic3;
    @NotNull(message = "content:不能为空;")
    String content;

//    @NotNull(message = "subject_class:不能为空;")
    public String subject_class;
    @NotNull(message = "subject:不能为空;")
    String subject;
//    @NotNull(message = "grade_class:不能为空;")
    public String grade_class;
    @NotNull(message = "grade:不能为空;")
    String grade;


    int user_id;
    int create_time;
    int article_id;

    public Integer attachment_type;
    public Integer attachment_id;
    public String attachment_name;
    public Integer attachment_subtype;

    public int getAttachment_type() {
        return attachment_type;
    }

    public void setAttachment_type(int attachment_type) {
        this.attachment_type = attachment_type;
    }

    public int getAttachment_id() {
        return attachment_id;
    }

    public void setAttachment_id(int attachment_id) {
        this.attachment_id = attachment_id;
    }

    public String getAttachment_name() {
        return attachment_name;
    }

    public void setAttachment_name(String attachment_name) {
        this.attachment_name = attachment_name;
    }

    public int getAttachment_subtype() {
        return attachment_subtype;
    }

    public void setAttachment_subtype(int attachment_subtype) {
        this.attachment_subtype = attachment_subtype;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

    public String getSubject_class() {
        return subject_class;
    }

    public void setSubject_class(String subject_class) {
        this.subject_class = subject_class;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGrade_class() {
        return grade_class;
    }

    public void setGrade_class(String grade_class) {
        this.grade_class = grade_class;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
