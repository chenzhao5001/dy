package com.guidesound.models;

public class VideoPools {
    int id;
    int user_id;
    int subject;
    int video_id;
    int video_pool;
    int pool_flag;
    int subject_flag;
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

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public int getVideo_pool() {
        return video_pool;
    }

    public void setVideo_pool(int video_pool) {
        this.video_pool = video_pool;
    }

    public int getPool_flag() {
        return pool_flag;
    }

    public void setPool_flag(int pool_flag) {
        this.pool_flag = pool_flag;
    }

    public int getSubject_flag() {
        return subject_flag;
    }

    public void setSubject_flag(int subject_flag) {
        this.subject_flag = subject_flag;
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
