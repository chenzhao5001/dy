package com.guidesound.models;

/**
 * 用户关注表
 */

public class UserFollow {
    int id;
    int user_id;
    int follow_user_id;
    int deleted;
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

    public int getFollow_user_id() {
        return follow_user_id;
    }

    public void setFollow_user_id(int follow_user_id) {
        this.follow_user_id = follow_user_id;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
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
