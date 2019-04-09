package com.guidesound.controller;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidesound.TempStruct.ClassTime;
import com.guidesound.TempStruct.ClassUseInfo;
import com.guidesound.TempStruct.CourseOutline;
import com.guidesound.TempStruct.StudentOrderTemp;
import com.guidesound.dao.*;
import com.guidesound.models.*;
import com.guidesound.ret.ClassInfo;
import com.guidesound.ret.ClassRoomRet;
import com.guidesound.ret.TeacherClass1;
import com.guidesound.ret.TeacherClass2;
import com.guidesound.util.JSONResult;
import com.guidesound.util.SignMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.*;


@Controller
@RequestMapping("/class_room")
public class ClassRoomController extends BaseController {

    int getTeacherClassState(ClassRoom classRoom) {
        if(classRoom.getIstest() == 1) {
            return 1;
        }
        ObjectMapper mapper_temp = new ObjectMapper();
        List<ClassTime> beanList = null;
        if (classRoom.getOutline().equals("") || classRoom.getOutline() == null) {
            beanList = new ArrayList<>();
        } else {
            try {
                beanList = mapper_temp.readValue(classRoom.getOutline(), new TypeReference<List<ClassTime>>() {
                });
            } catch (IOException e) {
                return 1;
            }
        }
        int all_hours = 0;
        int last_time = 0;
        int last_hour = 0;
        for (ClassTime item : beanList) {
            all_hours += item.getClass_hours();
            last_time = item.getClass_time();
            last_hour = item.getClass_hours();
        }

        //课程状态
        if (classRoom.getType() == 0) { //1v1
            if (iOrder.getReturnOrderByClassId(classRoom.getClass_id()) > 0) {
                return 4;
            } else {

                if(classRoom.getUser_id() == getCurrentUserId()) { //老师
                    if (all_hours >= classRoom.getAll_hours() && (last_time + 3600 * last_hour) < new Date().getTime() / 1000) {
                        return 3;
                    } else {
                        return 1;
                    }
                } else { //学生
                    List<StudentClass> list_student = iOrder.getStudentClassByClassId(classRoom.getClass_id());
                    if(list_student.size() != 0) {
                        int order_id = list_student.get(0).getOrder_id();
                        OrderInfo orderInfo = iOrder.getOrderById(order_id);
                        if(orderInfo != null) {
                            if(orderInfo.getRefund_amount() != 0) {
                                return 4;
                            } else {
                                if(all_hours == orderInfo.getAll_hours() && last_time < new Date().getTime() / 1000) {
                                    return 3;
                                } else {
                                    return orderInfo.getOrder_status();
                                }
                            }
                        }
                    }
                }

            }

        } else { //班课
            if(classRoom.getUser_id() == getCurrentUserId()) {
                ClassTime courseOutline = beanList.get(beanList.size() - 1);
                if (courseOutline.getClass_time() < new Date().getTime() / 1000) {
                    return 3;
                } else {
                    if (iOrder.getNoReturnOrderByClassId(classRoom.getClass_id()) > 0) {
                        return 1;
                    } else {
                        return 3;
                    }
                }
            } else {

                List<StudentClass> list_student = iOrder.getStudentClassByClassId(classRoom.getClass_id());
                if(list_student.size() != 0) {
                    int order_id = list_student.get(0).getOrder_id();
                    OrderInfo orderInfo = iOrder.getOrderById(order_id);
                    if(orderInfo != null) {
                        if(orderInfo.getRefund_amount() > 0) {
                            return 4;
                        } else {
                            if(last_time < new Date().getTime() / 1000) {
                                return 3;
                            } else {
                                return orderInfo.getOrder_status();
                            }
                        }
                    }
                }
            }
        }
        return 1;
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


    @Autowired
    IOrder iOrder;
    @Autowired
    IRecord iRecord;
    @Autowired
    IUser iUser;

    @Autowired
    ICourse iCourse;

    class NextClassInfo {
        int next_class_NO;
        String next_class_name;
        int next_clsss_time;
        int next_class_hour;
    }

    //1 结束 2 未结束 3 课程大纲失败 4 大纲无数据 5 当前没有发布的学时
    NextClassInfo isClassFinish(int class_id) {
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
            if (classRoom.getUser_id() == getCurrentUserId()) {
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
                if (classRoom.getUser_id() == getCurrentUserId()) {
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


    @RequestMapping("/enter_class_room")
    @ResponseBody
    JSONResult enterClassRoom(String class_id) {
        if (class_id == null) {
            return JSONResult.errorMsg("缺少 class_id 参数");
        }
        ClassRoom classRoom = iOrder.getClassRoomById(Integer.parseInt(class_id));
        if (classRoom == null) {
            return JSONResult.errorMsg("class_id不存在");
        }

        List<StudentClass> studentClasses = iOrder.getStudentClassByClassId(Integer.parseInt(class_id));
        if (studentClasses.size() < 0) {
            return JSONResult.errorMsg("课堂信息不存在");
        }


        List<ClassTime> class_item_list = null;
        ObjectMapper mapper_temp = new ObjectMapper();
        try {
            String outline = classRoom.getOutline();
            if(outline == null || outline.equals("")) {
                outline = "[]";
            }
            class_item_list = mapper_temp.readValue(outline, new TypeReference<List<ClassTime>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return JSONResult.errorMsg("课堂大纲解析失败");
        }
        if (class_item_list.size() == 0) {
            if(classRoom.getType() == 0) { //1v1
                return JSONResult.errorMsg("当前没有已发布且未未上课的课时");
            }
            return JSONResult.errorMsg("课堂大纲无数据");
        }

        ClassTime classTime = class_item_list.get(class_item_list.size() - 1);
        if (classRoom.getIstest() != 1) {
            if (iOrder.getNoReturnOrderByClassId(Integer.parseInt(class_id)) <= 0) {
                return JSONResult.errorMsg("没有学生上课，老师不允许进入课堂");
            }
        }
        if (classRoom.getIstest() == 1) { //试听课
            Course course = iCourse.getCourseById(classRoom.getCourse_id());
            if (course == null) {
                return JSONResult.errorMsg("课堂信息不存在");
            }
            int beginTime = course.getTest_time() - 60 * 10;
            int endTime = course.getTest_time() + 3600;
            if (new Date().getTime() / 1000 < beginTime) {
                return JSONResult.errorMsg("还没有到上课时间");
            }
            if (new Date().getTime() / 1000 > endTime) {
                return JSONResult.errorMsg("课程已结束");
            }


            Ret ret = new Ret();
            List<TeacherEnterInfo> teacherEnterInfos = iOrder.getTeacherEnterInfo(Integer.parseInt(class_id), -1);
            if (classRoom.getUser_id() == getCurrentUserId()) { //老师
                if (teacherEnterInfos.size() == 0) {
                    iOrder.setTeacherEnterInfo(getCurrentUserId(), Integer.parseInt(class_id), -1, (int) (new Date().getTime() / 1000), 1);
                } else {
                    iOrder.updateTeacherEnterInfo(getCurrentUserId(), Integer.parseInt(class_id), -1, 1);
                }
                //老师
                ret.setIshost(1);
            } else {
                if (teacherEnterInfos.size() == 0 || teacherEnterInfos.get(0).getState() == 0) {
                    return JSONResult.errorMsg("老师未进入");
                }
                //学生
                ret.setIshost(0);
            }
            ret.setRoom_number(classRoom.getRoom_number());
            return JSONResult.ok(ret);

        } else if (classRoom.getType() == 0) { //1v1
            int all_time = 0;
            for (ClassTime item : class_item_list) {
                all_time += item.getClass_hours();
            }
            if (all_time >= classRoom.getAll_hours() && classTime.getClass_time() + classTime.getClass_hours() * 3600 < new Date().getTime() / 1000) {
                return JSONResult.errorMsg("课程已结束");
            }

            if (all_time < classRoom.getAll_hours() && (classTime.getClass_time() + classTime.getClass_hours() * 3600) < new Date().getTime() / 1000) {
                return JSONResult.errorMsg("当前没有已发布且未未上课的课时");
            }
        } else { //班课
            if (classTime.getClass_time() + classTime.getClass_hours() * 3600 < new Date().getTime() / 1000) {
                return JSONResult.errorMsg("课程已结束");
            }
        }

        boolean flag = false;
        boolean teacher_flag = true;
        if (classRoom.getUser_id() == getCurrentUserId()) {
            flag = true;
            teacher_flag = true;
        } else {

        }
        for (StudentClass item : studentClasses) {
            if (item.getUser_id() == getCurrentUserId()) {
                flag = true;
                teacher_flag = false;
                break;
            }
        }

        if (flag == false) {
            return JSONResult.errorMsg("不是此课堂的学生或老师");
        }

        String outLine = classRoom.getOutline();
        if (outLine == "") {
            return JSONResult.errorMsg("没有发布课程");
        }

        boolean right_time = false;
        int begin_time = 0;
        int class_number = -1;
        for (ClassTime item : class_item_list) {
            int beginTime = item.getClass_time() - 60 * 10;
            int endTime = item.getClass_time() + 60 * 60 * item.getClass_hours();
            int current_time = (int) (new Date().getTime() / 1000);
            if (current_time > beginTime && current_time < endTime) {
                class_number = item.getClass_number();
                right_time = true;
                begin_time = beginTime;
                break;
            }
        }
        if (right_time == false) {
            return JSONResult.errorMsg("还没有到上课时间");
        }
        List<TeacherEnterInfo> teacherEnterInfos = iOrder.getTeacherEnterInfo(Integer.parseInt(class_id), class_number);
        if (teacher_flag == false) { //学生
            if (teacherEnterInfos.size() == 0 || teacherEnterInfos.get(0).getState() == 0) {
                return JSONResult.errorMsg("需要等待老师先进入房间");
            }
            ////
        } else { //老师
            if (teacherEnterInfos.size() == 0) {
                iOrder.setTeacherEnterInfo(getCurrentUserId(), Integer.parseInt(class_id), class_number, (int) (new Date().getTime() / 1000), 1);
            } else {
                iOrder.updateTeacherEnterInfo(getCurrentUserId(), Integer.parseInt(class_id), class_number, 1);
            }
        }

        iOrder.setClassTimeStatus(Integer.parseInt(class_id), getCurrentUserId(), begin_time, 1);


        Ret ret = new Ret();
        ret.setRoom_number(classRoom.getRoom_number());
        if (teacher_flag == true) {
            ret.setIshost(0);
        } else {
            ret.setIshost(1);
        }
        return JSONResult.ok(ret);
    }

    @RequestMapping("/leave_class_room")
    @ResponseBody
    JSONResult leaveClassRoom(String class_id) {
        if (class_id == null) {
            return JSONResult.errorMsg("缺少 class_id 参数");
        }
        ClassRoom classRoom = iOrder.getClassRoomById(Integer.parseInt(class_id));
        if (classRoom.getUser_id() == getCurrentUserId()) {
            iOrder.updateAllTeacherEnterInfo(getCurrentUserId(), Integer.parseInt(class_id), 0);
        }

        return JSONResult.ok();
    }

    @RequestMapping("/report_record_class")
    @ResponseBody
    JSONResult reportRecordClass(String record_course_id, String last_class_no, String last_class_pos) {
        if (record_course_id == null || last_class_no == null || last_class_pos == null) {
            return JSONResult.errorMsg("缺少参数");
        }
        Record record = iRecord.get(Integer.parseInt(record_course_id));
        if (record == null) {
            return JSONResult.ok("录播课不存在");
        }

        iRecord.report(Integer.parseInt(record_course_id), Integer.parseInt(last_class_no), Integer.parseInt(last_class_pos));
        return JSONResult.ok();
    }

    @RequestMapping("/get_class")
    @ResponseBody
    JSONResult getClassById(String class_id) throws IOException {
        if (class_id == null) {
            return JSONResult.errorMsg("缺少 class_id 参数");
        }

        ClassRoom classRoom = iOrder.getClassRoomById(Integer.parseInt(class_id));
        if (classRoom == null) {
            return JSONResult.ok(null);
        }


        ClassRoomRet classRoomRet = new ClassRoomRet();

        classRoomRet.setCourse_owner_id(classRoom.getUser_id());
        UserInfo userInfo = iUser.getUser(classRoom.getUser_id());
        if (userInfo != null) {
            classRoomRet.setCourse_owner_name(userInfo.getName());
            classRoomRet.setCourse_owner_head(userInfo.getHead());
        }
        classRoomRet.setCourse_name(classRoom.getCourse_name());
        classRoomRet.setTeacher_name(classRoom.getTeacher_name());
        classRoomRet.setSubject(SignMap.getSubjectTypeById(classRoom.getSubject()));
        classRoomRet.setGrade(SignMap.getGradeTypeByID(classRoom.getGrade()));
        classRoomRet.setForm(SignMap.getCourseTypeNameById(classRoom.getForm()));
        classRoomRet.setWay(classRoom.getWay());
        classRoomRet.setMax_person(classRoom.getMax_person());
        classRoomRet.setAll_hours(classRoom.getAll_hours());
        classRoomRet.setPrice_one_hour(classRoom.getPrice_one_hour());
        classRoomRet.setAll_charge(classRoom.getAll_charge());
        classRoomRet.setCreate_time(classRoom.getCreate_time());
        classRoomRet.setRefund_rule(classRoom.getRefund_rule());
        classRoomRet.setTutor_content(classRoom.getTutor_content());
        List<CourseOutline> beanList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        if (classRoom.getOutline() == null || classRoom.getOutline().equals("")) {
            classRoom.setOutline("[]");
        } else {
            beanList = mapper.readValue(classRoom.getOutline(), new TypeReference<List<CourseOutline>>() {
            });
            classRoomRet.setOutline(beanList);
        }

        //课程状态
        if (classRoom.getType() == 0) { //1v1
//            List<StudentClass> list = iOrder.getStudentClassByClassId(Integer.parseInt(class_id));
            if (iOrder.getReturnOrderByClassId(Integer.parseInt(class_id)) > 0) {
                classRoomRet.setClass_info_status(4); //退费
            } else {
                int all_hours = 0;
                int last_time = 0;
                int last_hour = 0;
                for (CourseOutline item : beanList) {
                    all_hours += item.getClass_hours();
                    last_time = item.getClass_time();
                    last_hour = item.getClass_hours();
                }
                if (all_hours >= classRoomRet.getAll_hours() && (last_time + 3600 * last_hour) < new Date().getTime() / 1000) {
                    classRoomRet.setClass_info_status(3); //退费
                } else {
                    classRoomRet.setClass_info_status(1); //上课中
                }
            }

        } else { //班课
            CourseOutline courseOutline = beanList.get(beanList.size() - 1);
            if (courseOutline.getClass_time() < new Date().getTime() / 1000) {
                classRoomRet.setClass_info_status(3); //已完成
            } else {
                if (iOrder.getNoReturnOrderByClassId(Integer.parseInt(class_id)) > 0) {
                    classRoomRet.setClass_info_status(1); //上课中
                } else {
                    classRoomRet.setClass_info_status(3);
                }
            }
        }

        boolean class_end_time_flag = true;
        for (CourseOutline item : beanList) {
            if (item.getClass_time() < new Date().getTime() / 1000) {
                class_end_time_flag = false;
                break;
            }
        }
        if (class_end_time_flag == true && beanList.size() > 0) {
            classRoomRet.setClass_info_status(3);
        } else {
            int count = iOrder.getReturnOrderByClassId(Integer.parseInt(class_id));
            int all_count = iOrder.getAllReturnOrderByClassId(Integer.parseInt(class_id));
            if (count == all_count) {
                classRoomRet.setClass_info_status(4);
            }
        }

        int student_id = 0;
        List<StudentClass> list_students = iOrder.getStudentClassByUserId(Integer.parseInt(class_id));
        if (list_students.size() > 0) {
            student_id = list_students.get(0).getUser_id();
        }

        int hour_theory_use = 0;
        int hour_actual_use = 0;
        int hour_forget_use = 0;
        int hour_surplus_use = 0;
        int hour_other_user = 0;
        int all_time = 0;

        all_time = classRoom.getAll_hours();
        for (CourseOutline item : beanList) {
            int temp = item.getClass_time() + item.getClass_hours() * 3600;
            System.out.println(temp);
            if ((item.getClass_time() + item.getClass_hours() * 3600) < new Date().getTime() / 1000) {
                hour_theory_use += item.getClass_hours();
                List<ClassTimeInfo> classTimeInfos = iOrder.getClassTimeStatus(Integer.parseInt(class_id), classRoom.getUser_id(), item.getClass_time());
                if (classTimeInfos.size() > 0 && classTimeInfos.get(0).getStatus() == 1) {
                    item.setClass_status(1);
                    hour_actual_use += item.getClass_hours();
                } else {
                    item.setClass_status(2);
                    hour_forget_use += item.getClass_hours();
                }
                //1v1 使用 计算学生实际进入房间时长
                List<ClassTimeInfo> classTimeInfos2 = iOrder.getClassTimeStatus(Integer.parseInt(class_id), student_id, item.getClass_time());
                if (classTimeInfos2.size() > 0 && classTimeInfos2.get(0).getStatus() == 1) {
                    hour_other_user += item.getClass_hours();
                }
            } else {
                item.setClass_status(0);
            }
        }
        if (classRoom.getType() == 1) { //班课
            hour_surplus_use = all_time - hour_theory_use;
        } else {
            hour_surplus_use = all_time - hour_other_user;
        }

        ClassUseInfo classUseInfo = new ClassUseInfo();
        classUseInfo.setHour_theory_use(hour_theory_use);
        classUseInfo.setHour_actual_use(hour_actual_use);
        classUseInfo.setHour_forget_use(hour_forget_use);
        classUseInfo.setHour_surplus_use(hour_surplus_use);
        classRoomRet.setClass_use_info(classUseInfo);
        List<StudentClass> student_list = iOrder.getStudentClassByClassId(Integer.parseInt(class_id));
        List<StudentOrderTemp> students = new ArrayList<>();
        for (StudentClass item : student_list) {
            StudentOrderTemp studentOrderTemp = new StudentOrderTemp();
            studentOrderTemp.setStudent_id(item.getUser_id());
            studentOrderTemp.setStudent_order(item.getOrder_id());
            UserInfo userInfo1 = iUser.getUser(item.getUser_id());
            if (userInfo1 != null) {
                studentOrderTemp.setStudent_head_pic(userInfo1.getHead());
                studentOrderTemp.setStudent_name(userInfo1.getName());
            }
            OrderInfo orderInfo = iOrder.getOrderById(item.getOrder_id());
            if (orderInfo != null) {
                studentOrderTemp.setStudent_status(orderInfo.getOrder_status());
            }
            students.add(studentOrderTemp);
        }
        classRoomRet.setStudents(students);
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
            int class_info_status;
            int istest;

            public int getClass_info_status() {
                return class_info_status;
            }

            public void setClass_info_status(int class_info_status) {
                this.class_info_status = class_info_status;
            }

            public int getIstest() {
                return istest;
            }

            public void setIstest(int istest) {
                this.istest = istest;
            }
        }

        int user_id = getCurrentUserId();
        List<ClassInfo> classInfoList = new ArrayList<>();
        List<StudentClass> student_list = iOrder.getStudentClassByUserId(user_id);
        List<ClassRoom> rooms = new ArrayList<>();
        if (student_list.size() > 0) {
            List<Integer> id_list = new ArrayList<>();
            for (StudentClass item : student_list) {
                id_list.add(item.getClass_id());
            }
            rooms = iOrder.getClassInfoByIds(id_list);
        }
        List<ClassRoom> rooms_teacher = iOrder.getClassInfoByUserId(user_id);
        for (ClassRoom item : rooms_teacher) {
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
            if (userInfo != null) {
                teacherClass1.setCourse_owner_name(userInfo.getName());
                teacherClass1.setCourse_owner_head(userInfo.getHead());
            }
            teacherClass1.setMax_person(item.getMax_person());
            teacherClass1.setSubject(SignMap.getSubjectTypeById(item.getSubject()));
            teacherClass1.setGrade(SignMap.getGradeTypeByID(item.getGrade()));
            teacherClass1.setForm(SignMap.getCourseFormById(item.getForm()));
            teacherClass1.setForm_id(item.getForm());
            teacherClass1.setWay(item.getWay());

            if (item.getIstest() == 1) { //试听课
                if (item.getIstest() == 1) {
                    Course course = iCourse.getCourseById(item.getCourse_id());
                    if (course == null || course.getTest_time() + 3600 < new Date().getTime() / 1000) {
                        continue;
                    }
                    teacherClass1.setNext_class_NO(0);
                    teacherClass1.setNext_class_name("班课前试听");
                    teacherClass1.setNext_clsss_time(course.getTest_time());
                    teacherClass1.setNext_class_hour(1);
                }

            } else {

                NextClassInfo nextClassInfo = isClassFinish(item.getClass_id());
                teacherClass1.setNext_class_hour(nextClassInfo.next_class_hour);
                teacherClass1.setNext_clsss_time(nextClassInfo.next_clsss_time);
                teacherClass1.setNext_class_name(nextClassInfo.next_class_name);
                teacherClass1.setNext_class_NO(nextClassInfo.next_class_NO);
            }


            if (item.flag == 0) { //学生
                Student student = new Student();
                List<StudentClass> s_list = iOrder.getStudentClassByInfo(getCurrentUserId(), item.getClass_id());
                if (s_list.size() > 0) {
                    student.setOrder_id(s_list.get(0).getOrder_id());
                    student.setOrder_status(getTeacherClassState(item));
                }
                teacherClass1.setStudent(student);
                teacherClass1.setTeacher(null);
            } else if (item.flag == 1) { //老师
                Teacher teacher = new Teacher();
                teacher.setIstest(item.getIstest());
                teacher.setClass_info_status(getTeacherClassState(item));
                teacherClass1.setStudent(null);
                teacherClass1.setTeacher(teacher);
            }
            ClassInfo classInfo = new ClassInfo();
            classInfo.setTeacher_class(teacherClass1);
            classInfo.setVideo_class(null);
            classInfoList.add(classInfo);
        }
        return JSONResult.ok(classInfoList);
    }

    @RequestMapping("/test_listen")
    @ResponseBody
    JSONResult testListen() {
        int user_id = getCurrentUserId();
        List<ClassInfo> classInfo_list = new ArrayList<>();
        List<ClassRoom> list = iOrder.getAllClassRoom(1, 1);
        List<Integer> user_ids = new ArrayList<>();
        Map<Integer, UserInfo> m_users = new HashMap<>();
        for (ClassRoom item : list) {
            if (!list.contains(item.getUser_id())) {
                user_ids.add(item.getUser_id());
            }
        }
        if (user_ids.size() > 0) {
            List<UserInfo> users = iUser.getUserByIds(user_ids);
            for (UserInfo userInfo : users) {
                m_users.put(userInfo.getId(), userInfo);
            }
        }

        for (ClassRoom item : list) {
            TeacherClass2 teacherClass2 = new TeacherClass2();
            teacherClass2.setClass_id(item.getClass_id());
            teacherClass2.setCourse_name(item.getCourse_name());
            teacherClass2.setCourse_pic(item.getCourse_pic());
            teacherClass2.setCourse_owner_id(item.getUser_id());
            if (m_users.get(item.getUser_id()) != null) {
                teacherClass2.setCourse_owner_head(m_users.get(item.getUser_id()).getHead());
                teacherClass2.setCourse_owner_name(m_users.get(item.getUser_id()).getName());
            }
            teacherClass2.setMax_person(item.getMax_person());
            teacherClass2.setSubject(SignMap.getSubjectTypeById(item.getSubject()));
            teacherClass2.setGrade(SignMap.getGradeTypeByID(item.getGrade()));
            teacherClass2.setForm(SignMap.getCourseFormById(item.getForm()));
            teacherClass2.setForm_id(item.getForm());
            teacherClass2.setWay(item.getWay());

            Course course = iCourse.getCourseById(item.getCourse_id());

            teacherClass2.setNext_clsss_time(course.getTest_time());
            ClassInfo classInfo = new ClassInfo();
            classInfo.setTeacher_class(teacherClass2);
            classInfo.setVideo_class(null);
            classInfo_list.add(classInfo);
        }

        return JSONResult.ok(classInfo_list);
    }
}
