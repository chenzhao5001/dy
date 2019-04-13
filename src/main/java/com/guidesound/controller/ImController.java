package com.guidesound.controller;

import com.guidesound.dao.IOrder;
import com.guidesound.dao.IUser;
import com.guidesound.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/im")
public class ImController extends BaseController{
    @Autowired
    IOrder iOrder;
    @Autowired
    IUser iUser;

    int getCurrentCount() {
        int count = iOrder.getCurrentCount();
        iOrder.setCurrentCount(count + 1);
        return count;
    }
    @RequestMapping("/new_group_id")
    @ResponseBody
    JSONResult getNewGroupId() {
        int currentCount =  getCurrentCount();
        return JSONResult.ok(currentCount);
    }

    @RequestMapping("/add_black_list")
    @ResponseBody
    JSONResult addBlackList(String uid) {
        if(uid == null || uid.equals("")) {
            return JSONResult.errorMsg("缺少 uid 参数");
        }
        int count = iUser.getBlackListCount(getCurrentUserId(),uid);
        if(count == 0) {
            iUser.addBlackList(getCurrentUserId(),uid);
        }
        return JSONResult.ok();
    }

    @RequestMapping("/remove_black_list")
    @ResponseBody
    JSONResult removeBlackList(String uid) {

        if(uid == null || uid.equals("")) {
            return JSONResult.errorMsg("缺少 uid 参数");
        }
        iUser.removeBlackList(getCurrentUserId(),uid);
        return JSONResult.ok();
    }

    @RequestMapping("/in_black_list")
    @ResponseBody
    JSONResult inBlackList(String uid) {
        if(uid == null || uid.equals("")) {
            return JSONResult.errorMsg("缺少 uid 参数");
        }
        int count = iUser.getBlackListCount(getCurrentUserId(),uid);
        boolean flag = true;
        if(count == 0) {
            flag = false;
        }
        return JSONResult.ok(flag);
    }



}
