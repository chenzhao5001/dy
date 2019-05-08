package com.guidesound.TempStruct;

public class InfoMsg {
    int msg_type;
    int type;
    int id;
    String name;
    String head;
    String grade;
    String subject;
    String user_name;

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public String toString() {
        return "{" +
                "msg_type=" + msg_type +
                ", type=" + type +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", head='" + head + '\'' +
                ", grade='" + grade + '\'' +
                ", subject='" + subject + '\'' +
                ", user_name='" + user_name + '\'' +
                '}';
    }
}
