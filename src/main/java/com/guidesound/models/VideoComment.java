package com.guidesound.models;

public class VideoComment {
    int id;
    int video_id;
    int first_user_id;
    String first_comment;
    String first_user_head;
    String first_user_name;

    int second_user_id;
    String second_comment;
    String second_user_head;
    String second_user_name;
    int create_time;
    int praise_count;
    boolean praise;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public int getFirst_user_id() {
        return first_user_id;
    }

    public void setFirst_user_id(int first_user_id) {
        this.first_user_id = first_user_id;
    }

    public String getFirst_comment() {
        return first_comment;
    }

    public void setFirst_comment(String first_comment) {
        this.first_comment = first_comment;
    }

    public String getFirst_user_head() {
        return first_user_head;
    }

    public void setFirst_user_head(String first_user_head) {
        this.first_user_head = first_user_head;
    }

    public String getFirst_user_name() {
        return first_user_name;
    }

    public void setFirst_user_name(String first_user_name) {
        this.first_user_name = first_user_name;
    }

    public int getSecond_user_id() {
        return second_user_id;
    }

    public void setSecond_user_id(int second_user_id) {
        this.second_user_id = second_user_id;
    }

    public String getSecond_comment() {
        return second_comment;
    }

    public void setSecond_comment(String second_comment) {
        this.second_comment = second_comment;
    }

    public String getSecond_user_head() {
        return second_user_head;
    }

    public void setSecond_user_head(String second_user_head) {
        this.second_user_head = second_user_head;
    }

    public String getSecond_user_name() {
        return second_user_name;
    }

    public void setSecond_user_name(String second_user_name) {
        this.second_user_name = second_user_name;
    }

    public int getPraise_count() {
        return praise_count;
    }

    public void setPraise_count(int praise_count) {
        this.praise_count = praise_count;
    }

    public boolean isPraise() {
        return praise;
    }

    public void setPraise(boolean praise) {
        this.praise = praise;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }
}
