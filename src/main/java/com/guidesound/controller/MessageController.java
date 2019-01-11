package com.guidesound.controller;

import com.alibaba.fastjson.JSON;
import com.guidesound.dao.IUser;
import com.guidesound.models.UserFriend;
import com.guidesound.models.UserInfo;
import com.guidesound.util.JSONResult;
import com.guidesound.util.TlsSigTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/message")
public class MessageController extends BaseController {

    @Autowired
    IUser iUser;

    @RequestMapping(value = "/send")
    @ResponseBody
    JSONResult sendMessage(String user_id ,String info) throws IOException {
        if(user_id == null || info == null ) {
            return JSONResult.errorMsg("缺少user_id 或 info");
        }
        String ret = TlsSigTest.PushMessage(user_id,info);
        if(!ret.equals(""))
            return JSONResult.errorMsg(ret);
        return JSONResult.ok();
    }

    @RequestMapping(value = "/add_friend_userid")
    @ResponseBody
    JSONResult addFriendByUserId(String add_user_id,String type) throws IOException {
        if(add_user_id == null || type == null) {
            return JSONResult.errorMsg("缺少 add_user_id 或 type");
        }
        int current_user_id = getCurrentUserId();
        iUser.addFriend(current_user_id,Integer.parseInt(add_user_id),Integer.parseInt(type));
        if(type.equals("1")) {
            TlsSigTest.PushMessage(add_user_id,"11");
        } else {
            TlsSigTest.PushMessage(add_user_id,"13");
        }
        return JSONResult.ok();
    }

    @RequestMapping(value = "/confirm_friend")
    @ResponseBody
    JSONResult confirmFriend(String add_user_id,String type) throws IOException {
        if(add_user_id == null && type == null) {
            return JSONResult.errorMsg("缺少 add_user_id 或 type");
        }

        int current_user_id = getCurrentUserId();
        iUser.updateFriendState(current_user_id,Integer.parseInt(add_user_id));
        if(type.equals("1")) {
            TlsSigTest.PushMessage(add_user_id,"12");
        } else {
            TlsSigTest.PushMessage(add_user_id,"14");
        }
        return JSONResult.ok();
    }

    @RequestMapping(value = "/new_friend_list")
    @ResponseBody
    JSONResult newFriendList() {
        int user_id = getCurrentUserId();
        List<UserFriend> friendList =  iUser.newFriend(user_id);
        List<Integer> list = new ArrayList<>();
        Map<Integer,String> m_state = new HashMap<>();
        for (UserFriend item : friendList) {
            list.add(item.getAdd_user_id());
            if(item.getState() == 1) {
                m_state.put(item.getAdd_user_id(),"已发送加好友通知");
            } else if(item.getState() == 2) {
                m_state.put(item.getAdd_user_id(),"已加为好友");
            }
        }

        List<UserInfo> user_list = iUser.getUserByIds(list);
        for (UserInfo info : user_list) {
            if(m_state.containsKey(info.getId())) {
                info.setFriend_state(m_state.get(info.getId()));
            }
        }
        return JSONResult.ok(user_list);
    }
}
