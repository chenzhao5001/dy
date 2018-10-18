package com.guidesound.util;

import java.util.HashMap;
import java.util.Map;

public class SignMap {
    static Map<Integer,String> subjectMap;
    static Map<Integer,String> watch_type;

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


}
