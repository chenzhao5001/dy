package com.guidesound.models;

public class Subject {
    int id;
    int level_1;
    int level_2;
    String level_1_name;
    String level_2_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel_1() {
        return level_1;
    }

    public void setLevel_1(int level_1) {
        this.level_1 = level_1;
    }

    public int getLevel_2() {
        return level_2;
    }

    public void setLevel_2(int level_2) {
        this.level_2 = level_2;
    }

    public String getLevel_1_name() {
        return level_1_name;
    }

    public void setLevel_1_name(String level_1_name) {
        this.level_1_name = level_1_name;
    }

    public String getLevel_2_name() {
        return level_2_name;
    }

    public void setLevel_2_name(String level_2_name) {
        this.level_2_name = level_2_name;
    }
}
