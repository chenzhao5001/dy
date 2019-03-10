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

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    IOrder iOrder;

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
