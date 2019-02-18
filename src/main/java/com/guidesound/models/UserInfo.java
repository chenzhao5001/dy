package com.guidesound.models;

public class UserInfo {
    private int id;
    private String unionid;
    private String phone;
    private String dy_id;
    private String name;
    private String head;
    private String token;
    private int type;
    private int level;
    private int status;
    private String sign_name;
    private String background_url;
    private boolean follow;
    private int subject;
    private String subject_name;
    private int teach_age;
    private String company_name;

    private int funs_counts;
    private int follow_count;
    private int praise_count;
    private int video_count;
    private int article_count;
    private int sex;
    private int grade;
    private String grade_name;
    private int grade_level;
    private String grade_level_name;
    private String province;
    private String city;
    private String area;


    int video_time;
    String user_introduce;
    String identity_card;
    String graduation_card;
    String teacher_card;
    String achievement;
    String license;
    String confirmation_letter;
    String shop_prove;
    int auth_state;
    String auth_info;
    int video_duration;



    private String im_id;
    private String im_sig;
    private int pwd_state;
    private int channel_stage;
    public String im_appid = "1400158534";
    public String friend_state = "0";

    public int getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(int video_duration) {
        this.video_duration = video_duration;
    }

    public String getAuth_info() {
        return auth_info;
    }

    public void setAuth_info(String auth_info) {
        this.auth_info = auth_info;
    }

    public int getAuth_state() {
        return auth_state;
    }

    public void setAuth_state(int auth_state) {
        this.auth_state = auth_state;
    }

    public String getUser_introduce() {
        return user_introduce;
    }

    public void setUser_introduce(String user_introduce) {
        this.user_introduce = user_introduce;
    }

    public String getFriend_state() {
        return friend_state;
    }

    public void setFriend_state(String friend_state) {
        this.friend_state = friend_state;
    }

    public String getIm_appid() {
        return im_appid;
    }

    public void setIm_appid(String im_appid) {
        this.im_appid = im_appid;
    }

    public int getChannel_stage() {
        return channel_stage;
    }

    public void setChannel_stage(int channel_stage) {
        this.channel_stage = channel_stage;
    }

    public String getIm_id() {
        return im_id;
    }

    public void setIm_id(String im_id) {
        this.im_id = im_id;
    }

    public String getIm_sig() {
        return im_sig;
    }

    public void setIm_sig(String im_sig) {
        this.im_sig = im_sig;
    }

    public int getPwd_state() {
        return pwd_state;
    }
    public void setPwd_state(int pwd_state) {
        this.pwd_state = pwd_state;
    }

    private int create_time;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public int getTeach_age() {
        return teach_age;
    }

    public void setTeach_age(int teach_age) {
        this.teach_age = teach_age;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
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

    public String getDy_id() {
        return dy_id;
    }

    public void setDy_id(String dy_id) {
        this.dy_id = dy_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSign_name() {
        return sign_name;
    }

    public void setSign_name(String sign_name) {
        this.sign_name = sign_name;
    }


    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getFuns_counts() {
        return funs_counts;
    }

    public void setFuns_counts(int funs_counts) {
        this.funs_counts = funs_counts;
    }

    public int getFollow_count() {
        return follow_count;
    }

    public void setFollow_count(int follow_count) {
        this.follow_count = follow_count;
    }

    public int getPraise_count() {
        return praise_count;
    }

    public void setPraise_count(int praise_count) {
        this.praise_count = praise_count;
    }

    public int getVideo_count() {
        return video_count;
    }

    public void setVideo_count(int video_count) {
        this.video_count = video_count;
    }

    public int getArticle_count() {
        return article_count;
    }

    public void setArticle_count(int article_count) {
        this.article_count = article_count;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBackground_url() {
        return background_url;
    }

    public void setBackground_url(String background_url) {
        this.background_url = background_url;
    }

    public String getGrade_name() {
        return grade_name;
    }

    public void setGrade_name(String grade_name) {
        this.grade_name = grade_name;
    }

    public int getGrade_level() {
        return grade_level;
    }

    public void setGrade_level(int grade_level) {
        this.grade_level = grade_level;
    }

    public String getGrade_level_name() {
        return grade_level_name;
    }

    public void setGrade_level_name(String grade_level_name) {
        this.grade_level_name = grade_level_name;
    }


    public int getVideo_time() {
        return video_time;
    }

    public void setVideo_time(int video_time) {
        this.video_time = video_time;
    }

    public String getIdentity_card() {
        return identity_card;
    }

    public void setIdentity_card(String identity_card) {
        this.identity_card = identity_card;
    }

    public String getGraduation_card() {
        return graduation_card;
    }

    public void setGraduation_card(String graduation_card) {
        this.graduation_card = graduation_card;
    }

    public String getTeacher_card() {
        return teacher_card;
    }

    public void setTeacher_card(String teacher_card) {
        this.teacher_card = teacher_card;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getConfirmation_letter() {
        return confirmation_letter;
    }

    public void setConfirmation_letter(String confirmation_letter) {
        this.confirmation_letter = confirmation_letter;
    }

    public String getShop_prove() {
        return shop_prove;
    }

    public void setShop_prove(String shop_prove) {
        this.shop_prove = shop_prove;
    }


}
