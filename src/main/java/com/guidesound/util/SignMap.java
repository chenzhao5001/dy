package com.guidesound.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidesound.TempStruct.ItemInfo;
import com.guidesound.dao.ICourse;
import com.guidesound.models.Subject;
import org.springframework.beans.factory.annotation.Autowired;

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

class Content2 {
    String channel_info;
    String channel_name;

    public String getChannel_info() {
        return channel_info;
    }

    public void setChannel_info(String channel_info) {
        this.channel_info = channel_info;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }
}

class Content3 {
    int id;
    String info;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

class GradeContent {
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
}

class SubjectContent {
    int id;
    String subjectName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
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
    static Map<Integer, String> subject_classify;
    static Map<Integer, Content> subject_type;
    static Map<Integer, String> watch_type;
    static Map<Integer, String> user_type;
    static Map<Integer, String> grade_type;
    static Map<Integer, String> user_level;
    static List<Content2> channel;
    static Map<Integer, String> video_state;
    static Map<Integer, String> commend_pools;
    static Map<Integer, String> course_type;
    static Map<Integer, String> course_form;
    static Map<Integer, String> course_type_name;
    static Map<Integer, String> sex_type;

    static boolean flag = false;

    public static void Init(List<Subject> subjectList) {
        if (flag == false) {
            flag = true;
            subject_classify = new TreeMap<>();
            subject_type = new TreeMap<>();
            for (Subject subject : subjectList) {
                if (!subject_classify.containsKey(subject.getLevel_1())) {
                    subject_classify.put(subject.getLevel_1(), subject.getLevel_1_name());
                }
                subject_type.put(subject.getLevel_2(), new Content(subject.getLevel_2_name(), subject.getLevel_1()));
            }
            subject_type.put(901, new Content("家长分享", 9));
            subject_type.put(902, new Content("学生分享", 9));
            subject_type.put(903, new Content("老师分享", 9));
            subject_type.put(904, new Content("校园生活", 9));
            subject_type.put(905, new Content("教育事件", 9));
            subject_type.put(906, new Content("学习方法", 9));
            subject_type.put(907, new Content("教育观点", 9));
            subject_type.put(908, new Content("政策法规", 9));
        }

    }

    static {

        course_type = new TreeMap<>();
        course_type.put(1, "1对1");
        course_type.put(2, "班课");
        course_type.put(3, "无");

        watch_type = new TreeMap<>();
        watch_type.put(1, "学龄前");
        watch_type.put(2, "小学");
        watch_type.put(3, "初中");
        watch_type.put(4, "高中");
        watch_type.put(5, "初高");

        user_type = new HashMap<>();
        user_type.put(1, "普通用户");
        user_type.put(2, "个人辅导老师");
        user_type.put(3, "教育企业");
        user_type.put(4, "教育达人");
        user_type.put(5, "教育商品商家");


        grade_type = new TreeMap<>();
        grade_type.put(101, "0-3岁");
        grade_type.put(102, "幼儿园");

        grade_type.put(201, "一年级");
        grade_type.put(202, "二年级");
        grade_type.put(203, "三年级");
        grade_type.put(204, "四年级");
        grade_type.put(205, "五年级");
        grade_type.put(206, "六年级");


        grade_type.put(301, "初一");
        grade_type.put(302, "初二");
        grade_type.put(303, "初三");

        grade_type.put(401, "高一");
        grade_type.put(402, "高二");
        grade_type.put(403, "高三");


        user_level = new HashMap<>();
        user_level.put(1, "游客");
        user_level.put(2, "初级");
        user_level.put(3, "中级");
        user_level.put(4, "高级");

        channel = new ArrayList<>();
        Content2 content2 = new Content2();
        content2.setChannel_info("101,102,103");
        content2.setChannel_name("语数外");
        channel.add(content2);
        content2 = new Content2();
        content2.setChannel_info("104,105,106");
        content2.setChannel_name("物化生");
        channel.add(content2);

        content2 = new Content2();
        content2.setChannel_info("107,108,109");
        content2.setChannel_name("历地政");
        channel.add(content2);

        content2 = new Content2();
        content2.setChannel_info("107,108,109");
        content2.setChannel_name("其他");
        channel.add(content2);

        video_state = new HashMap<>();
        video_state.put(0, "待审核");
        video_state.put(1, "已完成");
        video_state.put(2, "审核失败");
        video_state.put(3, "转码中");
        video_state.put(4, "转码失败");
        video_state.put(5, "推荐池");
        video_state.put(6, "已完成");
        video_state.put(7, "已下架");

        commend_pools = new TreeMap<>();
        commend_pools.put(101, "0-3岁");
        commend_pools.put(102, "幼儿园");
        commend_pools.put(199, "学龄前");

        commend_pools.put(201, "一年级");
        commend_pools.put(202, "二年级");
        commend_pools.put(203, "三年级");
        commend_pools.put(204, "四年级");
        commend_pools.put(205, "五年级");
        commend_pools.put(206, "六年级");
        commend_pools.put(299, "小学");

        commend_pools.put(301, "初一");
        commend_pools.put(302, "初二");
        commend_pools.put(303, "初三");
        commend_pools.put(399, "初中");

        commend_pools.put(401, "高一");
        commend_pools.put(402, "高二");
        commend_pools.put(403, "高三");
        commend_pools.put(498, "初高");
        commend_pools.put(499, "高中");
        commend_pools.put(999, "公共池");
        commend_pools.put(1000, "无");

        course_form = new HashMap<>();
        course_form.put(0, "线上");
        course_form.put(1, "上门");

        course_type_name = new HashMap<>();
        course_type_name.put(0, "1对1");
        course_type_name.put(1, "班课");

        sex_type = new HashMap<>();
        sex_type.put(1, "男");
        sex_type.put(2, "女");
    }

