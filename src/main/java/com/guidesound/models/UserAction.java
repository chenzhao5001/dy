package com.guidesound.models;

public class UserAction {
    int id;
    int from_user_id;
    String from_user_name;
    String from_user_head;
    int to_user_id;
    String to_user_name;
    String to_user_head;
    int type;
    int content_id;
    String content_url;
    String first_comment;
    String second_comment;
    int create_time;

    public String getFrom_user_head() {
        return from_user_head;
    }

    public void setFrom_user_head(String from_user_head) {
        this.from_user_head = from_user_head;
    }

    public String getTo_user_head() {
        return to_user_head;
    }

    public void setTo_user_head(String to_user_head) {
        this.to_user_head = to_user_head;
    }

    public String getFirst_comment() {
        return first_comment;
    }

    public void setFirst_comment(String first_comment) {
        this.first_comment = first_comment;
    }

    public String getSecond_comment() {
        return second_comment;
    }

    public void setSecond_comment(String second_comment) {
        this.second_comment = second_comment;
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

    public String getFrom_user_name() {
        return from_user_name;
    }

    public void setFrom_user_name(String from_user_name) {
        this.from_user_name = from_user_name;
    }

    public String getTo_user_name() {
        return to_user_name;
    }

    public void setTo_user_name(String to_user_name) {
        this.to_user_name = to_user_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrom_user_id() {
        return from_user_id;
    }

    public void setFrom_user_id(int from_user_id) {
        this.from_user_id = from_user_id;
    }

    public int getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(int to_user_id) {
        this.to_user_id = to_user_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }
}
