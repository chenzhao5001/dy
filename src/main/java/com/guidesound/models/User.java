package com.guidesound.models;

/**
 * 用户表
 */
public class User {
    private int id;
    private String token;
    private String unionid;
    private int dy_id;
    private String phone;
    private String name;
    private String head;
    private int type;
    private int level;
    private int status;
    private String sign_name;
    private int sex;
    private int teach_age;
    private int create_time;
    private int update_time;
    private int grade_level;
    private int subject;
    private int channel_stage;
    private int grade;
    private String province;
    private String city;
    private String area;
    private String user_introduce;

    private String identity_card;
    private String graduation_card;
    private String teacher_card;
    private String achievement;
    private String license;
    private String confirmation_letter;
    private String shop_prove;
    private int name_flag;
    private int head_flag;
    private int user_introduce_flag;
    private int auth_state;

    public int getAuth_state() {
        return auth_state;
    }

    public void setAuth_state(int auth_state) {
        this.auth_state = auth_state;
    }

    public int getName_flag() {
        return name_flag;
    }

    public void setName_flag(int name_flag) {
        this.name_flag = name_flag;
    }

    public int getHead_flag() {
        return head_flag;
    }

    public void setHead_flag(int head_flag) {
        this.head_flag = head_flag;
    }

    public int getUser_introduce_flag() {
        return user_introduce_flag;
    }

    public void setUser_introduce_flag(int user_introduce_flag) {
        this.user_introduce_flag = user_introduce_flag;
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

    public String getUser_introduce() {
        return user_introduce;
    }

    public void setUser_introduce(String user_introduce) {
        this.user_introduce = user_introduce;
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

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getChannel_stage() {
        return channel_stage;
    }

    public void setChannel_stage(int channel_stage) {
        this.channel_stage = channel_stage;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public int getGrade_level() {
        return grade_level;
    }

    public void setGrade_level(int grade_level) {
        this.grade_level = grade_level;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public int getDy_id() {
        return dy_id;
    }

    public void setDy_id(int dy_id) {
        this.dy_id = dy_id;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getTeach_age() {
        return teach_age;
    }

    public void setTeach_age(int teach_age) {
        this.teach_age = teach_age;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public int getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(int update_time) {
        this.update_time = update_time;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", unionid='" + unionid + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", sign_name='" + sign_name + '\'' +
                ", sex=" + sex +
                ", teach_age=" + teach_age +
                ", create_time=" + create_time +
                ", update_time=" + update_time +
                '}';
    }
}
