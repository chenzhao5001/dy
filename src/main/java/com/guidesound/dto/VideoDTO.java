package com.guidesound.dto;

import javax.validation.constraints.NotNull;

public class VideoDTO {

    @NotNull(message = "title:不能为空;")
    String title;
    @NotNull(message = "subject:不能为空;")
    String subject;
    @NotNull(message = "watch_type:不能为空;")
    String watch_type;
    @NotNull(message = "viedo_url:不能为空;")
    String viedo_url;
    @NotNull(message = "picture_url:不能为空;")
    String picture_url;
    @NotNull(message = "content:不能为空;")
    String content;
    @NotNull(message = "duration:不能为空;")
    String duration;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getWatch_type() {
        return watch_type;
    }

    public void setWatch_type(String watch_type) {
        this.watch_type = watch_type;
    }

    public String getViedo_url() {
        return viedo_url;
    }

    public void setViedo_url(String viedo_url) {
        this.viedo_url = viedo_url;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
