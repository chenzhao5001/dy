package com.guidesound.controller;

import com.alipay.AlipayConfig;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidesound.TempStruct.ClassTime;
import com.guidesound.TempStruct.ClassUseInfo;
import com.guidesound.TempStruct.CourseOutline;
import com.guidesound.TempStruct.RefundInfo;
import com.guidesound.dao.IOrder;
import com.guidesound.dao.IRecord;
import com.guidesound.dao.IUser;
import com.guidesound.dto.Order1V1DTO;
import com.guidesound.dto.OrderClassDTO;
import com.guidesound.models.*;
import com.guidesound.ret.ClassOrder;
import com.guidesound.ret.Order1V1;
import com.guidesound.util.JSONResult;
import com.guidesound.util.TlsSigTest;
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
@RequestMapping("/order")
public class OrderController extends BaseController {

    @Autowired
    IOrder iOrder;
    @Autowired
    IUser iUser;
    @Autowired
    IRecord iRecord;

    int getCurrentCount() {
        int count = iOrder.getCurrentCount();
        iOrder.setCurrentCount(count + 1);
        return count;
    }

    @RequestMapping("/current_time")
    @ResponseBody
    JSONResult currentTime() {
        int time = (int) (new Date().getTime() / 1000);
        class CurrentTime {
            int current_time;

            public int getCurrent_time() {
                return current_time;
            }

            public void setCurrent_time(int current_time) {
                this.current_time = current_time;
            }
        }
        CurrentTime currentTime = new CurrentTime();
        currentTime.setCurrent_time(time);
        return JSONResult.ok(currentTime);
    }

