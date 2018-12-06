package com.guidesound.models;

import com.guidesound.TempStruct.ShowInfoTemp;
import com.guidesound.TempStruct.UserInfoTemp;
import com.guidesound.TempStruct.VideoInfoTemp;

public class VideoVerify {

    UserInfoTemp userInfo = new UserInfoTemp();
    VideoInfoTemp videoInfo = new VideoInfoTemp();
    ShowInfoTemp showInfo = new ShowInfoTemp();
    int video_id;

    public void setVideo_title(String title) {
        videoInfo.video_title = title;
    }
    public void setVideo_duration(String duration) {
        videoInfo.video_duration = duration;
    }
    public void setVideo_pic_up_path(String pic_path) {
        showInfo.video_pic_up_path = pic_path;
    }
    public void setVideo_video_up_path(String video_path) {
        showInfo.video_video_up_path = video_path;
    }
    public void setVideo_resolution(String resolution) {
        videoInfo.video_resolution = resolution;
    }
    public void setVideo_subject(String subject) {
        videoInfo.video_subject = subject;
    }
    public void setVideo_watch_type(String watch_type) {
        videoInfo.video_watch_type = watch_type;
    }


    public void setUser_type(String type) {
        userInfo.user_type = type;
    }
    public void setUser_extend(String extend) {
        userInfo.user_extend = extend;
    }
    public void setUser_grade_level(String level) {
        userInfo.user_grade_level = level;
    }
    public void setUser_level(String level) {
        userInfo.user_level = level;
    }
    public void setUser_name(String name) {
        userInfo.user_name = name;
    }
    public void setUser_subject(String subject) {
        userInfo.user_subject = subject;
    }

    public UserInfoTemp getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoTemp userInfo) {
        this.userInfo = userInfo;
    }

    public VideoInfoTemp getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(VideoInfoTemp videoInfo) {
        this.videoInfo = videoInfo;
    }

    public ShowInfoTemp getShowInfo() {
        return showInfo;
    }

    public void setShowInfo(ShowInfoTemp showInfo) {
        this.showInfo = showInfo;
    }

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }
}
