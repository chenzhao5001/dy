package com.guidesound.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidesound.TempStruct.ClassTime;
import com.guidesound.TempStruct.RecordCoursePic;
import com.guidesound.TempStruct.RecordTeacherPic;
import com.guidesound.TempStruct.RecordVideo;
import com.guidesound.dao.IExamine;
import com.guidesound.dao.IRecord;
import com.guidesound.dao.IUser;
import com.guidesound.dto.RecordDTO;
import com.guidesound.models.*;
import com.guidesound.ret.RecordItem;
import com.guidesound.ret.WonderfulPart;
import com.guidesound.util.JSONResult;
import com.guidesound.util.SignMap;
import com.guidesound.util.ToolsFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/record")
public class RecordCourseController extends BaseController {

    @Autowired
    IRecord iRecord;
    @Autowired
    IExamine iExamine;
    @Autowired
    IUser iUser;

    @RequestMapping("/add")
    @ResponseBody
    JSONResult add(@Valid RecordDTO recordDTO, BindingResult result) {
        StringBuilder msg = new StringBuilder();
        if (!ToolsFunction.paramCheck(result, msg)) {
            return JSONResult.errorMsg(msg.toString());
        }
        recordDTO.setUser_id(getCurrentUserId());
        if (recordDTO.getSave().equals("0")) {
            recordDTO.setRecord_course_status(0);
        } else {
            recordDTO.setRecord_course_status(1);
        }


        if (recordDTO.getRecord_course_id().equals("0")) {
            recordDTO.setCreate_time((int) (new Date().getTime() / 1000));
            iRecord.add(recordDTO);
        } else {
            recordDTO.setUpdate_time((int) (new Date().getTime() / 1000));
            iRecord.update(recordDTO);
        }
        return JSONResult.ok();
    }

    @RequestMapping("/delete")
    @ResponseBody
    JSONResult delete(String record_course_id) {
        if (record_course_id == null) {
            return JSONResult.errorMsg("缺少 record_course_id 参数");
        }
        iRecord.delete(getCurrentUserId(), Integer.parseInt(record_course_id));
        return JSONResult.ok();
    }

    @RequestMapping("/list")
    @ResponseBody
    JSONResult list(String who, String user_id) {
        if (who == null || user_id == null) {
            return JSONResult.errorMsg("缺少 who 或 user_id 参数");
        }
        List<Record> list = iRecord.list(Integer.parseInt(user_id));
        List<RecordItem> recordList = new ArrayList<>();
        for (Record item : list) {
            RecordItem temp = new RecordItem();
            temp.setGrade(SignMap.getGradeTypeByID((int) item.getGrade()));
            temp.setGrade_id((int) (item.getGrade()));
            temp.setPrice(item.getPrice());
            temp.setRecord_course_id(item.getRecord_course_id());
            temp.setRecord_course_name(item.getRecord_course_name());
            temp.setRecord_course_pic(item.getRecord_course_pic());
            temp.setVideo_count(item.getVideo_count());
            temp.setRecord_course_status(item.getRecord_course_status());
            temp.setSubject(SignMap.getSubjectTypeById((int) item.getSubject()));
            temp.setSubject_id((int) item.getSubject());
            recordList.add(temp);
        }
        return JSONResult.ok(recordList);
    }

