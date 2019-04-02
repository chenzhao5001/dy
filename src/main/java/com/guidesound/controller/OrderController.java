package com.guidesound.controller;

import com.fasterxml.jackson.core.type.TypeReference;
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
        order1V1DTO.setType(1);
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
        orderClassDTO.setType(2);
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

        RefundInfo refundInfo = new RefundInfo();
        int hour_theory_use = 0;
        int hour_actual_use = 0;
        int hour_forget_use = 0;
        int hour_surplus_use = 0;
        int all_time = 0;
        for(CourseOutline item:beanList) {
            all_time += item.getClass_hours();
        }
        List<ClassTimeInfo> time_list = iOrder.getClassTimeByInfo(Integer.parseInt(order_id),getCurrentUserId(), (int) (new Date().getTime() /1000));
        for(ClassTimeInfo item : time_list) {
            hour_theory_use += (item.getEnd_time() - item.getBegin_time()) / 3600;
        }
        time_list =  iOrder.getTrueClassTimeByInfo(Integer.parseInt(order_id),getCurrentUserId(), (int) (new Date().getTime() /1000));
        for(ClassTimeInfo item : time_list) {
            hour_actual_use += (item.getEnd_time() - item.getBegin_time()) / 3600;
        }
        hour_forget_use = hour_theory_use - hour_actual_use;
        hour_surplus_use = all_time - hour_theory_use;
        ClassUseInfo classUseInfo = new ClassUseInfo();
        classUseInfo.setHour_theory_use(hour_theory_use);
        classUseInfo.setHour_actual_use(hour_actual_use);
        classUseInfo.setHour_forget_use(hour_forget_use);
        classUseInfo.setHour_surplus_use(hour_surplus_use);

        classOrder.setRefund_info(refundInfo);
        classOrder.setClass_use_info(classUseInfo);
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
        List<ClassTime> class_item_list = new ArrayList<>();
        ObjectMapper mapper_temp = new ObjectMapper();
        try {
            class_item_list = mapper_temp.readValue((String)order1V1.getOutline(), new TypeReference<List<ClassTime>>() {});
            order1V1.setOutline(class_item_list);
        } catch (IOException e) {
            e.printStackTrace();
            order1V1.setOutline("outline 格式错误");

        }
        order1V1.setRefund_info(new RefundInfo());

        int hour_theory_use = 0;
        int hour_actual_use = 0;
        int hour_forget_use = 0;
        int hour_surplus_use = 0;
        int all_time = 0;
        for(ClassTime item:class_item_list) {
            all_time += item.getClass_hours();
        }
        List<ClassTimeInfo> time_list = iOrder.getClassTimeByInfo(Integer.parseInt(order_id),getCurrentUserId(), (int) (new Date().getTime() /1000));
        for(ClassTimeInfo item : time_list) {
            hour_theory_use += (item.getEnd_time() - item.getBegin_time()) / 3600;
        }
        time_list =  iOrder.getTrueClassTimeByInfo(Integer.parseInt(order_id),getCurrentUserId(), (int) (new Date().getTime() /1000));
        for(ClassTimeInfo item : time_list) {
            hour_actual_use += (item.getEnd_time() - item.getBegin_time()) / 3600;
        }
        hour_forget_use = hour_theory_use - hour_actual_use;
        hour_surplus_use = all_time - hour_theory_use;
        ClassUseInfo classUseInfo = new ClassUseInfo();
        classUseInfo.setHour_theory_use(hour_theory_use);
        classUseInfo.setHour_actual_use(hour_actual_use);
        classUseInfo.setHour_forget_use(hour_forget_use);
        classUseInfo.setHour_surplus_use(hour_surplus_use);
        order1V1.setClass_use_info(classUseInfo);
        return JSONResult.ok(order1V1);
    }

    @RequestMapping("/pay")
    @ResponseBody
    JSONResult pay(String type, String order_id, String pay_way) {
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
            if (iOrder.getClassRoomByCourseId(orderInfo.getCourse_id()).size() == 0 || orderInfo.getType() == 1) {
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

                iOrder.addClassRoom(classRoom);
                int class_number = 1000000000 + classRoom.getClass_id();
                iOrder.addRoomNumber(classRoom.getClass_id(), class_number);
                course.setId(classRoom.getClass_id());
                iOrder.ClassRoomCourse(course);
                class_id = classRoom.getClass_id();
                teacher_id = course.getTeacher_id();
                outLine = course.getOutline();


                List<ClassTime> class_item_list = null;
                ObjectMapper mapper_temp = new ObjectMapper();
                try {
                    class_item_list = mapper_temp.readValue(outLine, new TypeReference<List<ClassTime>>() {});
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                    iOrder.addClassTime(classTimeInfo);
                }
            } else {
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
                    iOrder.addClassTime(classTimeInfo);
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
            studentClass.setCreate_time((int) (new Date().getTime() / 1000));
            studentClass.setUpdate_time((int) (new Date().getTime() / 1000));
            iOrder.addStudentClass(studentClass);
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

        ObjectMapper mapper = new ObjectMapper();
        ClassTime class_item = null;
        try {
            class_item = mapper.readValue(new_class_time, new TypeReference<ClassTime>() {});

        } catch (IOException e) {
            e.printStackTrace();
            return JSONResult.errorMsg("new_class_time json 格式错误");
        }

        int user_id = getCurrentUserId();
        List<StudentClass> s_list = iOrder.getStudentClassByInfo(getCurrentUserId(),Integer.parseInt(class_id));
        if(s_list.size() < 1) {
            return JSONResult.errorMsg("订单不存在");
        }
        StudentClass studentClass = s_list.get(0);
        Order1V1 order1V1 = iOrder.get1v1OrderById(studentClass.getOrder_id());

        if(order1V1.getOutline() != null) {
            ObjectMapper mapper_temp = new ObjectMapper();
            try {
                List<ClassTime> class_item_list = null;
                String info = null;
                if(((String)order1V1.getOutline()).equals("")) {
                    info = "[]";
                } else {
                    info = (String)order1V1.getOutline();
                }
                class_item_list = mapper_temp.readValue(info, new TypeReference<List<ClassTime>>() {});
                class_item_list.add(class_item);
                ObjectMapper mapper_other = new ObjectMapper();
                String mapJakcson = mapper.writeValueAsString(class_item_list);
                iOrder.setClassTime(Integer.parseInt(class_id), user_id, mapJakcson);
                iOrder.setOrderOutline(studentClass.getOrder_id(),mapJakcson);
                System.out.println(mapJakcson);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return JSONResult.ok();
    }

    @RequestMapping("/delete_class_time")
    @ResponseBody
    JSONResult deleteClassTime(String class_id) {
        if (class_id == null) {
            return JSONResult.errorMsg("缺少 class_id 参数");
        }
        int user_id = getCurrentUserId();
        iOrder.setClassTime(Integer.parseInt(class_id), user_id, "");
        return JSONResult.ok();
    }

}
