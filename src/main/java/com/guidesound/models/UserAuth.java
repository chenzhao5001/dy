package com.guidesound.models;

public class UserAuth {
    int type;
    String identity_card;
    String graduation_card;
    String teacher_card;
    String achievement;
    String license;
    String confirmation_letter;
    String shop_prove;
    String auth_info;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getAuth_info() {
        return auth_info;
    }

    public void setAuth_info(String auth_info) {
        this.auth_info = auth_info;
    }
}
