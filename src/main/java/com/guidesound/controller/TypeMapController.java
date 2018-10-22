package com.guidesound.controller;

import com.guidesound.util.JSONResult;
import com.guidesound.util.SignMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户控制器
 */
@Controller
@RequestMapping("/type")
public class TypeMapController {

    @RequestMapping(value = "/watch_type_list")
    @ResponseBody
    public JSONResult getWatchType() {
        return JSONResult.ok(SignMap.getWatchList());
    }

    @RequestMapping(value = "/grade_type_list")
    @ResponseBody
    public JSONResult getGradeType() {
        return JSONResult.ok(SignMap.getGradeTypeList());
    }

    /**
     * 获得用户类型列表
     */
    @RequestMapping(value = "/user_type_list")
    @ResponseBody
    public JSONResult getUserType() {
        return JSONResult.ok(SignMap.getUserTypeList());
    }

}
