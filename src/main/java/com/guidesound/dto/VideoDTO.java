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

    @NotNull(message = "resolution_w:不能为空;")
    String resolution_w;

    @NotNull(message = "resolution_h:不能为空;")
    String resolution_h;

    @NotNull(message = "pic_cut_url:不能为空;")
    String pic_cut_url;

//    @NotNull(message = "attachment_type:不能为空;")
    String attachment_type = "0";
//    @NotNull(message = "attachment_id:不能为空;")
    String attachment_id = "0";
//    @NotNull(message = "attachment_name:不能为空;")
    String attachment_name = "";

    String attachment_subtype = "0";


    public String getPic_cut_url() {
        return pic_cut_url;
    }

    public void setPic_cut_url(String pic_cut_url) {
        this.pic_cut_url = pic_cut_url;
    }

    public String getResolution_w() {
        return resolution_w;
    }

    public void setResolution_w(String resolution_w) {
        this.resolution_w = resolution_w;
    }

    public String getResolution_h() {
        return resolution_h;
    }

    public void setResolution_h(String resolution_h) {
        this.resolution_h = resolution_h;
    }

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

    public String getAttachment_type() {
        return attachment_type;
    }

    public void setAttachment_type(String attachment_type) {
        this.attachment_type = attachment_type;
    }

    public String getAttachment_id() {
        return attachment_id;
    }

    public void setAttachment_id(String attachment_id) {
        this.attachment_id = attachment_id;
    }

    public String getAttachment_name() {
        return attachment_name;
    }

    public void setAttachment_name(String attachment_name) {
        this.attachment_name = attachment_name;
    }

    public String getAttachment_subtype() {
        return attachment_subtype;
    }

    public void setAttachment_subtype(String attachment_subtype) {
        this.attachment_subtype = attachment_subtype;
    }
}
