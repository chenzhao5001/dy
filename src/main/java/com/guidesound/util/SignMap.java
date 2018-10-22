package com.guidesound.util;

import java.util.HashMap;
import java.util.Map;

class Grade {
    String name;
    int pro_id;

    Grade(String name, int pro_id) {
        this.name = name;
        this.pro_id = pro_id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPro_id() {
        return pro_id;
    }

    public void setPro_id(int pro_id) {
        this.pro_id = pro_id;
    }
}

public class SignMap {
    static Map<Integer,String> subjectMap;
    static Map<Integer,String> watch_type;
    static Map<Integer,String> user_type;
    static Map<Integer,Grade> grade_type;


    static {
        subjectMap = new HashMap<>();
        subjectMap.put(0,"未知");
        subjectMap.put(1,"语文");
        subjectMap.put(2,"数学");
        subjectMap.put(3,"英语");

        watch_type = new HashMap<>();
        watch_type.put(1,"小学前");
        watch_type.put(2,"小学");
        watch_type.put(3,"初中");
        watch_type.put(4,"高中");

        user_type = new HashMap<>();
        user_type.put(1,"教师");
        user_type.put(2,"家长");
        user_type.put(3,"学生");
        user_type.put(4,"机构");


        grade_type = new HashMap<>();
        grade_type.put(1,new Grade("1-3岁",1));
        grade_type.put(2,new Grade("4-6岁",1));
        grade_type.put(3,new Grade("1-6岁",1));

        grade_type.put(4,new Grade("1年级",2));
        grade_type.put(5,new Grade("2年级",2));
        grade_type.put(6,new Grade("3年级",2));
        grade_type.put(7,new Grade("4年级",2));
        grade_type.put(8,new Grade("5年级",2));
        grade_type.put(9,new Grade("6年级",2));

        grade_type.put(10,new Grade("初一",3));
        grade_type.put(11,new Grade("初二",3));
        grade_type.put(12,new Grade("初三",3));

        grade_type.put(13,new Grade("高一",4));
        grade_type.put(14,new Grade("高二",4));
        grade_type.put(15,new Grade("高三",4));


        grade_type.put(101,new Grade("小学",2));
        grade_type.put(102,new Grade("初中",3));
        grade_type.put(103,new Grade("高中",4));

    }

    public static String getSubjectById(int id) {
        if(subjectMap.containsKey(id)) {
            return subjectMap.get(id);
        }
        return "未知";
    }

    public static String getWatchById(int id) {
        if(watch_type.containsKey(id)) {
            return watch_type.get(id);
        }
        return "未知";
    }

    public static Map<Integer,String> getSubjectList() {
        return subjectMap;
    }

    public static Map<Integer,String> getWatchList() {
        return watch_type;
    }

    public static Map<Integer,String> getUserTypeList() {
        return user_type;
    }



    public static Map<Integer,Grade> getGradeTypeList() {
        return grade_type;
    }




}