    public static String getSexById(int id) {
        if (!sex_type.containsKey(id)) {
            return "未知";
        }
        return sex_type.get(id);
    }

    public static String getCourseFormById(int id) {
        if (!course_form.containsKey(id)) {
            return "未知";
        }
        return course_form.get(id);
    }

    public static String getCourseTypeNameById(int id) {
        if (!course_type_name.containsKey(id)) {
            return "未知";
        }
        return course_type_name.get(id);
    }

    public static Map<Integer, String> getPoolList() {
        return commend_pools;
    }

    public static String getPoolById(int pool_id) {
        if (commend_pools.containsKey(pool_id)) {
            return commend_pools.get(pool_id);
        }
        return "未知";
    }

    public static String getVideoState(int state) {
        if (video_state.containsKey(state)) {
            return video_state.get(state);
        }
        return "未知";
    }

    public static Map<Integer, String> getCourseType() {
        return course_type;
    }

    public static List<Content2> getChannelList() throws JsonProcessingException {
        return channel;
    }

    public static String getUserLevelById(int id) {
        if (user_level.containsKey(id)) {
            return user_level.get(id);
        }
        return "未知";
    }

    public static String getSubjectTypeById(int id) {
        if (subject_type.containsKey(id)) {
            return subject_type.get(id).getName();
        }
        return "未知";
    }

    public static List<GradeContent> getGrade() {
        Map<Integer, String> m = new LinkedHashMap<>();
        m.put(101, "0-3岁");
        m.put(102, "幼儿园");
        m.put(199, "学龄前");
        m.put(201, "一年级");
        m.put(202, "二年级");
        m.put(203, "三年级");
        m.put(204, "四年级");
        m.put(205, "五年级");
        m.put(206, "六年级");
        m.put(299, "小学");
        m.put(301, "初一");
        m.put(302, "初二");
        m.put(303, "初三");
        m.put(399, "初中");
        m.put(401, "高一");
        m.put(402, "高二");
        m.put(403, "高三");
        m.put(499, "高中");
        m.put(498, "初高");

        List<GradeContent> list = new ArrayList<>();
        for (Integer key : m.keySet()) {
            String content = m.get(key);
            GradeContent gradeItem = new GradeContent();
            gradeItem.setId(key);
            gradeItem.setGradeName(content);
            list.add(gradeItem);
        }
        return list;
    }

