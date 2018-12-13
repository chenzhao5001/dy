package com.guidesound.models;

public class UserPlayFinish {
    int id;
    String user_guid;
    String finish_videos;
    String create_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_guid() {
        return user_guid;
    }

    public void setUser_guid(String user_guid) {
        this.user_guid = user_guid;
    }

    public String getFinish_videos() {
        return finish_videos;
    }

    public void setFinish_videos(String finish_videos) {
        this.finish_videos = finish_videos;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
