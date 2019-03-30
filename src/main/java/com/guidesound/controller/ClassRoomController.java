package com.guidesound.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidesound.TempStruct.CourseOutline;
import com.guidesound.dao.IOrder;
import com.guidesound.dao.IRecord;
import com.guidesound.dao.IUser;
import com.guidesound.dao.IUserFollow;
import com.guidesound.models.*;
import com.guidesound.ret.ClassInfo;
import com.guidesound.ret.ClassRoomRet;
import com.guidesound.ret.TeacherClass1;
import com.guidesound.ret.TeacherClass2;
import com.guidesound.util.JSONResult;
import com.guidesound.util.SignMap;
import com.qcloud.Common.Sign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    JSONResult getClassById(String class_id) throws IOException {
        if(class_id == null) {
            return JSONResult.errorMsg("缺少 class_id 参数");
        }

        ClassRoom classRoom = iOrder.getClassRoomById(Integer.parseInt(class_id));
        if( classRoom == null) {
            return  JSONResult.ok(null);
        }


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

    @RequestMapping("/my_class_room")
    @ResponseBody
    JSONResult myClassRoom() {
        class Student {
            int order_id;
            int order_status;

            public int getOrder_id() {
                return order_id;
            }

            public void setOrder_id(int order_id) {
                this.order_id = order_id;
            }

            public int getOrder_status() {
                return order_status;
            }

            public void setOrder_status(int order_status) {
                this.order_status = order_status;
            }
        }

        class Teacher {
            int class_info_id;
            int class_info_status;

            public int getClass_info_id() {
                return class_info_id;
            }

            public void setClass_info_id(int class_info_id) {
                this.class_info_id = class_info_id;
            }

            public int getClass_info_status() {
                return class_info_status;
            }

            public void setClass_info_status(int class_info_status) {
                this.class_info_status = class_info_status;
            }
        }

        int user_id = getCurrentUserId();
        List<ClassInfo> classInfoList = new ArrayList<>();
        List<StudentClass> student_list =  iOrder.getStudentClassByUserId(user_id);
        if(student_list.size() > 0) {
            List<Integer> id_list = new ArrayList<>();
            for (StudentClass item : student_list) {
                id_list.add(item.getClass_id());
            }
            List<ClassRoom> rooms = iOrder.getClassInfoByIds(id_list);
            List<ClassRoom> rooms_teacher = iOrder.getClassInfoByUserId(user_id);
            for(ClassRoom item : rooms_teacher) {
                item.flag = 1;
                rooms.add(item);
            }

            for (ClassRoom item : rooms) {

                TeacherClass1 teacherClass1 = new TeacherClass1();
                teacherClass1.setClass_id(item.getClass_id());
                teacherClass1.setCourse_name(item.getCourse_name());
                teacherClass1.setCourse_pic(item.getCourse_pic());
                teacherClass1.setCourse_owner_id(item.getUser_id());
                UserInfo userInfo = iUser.getUser(item.getUser_id());
                if(userInfo != null) {
                    teacherClass1.setCourse_owner_name(userInfo.getName());
                    teacherClass1.setCourse_owner_head(userInfo.getHead());
                }
                teacherClass1.setMax_person(item.getMax_person());
                teacherClass1.setSubject(SignMap.getSubjectTypeById(item.getSubject()));
                teacherClass1.setGrade(SignMap.getGradeTypeByID(item.getGrade()));
                teacherClass1.setForm(SignMap.getCourseFormById(item.getForm()));
                teacherClass1.setForm_id(item.getForm());
                teacherClass1.setWay(item.getWay());
                teacherClass1.setNext_class_NO(1);
                teacherClass1.setNext_class_name("一元一次方程经典应用实例得到的点点");
                teacherClass1.setNext_clsss_time(123);

                if(item.flag == 0 ) {
                    Student student = new Student();
                    List<StudentClass> s_list = iOrder.getStudentClassByInfo(getCurrentUserId(),item.getClass_id());
                    if(s_list.size() > 0) {
                        student.setOrder_id(s_list.get(0).getOrder_id());
                        OrderInfo orderInfo = iOrder.getUserByOrderId(s_list.get(0).getOrder_id());
                        if(orderInfo != null) {
                            student.setOrder_status(orderInfo.getOrder_status());
                        }
                    }
                    teacherClass1.setStudent(student);
                    teacherClass1.setTeacher(null);
                } else if (item.flag == 1) {
                    Teacher teacher = new Teacher();
                    teacherClass1.setStudent(null);
                    teacherClass1.setTeacher(teacher);

                }
                ClassInfo classInfo = new ClassInfo();
                classInfo.setTeacher_class(teacherClass1);
                classInfo.setVideo_class(null);
                classInfoList.add(classInfo);
            }
        }

        return JSONResult.ok(classInfoList);
    }

    @RequestMapping("/test_listen")
    @ResponseBody
    JSONResult testListen() {
        int user_id = getCurrentUserId();
        List<ClassInfo> classInfo_list = new ArrayList<>();
        List<ClassRoom> list = iOrder.getAllClassRoom();
        List<Integer> user_ids = new ArrayList<>();
        Map<Integer, UserInfo> m_users = new HashMap<>();
        for(ClassRoom item : list) {
            if(!list.contains(item.getUser_id())) {
                user_ids.add(item.getUser_id());
            }
        }
        if(user_ids.size() > 0) {
            List<UserInfo> users = iUser.getUserByIds(user_ids);
            for(UserInfo userInfo : users) {
                m_users.put(userInfo.getId(),userInfo);
            }
        }

        for (ClassRoom item : list) {
            TeacherClass2 teacherClass2 = new TeacherClass2();
            teacherClass2.setClass_id(item.getClass_id());
            teacherClass2.setCourse_name(item.getCourse_name());
            teacherClass2.setCourse_pic(item.getCourse_pic());
            teacherClass2.setCourse_owner_id(item.getUser_id());
            if(m_users.get(item.getUser_id()) != null) {
                teacherClass2.setCourse_owner_head(m_users.get(item.getUser_id()).getHead());
                teacherClass2.setCourse_owner_name(m_users.get(item.getUser_id()).getName());
            }
            teacherClass2.setMax_person(item.getMax_person());
            teacherClass2.setSubject(SignMap.getSubjectTypeById(item.getSubject()));
            teacherClass2.setGrade(SignMap.getGradeTypeByID(item.getGrade()));
            teacherClass2.setForm(SignMap.getCourseFormById(item.getForm()));
            teacherClass2.setForm_id(item.getForm());
            teacherClass2.setWay(item.getWay());
            ClassInfo classInfo = new ClassInfo();
            classInfo.setTeacher_class(teacherClass2);
            classInfo.setVideo_class(null);
            classInfo_list.add(classInfo);
        }
        return JSONResult.ok(classInfo_list);
    }

}