    public static Map<Integer, String> getGradeByClass(int type, boolean role_flag) {
        Map<Integer, String> m = new TreeMap<>();
        if (type == 1) {
            m.put(101, "0-3岁");
            m.put(102, "幼儿园");
            if (role_flag == true) {
                m.put(199, "学龄前");
            }
        } else if (type == 2) {
            m.put(201, "一年级");
            m.put(202, "二年级");
            m.put(203, "三年级");
            m.put(204, "四年级");
            m.put(205, "五年级");
            m.put(206, "六年级");
            if (role_flag == true) {
                m.put(299, "小学");
            }
        } else if (type == 3) {
            m.put(301, "初一");
            m.put(302, "初二");
            m.put(303, "初三");
            if (role_flag == true) {
                m.put(399, "初中");
            }
        } else if (type == 4) {
            m.put(401, "高一");
            m.put(402, "高二");
            m.put(403, "高三");
            if (role_flag == true) {
                m.put(499, "高中");
            }
        } else if (type == 5) {
            m.put(301, "初一");
            m.put(302, "初二");
            m.put(303, "初三");
            m.put(399, "初三");
            m.put(401, "高一");
            m.put(402, "高二");
            m.put(403, "高三");
            if (role_flag == true) {
                m.put(399, "初中");
                m.put(499, "高中");
                m.put(498, "初高");
            }
        }
        return m;
    }


    public static String getGradeTypeByID(int id) {
        if (grade_type.containsKey(id)) {
            return grade_type.get(id);
        }
        if (id == 199) {
            return "学龄前";
        }
        if (id == 299) {
            return "小学";
        }
        if (id == 399) {
            return "初中";
        }
        if (id == 499) {
            return "高中";
        }
        if (id == 498) {
            return "初高";
        }
        return "未知";
    }

    public static String getSubjectClassifyById(int id) {
        if (subject_classify.containsKey(id)) {
            return subject_classify.get(id);
        }
        return "未知";
    }

    public static String getWatchById(int id) {
        if (watch_type.containsKey(id)) {
            return watch_type.get(id);
        }
        return "未知";
    }

    public static String getUserTypeById(int id) {
        if (user_type.containsKey(id)) {
            return user_type.get(id);
        }
        return "未知";
    }

    public static Map<Integer, String> getSubjectClassifyList() {
        return subject_classify;
    }

    public static Map<Integer, Content> getSubjectTypeList() {
        return subject_type;
    }

    public static Map<Integer, String> getWatchList() {
        return watch_type;
    }

    public static Map<Integer, String> getUserTypeList() {
        return user_type;
    }

    public static Object getGradeTypeInfo2(boolean web_flag) {
        List<Content3> list = new ArrayList<>();
        for (Integer id : grade_type.keySet()) {
            String info = grade_type.get(id);
            Content3 temp = new Content3();
            temp.id = id;
            temp.info = info;
            list.add(temp);
        }
        if (web_flag == true) {
            Content3 temp = new Content3();
            temp.id = 199;
            temp.info = "学龄前";
            list.add(temp);
            temp = new Content3();
            temp.id = 299;
            temp.info = "小学";
            list.add(temp);
            temp = new Content3();
            temp.id = 399;
            temp.info = "初中";
            list.add(temp);
            temp = new Content3();
            temp.id = 499;
            temp.info = "高中";
            list.add(temp);
            temp = new Content3();
            temp.id = 498;
            temp.info = "初高";
            list.add(temp);
        } else {
            Content3 temp = new Content3();
            temp = new Content3();
            temp.id = 0;
            temp.info = "全部";
            list.add(temp);
        }
        return list;
    }


    public static Map<Integer, String> getGradeTypeList() {
        return grade_type;
    }

    public static Object getGradeTypeInfo() {
        ListInfo gradeInfo1 = new ListInfo();//小学前
        ListInfo gradeInfo2 = new ListInfo();//小学
        ListInfo gradeInfo3 = new ListInfo();//初中
        ListInfo gradeInfo4 = new ListInfo();//高中
        ListInfo gradeInfo5 = new ListInfo();//初高

        gradeInfo1.setId(1);
        gradeInfo1.setLevel("学龄前");
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
        gradeInfo5.setId(5);
        gradeInfo5.setLevel("初高");
        gradeInfo5.setList(new ArrayList<>());

        for (Integer key : grade_type.keySet()) {
            String content = grade_type.get(key);
            ListItem gradeItem = new ListItem();

            gradeItem.setId(key);
            gradeItem.setGradeName(content);
            if (key / 100 == 1) {
                gradeInfo1.getList().add(gradeItem);
            }
            if (key / 100 == 2) {
                gradeInfo2.getList().add(gradeItem);
            }
            if (key / 100 == 3) {
                gradeInfo3.getList().add(gradeItem);
                gradeInfo5.getList().add(gradeItem);
            }
            if (key / 100 == 4) {
                gradeInfo4.getList().add(gradeItem);
                gradeInfo5.getList().add(gradeItem);
            }
        }

        Collections.sort(gradeInfo1.getList());
        Collections.sort(gradeInfo2.getList());
        Collections.sort(gradeInfo3.getList());
        Collections.sort(gradeInfo4.getList());
        Collections.sort(gradeInfo5.getList());

        List<ListInfo> list = new ArrayList<>();
        list.add(gradeInfo1);
        list.add(gradeInfo2);
        list.add(gradeInfo3);
        list.add(gradeInfo4);
        list.add(gradeInfo5);
        return list;
    }

