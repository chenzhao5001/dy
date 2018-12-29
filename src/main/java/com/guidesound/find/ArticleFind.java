package com.guidesound.find;

public class ArticleFind {

    String head;
    int subject;
    int begin;
    int end;
    int type;
    String user_id;
    boolean ower_flag;

    public boolean isOwer_flag() {
        return ower_flag;
    }

    public void setOwer_flag(boolean ower_flag) {
        this.ower_flag = ower_flag;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
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
