package com.guidesound.models;

public class ArticleAnswer {

    int id;
    int user_id;
    int ask_id;
    String abstract_info;
    String pic1_url;
    String pic2_url;
    String pic3_url;
    String content_url;
    int praise_count;
    boolean praise;
    boolean collection;
    boolean follow;

    private String user_head;
    private String user_name;
    private int user_type;
    private String user_grade_level_name;
    private int comment_count;

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    int create_time;

    public boolean isCollection() {
        return collection;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }

    public String getUser_head() {
        return user_head;
    }

    public void setUser_head(String user_head) {
        this.user_head = user_head;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public String getUser_grade_level_name() {
        return user_grade_level_name;
    }

    public void setUser_grade_level_name(String user_grade_level_name) {
        this.user_grade_level_name = user_grade_level_name;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
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

    public int getAsk_id() {
        return ask_id;
    }

    public void setAsk_id(int ask_id) {
        this.ask_id = ask_id;
    }

    public String getAbstract_info() {
        return abstract_info;
    }

    public void setAbstract_info(String abstract_info) {
        this.abstract_info = abstract_info;
    }

    public String getPic1_url() {
        return pic1_url;
    }

    public void setPic1_url(String pic1_url) {
        this.pic1_url = pic1_url;
    }

    public String getPic2_url() {
        return pic2_url;
    }

    public void setPic2_url(String pic2_url) {
        this.pic2_url = pic2_url;
    }

    public String getPic3_url() {
        return pic3_url;
    }

    public void setPic3_url(String pic3_url) {
        this.pic3_url = pic3_url;
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

    public int getPraise_count() {
        return praise_count;
    }

    public void setPraise_count(int praise_count) {
        this.praise_count = praise_count;
    }

    public boolean isPraise() {
        return praise;
    }

    public void setPraise(boolean praise) {
        this.praise = praise;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }
}
