package com.guidesound.util;

import java.util.HashMap;
import java.util.Map;

class Content {
    String name;
    int pro_id;

    Content(String name, int pro_id) {
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
    static Map<Integer,String> subject_classify;
    static Map<Integer,Content> subject_type;
    static Map<Integer,String> watch_type;
    static Map<Integer,String> user_type;
    static Map<Integer,Content> grade_type;


    static {
        subject_classify = new HashMap<>();

        subject_classify.put(1,"文化课");
        subject_classify.put(2,"音乐");
        subject_classify.put(3,"美术");
        subject_classify.put(4,"舞蹈");
        subject_classify.put(5,"美术");
        subject_classify.put(6,"其他");

        subject_type = new HashMap<>();
        subject_type.put(101,new Content("语文",1));
        subject_type.put(102,new Content("数学",1));
        subject_type.put(103,new Content("英语",1));
        subject_type.put(104,new Content("物理",1));
        subject_type.put(105,new Content("化学",1));
        subject_type.put(106,new Content("生物",1));
        subject_type.put(107,new Content("历史",1));
        subject_type.put(108,new Content("地理",1));
        subject_type.put(109,new Content("政治",1));


        subject_type.put(201,new Content("钢琴",2));
        subject_type.put(202,new Content("吉他",2));
        subject_type.put(203,new Content("古筝",2));
        subject_type.put(204,new Content("架子鼓",2));
        subject_type.put(205,new Content("尤克里里",2));
        subject_type.put(299,new Content("全部",2));

        subject_type.put(301,new Content("素描",3));
        subject_type.put(302,new Content("油画",3));
        subject_type.put(303,new Content("水粉",3));
        subject_type.put(304,new Content("漫画",3));
        subject_type.put(305,new Content("水彩",3));
        subject_type.put(306,new Content("速写",3));
        subject_type.put(307,new Content("少儿美术",3));
        subject_type.put(399,new Content("全部",3));

        subject_type.put(401,new Content("芭蕾",4));
        subject_type.put(402,new Content("民族",4));
        subject_type.put(403,new Content("古典",4));
        subject_type.put(404,new Content("爵士",4));
        subject_type.put(405,new Content("拉丁",4));
        subject_type.put(406,new Content("街舞",4));
        subject_type.put(499,new Content("全部",4));

        subject_type.put(501,new Content("跆拳道",5));
        subject_type.put(502,new Content("武术",5));
        subject_type.put(503,new Content("散打",5));
        subject_type.put(504,new Content("柔道",5));
        subject_type.put(599,new Content("全部",5));

        subject_type.put(601,new Content("奥数",6));
        subject_type.put(602,new Content("国学",6));
        subject_type.put(603,new Content("智力开发",6));
        subject_type.put(604,new Content("速算",6));
        subject_type.put(699,new Content("全部",6));


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
        grade_type.put(1,new Content("1-3岁",1));
        grade_type.put(2,new Content("4-6岁",1));
        grade_type.put(3,new Content("1-6岁",1));

        grade_type.put(4,new Content("1年级",2));
        grade_type.put(5,new Content("2年级",2));
        grade_type.put(6,new Content("3年级",2));
        grade_type.put(7,new Content("4年级",2));
        grade_type.put(8,new Content("5年级",2));
        grade_type.put(9,new Content("6年级",2));

        grade_type.put(10,new Content("初一",3));
        grade_type.put(11,new Content("初二",3));
        grade_type.put(12,new Content("初三",3));

        grade_type.put(13,new Content("高一",4));
        grade_type.put(14,new Content("高二",4));
        grade_type.put(15,new Content("高三",4));


        grade_type.put(101,new Content("小学",2));
        grade_type.put(102,new Content("初中",3));
        grade_type.put(103,new Content("高中",4));

    }

    public static String getSubjectTypeById(int id) {
        if(subject_type.containsKey(id)) {
            return subject_type.get(id).getName();
        }
        return "未知";
    }

    public static String getSubjectClassifyById(int id) {
        if(subject_classify.containsKey(id)) {
            return subject_classify.get(id);
        }
        return "未知";
    }

    public static String getWatchById(int id) {
        if(watch_type.containsKey(id)) {
            return watch_type.get(id);
        }
        return "未知";
    }

    public static Map<Integer,String> getSubjectClassifyList() {
        return subject_classify;
    }

    public static Map<Integer,Content> getSubjectTypeList() {
        return subject_type;
    }

    public static Map<Integer,String> getWatchList() {
        return watch_type;
    }

    public static Map<Integer,String> getUserTypeList() {
        return user_type;
    }



    public static Map<Integer,Content> getGradeTypeList() {
        return grade_type;
    }




}
