package com.guidesound.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidesound.TempStruct.ClassTime;
import com.guidesound.TempStruct.ClassUseInfo;
import com.guidesound.TempStruct.CourseOutline;
import com.guidesound.TempStruct.RefundInfo;
import com.guidesound.dao.IOrder;
import com.guidesound.dao.IUser;
import com.guidesound.dto.Order1V1DTO;
import com.guidesound.dto.OrderClassDTO;
import com.guidesound.models.*;
import com.guidesound.ret.ClassOrder;
import com.guidesound.ret.Order1V1;
import com.guidesound.util.JSONResult;
import com.guidesound.util.TlsSigTest;
import com.guidesound.util.ToolsFunction;
import com.sun.javafx.image.impl.IntArgbPre;
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

    //0待上课，1已完成，2缺课
    public int getCourseState(List<ClassTime> iClassTime) {


        return 0;
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
        if(course == null) {
            return JSONResult.errorMsg("课程不存在");
        }
        if(course.getUser_id() != getCurrentUserId()) {
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
            if(courseOutline.getClass_time() + courseOutline.getClass_hours()*3600 < new Date().getTime() / 1000 ) {

                List<ClassTimeInfo> classTimeInfo_teacher = iOrder.getClassTimeStatus(Integer.parseInt(order_id),classOrder.getCourse_owner_id(),courseOutline.getClass_time());
                if(classTimeInfo_teacher.size() > 0 && classTimeInfo_teacher.get(0).getStatus() == 1 ) {
                    hour_theory_use += courseOutline.getClass_hours();
                }
                List<ClassTimeInfo> classTimeInfo_student = iOrder.getClassTimeStatus(Integer.parseInt(order_id),classOrder.getStudent_id(),courseOutline.getClass_time());
                if(classTimeInfo_student.size() > 0 && classTimeInfo_student.get(0).getStatus() == 1) {
                    hour_actual_use += courseOutline.getClass_hours();
                    courseOutline.setClass_status(1); //已完成
                } else {
                    hour_forget_use += courseOutline.getClass_hours();
                    courseOutline.setClass_status(2); //缺课
                }
            }
            last_time = courseOutline.getClass_time() + 3600*courseOutline.getClass_hours();
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
        if(orderInfo.getRefund_amount() != 0) {
            RefundInfo refundInfo = new RefundInfo();
            refundInfo.setAll_charge(classOrder.getAll_charge());
            refundInfo.setHour_theory_use(hour_theory_use);
            refundInfo.setHour_actual_use(hour_actual_use);
            refundInfo.setHour_forget_use(hour_forget_use);
            refundInfo.setRefund_amount(orderInfo.getRefund_amount());
            refundInfo.setSubmit_time(orderInfo.getSubmit_time());
            classOrder.setRefund_info(refundInfo);
        }

        if(orderInfo.getRefund_amount() > 0) {
            classOrder.setOrder_status(4); //退费
        } else {
            if(last_time < new Date().getTime() / 1000) {
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

        if(order1V1.getOutline().equals("")) {
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
            class_item_list = mapper_temp.readValue((String)order1V1.getOutline(), new TypeReference<List<ClassTime>>() {});
            for(ClassTime item : class_item_list) {
                temp_all_hours += item.getClass_hours();
                if(item.getClass_time() + 3600*item.getClass_hours() < new Date().getTime() / 1000) {
                    hour_theory_use += item.getClass_hours();
                    List<ClassTimeInfo> classTimeInfo_student = iOrder.getClassTimeStatus(Integer.parseInt(order_id),order1V1.getStudent_id(),item.getClass_time());
                    if(classTimeInfo_student.size() > 0 && classTimeInfo_student.get(0).getStatus() == 1) {
                        hour_actual_use += item.getClass_hours();
                    } else {
                        hour_forget_use += item.getClass_hours();
                    }
                }
                hour_surplus_use = all_time - hour_actual_use;
                last_time = item.getClass_time() + item.getClass_hours()*3600;
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
        if(orderInfo.getRefund_amount() != 0) {
            RefundInfo refundInfo = new RefundInfo();
            refundInfo.setAll_charge(order1V1.getAll_charge());
            refundInfo.setHour_theory_use(hour_theory_use);
            refundInfo.setHour_actual_use(hour_actual_use);
            refundInfo.setHour_forget_use(hour_forget_use);
            refundInfo.setRefund_amount(orderInfo.getRefund_amount());
            refundInfo.setSubmit_time(orderInfo.getSubmit_time());
            order1V1.setRefund_info(refundInfo);
        }

        if(orderInfo.getRefund_amount() != 0) {
            order1V1.setOrder_status(4);
        } else {
            if(temp_all_hours == order1V1.getAll_hours() && last_time < new Date().getTime() / 1000) {
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
        OrderInfo orderInfo = iOrder.getUserByOrderIdAndUserId(Integer.parseInt(order_id),getCurrentUserId());
        if (orderInfo == null) {
            return JSONResult.errorMsg("订单不存在");
        }
        if(orderInfo.getOrder_status() != 0) {
            return JSONResult.errorMsg("此状态不能支付");
        }

        List<StudentClass> student = iOrder.getStudentClassByOrder(Integer.parseInt(order_id));
        if(student.size() > 0) {
            return JSONResult.errorMsg("此订单已经支付过");
        }

        if(type.equals("0")) { //课堂
            int class_id = 0;
            int teacher_id = 0;
            String outLine = "";
            if (orderInfo.getClass_id() == 0 || orderInfo.getType() == 0) {
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

                String group_name = null;
                if(orderInfo.getType() == 1) {
                    group_name = TlsSigTest.createGroup(userInfo.getIm_id(),course.getCourse_name());
                    if(group_name.equals("")) {
                        return JSONResult.errorMsg("创建im群失败");
                    }
                }

                //创建课堂
                iOrder.addClassRoom(classRoom);
                int class_number = 1000000000 + classRoom.getClass_id();
                iOrder.addRoomNumber(classRoom.getClass_id(), class_number);
                course.setId(classRoom.getClass_id());
                //创建补充课堂信息
                iOrder.ClassRoomCourse(course);
                if(group_name != null) {
                    iOrder.setClassRoomImGroupId(classRoom.getClass_id(),"班课群 " + group_name);
                }

                class_id = classRoom.getClass_id();
                teacher_id = course.getUser_id();
                outLine = course.getOutline();

                List<ClassTime> class_item_list = null;
                ObjectMapper mapper_temp = new ObjectMapper();
                try {
                    class_item_list = mapper_temp.readValue(outLine, new TypeReference<List<ClassTime>>() {});
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(class_item_list != null) {
                    for(ClassTime classTime : class_item_list) {
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
                ClassRoom classRoom = iOrder.getClassRoomByCourseId(orderInfo.getCourse_id()).get(0);
                List<StudentClass> student_list = iOrder.getStudentClassByCourseId(orderInfo.getCourse_id());
                if(student_list.size() >= classRoom.getMax_person()) {
                    return JSONResult.errorMsg("超过最大上课人数，无法支付");
                }

                class_id = classRoom.getClass_id();
                teacher_id = classRoom.getUser_id();
                outLine = classRoom.getOutline();
            }

            List<ClassTime> class_item_list = null;
            ObjectMapper mapper_temp = new ObjectMapper();
            if(outLine == null || outLine.equals("")) {
                outLine = "[]";
            }
            try {
                class_item_list = mapper_temp.readValue(outLine, new TypeReference<List<ClassTime>>() {});
                for(ClassTime classTime : class_item_list) {
                    ClassTimeInfo classTimeInfo = new ClassTimeInfo();
                    classTimeInfo.setOrder_id(Integer.parseInt(order_id));
                    classTimeInfo.setClass_id(class_id);
                    classTimeInfo.setStudent_id(getCurrentUserId());
                    classTimeInfo.setTeacher_id(teacher_id);
                    classTimeInfo.setBegin_time(classTime.getClass_time());
                    classTimeInfo.setEnd_time(classTime.getClass_time() + 3600*classTime.getClass_hours());
                    classTimeInfo.setClass_number(classTime.getClass_number());
                    classTimeInfo.setStatus(0);
                    iOrder.addClassTime(classTimeInfo); //学生创建学时
                }
            } catch (IOException e) {
                e.printStackTrace();
                return JSONResult.errorMsg("课堂大纲格式错误");
            }

            iOrder.setOrderStatus(Integer.parseInt(order_id),1);
            StudentClass studentClass = new StudentClass();
            studentClass.setUser_id(getCurrentUserId());
            studentClass.setCourse_id(orderInfo.getCourse_id());
            studentClass.setClass_id(class_id);
            studentClass.setOrder_id(Integer.parseInt(order_id));
            studentClass.setTeacher_id(orderInfo.getCourse_owner_id());
            studentClass.setCreate_time((int) (new Date().getTime() / 1000));
            studentClass.setUpdate_time((int) (new Date().getTime() / 1000));

            iOrder.addStudentClass(studentClass);
            iOrder.addOrderClassId(Integer.parseInt(order_id),class_id);

            ClassRoom classRoom = iOrder.getClassRoomById(class_id);
            UserInfo userInfo_me = iUser.getUser(getCurrentUserId());
            UserInfo info = iUser.getUser(classRoom.getUser_id());
            if(orderInfo.getType() == 1) {
                TlsSigTest.addGroupPerson(classRoom.getIm_group_id(),userInfo_me.getIm_id());
                TlsSigTest.addGroupPerson(classRoom.getIm_group_id(),info.getIm_id());
            }

        } else { //录播课
              
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
        if(classRoom == null) {
            return JSONResult.errorMsg("课堂不存在");
        }
        List<StudentClass> s_list = iOrder.getStudentClassByTeacherId(getCurrentUserId(),Integer.parseInt(class_id));
        if(s_list.size() < 1) {
            return JSONResult.errorMsg("订单不存在");
        }
        StudentClass studentClass = s_list.get(0);
        OrderInfo orderInfo = iOrder.getOrderById(studentClass.getOrder_id());
        if(orderInfo.getType() != 1) {
            return JSONResult.errorMsg("不是1v1课程");
        }
        ObjectMapper mapper = new ObjectMapper();
        ClassTime class_item = null;
        try {
            class_item = mapper.readValue(new_class_time, new TypeReference<ClassTime>() {});
            if(class_item.getClass_time() < new Date().getTime() / 1000) {
                return JSONResult.errorMsg("时间已过，无法发布");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return JSONResult.errorMsg("new_class_time json 格式错误");
        }

        List<ClassTime> class_item_list = new ArrayList<>();
        String outLine = classRoom.getOutline();
        if(outLine != null && !outLine.equals("")) {
            ObjectMapper mapper_temp = new ObjectMapper();
            try {
                class_item_list = mapper_temp.readValue(outLine, new TypeReference<List<ClassTime>>() {});
            } catch (IOException e) {
                e.printStackTrace();
                return JSONResult.errorMsg("大纲无法解析");
            }

            for(ClassTime item : class_item_list) {
                if(item.getClass_time() > new Date().getTime() /1000) {
                    return JSONResult.errorMsg("有未上课时");
                }
            }
        }
        class_item_list.add(class_item);
        try {
            String mapJakcson = mapper.writeValueAsString(class_item_list);
            iOrder.setClassTime(Integer.parseInt(class_id), new_class_time);
            iOrder.setClassRoomOutLine(Integer.parseInt(class_id), mapJakcson);
            iOrder.setOrderOutline(studentClass.getOrder_id(),mapJakcson);
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
        classTimeInfo.setEnd_time(class_item.getClass_time() + 3600*class_item.getClass_hours());
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
        List<StudentClass> s_list = iOrder.getStudentClassByTeacherId(getCurrentUserId(),Integer.parseInt(class_id));
        if(s_list.size() < 1) {
            return JSONResult.errorMsg("订单不存在");
        }
        StudentClass studentClass = s_list.get(0);
        iOrder.setClassTime(Integer.parseInt(class_id), "");
        ClassRoom classRoom = iOrder.getClassRoomById(Integer.parseInt(class_id));
        String outLine = classRoom.getOutline();
        if(outLine == null || outLine.equals("")) {
            iOrder.setClassRoomOutLine(Integer.parseInt(class_id), "");
            iOrder.setOrderOutline(studentClass.getOrder_id(),"");
            return JSONResult.ok();
        }

        List<ClassTime> class_item_list = new ArrayList<>();
        ObjectMapper mapper_temp = new ObjectMapper();
        class_item_list = mapper_temp.readValue(outLine, new TypeReference<List<ClassTime>>() {});

        List<ClassTime> class_item_list_other = new ArrayList<>();
        for(ClassTime item : class_item_list) {
            if(item.getClass_time() < new Date().getTime() /1000) {
                class_item_list_other.add(item);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        String mapJakcson = mapper.writeValueAsString(class_item_list_other);
        iOrder.setClassRoomOutLine(Integer.parseInt(class_id), mapJakcson);
        iOrder.setOrderOutline(studentClass.getOrder_id(),mapJakcson);
        iOrder.deleteClassTime(Integer.parseInt(class_id), (int) (new Date().getTime() / 1000));

        return JSONResult.ok();
    }

    @RequestMapping("/refund_amount")
    @ResponseBody
    JSONResult deleteClassTime(String order_id,String refund_amount) {

        if(order_id == null || refund_amount == null) {
            return JSONResult.errorMsg("缺少参数");
        }
        OrderInfo orderInfo = iOrder.getOrderById(Integer.parseInt(order_id));
        if(orderInfo.getRefund_amount() != 0) {
            return JSONResult.errorMsg("已经退费过");
        }
        int all_time = orderInfo.getAll_hours();
        int hour_theory_use = 0;
        List<ClassTimeInfo> time_list = iOrder.getClassTimeByInfo(Integer.parseInt(order_id),getCurrentUserId(), (int) (new Date().getTime() /1000));
        for(ClassTimeInfo item : time_list) {
            if(item.getBegin_time() < new Date().getTime()/1000) {
                hour_theory_use +=  (item.getEnd_time() - item.getBegin_time()) / 3600;
            }
        }
        int hour_surplus_use = all_time - hour_theory_use;
        int leaveMoney = orderInfo.getPrice_one_hour()*hour_surplus_use;

        if(leaveMoney != Integer.parseInt(refund_amount)) {
            return JSONResult.errorMsg("金额不符");
        }
        iOrder.setRefundAmount(Integer.parseInt(order_id),leaveMoney, (int) (new Date().getTime() / 1000));
        iOrder.setOrderStatus(Integer.parseInt(order_id),2);
        return JSONResult.ok();
    }

}