    @RequestMapping("/get_by_id")
    @ResponseBody
    JSONResult getById(String record_course_id) {
        if (record_course_id == null) {
            return JSONResult.errorMsg("");
        }
        Record record = iRecord.get(Integer.parseInt(record_course_id));
        if (record == null) {
            return JSONResult.ok();
        }

        record.setRecord_owner_id(record.getUser_id());
        UserInfo userInfo = iUser.getUser(record.getUser_id());
        if (userInfo != null) {
            record.setRecord_owner_name(userInfo.getName());
            record.setRecord_owner_head(userInfo.getHead());
        }

        record.setGrade_id((Integer) record.getGrade());
        record.setGrade(SignMap.getGradeTypeByID(record.getGrade_id()));
        record.setSubject_id((Integer) record.getSubject());
        record.setSubject(SignMap.getSubjectTypeById(record.getSubject_id()));

        int count = iRecord.getUserRecordCountByCourseId(record.getRecord_course_id());
        record.setStudent_count(count);

        List<UserRecordCourse> lists_temp = iRecord.getUserRecordByUserIdAndCourseId(getCurrentUserId(), record.getRecord_course_id());
        if (lists_temp.size() == 0) {
            record.setIs_pay(false);
        } else {
            record.setIs_pay(true);
        }

        List<RecordTeacherPic> recordTeacherPicList = null;
        ObjectMapper mapper_temp = new ObjectMapper();
        try {
            recordTeacherPicList = mapper_temp.readValue((String) record.getIntro_teacher_pic(), new TypeReference<List<RecordTeacherPic>>() {
            });
            record.setIntro_teacher_pic(recordTeacherPicList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mapper_temp = new ObjectMapper();
        List<RecordCoursePic> recordCoursePicList = null;
        try {
            recordCoursePicList = mapper_temp.readValue((String) record.getIntro_course_pic(), new TypeReference<List<RecordCoursePic>>() {
            });
            record.setIntro_course_pic(recordCoursePicList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mapper_temp = new ObjectMapper();
        List<RecordVideo> recordVideoList = null;
        List<UserRecordCourse> userRecords = iRecord.getUserRecordByCourseId(Integer.parseInt(record_course_id));
        List<Integer> user_ids = new ArrayList<>();
        user_ids.add(record.getUser_id());
        for (UserRecordCourse userRecordCourse : userRecords) {
            user_ids.add(userRecordCourse.getUser_id());
        }
        try {
            String temp = (String) record.getVideos();
            System.out.println(temp);
            JavaType javaType = getCollectionType(ArrayList.class, RecordVideo.class);
            recordVideoList = mapper_temp.readValue((String) record.getVideos(), javaType);
            for (RecordVideo recordVideo : recordVideoList) {
                String cdnClassUrl = recordVideo.getClass_url().replace("cos.ap-beijing", "file");
                if (!user_ids.contains(getCurrentUserId()) && recordVideo.getCharge_type() == 1) {
                    recordVideo.setClass_url("");
                } else {
                    recordVideo.setClass_url(cdnClassUrl);
                }
            }
            record.setVideos(recordVideoList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return JSONResult.ok(record);
    }

    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    ///录播课审核成功
    @RequestMapping("/record_course_finish")
    @ResponseBody
    public JSONResult recordCourseFinish(String record_course_id) {
        if (record_course_id == null) {
            return JSONResult.errorMsg("缺少 record_course_id 参数");
        }
        iLogService.addLog("转码服务器","record_course_finish",record_course_id);

        Record record = iRecord.get(Integer.parseInt(record_course_id));
        if(record == null) {
            return JSONResult.errorMsg("课程不存在");
        }

        ObjectMapper mapper_temp = new ObjectMapper();
        List<RecordVideo> recordVideoList = null;
        try {
            JavaType javaType = getCollectionType(ArrayList.class, RecordVideo.class);
            recordVideoList = mapper_temp.readValue((String) record.getVideos(), javaType);
            for (RecordVideo recordVideo : recordVideoList) {
                try {
                    if (recordVideo.getWonderful_part() != null) {
                        WonderfulPart wonderfulPart = recordVideo.getWonderful_part();
                        TestRecordCourse testRecordCourse = new TestRecordCourse();
                        testRecordCourse.setUser_id(record.getUser_id());
                        testRecordCourse.setRecord_course_id(record.getRecord_course_id());
                        testRecordCourse.setClass_NO(recordVideo.getClass_number());
                        testRecordCourse.setClass_name(recordVideo.getClass_title());
                        testRecordCourse.setClass_url(recordVideo.getClass_url());
                        testRecordCourse.setTime_start(wonderfulPart.getTime_start());
                        testRecordCourse.setTime_end(wonderfulPart.getTime_end());
                        testRecordCourse.setPicture(wonderfulPart.getPicture() != null ? wonderfulPart.getPicture(): "");
                        iRecord.addTestRecordCourse(testRecordCourse);
                    }
                } catch (Exception e) {
                    return JSONResult.errorMsg(e.getMessage());
//                    System.out.println(e);
                }
            }
            return JSONResult.ok();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSONResult.ok();
    }

}