    public static Object getSubjectTypeInfo() {

        ListInfo info1 = new ListInfo();//文化课
        ListInfo info2 = new ListInfo();//音乐
        ListInfo info3 = new ListInfo();//美术
        ListInfo info4 = new ListInfo();//舞蹈
        ListInfo info5 = new ListInfo();//武术
        ListInfo info6 = new ListInfo();//
        ListInfo info7 = new ListInfo();//
        ListInfo info8 = new ListInfo();//
//        ListInfo info10 = new ListInfo();//

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
        info5.setLevel("体育");
        info5.setList(new ArrayList<>());
        info6.setId(6);
        info6.setLevel("思维训练");
        info6.setList(new ArrayList<>());
        info7.setId(7);
        info7.setLevel("中华文化");
        info7.setList(new ArrayList<>());
        info8.setId(8);
        info8.setLevel("家长课");
        info8.setList(new ArrayList<>());

//        info10.setId(10);
//        info10.setLevel("业余爱好");
//        info10.setList(new ArrayList<>());

        for (Integer key : subject_type.keySet()) {
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
                case 7:
                    info7.getList().add(item);
                    break;
                case 8:
                    info8.getList().add(item);
                    break;
//                case 10:
//                    info10.getList().add(item);
//                    break;
            }
        }

        Collections.sort(info1.getList());
        Collections.sort(info2.getList());
        Collections.sort(info3.getList());
        Collections.sort(info4.getList());
        Collections.sort(info5.getList());
        Collections.sort(info6.getList());
        Collections.sort(info7.getList());
        Collections.sort(info8.getList());
//        Collections.sort(info10.getList());

        List<ListInfo> list = new ArrayList<>();
        list.add(info1);
        list.add(info2);
        list.add(info3);
        list.add(info4);
        list.add(info5);
        list.add(info6);
        list.add(info7);
        list.add(info8);
//        list.add(info10);
        return list;
    }


    public static Object getSubjectList() {
        List<SubjectContent> list = new ArrayList<>();
        for (Integer key : subject_type.keySet()) {
            Content content = subject_type.get(key);
            SubjectContent item = new SubjectContent();
            item.setId(key);
            item.setSubjectName(content.getName());
            list.add(item);
        }
        return list;
    }

