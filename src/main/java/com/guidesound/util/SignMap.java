package com.guidesound.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    static Map<Integer,String> grade_type;
    static Map<Integer,String> user_level;
    static List<Content2> channel;
    static List<Content2> article_channel;

    static boolean flag = false;

    public static void Init(List<Subject> subjectList) {
        if ( flag == false) {
            flag = true;
            subject_classify = new TreeMap<>();
            subject_type = new HashMap<>();
            for(Subject subject : subjectList) {
                if(!subject_classify.containsKey(subject.getLevel_1())) {
                    subject_classify.put(subject.getLevel_1(),subject.getLevel_1_name());
                }
                subject_type.put(subject.getLevel_2(),new Content(subject.getLevel_2_name(),subject.getLevel_1()));
            }
        }

    }
    static {

//        subject_type = new TreeMap<>();
//        subject_type.put(101,new Content("语文",1));
//        subject_type.put(102,new Content("数学",1));
//        subject_type.put(103,new Content("英语",1));
//        subject_type.put(104,new Content("物理",1));
//        subject_type.put(105,new Content("化学",1));
//        subject_type.put(106,new Content("生物",1));
//        subject_type.put(107,new Content("历史",1));
//        subject_type.put(108,new Content("地理",1));
//        subject_type.put(109,new Content("政治",1));
//        subject_type.put(199,new Content("全部",1));
//
//        subject_type.put(201,new Content("钢琴",2));
//        subject_type.put(202,new Content("吉他",2));
//        subject_type.put(203,new Content("古筝",2));
//        subject_type.put(204,new Content("架子鼓",2));
//        subject_type.put(205,new Content("尤克里里",2));
//        subject_type.put(299,new Content("全部",2));
//
//        subject_type.put(301,new Content("素描",3));
//        subject_type.put(302,new Content("油画",3));
//        subject_type.put(303,new Content("水粉",3));
//        subject_type.put(304,new Content("漫画",3));
//        subject_type.put(305,new Content("水彩",3));
//        subject_type.put(306,new Content("速写",3));
//        subject_type.put(307,new Content("少儿美术",3));
//        subject_type.put(399,new Content("全部",3));
//
//        subject_type.put(401,new Content("芭蕾",4));
//        subject_type.put(402,new Content("民族",4));
//        subject_type.put(403,new Content("古典",4));
//        subject_type.put(404,new Content("爵士",4));
//        subject_type.put(405,new Content("拉丁",4));
//        subject_type.put(406,new Content("街舞",4));
//        subject_type.put(499,new Content("全部",4));
//
//        subject_type.put(501,new Content("跆拳道",5));
//        subject_type.put(502,new Content("武术",5));
//        subject_type.put(503,new Content("散打",5));
//        subject_type.put(504,new Content("柔道",5));
//        subject_type.put(599,new Content("全部",5));
//
//        subject_type.put(601,new Content("奥数",6));
//        subject_type.put(602,new Content("国学",6));
//        subject_type.put(603,new Content("智力开发",6));
//        subject_type.put(604,new Content("速算",6));
//        subject_type.put(699,new Content("全部",6));


        watch_type = new TreeMap<>();
        watch_type.put(1,"小学前");
        watch_type.put(2,"小学");
        watch_type.put(3,"初中");
        watch_type.put(4,"高中");
//        watch_type.put(5,"小初");
        watch_type.put(6,"初高");

        user_type = new HashMap<>();
        user_type.put(1,"教师");
        user_type.put(2,"家长");
        user_type.put(3,"学生");
        user_type.put(4,"达人");
        user_type.put(5,"商家");


        grade_type = new HashMap<>();
        grade_type.put(101,"入园前");
        grade_type.put(102,"幼儿园");

        grade_type.put(201,"1年级");
        grade_type.put(202,"2年级");
        grade_type.put(203,"3年级");
        grade_type.put(204,"4年级");
        grade_type.put(205,"5年级");
        grade_type.put(206,"6年级");
        grade_type.put(299,"小学");


        grade_type.put(301,"初一");
        grade_type.put(302,"初二");
        grade_type.put(303,"初三");
        grade_type.put(399,"初中");

        grade_type.put(401,"高一");
        grade_type.put(402,"高二");
        grade_type.put(403,"高三");
        grade_type.put(499,"高中");

        user_level = new HashMap<>();
        user_level.put(1,"游客");
        user_level.put(2,"初级");
        user_level.put(3,"中级");
        user_level.put(4,"高级");

        channel = new ArrayList<>();
        Content2 content2 = new Content2();
//        content2.setChannel_info("1");
//        content2.setChannel_name("推荐");
//        channel.add(content2);
//        content2 = new Content2();
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

        article_channel = new ArrayList<>();
        content2 = new Content2();
        content2.setChannel_info("1");
        content2.setChannel_name("推荐");
        article_channel.add(content2);

        content2 = new Content2();
        content2.setChannel_info("2");
        content2.setChannel_name("问答");
        article_channel.add(content2);


        content2 = new Content2();
        content2.setChannel_info("101");
        content2.setChannel_name("语文");
        article_channel.add(content2);

        content2 = new Content2();
        content2.setChannel_info("102");
        content2.setChannel_name("数学");
        article_channel.add(content2);

        content2 = new Content2();
        content2.setChannel_info("103");
        content2.setChannel_name("英语");
        article_channel.add(content2);

        content2 = new Content2();
        content2.setChannel_info("104");
        content2.setChannel_name("物理");
        article_channel.add(content2);
    }

    public static List<Content2> getChannelList() throws JsonProcessingException {
        return channel;
    }
    public static String getUserLevelById(int id) {
        if(user_level.containsKey(id)) {
            return user_level.get(id);
        }
        return "未知";
    }

    public static String getSubjectTypeById(int id) {
        if(subject_type.containsKey(id)) {
            return subject_type.get(id).getName();
        }
        return "未知";
    }

    public static Map<Integer,String> getGradeByClass(int type) {
        Map<Integer,String> m = new TreeMap<>();
        if(type == 1) {
            m.put(101,"入园前");
            m.put(102,"幼儿园");
        } else if(type == 2){
            m.put(201,"1年级");
            m.put(202,"2年级");
            m.put(203,"3年级");
            m.put(204,"4年级");
            m.put(205,"5年级");
            m.put(206,"6年级");
            m.put(299,"小学");
        } else if (type == 3) {
            m.put(301,"初一");
            m.put(302,"初二");
            m.put(303,"初三");
            m.put(399,"初中");
        } else if(type == 4){
            m.put(401,"高一");
            m.put(402,"高二");
            m.put(403,"高三");
            m.put(499,"高中");
        } else if(type == 6){
            m.put(301,"初一");
            m.put(302,"初二");
            m.put(303,"初三");
            m.put(399,"初中");
            m.put(401,"高一");
            m.put(402,"高二");
            m.put(403,"高三");
            m.put(499,"高中");
        }
        return m;
    }
    public static String getGradeTypeByID(int id) {
        if(grade_type.containsKey(id)) {
            return grade_type.get(id);
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

    public static String getUserTypeById(int id) {
        if(user_type.containsKey(id)) {
            return user_type.get(id);
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



//    public static Map<Integer,Content> getGradeTypeList() {
//        return grade_type;
//    }

    public static Object getGradeTypeInfo() {
        ListInfo gradeInfo1 = new ListInfo();//小学前
        ListInfo gradeInfo2 = new ListInfo();//小学
        ListInfo gradeInfo3 = new ListInfo();//初中
        ListInfo gradeInfo4 = new ListInfo();//高中
        ListInfo gradeInfo5 = new ListInfo();//小初
        ListInfo gradeInfo6 = new ListInfo();//初高

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

        gradeInfo5.setId(5);
        gradeInfo5.setLevel("小初");
        gradeInfo5.setList(new ArrayList<>());

        gradeInfo6.setId(6);
        gradeInfo6.setLevel("初高");
        gradeInfo6.setList(new ArrayList<>());

        for(Integer key : grade_type.keySet()) {
            String content = grade_type.get(key);
            ListItem gradeItem = new ListItem();

            gradeItem.setId(key);
            gradeItem.setGradeName(content);
            if ( key / 100 ==  1 ) {
                gradeInfo1.getList().add(gradeItem);
            }
            if ( key / 100 ==  2) {
                gradeInfo2.getList().add(gradeItem);
                gradeInfo5.getList().add(gradeItem);
            }
            if (key / 100 ==  3) {
                gradeInfo3.getList().add(gradeItem);
                gradeInfo5.getList().add(gradeItem);
                gradeInfo6.getList().add(gradeItem);
            }
            if (key / 100 ==  4) {
                gradeInfo4.getList().add(gradeItem);
                gradeInfo6.getList().add(gradeItem);
            }
        }

        Collections.sort(gradeInfo1.getList());
        Collections.sort(gradeInfo2.getList());
        Collections.sort(gradeInfo3.getList());
        Collections.sort(gradeInfo4.getList());
        Collections.sort(gradeInfo5.getList());
        Collections.sort(gradeInfo6.getList());

        List<ListInfo> list  = new ArrayList<>();
        list.add(gradeInfo1);
        list.add(gradeInfo2);
        list.add(gradeInfo3);
        list.add(gradeInfo4);
        list.add(gradeInfo5);
        list.add(gradeInfo6);
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

    static public List<Content2> getArticleChannel() {
        return article_channel;
    }
    static public List<Content2> getChannelList(int grade_stage) {
        List<Content2> list = new ArrayList<>();
        Content2 content2 = null;
        if(grade_stage == 101) {
            content2 = new Content2();
            content2.setChannel_info("801");
            content2.setChannel_name("育儿");
            list.add(content2);
            content2 = new Content2();
            content2.setChannel_info("0");
            content2.setChannel_name("其他");
            list.add(content2);

        } else if(grade_stage == 102){
            content2 = new Content2();
            content2.setChannel_info("101,102,103,104,105,106,107,108,109");
            content2.setChannel_name("学知识");
            list.add(content2);
            content2 = new Content2();
            content2.setChannel_info("201,202,203,301,302,303,304,305,401,402,403,404,405,406,501,502,503,504");
            content2.setChannel_name("兴趣爱好");
            list.add(content2);
            content2 = new Content2();
            content2.setChannel_info("0");
            content2.setChannel_name("其他");
            list.add(content2);
        } else if(grade_stage == 2){
            content2 = new Content2();
            content2.setChannel_info("101");
            content2.setChannel_name("数学");
            list.add(content2);
            content2 = new Content2();
            content2.setChannel_info("102");
            content2.setChannel_name("语文");
            list.add(content2);
            content2 = new Content2();
            content2.setChannel_info("103");
            content2.setChannel_name("英语");
            list.add(content2);
            content2 = new Content2();
            content2.setChannel_info("201,202,203,301,302,303,304,305,401,402,403,404,405,406,501,502,503,504");
            content2.setChannel_name("兴趣爱好");
            list.add(content2);
            content2 = new Content2();
            content2.setChannel_info("0");
            content2.setChannel_name("其他");
            list.add(content2);

        } else if(grade_stage == 3){
            content2 = new Content2();
            content2.setChannel_info("101");
            content2.setChannel_name("数学");
            list.add(content2);
            content2 = new Content2();
            content2.setChannel_info("102");
            content2.setChannel_name("语文");
            list.add(content2);
            content2 = new Content2();
            content2.setChannel_info("103");
            content2.setChannel_name("英语");
            list.add(content2);
            content2 = new Content2();
            content2.setChannel_info("104");
            content2.setChannel_name("物理");
            list.add(content2);
            content2 = new Content2();
            content2.setChannel_info("105");
            content2.setChannel_name("化学");
            list.add(content2);
            content2 = new Content2();
            content2.setChannel_info("0");
            content2.setChannel_name("其他");
            list.add(content2);

        } else if(grade_stage == 4){
            content2 = new Content2();
            content2.setChannel_info("101");
            content2.setChannel_name("数学");
            list.add(content2);
            content2 = new Content2();
            content2.setChannel_info("102");
            content2.setChannel_name("语文");
            list.add(content2);
            content2 = new Content2();
            content2.setChannel_info("103");
            content2.setChannel_name("英语");
            list.add(content2);
            content2 = new Content2();
            content2.setChannel_info("104");
            content2.setChannel_name("物理");
            list.add(content2);
            content2 = new Content2();
            content2.setChannel_info("105");
            content2.setChannel_name("化学");
            list.add(content2);
            content2 = new Content2();
            content2.setChannel_info("0");
            content2.setChannel_name("其他 ");
            list.add(content2);
        } else {
            content2 = new Content2();
            content2.setChannel_info("101");
            content2.setChannel_name("数学");
            list.add(content2);
            content2 = new Content2();
            content2.setChannel_info("102");
            content2.setChannel_name("语文");
            list.add(content2);
            content2 = new Content2();
            content2.setChannel_info("103");
            content2.setChannel_name("英语");
            list.add(content2);
            content2 = new Content2();
            content2.setChannel_info("0");
            content2.setChannel_name("其他 ");
            list.add(content2);
        }
        return list;
    }

}
