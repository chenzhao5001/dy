package com.guidesound.Service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidesound.Service.ICommonService;
import com.guidesound.TempStruct.ClassTime;
import com.guidesound.TempStruct.ItemInfo;
import com.guidesound.TempStruct.NextClassInfo;
import com.guidesound.dao.*;
import com.guidesound.models.*;
import com.guidesound.ret.CommodityInfo;
import com.guidesound.ret.TeacherClass;
import com.guidesound.ret.VideoClass;
import com.guidesound.util.SignMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Service
public class CommonServiceImpl implements ICommonService {

    @Autowired
    IUser iUser;

    @Autowired
    IVideo iVideo;

    @Autowired
    ICourse iCourse;

    @Autowired
    IRecord iRecord;

    @Autowired
    IArticle iArticle;

    @Autowired
    IOrder iOrder;
    @Override
    public void changeUserSurplusAmount(int user_id, int change_amount) {
        List<UserSurplusAmount> lists =  iUser.getUserSurplusAmount(user_id);
        if(lists.size() == 0) {
            iUser.InsertUserSurplusAmount(user_id,change_amount);
        } else {
            UserSurplusAmount userSurplusAmount = lists.get(0);
            int amount = userSurplusAmount.getAmount() + change_amount;
            iUser.updateUserSurplusAmount(user_id,amount);
        }
    }

    @Override
    public void changeUserAmount(int user_id, int change_amount) {

        List<UserAmount> lists =  iUser.getUserAmount(user_id);
        if(lists.size() == 0) {
            int time = (int) (new Date().getTime() / 1000);
            iUser.InsertUserAmount(user_id,change_amount,time,time);
        } else {
            UserAmount userAmount = lists.get(0);
            int amount = userAmount.getAmount() + change_amount;
            iUser.updateUserAmount(user_id,amount);
        }
    }

    public void updateAliPayUserId(int user_id,String alipay_user_id) {
        String alipay_user_id_temp = iUser.getAliPayUserId(user_id);
        if(alipay_user_id_temp == null || alipay_user_id_temp.equals("")) {
            iUser.setAliPayUserId(user_id,alipay_user_id);
        }

    }

