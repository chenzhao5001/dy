package com.guidesound.models;

public class UserFriend {
    int user_id;
    int add_user_id;
    int type;
    int state;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAdd_user_id() {
        return add_user_id;
    }

    public void setAdd_user_id(int add_user_id) {
        this.add_user_id = add_user_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
