package com.guidesound.util;

import java.util.HashMap;
import java.util.Map;

public class SignMap {
    static Map<Integer,String> subjectMap;
    static Map<Integer,String> watch_type;
    static Map<Integer,String> user_type;

    static {
        subjectMap = new HashMap<>();
        subjectMap.put(0,"未知");
        subjectMap.put(1,"语文");
        subjectMap.put(2,"数学");
        subjectMap.put(3,"英语");

        watch_type = new HashMap<>();
        watch_type.put(0,"未知");
        watch_type.put(1,"小学");
        watch_type.put(2,"初中");
        watch_type.put(3,"高中");
        watch_type.put(4,"大学");

        user_type = new HashMap<>();
        user_type.put(0,"未知");
        user_type.put(1,"教师");
        user_type.put(2,"家长");
        user_type.put(3,"学生");
        user_type.put(4,"机构");




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


}