    @Override
    public void improveVideoList(List<VideoShow> list_temp, int user_id) {
        List<Integer> idList = new ArrayList<>();
        for (VideoShow item : list_temp) {
            item.setVideo_state(SignMap.getVideoState(item.getExamine_status()));
            item.setWatch_type_name(SignMap.getGradeTypeByID(item.getWatch_type()));
            item.setSubject_name(SignMap.getSubjectTypeById(item.getSubject()));
            String video_temp = item.getVideo_show_path().replace("cos.ap-beijing", "file");
//            String pic_temp = item.getPic_up_path().replace("cos.ap-beijing","file");
            String pic_temp = item.getPic_up_path().replace("cos.ap-beijing", "image");
            item.setVideo_show_path(video_temp);
            item.setPic_up_path(pic_temp);


            String[] temps = ((String) item.getPools()).split(",");
            List<ItemInfo> poolList = new ArrayList<>();
            for (String cell : temps) {
                if (cell.equals("")) {
                    continue;
                }
                ItemInfo itemInfo = new ItemInfo();
                itemInfo.setId(Integer.parseInt(cell));
                itemInfo.setInfo(SignMap.getPoolById(Integer.parseInt(cell)));
                poolList.add(itemInfo);

            }
            if (poolList.size() > 0) {
                item.setPools(poolList);
            }

            if (item.getExamine_status() == 3) {
                item.setExamine_status(0);
            }
            idList.add(item.getUser_id());
        }

        if (user_id != 0) {
            List<Integer> videoIds = iVideo.getCollectionVideoById(user_id);
            if (videoIds != null) {
                for (VideoShow item : list_temp) {
                    if (videoIds.contains(item.getId())) {
                        item.setCollection(true);
                    }
                }
            }
            videoIds = iVideo.getPraiseVideoById(user_id);
            if (videoIds != null) {
                for (VideoShow item : list_temp) {
                    if (videoIds.contains(item.getId())) {
                        item.setPraise(true);
                    }
                }
            }

            List<Integer> userIds = iUser.getFollowUsers(user_id);
            if (userIds != null) {
                for (VideoShow item : list_temp) {
                    if (userIds.contains(item.getUser_id())) {
                        item.setFollow(true);
                    }
                }
            }
        }

        if (idList != null && idList.size() > 0) {
            List<User> userList = iVideo.getUserHeadByIds(idList);
            Map<Integer, User> userMap = new HashMap<>();
            for (User user : userList) {
                userMap.put(user.getId(), user);
            }
            for (VideoShow item : list_temp) {
                if (userMap.containsKey(item.getUser_id())) {
                    item.setUser_head(userMap.get(item.getUser_id()).getHead());
                    item.setUser_name(userMap.get(item.getUser_id()).getName());
                    item.setUser_type(userMap.get(item.getUser_id()).getType());
                    item.setUser_type_name(SignMap.getUserTypeById(userMap.get(item.getUser_id()).getType()));
                    item.setUser_subject(SignMap.getSubjectTypeById(userMap.get(item.getUser_id()).getSubject()));
                    item.setUser_grade(SignMap.getWatchById(userMap.get(item.getUser_id()).getGrade_level()));
                    item.setUser_level(SignMap.getUserLevelById(userMap.get(item.getUser_id()).getLevel()));



                    if(item.getAttachment_id() != 0) {
                        if(item.getAttachment_type() == 1) { // 商品
                            CommodityInfo commodityInfo = iUser.getCommodityInfoByid(item.getAttachment_id());
                            if(commodityInfo != null) {
                                item.setCommodity(commodityInfo);
                            } else {
                                item.setAttachment_type(0);
                                item.setAttachment_id(-1);
                            }

                        } else if(item.getAttachment_type() == 2) {  //录播课
                            VideoClass videoClass = iRecord.getVideoClass(item.getAttachment_id());
                            if(videoClass != null) {
                                videoClass.setSubject_id(Integer.parseInt(videoClass.getSubject()));
                                videoClass.setSubject(SignMap.getSubjectTypeById(Integer.parseInt(videoClass.getSubject())));
                                videoClass.setGrade_id(Integer.parseInt(videoClass.getGrade()));
                                videoClass.setGrade(SignMap.getGradeTypeByID(Integer.parseInt(videoClass.getGrade())));
                                item.setVideo_class(videoClass);
                            } else {
                                item.setAttachment_type(0);
                                item.setAttachment_id(-1);
                            }


                        } else if(item.getAttachment_type() == 3) { // 辅导课
                            Course course = iCourse.getCouresByid(item.getAttachment_id());
                            if(course != null && course.getCourse_status() == 3) {
                                TeacherClass teacherClass = new TeacherClass();
                                teacherClass.setCourse_id(course.getId());
                                teacherClass.setCourse_name(course.getCourse_name());
                                teacherClass.setCourse_pic(course.getCourse_pic());
                                teacherClass.setCourse_status(course.getCourse_status());
                                teacherClass.setCourse_type(course.getType());
                                teacherClass.setCourse_type_name(SignMap.getCourseTypeNameById(course.getType()));
                                teacherClass.setForm(SignMap.getCourseFormById(course.getForm()));
                                teacherClass.setGrade(SignMap.getGradeTypeByID(course.getGrade()));
                                teacherClass.setPrice(course.getAll_charge());
                                teacherClass.setStudent_count(course.getMax_person());
                                teacherClass.setSubject(SignMap.getSubjectTypeById(course.getSubject()));
                                item.setTeacher_class(teacherClass);
                            } else {
                                item.setAttachment_type(0);
                                item.setAttachment_id(-1);
                            }
                        }
                    }

                }
            }
        }
    }

