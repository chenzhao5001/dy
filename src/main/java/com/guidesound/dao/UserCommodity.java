package com.guidesound.dao;

public class UserCommodity {

    int id;
    int user_id;
    String commodity_name;
    String commodity_pic;
    String commodity_url;
    int commodity_price;
    int state;
    int create_time;
    int update_time;

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

    public String getCommodity_name() {
        return commodity_name;
    }

    public void setCommodity_name(String commodity_name) {
        this.commodity_name = commodity_name;
    }

    public String getCommodity_url() {
        return commodity_url;
    }

    public void setCommodity_url(String commodity_url) {
        this.commodity_url = commodity_url;
    }

    public int getCommodity_price() {
        return commodity_price;
    }

    public void setCommodity_price(int commodity_price) {
        this.commodity_price = commodity_price;
    }

    public String getCommodity_pic() {
        return commodity_pic;
    }

    public void setCommodity_pic(String commodity_pic) {
        this.commodity_pic = commodity_pic;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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
}
