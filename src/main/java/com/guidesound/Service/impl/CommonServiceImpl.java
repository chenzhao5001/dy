package com.guidesound.Service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.guidesound.Service.ICommonService;
import com.guidesound.TempStruct.ItemInfo;
import com.guidesound.dao.ICourse;
import com.guidesound.dao.IRecord;
import com.guidesound.dao.IUser;
import com.guidesound.dao.IVideo;
import com.guidesound.models.*;
import com.guidesound.ret.CommodityInfo;
import com.guidesound.ret.TeacherClass;
import com.guidesound.ret.VideoClass;
import com.guidesound.util.SignMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


}
