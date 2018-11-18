package com.guidesound.models;

public class VideoInfo {

    int id;
    int user_id;
    String title;
    int subject;
    int  watch_type;
    int duration;
    String resolution;
    String pic_up_path;
    String video_up_path;


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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public int getWatch_type() {
        return watch_type;
    }

    public void setWatch_type(int watch_type) {
        this.watch_type = watch_type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getPic_up_path() {
        return pic_up_path;
    }

    public void setPic_up_path(String pic_up_path) {
        this.pic_up_path = pic_up_path;
    }

    public String getVideo_up_path() {
        return video_up_path;
    }

    public void setVideo_up_path(String video_up_path) {
        this.video_up_path = video_up_path;
    }
}
