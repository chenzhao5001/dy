package com.guidesound.models;

import com.guidesound.ret.CommodityInfo;
import com.guidesound.ret.TeacherClass;
import com.guidesound.ret.VideoClass;

public class ArticleInfo {

    int id;
    int user_id;
    private String head;
    private String head_pic1;
    private String head_pic2;
    private String head_pic3;
    private int subject;
    private String subject_name;
    private int grade_class;
    private Object grade;
    private int type;
    private int answer_count;
    private String user_head;
    private String user_name;
    private int user_type;
    private int user_subject;
    private String user_subject_name;
    private int user_grade;
    private String user_grade_name;
    private int user_grade_level;
    private String user_grade_level_name;
    private String content_url;
    private String type_list;
    private Object pools;

    private int collection_count;
    private boolean collection;
    private boolean follow;
    private int comment_count;
    private int create_time;
    private int praise_count;
    private boolean praise;
    private int examine_status;


    int out_relay;
    int inner_relay;
    int group;
    String article_state;
    int rec_count;
    int play_count;
    String auth_info;

    public String getAuth_info() {
        return auth_info;
    }

    public void setAuth_info(String auth_info) {
        this.auth_info = auth_info;
    }

    public Object getPools() {
        return pools;
    }

    public void setPools(Object pools) {
        this.pools = pools;
    }

    public int getPlay_count() {
        return play_count;
    }

    public void setPlay_count(int play_count) {
        this.play_count = play_count;
    }

    private int attachment_type;
    private int attachment_id;
    private String attachment_name;
    private int attachment_subtype;

    VideoClass video_class;
    TeacherClass teacher_class;
    CommodityInfo commodity;


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

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getArticle_state() {
        return article_state;
    }

    public void setArticle_state(String article_state) {
        this.article_state = article_state;
    }

    public int getRec_count() {
        return rec_count;
    }

    public void setRec_count(int rec_count) {
        this.rec_count = rec_count;
    }

    public String getType_list() {
        return type_list;
    }

    public void setType_list(String type_list) {
        this.type_list = type_list;
    }

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

    public String getAttachment_name() {
        return attachment_name;
    }

    public void setAttachment_name(String attachment_name) {
        this.attachment_name = attachment_name;
    }

    public int getAttachment_subtype() {
        return attachment_subtype;
    }

    public void setAttachment_subtype(int attachment_subtype) {
        this.attachment_subtype = attachment_subtype;
    }

    public int getExamine_status() {
        return examine_status;
    }

    public void setExamine_status(int examine_status) {
        this.examine_status = examine_status;
    }

    public Object getGrade() {
        return grade;
    }

    public void setGrade(Object grade) {
        this.grade = grade;
    }

    public int getGrade_class() {
        return grade_class;
    }

    public void setGrade_class(int grade_class) {
        this.grade_class = grade_class;
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

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    public int getCollection_count() {
        return collection_count;
    }

    public void setCollection_count(int collection_count) {
        this.collection_count = collection_count;
    }

    public boolean isCollection() {
        return collection;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }

    public int getAnswer_count() {
        return answer_count;
    }

    public void setAnswer_count(int answer_count) {
        this.answer_count = answer_count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

    public int getUser_subject() {
        return user_subject;
    }

    public void setUser_subject(int user_subject) {
        this.user_subject = user_subject;
    }

    public String getUser_subject_name() {
        return user_subject_name;
    }

    public void setUser_subject_name(String user_subject_name) {
        this.user_subject_name = user_subject_name;
    }

    public int getUser_grade() {
        return user_grade;
    }

    public void setUser_grade(int user_grade) {
        this.user_grade = user_grade;
    }

    public String getUser_grade_name() {
        return user_grade_name;
    }

    public void setUser_grade_name(String user_grade_name) {
        this.user_grade_name = user_grade_name;
    }

    public int getUser_grade_level() {
        return user_grade_level;
    }

    public void setUser_grade_level(int user_grade_level) {
        this.user_grade_level = user_grade_level;
    }

    public String getUser_grade_level_name() {
        return user_grade_level_name;
    }

    public void setUser_grade_level_name(String user_grade_level_name) {
        this.user_grade_level_name = user_grade_level_name;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getHead_pic1() {
        return head_pic1;
    }

    public void setHead_pic1(String head_pic1) {
        this.head_pic1 = head_pic1;
    }

    public String getHead_pic2() {
        return head_pic2;
    }

    public void setHead_pic2(String head_pic2) {
        this.head_pic2 = head_pic2;
    }

    public String getHead_pic3() {
        return head_pic3;
    }

    public void setHead_pic3(String head_pic3) {
        this.head_pic3 = head_pic3;
    }

//    public int getCollection_count() {
//        return collection_count;
//    }
//
//    public void setCollection_count(int collection_count) {
//        this.collection_count = collection_count;
//    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

//    public int getPriase_count() {
//        return priase_count;
//    }
//
//    public void setPriase_count(int priase_count) {
//        this.priase_count = priase_count;
//    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

}
