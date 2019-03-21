package com.guidesound.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidesound.TempStruct.CourseOutline;
import com.guidesound.dao.IOrder;
import com.guidesound.dao.IUser;
import com.guidesound.dto.Order1V1DTO;
import com.guidesound.dto.OrderClassDTO;
import com.guidesound.models.ClassRoom;
import com.guidesound.models.Course;
import com.guidesound.models.OrderInfo;
import com.guidesound.models.UserInfo;
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
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController extends BaseController{

    @Autowired
    IOrder iOrder;
    @Autowired
    IUser iUser;


    @RequestMapping("/current_time")
    @ResponseBody
    JSONResult currentTime() {
        int time = (int) (new Date().getTime() / 1000);
        class CurrentTime{
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
        if(!ToolsFunction.paramCheck(result,msg)) {
            return JSONResult.errorMsg(msg.toString());
        }
        int user_id = getCurrentUserId();
        order1V1DTO.setType(1);
        order1V1DTO.setCreate_time((int) (new Date().getTime() / 1000));
        order1V1DTO.setUser_id(user_id);
        order1V1DTO.setOutline("");
        iOrder.add1v1Order(order1V1DTO);
        class RetTemp{
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
        if(!ToolsFunction.paramCheck(result,msg)) {
            return JSONResult.errorMsg(msg.toString());
        }
        int user_id = getCurrentUserId();
        orderClassDTO.setType(2);
        orderClassDTO.setCreate_time((int) (new Date().getTime() / 1000));
        orderClassDTO.setUser_id(user_id);

        iOrder.addClassOrder(orderClassDTO);
        class RetTemp{
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
        if(order_id == null) {
            return JSONResult.errorMsg("缺少 order_id 参数");
        }
        ClassOrder classOrder = iOrder.getClassOrderById(Integer.parseInt(order_id));

        if(classOrder == null) {
            return JSONResult.errorMsg("班课订单不存在");
        }

        UserInfo userInfo = iUser.getUser(classOrder.getCourse_owner_id());
        if(userInfo != null) {
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
            beanList = mapper.readValue((String) classOrder.getOutline(), new TypeReference<List<CourseOutline>>() {});
            classOrder.setOutline(beanList);
        } catch (IOException e) {
            e.printStackTrace();
            classOrder.setOutline("json 格式错误");
        }

        return JSONResult.ok(classOrder);
    }

    @RequestMapping("/get_1v1_order")
    @ResponseBody
    JSONResult get1v1Order(String order_id) {
        if(order_id == null) {
            return JSONResult.errorMsg("缺少 order_id 参数");
        }
        Order1V1 order1V1 = iOrder.get1v1OrderById(Integer.parseInt(order_id));
        if(order1V1 == null) {
            return JSONResult.errorMsg("订单不存在");
        }

        UserInfo userInfo = iUser.getUser(order1V1.getCourse_owner_id());
        if(userInfo != null) {
            order1V1.setCourse_owner_pic(userInfo.getHead());
            order1V1.setCourse_owner_name(userInfo.getName());
        } else {
            order1V1.setCourse_owner_pic("");
            order1V1.setCourse_owner_name("");
        }

        return JSONResult.ok(order1V1);
    }

    @RequestMapping("/pay")
    @ResponseBody
    JSONResult pay(String type,String order_id,String pay_way) {
        if(type == null || order_id == null || pay_way == null) {
            return JSONResult.errorMsg("缺少参数");
        }
        OrderInfo orderInfo = iOrder.getUserByOrderId(Integer.parseInt(order_id));
        if(orderInfo == null) {
            return JSONResult.errorMsg("订单不存在");
        }
        if(iOrder.getClassRoomByCourseId(orderInfo.getCourse_id()).size() == 0) {
            if(type.equals("1")) {
                Course course = iCourse.getCourseById(orderInfo.getCourse_id());
                course.setWay(orderInfo.getWay());
                course.setRefund_rule(orderInfo.getRefund_rule());
                course.setTutor_content(orderInfo.getTutor_content());
                if(course == null) {
                    return JSONResult.errorMsg("辅导课不存在");
                }
                ClassRoom classRoom = new ClassRoom();
                classRoom.setUser_id(orderInfo.getCourse_owner_id());
                classRoom.setCourse_id(orderInfo.getCourse_id());
                classRoom.setCreate_time((int) (new Date().getTime() / 1000));
                iOrder.addClassRoom(classRoom);
                int class_number = 1000000000 + classRoom.getClass_id();
                iOrder.addRoomNumber(classRoom.getClass_id(),class_number);
                course.setId(classRoom.getClass_id());
                iOrder.ClassRoomCourse(course);
            } else if(type.equals("0")){

            } else {
                return JSONResult.errorMsg("type 类型错误");
            }
        } else {
            return JSONResult.errorMsg("此课程已经支付过");
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
        if(order_sn == null) {
            return JSONResult.errorMsg("缺少 order_sn 参数");
        }
        return JSONResult.ok();
    }

    @RequestMapping("/add_class_time")
    @ResponseBody
    JSONResult addClassTime(String class_id, String new_class_time) {
        if(class_id == null || new_class_time == null) {
            return JSONResult.errorMsg("缺少参数");
        }

        int user_id = getCurrentUserId();
        iOrder.setClassTime(Integer.parseInt(class_id),user_id,new_class_time);

        return JSONResult.ok();
    }

    @RequestMapping("/delete_class_time")
    @ResponseBody
    JSONResult deleteClassTime(String class_id) {
        if(class_id == null) {
            return JSONResult.errorMsg("缺少 class_id 参数");
        }
        int user_id = getCurrentUserId();
        iOrder.setClassTime(Integer.parseInt(class_id),user_id,"");
        return JSONResult.ok();
    }

}