    @Override
    public void improveArticleList(List<ArticleInfo> list,int user_id) {
        if (list.size() < 1) {
            return;
        }
        int currentUserID = user_id;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        List<Integer> collectList = new ArrayList<>();
        List<Integer> followList = new ArrayList<>();
        List<Integer> praiseList = new ArrayList<>();
        if (currentUserID != 0) {
            collectList = iArticle.getArticleListByUserId(currentUserID);
            followList = iUser.getFollowUsers(currentUserID);
            praiseList = iArticle.getPraiseListByUserId(currentUserID);
        }


        List<Integer> user_ids = new ArrayList<>();
        for (ArticleInfo item : list) {
            if (collectList.contains(item.getId())) {
                item.setCollection(true);
            }
            if (followList.contains(item.getUser_id())) {
                item.setFollow(true);
            }
            if (praiseList.contains(item.getId())) {
                item.setPraise(true);
            }

            if(item.getType() == 1) {
                item.setSubject_name(SignMap.getSubjectTypeById(item.getSubject()));
            } else {
                item.setSubject_name(SignMap.getSubjectTypeById(item.getAsk_subject()));
            }


            item.setContent_url(request.getScheme() + "://" + request.getServerName() + ":"
                    + request.getServerPort() + "/guidesound/article/preview?article_id=" + item.getId());
            if (!user_ids.contains(item.getUser_id())) {
                user_ids.add(item.getUser_id());
            }

            //推荐池解析
            String[] temps = ((String) item.getPools()).split(",");
            List<ItemInfo> poolList = new ArrayList<>();
            for (String cell : temps) {
                if (cell.equals("")) {
                    continue;
                }
                ItemInfo itemInfo = new ItemInfo();
                itemInfo.setId(Integer.parseInt(cell));
                itemInfo.setInfo(SignMap.getPoolById(Integer.parseInt(cell)));
                poolList.add(itemInfo);

            }
            if (poolList.size() > 0) {
                item.setPools(poolList);
            }

            item.setArticle_state(SignMap.getVideoState(item.getExamine_status()));

        }


        if (user_ids.size() > 0) {
            List<UserInfo> userList = iUser.getUserByIds(user_ids);
            Map<Integer, UserInfo> userMap = new HashMap<>();
            for (UserInfo user : userList) {
                userMap.put(user.getId(), user);
            }
            for (ArticleInfo item : list) {
                if (userMap.containsKey(item.getUser_id())) {
                    item.setUser_head(userMap.get(item.getUser_id()).getHead());
                    item.setUser_name(userMap.get(item.getUser_id()).getName());
                    item.setUser_type(userMap.get(item.getUser_id()).getType());
                    item.setAuth_info(userMap.get(item.getUser_id()).getAuth_info());
                    item.setUser_subject(userMap.get(item.getUser_id()).getSubject());
                    item.setUser_subject_name(SignMap.getSubjectTypeById(userMap.get(item.getUser_id()).getSubject()));

                    item.setUser_grade(userMap.get(item.getUser_id()).getGrade());
                    item.setUser_grade_name(SignMap.getGradeTypeByID(userMap.get(item.getUser_id()).getGrade()));

                    item.setUser_grade_level(userMap.get(item.getUser_id()).getGrade_level());
                    item.setUser_grade_level_name(SignMap.getWatchById(userMap.get(item.getUser_id()).getGrade_level()));
                    item.setGrade(SignMap.getGradeTypeByID((Integer)item.getGrade()));


                    if (item.getAttachment_id() != 0) {
                        if (item.getAttachment_type() == 1) { // 商品
                            CommodityInfo commodityInfo = iUser.getCommodityInfoByid(item.getAttachment_id());
                            if (commodityInfo != null) {
                                item.setCommodity(commodityInfo);
                            } else {
                                item.setAttachment_type(0);
                                item.setAttachment_id(-1);
                            }

                        } else if (item.getAttachment_type() == 2) {  //录播课
                            VideoClass videoClass = iRecord.getVideoClass(item.getAttachment_id());
                            if (videoClass != null) {
                                videoClass.setSubject_id(Integer.parseInt(videoClass.getSubject()));
                                videoClass.setSubject(SignMap.getSubjectTypeById(Integer.parseInt(videoClass.getSubject())));
                                videoClass.setGrade_id(Integer.parseInt(videoClass.getGrade()));
                                videoClass.setGrade(SignMap.getGradeTypeByID(Integer.parseInt(videoClass.getGrade())));
                                item.setVideo_class(videoClass);
                            } else {
                                item.setAttachment_type(0);
                                item.setAttachment_id(-1);
                            }


                        } else if (item.getAttachment_type() == 3) { // 辅导课
                            Course course = iCourse.getCouresByid(item.getAttachment_id());
                            if (course != null && course.getCourse_status() == 3) {
                                TeacherClass teacherClass = new TeacherClass();
                                teacherClass.setCourse_id(course.getId());
                                teacherClass.setCourse_name(course.getCourse_name());
                                teacherClass.setCourse_pic(course.getCourse_pic());
                                teacherClass.setCourse_status(course.getCourse_status());
                                teacherClass.setCourse_type(course.getType());
                                teacherClass.setCourse_type_name(SignMap.getCourseTypeNameById(course.getType()));
                                teacherClass.setForm(SignMap.getCourseFormById(course.getForm()));
                                teacherClass.setGrade(SignMap.getGradeTypeByID(course.getGrade()));
                                if(course.getType() == 0) {
                                    teacherClass.setPrice(course.getPrice_one_hour());
                                } else {
                                    teacherClass.setPrice(course.getAll_charge());
                                }

                                teacherClass.setStudent_count(course.getMax_person());
                                teacherClass.setSubject(SignMap.getSubjectTypeById(course.getSubject()));
                                item.setTeacher_class(teacherClass);
                            } else {
                                item.setAttachment_type(0);
                                item.setAttachment_id(-1);
                            }
                        }
                    }
                }
            }
        }
        return;
    }


