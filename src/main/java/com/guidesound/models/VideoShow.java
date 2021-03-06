package com.guidesound.models;

import com.guidesound.ret.CommodityInfo;
import com.guidesound.ret.TeacherClass;
import com.guidesound.ret.VideoClass;

public class VideoShow {

    int id;
    int user_id;
    String user_name;
    String title;
    int subject;
    String subject_name;

    int watch_type;
    String watch_type_name;
    int examine_status;

    int duration;
    int resolution_w;
    int resolution_h;
    int shared_count;

    String content;
    String pic_up_path;
    String video_show_path;
    String video_up_path;
    String pic_cut_path;
    int play_count;
    boolean praise;
    int praise_count;
    int chat_count;
    String user_head;
    boolean collection;
    int collection_count;
    boolean follow;
    int user_type;
    String user_type_name;
    String user_subject;
    String user_grade;
    String user_level;
    int out_relay;
    int inner_relay;
    int group;
    String video_state;
    Object pools;
    int rec_count;

    String type_list;
    String examine_reason;
    int create_time;
    String fail_content;

    int attachment_type;
    int attachment_id;
    String attachment_name;
    int attachment_subtype;

    VideoClass video_class;
    TeacherClass teacher_class;
    CommodityInfo commodity;

    public VideoClass getVideo_class() {
        return video_class;
    }

    public void setVideo_class(VideoClass video_class) {
        this.video_class = video_class;
    }

    public TeacherClass getTeacher_class() {
        return teacher_class;
    }

    public void setTeacher_class(TeacherClass teacher_class) {
        this.teacher_class = teacher_class;
    }

    public CommodityInfo getCommodity() {
        return commodity;
    }

    public void setCommodity(CommodityInfo commodity) {
        this.commodity = commodity;
    }

    public int getAttachment_subtype() {
        return attachment_subtype;
    }

    public void setAttachment_subtype(int attachment_subtype) {
        this.attachment_subtype = attachment_subtype;
    }

    public int getAttachment_type() {
        return attachment_type;
    }

    public void setAttachment_type(int attachment_type) {
        this.attachment_type = attachment_type;
    }

    public int getAttachment_id() {
        return attachment_id;
    }

    public void setAttachment_id(int attachment_id) {
        this.attachment_id = attachment_id;
    }

    public String getFail_content() {
        return fail_content;
    }

    public void setFail_content(String fail_content) {
        this.fail_content = fail_content;
    }

    public String getType_list() {
        return type_list;
    }
    public void setType_list(String type_list) {
        this.type_list = type_list;
    }

    public String getExamine_reason() {
        return examine_reason;
    }

    public void setExamine_reason(String examine_reason) {
        this.examine_reason = examine_reason;
    }

    public int getRec_count() {
        return rec_count;
    }

    public void setRec_count(int rec_count) {
        this.rec_count = rec_count;
    }

    public int getExamine_status() {
        return examine_status;
    }

    public void setExamine_status(int examine_status) {
        this.examine_status = examine_status;
    }

    public int getResolution_w() {
        return resolution_w;
    }

    public void setResolution_w(int resolution_w) {
        this.resolution_w = resolution_w;
    }

    public int getResolution_h() {
        return resolution_h;
    }

    public void setResolution_h(int resolution_h) {
        this.resolution_h = resolution_h;
    }


    public Object getPools() {
        return pools;
    }

    public void setPools(Object pools) {
        this.pools = pools;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getVideo_state() {
        return video_state;
    }

    public void setVideo_state(String video_state) {
        this.video_state = video_state;
    }

    public int getOut_relay() {
        return out_relay;
    }

    public void setOut_relay(int out_relay) {
        this.out_relay = out_relay;
    }

    public int getInner_relay() {
        return inner_relay;
    }

    public void setInner_relay(int inner_relay) {
        this.inner_relay = inner_relay;
    }

    public String getUser_subject() {
        return user_subject;
    }

    public void setUser_subject(String user_subject) {
        this.user_subject = user_subject;
    }

    public String getUser_grade() {
        return user_grade;
    }

    public void setUser_grade(String user_grade) {
        this.user_grade = user_grade;
    }

    public String getUser_level() {
        return user_level;
    }

    public void setUser_level(String user_level) {
        this.user_level = user_level;
    }

    public String getUser_type_name() {
        return user_type_name;
    }

    public void setUser_type_name(String user_type_name) {
        this.user_type_name = user_type_name;
    }

    public String getPic_cut_path() {
        return pic_cut_path;
    }

    public void setPic_cut_path(String pic_cut_path) {
        this.pic_cut_path = pic_cut_path;
    }

    public String getVideo_up_path() {
        return video_up_path;
    }

    public void setVideo_up_path(String video_up_path) {
        this.video_up_path = video_up_path;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    public boolean isPraise() {
        return praise;
    }

    public void setPraise(boolean praise) {
        this.praise = praise;
    }

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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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

    public boolean isCollection() {
        return collection;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }

    public int getCollection_count() {
        return collection_count;
    }

    public void setCollection_count(int collection_count) {
        this.collection_count = collection_count;
    }

    public int getShared_count() {
        return shared_count;
    }

    public void setShared_count(int shared_count) {
        this.shared_count = shared_count;
    }

    public String getAttachment_name() {
        return attachment_name;
    }

    public void setAttachment_name(String attachment_name) {
        this.attachment_name = attachment_name;
    }
}
