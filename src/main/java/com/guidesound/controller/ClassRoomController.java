package com.guidesound.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidesound.TempStruct.CourseOutline;
import com.guidesound.dao.IOrder;
import com.guidesound.dao.IRecord;
import com.guidesound.dao.IUser;
import com.guidesound.dao.IUserFollow;
import com.guidesound.models.ClassRoom;
import com.guidesound.models.Record;
import com.guidesound.models.UserInfo;
import com.guidesound.ret.ClassRoomRet;
import com.guidesound.util.JSONResult;
import com.guidesound.util.SignMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/class_room")
public class ClassRoomController extends BaseController {

    @Autowired
    IOrder  iOrder;
    @Autowired
    IRecord iRecord;
    @Autowired
    IUser iUser;

    @RequestMapping("/enter_class_room")
    @ResponseBody
    JSONResult enterClassRoom(String class_id) {
        if(class_id == null) {
            return JSONResult.errorMsg("缺少 class_id 参数");
        }
        ClassRoom classRoom = iOrder.getClassRoomById(Integer.parseInt(class_id));
        if(classRoom == null) {
            return JSONResult.errorMsg("class_id不存在");
        }
        class Ret {
            int room_number;
            int ishost;

            public int getRoom_number() {
                return room_number;
            }

            public void setRoom_number(int room_number) {
                this.room_number = room_number;
            }

            public int getIshost() {
                return ishost;
            }

            public void setIshost(int ishost) {
                this.ishost = ishost;
            }
        }

        Ret ret = new Ret();
        ret.setRoom_number(classRoom.getRoom_number());
        int user_id = classRoom.getUser_id();
        if(user_id == getCurrentUserId()) {
            ret.setIshost(0);
        } else {
            ret.setIshost(1);
        }
        return JSONResult.ok(ret);
    }

    @RequestMapping("/leave_class_room")
    @ResponseBody
    JSONResult leaveClassRoom(String class_id) {
        if(class_id == null) {
            return JSONResult.errorMsg("缺少 class_id 参数");
        }
        return JSONResult.ok();
    }

    @RequestMapping("/report_record_class")
    @ResponseBody
    JSONResult reportRecordClass(String record_course_id,String last_class_no,String last_class_pos) {
        if(record_course_id == null || last_class_no == null || last_class_pos == null) {
            return JSONResult.errorMsg("缺少参数");
        }
        Record record = iRecord.get(Integer.parseInt(record_course_id));
        if(record == null) {
            return JSONResult.ok("录播课不存在");
        }

        iRecord.report(Integer.parseInt(record_course_id),Integer.parseInt(last_class_no),Integer.parseInt(last_class_pos));
        return JSONResult.ok();
    }

    @RequestMapping("/get_class")
    @ResponseBody
    JSONResult getClassById(String class_info_id) throws IOException {
        if(class_info_id == null) {
            return JSONResult.errorMsg("缺少 class_info_id 参数");
        }

        ClassRoom classRoom = iOrder.getClassRoomById(Integer.parseInt(class_info_id));
        if( classRoom == null) {
            return  JSONResult.ok(null);
        }




        Object class_use_info;
        Object outline;
        Object students;



        ClassRoomRet classRoomRet = new ClassRoomRet();
        classRoomRet.setClass_info_status(classRoom.getClass_info_status());
        classRoomRet.setCourse_owner_id(classRoom.getUser_id());
        UserInfo userInfo = iUser.getUser(classRoom.getUser_id());
        if( userInfo != null) {
            classRoomRet.setCourse_name(userInfo.getName());
            classRoomRet.setCourse_owner_head(userInfo.getHead());
        }
        classRoomRet.setCourse_name(classRoom.getCourse_name());
        classRoomRet.setTeacher_name(classRoom.getTeacher_name());
        classRoomRet.setSubject(SignMap.getSubjectTypeById(classRoom.getSubject()));
        classRoomRet.setGrade(SignMap.getGradeTypeByID(classRoom.getGrade()));
        classRoomRet.setForm(SignMap.getCourseFormById(classRoom.getForm()));
        classRoomRet.setWay(classRoom.getWay());
        classRoomRet.setMax_person(classRoom.getMax_person());
        classRoomRet.setAll_hours(classRoom.getAll_hours());
        classRoomRet.setPrice_one_hour(classRoom.getPrice_one_hour());
        classRoomRet.setAll_charge(classRoom.getAll_charge());
        classRoomRet.setCreate_time(classRoom.getCreate_time());
        classRoomRet.setRefund_rule(classRoom.getRefund_rule());
        classRoomRet.setTutor_content(classRoom.getTutor_content());
        ObjectMapper mapper = new ObjectMapper();
        List<CourseOutline> beanList = mapper.readValue(classRoom.getOutline(), new TypeReference<List<CourseOutline>>() {});
        classRoomRet.setOutline(beanList);

        return JSONResult.ok(classRoomRet);
    }

}