    //1 结束 2 未结束 3 课程大纲失败 4 大纲无数据 5 当前没有发布的学时
    @Override
    public  NextClassInfo isClassFinish(int class_id,int user_id) {
        NextClassInfo nextClassInfo = new NextClassInfo();
        nextClassInfo.next_class_NO = -1;
        ClassRoom classRoom = iOrder.getClassRoomById(class_id);
        List<ClassTime> class_item_list = null;
        ObjectMapper mapper_temp = new ObjectMapper();
        try {
            if (classRoom.getOutline().equals("") || classRoom.getOutline() == null) {
                class_item_list = new ArrayList<>();
            } else {
                class_item_list = mapper_temp.readValue(classRoom.getOutline(), new TypeReference<List<ClassTime>>() {
                });
            }

        } catch (IOException e) {
            nextClassInfo.next_class_NO = 0;
            nextClassInfo.next_class_name = "解析大纲失败";
            nextClassInfo.next_clsss_time = 0;
            nextClassInfo.next_class_hour = 0;
            return nextClassInfo;

        }

        if (class_item_list.size() == 0 && classRoom.getType() == 1) {
            nextClassInfo.next_class_NO = 0;
            nextClassInfo.next_class_name = "大纲无内容";
            nextClassInfo.next_clsss_time = 0;
            nextClassInfo.next_class_hour = 0;
            return nextClassInfo;
        } else if (class_item_list.size() == 0 && classRoom.getType() == 0) {
            nextClassInfo.next_class_NO = 0;
            nextClassInfo.next_clsss_time = 0;
            nextClassInfo.next_class_hour = 0;
            if (classRoom.getUser_id() == user_id) {
                nextClassInfo.next_class_name = "需要你发布新课时";
            } else {
                nextClassInfo.next_class_name = "等待老师发布新课时";
            }
            return nextClassInfo;
        }
        ClassTime lastClassTime = class_item_list.get(class_item_list.size() - 1);
        if (classRoom.getType() == 0) { //1v1
            int all_time = 0;
            for (ClassTime item : class_item_list) {
                all_time += item.getClass_hours();
            }
            if (all_time >= classRoom.getAll_hours() && lastClassTime.getClass_time() + lastClassTime.getClass_hours() * 3600 < new Date().getTime() / 1000) {
                nextClassInfo.next_class_NO = 0;
                nextClassInfo.next_class_name = "课程已结束";
                nextClassInfo.next_clsss_time = 0;
                nextClassInfo.next_class_hour = 0;
                return nextClassInfo;
            }
            if (all_time < classRoom.getAll_hours() && (lastClassTime.getClass_time() + lastClassTime.getClass_hours() * 3600) < new Date().getTime() / 1000) {
                nextClassInfo.next_class_NO = 0;
                nextClassInfo.next_clsss_time = 0;
                nextClassInfo.next_class_hour = 0;

                if (classRoom.getUser_id() == user_id) {
                    nextClassInfo.next_class_name = "需要你发布新课时";
                } else {
                    nextClassInfo.next_class_name = "等待老师发布新课时";
                }
                return nextClassInfo;
            }
        } else { //班课
            if (lastClassTime.getClass_time() + lastClassTime.getClass_hours() * 3600 < new Date().getTime() / 1000) {
                nextClassInfo.next_class_NO = 0;
                nextClassInfo.next_class_name = "课程已结束";
                nextClassInfo.next_clsss_time = 0;
                nextClassInfo.next_class_hour = 0;
                return nextClassInfo;
            }
        }

        //当前时间
        for (ClassTime item : class_item_list) {
            int beginTime = item.getClass_time();
            int endTime = item.getClass_time() + 3600 * item.getClass_hours();
            int currentTime = (int) (new Date().getTime() / 1000);
            if (currentTime >= beginTime && currentTime <= endTime) {
                nextClassInfo.next_class_name = item.getClass_content();
                nextClassInfo.next_class_hour = item.getClass_hours();
                nextClassInfo.next_clsss_time = item.getClass_time();
                nextClassInfo.next_class_NO = item.getClass_number();
                return nextClassInfo;
            }
        }
        //下次课时间
        for (ClassTime item : class_item_list) {
            int beginTime = item.getClass_time();
            int currentTime = (int) (new Date().getTime() / 1000);
            if (currentTime < beginTime + 3600 * item.getClass_hours()) {
                nextClassInfo.next_class_name = item.getClass_content();
                nextClassInfo.next_class_hour = item.getClass_hours();
                nextClassInfo.next_clsss_time = item.getClass_time();
                nextClassInfo.next_class_NO = item.getClass_number();
                break;
            }
        }
        return nextClassInfo;
    }

}
