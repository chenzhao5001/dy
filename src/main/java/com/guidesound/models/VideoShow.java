package com.guidesound.models;

public class VideoShow {

    int id;
    int user_id;
    String title;
    int subject;
    String subject_name;

    int watch_type;
    String watch_type_name;

    int duration;

    String content;
    String pic_up_path;
    String video_show_path;
    int play_count;
    int praise_count;
    int chat_count;
    String user_head;
    boolean follow;
    int create_time;



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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPic_up_path() {
        return pic_up_path;
    }

    public void setPic_up_path(String pic_up_path) {
        this.pic_up_path = pic_up_path;
    }

    public String getVideo_show_path() {
        return video_show_path;
    }

    public void setVideo_show_path(String video_show_path) {
        this.video_show_path = video_show_path;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public int getPlay_count() {
        return play_count;
    }

    public void setPlay_count(int play_count) {
        this.play_count = play_count;
    }

    public int getPraise_count() {
        return praise_count;
    }

    public void setPraise_count(int praise_count) {
        this.praise_count = praise_count;
    }

    public int getChat_count() {
        return chat_count;
    }

    public void setChat_count(int chat_count) {
        this.chat_count = chat_count;
    }

    public String getUser_head() {
        return user_head;
    }

    public void setUser_head(String user_head) {
        this.user_head = user_head;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }


    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getWatch_type_name() {
        return watch_type_name;
    }

    public void setWatch_type_name(String watch_type_name) {
        this.watch_type_name = watch_type_name;
    }

}
