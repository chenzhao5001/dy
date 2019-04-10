package com.guidesound.controller;

import com.guidesound.util.JSONResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/open")
public class OpenController {

    @RequestMapping(value = "/version")
    @ResponseBody
    JSONResult version(String os_type,String os_version) {
        if(os_type == null || os_version == null) {
            return JSONResult.errorMsg("缺少参数 os_type 或 os_version");
        }
        return null;
    }

    @RequestMapping(value = "/trade_description")
    String tradeDescription() {
        return "tradeDescription";
    }

    @RequestMapping(value = "/shop_description")
    String shopDescription() {
        return "shopDescription";
    }

    @RequestMapping(value = "/report_description")
    String reportDescription() {
        return "reportDescription";
    }


    @RequestMapping(value = "/course_description")
    String courseDescription() {
        return "courseDescription";
    }

    @RequestMapping(value = "/about")
    String about() {
        return "about";
    }
    @RequestMapping(value = "/privacy_policy")
    String privacyPolicy() {
        return "privacyPolicy";
    }

    @RequestMapping(value = "/user_policy")
    String userPolicy() {
        return "userPolicy";
    }




}
