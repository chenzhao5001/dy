package com.guidesound.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidesound.TempStruct.ClassTime;
import com.guidesound.TempStruct.RecordCoursePic;
import com.guidesound.TempStruct.RecordTeacherPic;
import com.guidesound.TempStruct.RecordVideo;
import com.guidesound.dao.IRecord;
import com.guidesound.dto.RecordDTO;
import com.guidesound.models.Record;
import com.guidesound.ret.RecordItem;
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
    JSONResult list(String who) {
        if (who == null) {
            return JSONResult.errorMsg("缺少 who 参数");
        }
        List<Record> list = iRecord.list();
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
        try {
            String temp = (String) record.getVideos();
            System.out.println(temp);
            JavaType javaType = getCollectionType(ArrayList.class, RecordVideo.class);
            recordVideoList = mapper_temp.readValue((String) record.getVideos(), javaType);

//
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode node = mapper.readTree(temp);
//            String resultJson = node.get("result").toString();

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
}
