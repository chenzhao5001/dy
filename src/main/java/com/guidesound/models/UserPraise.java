package com.guidesound.models;

public class UserPraise {
    int id;
    int user_id;
    int praise_user_id;
    int create_time;
    int update_time;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPraise_user_id() {
        return praise_user_id;
    }

    public void setPraise_user_id(int praise_user_id) {
        this.praise_user_id = praise_user_id;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public int getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(int update_time) {
        this.update_time = update_time;
    }
}