    //    static public List<Content2> getArticleChannel() {
//        return article_channel;
//    }
    static public List<Content2> getChannelList(int grade_stage, boolean type) {


        List<Content2> list = new ArrayList<>();
        if (type == true) {
            Content2 content2 = new Content2();
            content2.setChannel_info("1");
            content2.setChannel_name("推荐");
            list.add(content2);

            content2 = new Content2();
            content2.setChannel_info("801,802,803,901,902,903,904,905,906,907,908");
            content2.setChannel_name("放松");
            list.add(content2);

            content2 = new Content2();
            content2.setChannel_info("101,102,103,104,105,106,107,108,109,201,202,203,204,301,302,303,304,305,401,402,403,404,405,406,501,502,503,504,601,602,701,702");
            content2.setChannel_name("专注");
            list.add(content2);

//            content2 = new Content2();
//            content2.setChannel_info("2");
//            content2.setChannel_name("问答");
//            list.add(content2);
        } else {

            Content2 content2 = new Content2();
            content2.setChannel_info("801,802,803,901,902,903,904,905,906,907,908");
            content2.setChannel_name("放松");
            list.add(content2);

            content2 = new Content2();
            content2.setChannel_info("1");
            content2.setChannel_name("推荐");
            list.add(content2);

            content2 = new Content2();
            content2.setChannel_info("101,102,103,104,105,106,107,108,109,201,202,203,204,301,302,303,304,305,401,402,403,404,405,406,501,502,503,504,601,602,701,702");
            content2.setChannel_name("专注");
            list.add(content2);

        }

//
//        if(grade_stage == 101) {
//            content2 = new Content2();
//            content2.setChannel_info("801");
//            content2.setChannel_name("育儿");
//            list.add(content2);
//            content2 = new Content2();
//            content2.setChannel_info("101,102,103,104,105,106,107,108,109,201,202,203,301,302,303,304,305,401,402,403,404,405,406,501,502,503,504,601,602,701,702,802,803,901,902,903,904,905,1001");
//            content2.setChannel_name("其他");
//            list.add(content2);
//
//        } else if(grade_stage == 102){
//            content2 = new Content2();
//            content2.setChannel_info("101,102,103");
//            content2.setChannel_name("学知识");
//            list.add(content2);
//            content2 = new Content2();
//            content2.setChannel_info("201,202,203,204,301,302,303,304,305,401,402,403,404,405,406,501,502,503,504,104,105,106,107,108,109,601,602,701,702,801,802,803,901,902,903,904,905,1001");
//            content2.setChannel_name("其他");
//            list.add(content2);
//        } else if(grade_stage == 2){
//            content2 = new Content2();
//            content2.setChannel_info("101,102,103");
//            content2.setChannel_name("学知识");
//            list.add(content2);
//            content2 = new Content2();
//            content2.setChannel_info("201,202,203,204,301,302,303,304,305,401,402,403,404,405,406,501,502,503,504,104,105,106,107,108,109,601,602,701,702,801,802,803,901,902,903,904,905,1001");
//            content2.setChannel_name("其他");
//            list.add(content2);
//
//        } else if(grade_stage == 3){
//            content2 = new Content2();
//            content2.setChannel_info("101");
//            content2.setChannel_name("语文");
//            list.add(content2);
//            content2 = new Content2();
//            content2.setChannel_info("102");
//            content2.setChannel_name("数学");
//            list.add(content2);
//            content2 = new Content2();
//            content2.setChannel_info("103");
//            content2.setChannel_name("英语");
//            list.add(content2);
//            content2 = new Content2();
//            content2.setChannel_info("104");
//            content2.setChannel_name("物理");
//            list.add(content2);
//            content2 = new Content2();
//            content2.setChannel_info("105");
//            content2.setChannel_name("化学");
//            list.add(content2);
//            content2 = new Content2();
//            content2.setChannel_info("106,107,108,109,201,202,203,301,302,303,304,305,401,402,403,404,405,406,501,502,503,504,601,602,701,702,801,802,803,901,902,903,904,905,1001");
//            content2.setChannel_name("其他");
//            list.add(content2);
//
//        } else if(grade_stage == 4){
//            content2 = new Content2();
//            content2.setChannel_info("101");
//            content2.setChannel_name("语文");
//            list.add(content2);
//            content2 = new Content2();
//            content2.setChannel_info("102");
//            content2.setChannel_name("数学");
//            list.add(content2);
//            content2 = new Content2();
//            content2.setChannel_info("103");
//            content2.setChannel_name("英语");
//            list.add(content2);
//            content2 = new Content2();
//            content2.setChannel_info("104");
//            content2.setChannel_name("物理");
//            list.add(content2);
//            content2 = new Content2();
//            content2.setChannel_info("105");
//            content2.setChannel_name("化学");
//            list.add(content2);
//            content2 = new Content2();
//            content2.setChannel_info("106,107,108,109,201,202,203,301,302,303,304,305,401,402,403,404,405,406,501,502,503,504,601,602,701,702,801,802,803,901,902,903,904,905,1001");
//            content2.setChannel_name("其他 ");
//            list.add(content2);
//        } else {
//            content2 = new Content2();
//            content2.setChannel_info("101");
//            content2.setChannel_name("语文");
//            list.add(content2);
//            content2 = new Content2();
//            content2.setChannel_info("102");
//            content2.setChannel_name("数学");
//            list.add(content2);
//            content2 = new Content2();
//            content2.setChannel_info("103");
//            content2.setChannel_name("英语");
//            list.add(content2);
//            content2 = new Content2();
//            content2.setChannel_info("104,105,106,107,108,109,201,202,203,301,302,303,304,305,401,402,403,404,405,406,501,502,503,504,601,602,701,702,801,802,803,901,902,903,904,905,1001");
//            content2.setChannel_name("其他");
//            list.add(content2);
//        }
        return list;
    }

}
