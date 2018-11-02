package com.guidesound.util;

import java.util.*;

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

class ListItem implements Comparable<ListItem> {
    int id;
    String gradeName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }
    @Override
    public int compareTo(ListItem o) {
        return this.getId() - o.getId();
    }
}

class ListInfo {
    int id;
    String level;
    List<ListItem> list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<ListItem> getList() {
        return list;
    }

    public void setList(List<ListItem> list) {
        this.list = list;
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
        subject_classify.put(5,"武术");
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
        grade_type.put(101,new Content("1-3岁",1));
        grade_type.put(102,new Content("4-6岁",1));
        grade_type.put(199,new Content("1-6岁",1));

        grade_type.put(201,new Content("1年级",2));
        grade_type.put(202,new Content("2年级",2));
        grade_type.put(203,new Content("3年级",2));
        grade_type.put(204,new Content("4年级",2));
        grade_type.put(205,new Content("5年级",2));
        grade_type.put(206,new Content("6年级",2));
        grade_type.put(299,new Content("小学",2));


        grade_type.put(301,new Content("初一",3));
        grade_type.put(302,new Content("初二",3));
        grade_type.put(303,new Content("初三",3));
        grade_type.put(399,new Content("初中",3));

        grade_type.put(401,new Content("高一",4));
        grade_type.put(402,new Content("高二",4));
        grade_type.put(403,new Content("高三",4));
        grade_type.put(499,new Content("高中",4));

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

    public static Object getGradeTypeInfo() {
        ListInfo gradeInfo1 = new ListInfo();//小学前
        ListInfo gradeInfo2 = new ListInfo();//小学
        ListInfo gradeInfo3 = new ListInfo();//初中
        ListInfo gradeInfo4 = new ListInfo();//高中

        gradeInfo1.setId(1);
        gradeInfo1.setLevel("小学前");
        gradeInfo1.setList(new ArrayList<>());
        gradeInfo2.setId(2);
        gradeInfo2.setLevel("小学");
        gradeInfo2.setList(new ArrayList<>());
        gradeInfo3.setId(3);
        gradeInfo3.setLevel("初中");
        gradeInfo3.setList(new ArrayList<>());
        gradeInfo4.setId(4);
        gradeInfo4.setLevel("高中");
        gradeInfo4.setList(new ArrayList<>());

        for(Integer key : grade_type.keySet()) {
            Content content = grade_type.get(key);
            ListItem gradeItem = new ListItem();
            gradeItem.setId(key);
            gradeItem.setGradeName(content.getName());
            if (content.getPro_id() == 1) {
                gradeInfo1.getList().add(gradeItem);
            }
            if (content.getPro_id() == 2) {
                gradeInfo2.getList().add(gradeItem);
            }
            if (content.getPro_id() == 3) {
                gradeInfo3.getList().add(gradeItem);
            }
            if (content.getPro_id() == 4) {
                gradeInfo4.getList().add(gradeItem);
            }
        }

        Collections.sort(gradeInfo1.getList());
        Collections.sort(gradeInfo2.getList());
        Collections.sort(gradeInfo3.getList());
        Collections.sort(gradeInfo4.getList());

        List<ListInfo> list  = new ArrayList<>();
        list.add(gradeInfo1);
        list.add(gradeInfo2);
        list.add(gradeInfo3);
        list.add(gradeInfo4);
        return list;
    }

    public static Object getSubjectTypeInfo() {

        ListInfo info1 = new ListInfo();//文化课
        ListInfo info2 = new ListInfo();//音乐
        ListInfo info3 = new ListInfo();//美术
        ListInfo info4 = new ListInfo();//舞蹈
        ListInfo info5 = new ListInfo();//武术
        ListInfo info6 = new ListInfo();//其他

        info1.setId(1);
        info1.setLevel("文化课");
        info1.setList(new ArrayList<>());
        info2.setId(2);
        info2.setLevel("音乐");
        info2.setList(new ArrayList<>());
        info3.setId(3);
        info3.setLevel("美术");
        info3.setList(new ArrayList<>());
        info4.setId(4);
        info4.setLevel("舞蹈");
        info4.setList(new ArrayList<>());
        info5.setId(5);
        info5.setLevel("武术");
        info5.setList(new ArrayList<>());
        info6.setId(6);
        info6.setLevel("其他");
        info6.setList(new ArrayList<>());

        for(Integer key : subject_type.keySet()) {
            Content content = subject_type.get(key);
            ListItem item = new ListItem();
            item.setId(key);
            item.setGradeName(content.getName());
            switch (content.getPro_id()) {
                case 1:
                    info1.getList().add(item);
                    break;
                case 2:
                    info2.getList().add(item);
                    break;
                case 3:
                    info3.getList().add(item);
                    break;
                case 4:
                    info4.getList().add(item);
                    break;
                case 5:
                    info5.getList().add(item);
                    break;
                case 6:
                    info6.getList().add(item);
                    break;
            }
        }

        Collections.sort(info1.getList());
        Collections.sort(info2.getList());
        Collections.sort(info3.getList());
        Collections.sort(info4.getList());
        Collections.sort(info5.getList());
        Collections.sort(info6.getList());

        List<ListInfo> list  = new ArrayList<>();
        list.add(info1);
        list.add(info2);
        list.add(info3);
        list.add(info4);
        list.add(info5);
        list.add(info6);
        return list;

    }

}
