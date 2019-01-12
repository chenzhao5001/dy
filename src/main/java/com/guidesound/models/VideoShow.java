package com.guidesound.models;

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
    int video_state;
    String pools;
    int create_time;

    public String getPools() {
        return pools;
    }

    public void setPools(String pools) {
        this.pools = pools;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getVideo_state() {
        return video_state;
    }

    public void setVideo_state(int video_state) {
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
}
