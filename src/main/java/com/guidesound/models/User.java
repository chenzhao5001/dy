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
