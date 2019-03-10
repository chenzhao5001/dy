package com.guidesound.controller;

import com.guidesound.dao.IOrder;
import com.guidesound.dto.Order1V1DTO;
import com.guidesound.dto.OrderClassDTO;
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
import java.util.Date;

@Controller
@RequestMapping("/order")
public class OrderController extends BaseController{

    @Autowired
    IOrder iOrder;

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
        iOrder.add1v1Order(order1V1DTO);
        return JSONResult.ok();
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
        return JSONResult.ok();
    }

    @RequestMapping("/get_class_order")
    @ResponseBody
    JSONResult getClassOrder(String order_id) {
        if(order_id == null) {
            return JSONResult.errorMsg("缺少 order_id 参数");
        }
        ClassOrder classOrder = iOrder.getClassOrderById(Integer.parseInt(order_id));
        return JSONResult.ok(classOrder);
    }

    @RequestMapping("/get_1v1_order")
    @ResponseBody
    JSONResult get1v1Order(String order_id) {
        if(order_id == null) {
            return JSONResult.errorMsg("缺少 order_id 参数");
        }
        Order1V1 order1V1 = iOrder.get1v1OrderById(Integer.parseInt(order_id));
        return JSONResult.ok(order1V1);
    }


}
