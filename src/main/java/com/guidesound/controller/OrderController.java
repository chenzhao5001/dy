package com.guidesound.controller;

import com.guidesound.dao.IOrder;
import com.guidesound.dto.OrderClassDTO;
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
public class OrderController {

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
    JSONResult add1V1Order() {
        return JSONResult.ok();
    }
    @RequestMapping("/add_class_order")
    @ResponseBody
    JSONResult addClassOrder(@Valid OrderClassDTO orderClassDTO, BindingResult result) {

        StringBuilder msg = new StringBuilder();
        if(!ToolsFunction.paramCheck(result,msg)) {
            return JSONResult.errorMsg(msg.toString());
        }
        orderClassDTO.setType(2);
        iOrder.addClassOrder(orderClassDTO);
        return JSONResult.ok();
    }
}