    @RequestMapping("/add_1v1_order")
    @ResponseBody
    JSONResult add1V1Order(@Valid Order1V1DTO order1V1DTO, BindingResult result) {
        StringBuilder msg = new StringBuilder();
        if (!ToolsFunction.paramCheck(result, msg)) {
            return JSONResult.errorMsg(msg.toString());
        }
        int user_id = getCurrentUserId();
        order1V1DTO.setType(0);
        order1V1DTO.setCreate_time((int) (new Date().getTime() / 1000));
        order1V1DTO.setUser_id(user_id);
        order1V1DTO.setOutline("");
        iOrder.add1v1Order(order1V1DTO);
        class RetTemp {
            int id;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
        RetTemp retTemp = new RetTemp();
        retTemp.setId(order1V1DTO.getId());

        return JSONResult.ok(retTemp);
    }

    @RequestMapping("/add_class_order")
    @ResponseBody
    JSONResult addClassOrder(@Valid OrderClassDTO orderClassDTO, BindingResult result) {

        StringBuilder msg = new StringBuilder();
        if (!ToolsFunction.paramCheck(result, msg)) {
            return JSONResult.errorMsg(msg.toString());
        }
        int user_id = getCurrentUserId();

        Course course = iCourse.getCourseById(Integer.parseInt(orderClassDTO.getCourse_id()));
        if (course == null) {
            return JSONResult.errorMsg("课程不存在");
        }
        if (course.getUser_id() != getCurrentUserId()) {
            return JSONResult.errorMsg("不是课程拥有者，无法发布订单");
        }


        orderClassDTO.setType(1);
        orderClassDTO.setCreate_time((int) (new Date().getTime() / 1000));
        orderClassDTO.setUser_id(user_id);

        iOrder.addClassOrder(orderClassDTO);
        class RetTemp {
            int id;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
        RetTemp retTemp = new RetTemp();
        retTemp.setId(orderClassDTO.getId());
        return JSONResult.ok(retTemp);
    }

    @RequestMapping("/get_class_order")
    @ResponseBody
    JSONResult getClassOrder(String order_id) {
        if (order_id == null) {
            return JSONResult.errorMsg("缺少 order_id 参数");
        }
        ClassOrder classOrder = iOrder.getClassOrderById(Integer.parseInt(order_id));

        if (classOrder == null) {
            return JSONResult.errorMsg("班课订单不存在");
        }
        int student_id = classOrder.getStudent_id();

        UserInfo userInfo = iUser.getUser(classOrder.getCourse_owner_id());
        if (userInfo != null) {
            classOrder.setCourse_owner_pic(userInfo.getHead());
            classOrder.setCourse_owner_name(userInfo.getName());
        } else {
            classOrder.setCourse_owner_pic("");
            classOrder.setCourse_owner_name("");
        }

        ObjectMapper mapper = new ObjectMapper();
        List<CourseOutline> beanList = null;
        try {
            String str = (String) classOrder.getOutline();
            System.out.println(str);
            beanList = mapper.readValue((String) classOrder.getOutline(), new TypeReference<List<CourseOutline>>() {
            });
            classOrder.setOutline(beanList);
        } catch (IOException e) {
            e.printStackTrace();
            classOrder.setOutline("json 格式错误");
        }

        int hour_theory_use = 0;
        int hour_actual_use = 0;
        int hour_forget_use = 0;
        int hour_surplus_use = 0;
        int all_time = classOrder.getAll_hours();


        int last_time = 0;
        for (CourseOutline courseOutline : beanList) {
            courseOutline.setClass_status(0);
            if (courseOutline.getClass_time() + courseOutline.getClass_hours() * 3600 < new Date().getTime() / 1000) {

                List<ClassTimeInfo> classTimeInfo_teacher = iOrder.getClassTimeStatus(Integer.parseInt(order_id), classOrder.getCourse_owner_id(), courseOutline.getClass_time());
                if (classTimeInfo_teacher.size() > 0 && classTimeInfo_teacher.get(0).getStatus() == 1) {
                    hour_theory_use += courseOutline.getClass_hours();
                }
                List<ClassTimeInfo> classTimeInfo_student = iOrder.getClassTimeStatus(Integer.parseInt(order_id), classOrder.getStudent_id(), courseOutline.getClass_time());
                if (classTimeInfo_student.size() > 0 && classTimeInfo_student.get(0).getStatus() == 1) {
                    hour_actual_use += courseOutline.getClass_hours();
                    courseOutline.setClass_status(1); //已完成
                } else {
                    hour_forget_use += courseOutline.getClass_hours();
                    courseOutline.setClass_status(2); //缺课
                }
            }
            last_time = courseOutline.getClass_time() + 3600 * courseOutline.getClass_hours();
        }

        hour_surplus_use = all_time - hour_theory_use;
        ClassUseInfo classUseInfo = new ClassUseInfo();
        classUseInfo.setHour_theory_use(hour_theory_use);
        classUseInfo.setHour_actual_use(hour_actual_use);
        classUseInfo.setHour_forget_use(hour_forget_use);
        classUseInfo.setHour_surplus_use(hour_surplus_use);


        classOrder.setClass_use_info(classUseInfo);

        classOrder.setRefund_info(new RefundInfo());
        OrderInfo orderInfo = iOrder.getOrderById(Integer.parseInt(order_id));
        if (orderInfo.getRefund_amount() != 0) {
            RefundInfo refundInfo = new RefundInfo();
            refundInfo.setAll_charge(classOrder.getAll_charge());
            refundInfo.setHour_theory_use(hour_theory_use);
            refundInfo.setHour_actual_use(hour_actual_use);
            refundInfo.setHour_forget_use(hour_forget_use);
            refundInfo.setRefund_amount(orderInfo.getRefund_amount());
            refundInfo.setSubmit_time(orderInfo.getSubmit_time());
            classOrder.setRefund_info(refundInfo);
        }

        if (orderInfo.getRefund_amount() > 0) {
            classOrder.setOrder_status(4); //退费
        } else {
            if (last_time < new Date().getTime() / 1000) {
                classOrder.setOrder_status(3); //已经完成
            }
        }

        return JSONResult.ok(classOrder);
    }

    @RequestMapping("/get_1v1_order")
    @ResponseBody
    JSONResult get1v1Order(String order_id) {
        if (order_id == null) {
            return JSONResult.errorMsg("缺少 order_id 参数");
        }

        Order1V1 order1V1 = iOrder.get1v1OrderById(Integer.parseInt(order_id));
        if (order1V1 == null) {
            return JSONResult.errorMsg("订单不存在");
        }

        UserInfo userInfo = iUser.getUser(order1V1.getCourse_owner_id());
        if (userInfo != null) {
            order1V1.setCourse_owner_pic(userInfo.getHead());
            order1V1.setCourse_owner_name(userInfo.getName());
        } else {
            order1V1.setCourse_owner_pic("");
            order1V1.setCourse_owner_name("");
        }

        if (order1V1.getOutline().equals("")) {
            order1V1.setOutline("[]");
        }
        int hour_theory_use = 0;
        int hour_actual_use = 0;
        int hour_forget_use = 0;
        int hour_surplus_use = 0;
        int all_time = order1V1.getAll_hours();

        List<ClassTime> class_item_list = new ArrayList<>();
        ObjectMapper mapper_temp = new ObjectMapper();
        int last_time = 0;
        int temp_all_hours = 0;
        try {
            class_item_list = mapper_temp.readValue((String) order1V1.getOutline(), new TypeReference<List<ClassTime>>() {
            });
            for (ClassTime item : class_item_list) {
                temp_all_hours += item.getClass_hours();
                if (item.getClass_time() + 3600 * item.getClass_hours() < new Date().getTime() / 1000) {
                    hour_theory_use += item.getClass_hours();
                    List<ClassTimeInfo> classTimeInfo_student = iOrder.getClassTimeStatus(Integer.parseInt(order_id), order1V1.getStudent_id(), item.getClass_time());
                    if (classTimeInfo_student.size() > 0 && classTimeInfo_student.get(0).getStatus() == 1) {
                        hour_actual_use += item.getClass_hours();
                    } else {
                        hour_forget_use += item.getClass_hours();
                    }
                }
                hour_surplus_use = all_time - hour_actual_use;
                last_time = item.getClass_time() + item.getClass_hours() * 3600;

                if(last_time < new Date().getTime() / 1000) {
                    List<ClassTimeInfo> classTimeInfo_student = iOrder.getClassTimeStatus(Integer.parseInt(order_id), order1V1.getStudent_id(), item.getClass_time());
                    if (classTimeInfo_student.size() > 0 && classTimeInfo_student.get(0).getStatus() == 1) {
                        item.setClass_status(1); //已完成
                    } else {
                        item.setClass_status(2); //缺课
                    }
                }
            }

            order1V1.setOutline(class_item_list);
        } catch (IOException e) {
            e.printStackTrace();
            order1V1.setOutline("outline 格式错误");

        }
        order1V1.setRefund_info(new RefundInfo());

        ClassUseInfo classUseInfo = new ClassUseInfo();
        classUseInfo.setHour_theory_use(hour_theory_use);
        classUseInfo.setHour_actual_use(hour_actual_use);
        classUseInfo.setHour_forget_use(hour_forget_use);
        classUseInfo.setHour_surplus_use(hour_surplus_use);
        order1V1.setClass_use_info(classUseInfo);

        OrderInfo orderInfo = iOrder.getOrderById(Integer.parseInt(order_id));
        if (orderInfo.getRefund_amount() != 0) {
            RefundInfo refundInfo = new RefundInfo();
            refundInfo.setAll_charge(order1V1.getAll_charge());
            refundInfo.setHour_theory_use(hour_theory_use);
            refundInfo.setHour_actual_use(hour_actual_use);
            refundInfo.setHour_forget_use(hour_forget_use);
            refundInfo.setRefund_amount(orderInfo.getRefund_amount());
            refundInfo.setSubmit_time(orderInfo.getSubmit_time());
            order1V1.setRefund_info(refundInfo);
        }

        if (orderInfo.getRefund_amount() != 0) {
            order1V1.setOrder_status(4);
        } else {
            if (temp_all_hours == order1V1.getAll_hours() && last_time < new Date().getTime() / 1000) {
                order1V1.setOrder_status(3);
            }
        }


        return JSONResult.ok(order1V1);
    }

    @RequestMapping("/pay")
    @ResponseBody
    JSONResult pay(String type, String order_id, String pay_way) throws IOException {
        if (type == null || order_id == null || pay_way == null) {
            return JSONResult.errorMsg("缺少参数");
        }
        if (type.equals("0")) { //课堂
            OrderInfo orderInfo = iOrder.getUserByOrderIdAndUserId(Integer.parseInt(order_id), getCurrentUserId());
            if (orderInfo == null) {
                return JSONResult.errorMsg("订单不存在");
            }
            if (orderInfo.getOrder_status() != 0) {
                return JSONResult.errorMsg("此状态不能支付");
            }

            List<StudentClass> student = iOrder.getStudentClassByOrder(Integer.parseInt(order_id));
            if (student.size() > 0) {
                return JSONResult.errorMsg("此订单已经支付过");
            }

            if(orderInfo.getType() == 1) {
                String order_outLine = orderInfo.getOutline();
                try {
                    ObjectMapper mapper_temp = new ObjectMapper();
                    List<ClassTime> class_item_list = mapper_temp.readValue(order_outLine, new TypeReference<List<ClassTime>>() {
                    });
                    if(class_item_list.size() == 0) {
                        return JSONResult.errorMsg("班课无内容");
                    }
                    if(class_item_list.get(0).getClass_time() < new Date().getTime() / 1000) {
                        return JSONResult.errorMsg("已经开课，不允许支付");
                    }

                } catch (IOException e) {
                    return JSONResult.errorMsg("班课课堂大纲错误");
                }
            }

            int class_id = 0;
            int teacher_id = 0;
            String outLine = "";

            List<ClassRoom> r_list = iOrder.getClassRoomByCourseId(orderInfo.getCourse_id());
            boolean fitst_flag = true;
            for (ClassRoom item : r_list) {
                if(item.getIstest() != 1) {
                    fitst_flag = false;
                    break;
                }
            }
            log.info("fitst_flag 标志 " + fitst_flag );
            if (fitst_flag || orderInfo.getType() == 0) {
                Course course = iCourse.getCourseById(orderInfo.getCourse_id());
                if (course == null) {
                    return JSONResult.errorMsg("辅导课不存在");
                }
                course.setWay(orderInfo.getWay());
                course.setRefund_rule(orderInfo.getRefund_rule());
                course.setTutor_content(orderInfo.getTutor_content());
                ClassRoom classRoom = new ClassRoom();
                classRoom.setUser_id(orderInfo.getCourse_owner_id());
                classRoom.setCourse_id(orderInfo.getCourse_id());
                classRoom.setCreate_time((int) (new Date().getTime() / 1000));
                classRoom.setAll_hours(orderInfo.getAll_hours());
                classRoom.setType(orderInfo.getType());
                classRoom.setAll_charge(orderInfo.getAll_charge());
                classRoom.setPrice_one_hour(orderInfo.getPrice_one_hour());
                UserInfo userInfo = iUser.getUser(classRoom.getUser_id());

                String group_id = null;
                if (orderInfo.getType() == 1) { //创建群
                    int currentCount =  getCurrentCount();
                    group_id = TlsSigTest.createGroup(userInfo.getIm_id(), "班课群 " + course.getId(),String.valueOf(currentCount),course.getCourse_pic());
                    if (!group_id.equals(String.valueOf(currentCount))) {
                        log.info("创建群失败 im_id = userInfo.getIm_id ={} ,group_name = {} ,ret = {}",userInfo.getIm_id(),"班课群 " + course.getCourse_name(),group_id);
                        return JSONResult.errorMsg("创建im群失败");
                    }
                    UserInfo user_temp = iUser.getUser(course.getUser_id());
                    String info_ret = TlsSigTest.addGroupPerson(group_id, String.valueOf(user_temp.getIm_id()));
                    log.info("加入群拥有者 group_id = {},user_id = {},ret = {}",group_id,String.valueOf(user_temp.getIm_id()),info_ret);
                    UserInfo user_temp2 = iUser.getUser(getCurrentUserId());
                    info_ret = TlsSigTest.addGroupPerson(group_id, user_temp2.getIm_id());
                    UserInfo userInfo1 = iUser.getUser(getCurrentUserId());
                    TlsSigTest.sendGroupMsg(group_id,"欢迎新同学：" + userInfo1.getName(),user_temp.getIm_id());
                    log.info("加入支付用户 group_id = {},user_id = {},ret = {}",group_id,String.valueOf(getCurrentUserId()),info_ret);
                }

                //创建课堂
                iOrder.addClassRoom(classRoom);
                int class_number = getCurrentCount();
                iOrder.addRoomNumber(classRoom.getClass_id(), class_number);
                course.setId(classRoom.getClass_id());
                if(orderInfo.getType() == 0) {
                    course.setAll_hours(orderInfo.getAll_hours());
                    course.setPrice_one_hour(orderInfo.getPrice_one_hour());
                    course.setAll_charge(orderInfo.getAll_charge());
                }
                //创建补充课堂信息
                iOrder.ClassRoomCourse(course);
                if (group_id != null) {
                    iOrder.setClassRoomImGroupId(classRoom.getClass_id(), group_id);
                }

                class_id = classRoom.getClass_id();
                teacher_id = course.getUser_id();
                outLine = course.getOutline();

                List<ClassTime> class_item_list = null;
                ObjectMapper mapper_temp = new ObjectMapper();
                try {
                    class_item_list = mapper_temp.readValue(outLine, new TypeReference<List<ClassTime>>() {
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (class_item_list != null) {
                    for (ClassTime classTime : class_item_list) {
                        ClassTimeInfo classTimeInfo = new ClassTimeInfo();
                        classTimeInfo.setOrder_id(Integer.parseInt(order_id));
                        classTimeInfo.setClass_id(class_id);
                        classTimeInfo.setStudent_id(teacher_id);
                        classTimeInfo.setTeacher_id(teacher_id);
                        classTimeInfo.setBegin_time(classTime.getClass_time());
                        classTimeInfo.setEnd_time(classTime.getClass_time() + 3600 * classTime.getClass_hours());
                        classTimeInfo.setClass_number(classTime.getClass_number());
                        classTimeInfo.setStatus(0);
                        iOrder.addClassTime(classTimeInfo); //老师创建学时
                    }
                }
            } else { //第n次班课
                log.info("班课增加成员");
                ClassRoom classRoom = iOrder.getClassRoomByCourseId(orderInfo.getCourse_id()).get(1);
                List<StudentClass> student_list = iOrder.getStudentClassByCourseId(orderInfo.getCourse_id());
                if (student_list.size() >= classRoom.getMax_person()) {
                    return JSONResult.errorMsg("超过最大上课人数，无法支付");
                }
                String info_ret = TlsSigTest.addGroupPerson(classRoom.getIm_group_id(), String.valueOf(getCurrentUserId()));
                log.info("班课群增加成员 group_id = {},user_id = {},ret = {}",classRoom.getIm_group_id(),String.valueOf(getCurrentUserId()),info_ret);
                UserInfo userInfo = iUser.getUser(getCurrentUserId());
                UserInfo userInfo2 = iUser.getUser(classRoom.getUser_id());
                TlsSigTest.sendGroupMsg(classRoom.getIm_group_id(),"欢迎新同学：" + userInfo.getName(),userInfo2.getIm_id());
                class_id = classRoom.getClass_id();
                teacher_id = classRoom.getUser_id();
                outLine = classRoom.getOutline();
            }

            List<ClassTime> class_item_list = null;
            ObjectMapper mapper_temp = new ObjectMapper();
            if (outLine == null || outLine.equals("")) {
                outLine = "[]";
            }
            try {
                class_item_list = mapper_temp.readValue(outLine, new TypeReference<List<ClassTime>>() {
                });
                for (ClassTime classTime : class_item_list) {
                    ClassTimeInfo classTimeInfo = new ClassTimeInfo();
                    classTimeInfo.setOrder_id(Integer.parseInt(order_id));
                    classTimeInfo.setClass_id(class_id);
                    classTimeInfo.setStudent_id(getCurrentUserId());
                    classTimeInfo.setTeacher_id(teacher_id);
                    classTimeInfo.setBegin_time(classTime.getClass_time());
                    classTimeInfo.setEnd_time(classTime.getClass_time() + 3600 * classTime.getClass_hours());
                    classTimeInfo.setClass_number(classTime.getClass_number());
                    classTimeInfo.setStatus(0);
                    iOrder.addClassTime(classTimeInfo); //学生创建学时
                }
            } catch (IOException e) {
                e.printStackTrace();
                return JSONResult.errorMsg("课堂大纲格式错误");
            }

            iOrder.setOrderStatus(Integer.parseInt(order_id), 1);
            StudentClass studentClass = new StudentClass();
            studentClass.setUser_id(getCurrentUserId());
            studentClass.setCourse_id(orderInfo.getCourse_id());
            studentClass.setClass_id(class_id);
            studentClass.setOrder_id(Integer.parseInt(order_id));
            studentClass.setTeacher_id(orderInfo.getCourse_owner_id());
            studentClass.setCreate_time((int) (new Date().getTime() / 1000));
            studentClass.setUpdate_time((int) (new Date().getTime() / 1000));

            iOrder.addStudentClass(studentClass);
            iOrder.addOrderClassId(Integer.parseInt(order_id), class_id);


            ///班课订单达到最大人数 下架课程
            if(orderInfo.getType() == 1){
                ClassRoom classRoom = iOrder.getClassRoomByCourseId(orderInfo.getCourse_id()).get(1);
                List<StudentClass> student_list = iOrder.getStudentClassByCourseId(orderInfo.getCourse_id());
                if (student_list.size() >= classRoom.getMax_person()) {
                    iCourse.setCourseState(orderInfo.getCourse_id(),4);
                }
            }

        } else { //录播课
            Record record = iRecord.get(Integer.parseInt(order_id));
            if(record == null || record.getRecord_course_status() != 3) {
                return JSONResult.errorMsg("录播课信息不存在");
            }
            List<UserRecordCourse> lists = iRecord.getRecordByUserAndId(getCurrentUserId(),Integer.parseInt(order_id));
            if(lists.size() > 0) {
                return JSONResult.errorMsg("此录播课已经购买过");
            }

            UserInfo userInfo = iUser.getUser(record.getUser_id());
            if(record.getGrade_id() == 0) { //创建群
                int currentCount =  getCurrentCount();

                if(userInfo != null) {
                    TlsSigTest.createGroup(String.valueOf(userInfo.getIm_id()), "录播课群： " + record.getRecord_course_id(),String.valueOf(currentCount),record.getRecord_course_pic());
                    TlsSigTest.addGroupPerson(String.valueOf(currentCount), String.valueOf(String.valueOf(userInfo.getIm_id())));
                    UserInfo userInfo1 = iUser.getUser(getCurrentUserId());
                    TlsSigTest.addGroupPerson(String.valueOf(currentCount), String.valueOf(String.valueOf(userInfo1.getIm_id())));
                    iRecord.setGroupId(currentCount,record.getRecord_course_id());
                    TlsSigTest.sendGroupMsg(String.valueOf(currentCount),"欢迎新同学：" + userInfo1.getName(),userInfo.getIm_id());
                }

            } else { //加入群
                if(userInfo != null) {
                    UserInfo userInfo1 = iUser.getUser(getCurrentUserId());
                    TlsSigTest.addGroupPerson(String.valueOf(record.getGroup_id()), String.valueOf(String.valueOf(userInfo1.getIm_id())));
                    TlsSigTest.sendGroupMsg(String.valueOf(record.getGroup_id()),"欢迎新同学：" + userInfo1.getName(),userInfo.getIm_id());
                }
            }
            UserRecordCourse userRecordCourse = new UserRecordCourse();
            userRecordCourse.setUser_id(getCurrentUserId());
            userRecordCourse.setUser_record_course_id(Integer.parseInt(order_id));
            userRecordCourse.setCreate_time((int) (new Date().getTime() / 1000));
            iRecord.insertRecordCourse(userRecordCourse);
        }
        class Ret {
            String token;
            int price;
            String order_sn;

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public String getOrder_sn() {
                return order_sn;
            }

            public void setOrder_sn(String order_sn) {
                this.order_sn = order_sn;
            }
        }

        Ret ret = new Ret();
        ret.setToken("test");
        ret.setPrice(0);
        ret.setOrder_sn("test_sn");
        return JSONResult.ok(ret);
    }

    @RequestMapping("/search_pay")
    @ResponseBody
    JSONResult searchPay(String order_sn) {
        if (order_sn == null) {
            return JSONResult.errorMsg("缺少 order_sn 参数");
        }
        return JSONResult.ok();
    }

    @RequestMapping("/add_class_time")
    @ResponseBody
    JSONResult addClassTime(String class_id, String new_class_time) {
        if (class_id == null || new_class_time == null) {
            return JSONResult.errorMsg("缺少参数");
        }

        ClassRoom classRoom = iOrder.getClassRoomById(Integer.parseInt(class_id));
        if (classRoom == null) {
            return JSONResult.errorMsg("课堂不存在");
        }
        List<StudentClass> s_list = iOrder.getStudentClassByTeacherId(getCurrentUserId(), Integer.parseInt(class_id));
        if (s_list.size() < 1) {
            return JSONResult.errorMsg("订单不存在");
        }
        StudentClass studentClass = s_list.get(0);
        OrderInfo orderInfo = iOrder.getOrderById(studentClass.getOrder_id());
        if (orderInfo.getType() != 0) {
            return JSONResult.errorMsg("不是1v1课程");
        }
        ObjectMapper mapper = new ObjectMapper();
        ClassTime class_item = null;
        try {
            class_item = mapper.readValue(new_class_time, new TypeReference<ClassTime>() {
            });
            if (class_item.getClass_time() < new Date().getTime() / 1000) {
                return JSONResult.errorMsg("时间已过，无法发布");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return JSONResult.errorMsg("new_class_time json 格式错误");
        }

        List<ClassTime> class_item_list = new ArrayList<>();
        String outLine = classRoom.getOutline();
        if (outLine != null && !outLine.equals("")) {
            ObjectMapper mapper_temp = new ObjectMapper();
            try {
                class_item_list = mapper_temp.readValue(outLine, new TypeReference<List<ClassTime>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
                return JSONResult.errorMsg("大纲无法解析");
            }

            for (ClassTime item : class_item_list) {
                if (item.getClass_time() > new Date().getTime() / 1000) {
                    return JSONResult.errorMsg("有未上课时");
                }
            }
        }
        class_item_list.add(class_item);
        try {
            String mapJakcson = mapper.writeValueAsString(class_item_list);
            iOrder.setClassTime(Integer.parseInt(class_id), new_class_time);
            iOrder.setClassRoomOutLine(Integer.parseInt(class_id), mapJakcson);
            iOrder.setOrderOutline(studentClass.getOrder_id(), mapJakcson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return JSONResult.errorMsg("json 格式错误");
        }
        ClassTimeInfo classTimeInfo = new ClassTimeInfo();
        classTimeInfo.setClass_id(Integer.parseInt(class_id));
        classTimeInfo.setOrder_id(orderInfo.getId());
        classTimeInfo.setTeacher_id(getCurrentUserId());
        classTimeInfo.setStudent_id(orderInfo.getStudent_id());
        classTimeInfo.setClass_number(class_item.getClass_number());
        classTimeInfo.setBegin_time(class_item.getClass_time());
        classTimeInfo.setEnd_time(class_item.getClass_time() + 3600 * class_item.getClass_hours());
        classTimeInfo.setStatus(0);
        iOrder.addClassTime(classTimeInfo);
        classTimeInfo.setStudent_id(getCurrentUserId());
        iOrder.addClassTime(classTimeInfo);
        return JSONResult.ok();
    }

    @RequestMapping("/delete_class_time")
    @ResponseBody
    JSONResult deleteClassTime(String class_id) throws IOException {
        if (class_id == null) {
            return JSONResult.errorMsg("缺少 class_id 参数");
        }
        List<StudentClass> s_list = iOrder.getStudentClassByTeacherId(getCurrentUserId(), Integer.parseInt(class_id));
        if (s_list.size() < 1) {
            return JSONResult.errorMsg("订单不存在");
        }
        StudentClass studentClass = s_list.get(0);
        iOrder.setClassTime(Integer.parseInt(class_id), "");
        ClassRoom classRoom = iOrder.getClassRoomById(Integer.parseInt(class_id));
        String outLine = classRoom.getOutline();
        if (outLine == null || outLine.equals("")) {
            iOrder.setClassRoomOutLine(Integer.parseInt(class_id), "");
            iOrder.setOrderOutline(studentClass.getOrder_id(), "");
            return JSONResult.ok();
        }

        List<ClassTime> class_item_list = new ArrayList<>();
        ObjectMapper mapper_temp = new ObjectMapper();
        class_item_list = mapper_temp.readValue(outLine, new TypeReference<List<ClassTime>>() {
        });

        List<ClassTime> class_item_list_other = new ArrayList<>();
        for (ClassTime item : class_item_list) {
            if (item.getClass_time() < new Date().getTime() / 1000) {
                class_item_list_other.add(item);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        String mapJakcson = mapper.writeValueAsString(class_item_list_other);
        iOrder.setClassRoomOutLine(Integer.parseInt(class_id), mapJakcson);
        iOrder.setOrderOutline(studentClass.getOrder_id(), mapJakcson);
        iOrder.deleteClassTime(Integer.parseInt(class_id), (int) (new Date().getTime() / 1000));

        return JSONResult.ok();
    }

    @RequestMapping("/refund_amount")
    @ResponseBody
    JSONResult deleteClassTime(String order_id, String refund_amount) {

        if (order_id == null || refund_amount == null) {
            return JSONResult.errorMsg("缺少参数");
        }

        OrderInfo order = iOrder.getOrderById(Integer.parseInt(order_id));
        if (order == null) {
            return JSONResult.errorMsg("订单不存在");
        }

        if (order.getRefund_amount() != 0) {
            return JSONResult.errorMsg("已经退费过");
        }

        String outLine = (String) order.getOutline();
        if (outLine != null && !outLine.equals("")) {
            List<ClassTime> class_item_list = null;
            ObjectMapper mapper_temp = new ObjectMapper();
            try {
                class_item_list = mapper_temp.readValue(outLine, new TypeReference<List<ClassTime>>() {
                });
                for(ClassTime item : class_item_list) {
                    int beginTime = item.getClass_time();
                    int endTime = beginTime + 3600*item.getClass_hours();
                    int currentTime = (int) (new Date().getTime() / 1000);
                    if(currentTime >= beginTime && currentTime <= endTime) {
                        return JSONResult.errorMsg("上课期间不允许退款");
                    }
                }
            } catch (IOException e) {
                return JSONResult.errorMsg("课堂大纲解析失败");
            }
        }

        int leaveMoney = 0;
        if(order.getType() == 0) { //1v1
            JSONResult result = get1v1Order(order_id);
            Order1V1 order1V1 = (Order1V1) result.getData();
            leaveMoney = (order1V1.getAll_hours() - order1V1.getClass_use_info().getHour_actual_use()) * order1V1.getPrice_one_hour();


        } else {  //班课
            JSONResult result = getClassOrder(order_id);
            ClassOrder classOrder = (ClassOrder) result.getData();
            leaveMoney = (classOrder.getAll_hours() - classOrder.getClass_use_info().getHour_theory_use() )* classOrder.getPrice_one_hour();
        }
        if(leaveMoney != Integer.parseInt(refund_amount)) {
            return JSONResult.errorMsg("金额不符");
        }

        iOrder.setRefundAmount(Integer.parseInt(order_id), leaveMoney, (int) (new Date().getTime() / 1000));
        iOrder.setOrderStatus(Integer.parseInt(order_id), 2);
        return JSONResult.ok();
    }

    @RequestMapping("/pay_")
    @ResponseBody
    public JSONResult payCallBack() {
        payOrder("body","subject","outTradeNo","0.01");
        return null;
    }

    @RequestMapping("/pay_test")
    @ResponseBody
    public JSONResult payTest() {
        payOrder("body","subject","outTradeNo","0.01");
        return null;
    }

    String payOrder(String body,String subject,String outTradeNo,String totalAmount) {
        String orderString = "";
        try{
            //实例化客户端（参数：网关地址、商户appid、商户私钥、格式、编码、支付宝公钥、加密类型），为了取得预付订单信息
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID,
                    AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET,
                    AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);

            //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
            AlipayTradeAppPayRequest ali_request = new AlipayTradeAppPayRequest();

            //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

            //业务参数传入,可以传很多，参考API
            //model.setPassbackParams(URLEncoder.encode(request.getBody().toString())); //公用参数（附加数据）
            model.setBody(body);                       //对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
            model.setSubject(subject);                 //商品名称
            model.setOutTradeNo(outTradeNo);           //商户订单号(自动生成)
            // model.setTimeoutExpress("30m");     			  //交易超时时间
            model.setTotalAmount(totalAmount);         //支付金额
            model.setProductCode("QUICK_MSECURITY_PAY");        	  //销售产品码（固定值）
            ali_request.setBizModel(model);
//            logger.info("====================异步通知的地址为："+alipayment.getNotifyUrl());
            ali_request.setNotifyUrl(AlipayConfig.notify_url);        //异步回调地址（后台）
            ali_request.setReturnUrl(AlipayConfig.return_url);	    //同步回调地址（APP）

            // 这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse alipayTradeAppPayResponse = alipayClient.sdkExecute(ali_request); //返回支付宝订单信息(预处理)
            orderString=alipayTradeAppPayResponse.getBody();//就是orderString 可以直接给APP请求，无需再做处理。
//            this.createAlipayMentOrder(alipaymentOrder);//创建新的商户支付宝订单

        } catch (AlipayApiException e) {
            e.printStackTrace();
//            logger.info("与支付宝交互出错，未能生成订单，请检查代码！");
        }
        return orderString;
    }



}
