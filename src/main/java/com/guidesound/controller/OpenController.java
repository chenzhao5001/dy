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

}
