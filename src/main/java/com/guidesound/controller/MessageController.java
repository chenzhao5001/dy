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
import java.util.*;


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
        int other_user_id = Integer.parseInt(add_user_id);
        int time = (int) (new Date().getTime()/1000);
        int min_user_id = 0;
        int max_user_id = 0;
        if(current_user_id < other_user_id) {
            min_user_id = current_user_id;
            max_user_id = other_user_id;
        } else {
            min_user_id = other_user_id;
            max_user_id = current_user_id;
        }

        List<UserFriend> friends = iUser.findFriend(min_user_id,max_user_id);
        String toUsers = "";
        if(friends.size() > 0) {
            toUsers = friends.get(friends.size()-1).getTo_user_id();
            if(!toUsers.contains(add_user_id + ",")) {
                toUsers += add_user_id + ",";
            }
        } else {
            toUsers = add_user_id + ",";
        }
        if(friends.size() > 0 && friends.get(friends.size()-1).getType() < Integer.parseInt(type)) {
                iUser.updateAddFriendType(min_user_id,max_user_id,Integer.parseInt(type),toUsers);
            return JSONResult.ok();
        } else if(friends.size() > 0)  {
            iUser.updateToUser(min_user_id,max_user_id,toUsers);
            return JSONResult.ok();
        }
        iUser.addFriend(min_user_id,max_user_id,Integer.parseInt(type),time,toUsers);

        if(type.equals("1")) {
            TlsSigTest.PushMessage(add_user_id,"11");
        } else {
            TlsSigTest.PushMessage(add_user_id,"13");
        }
        return JSONResult.ok();
    }

    @RequestMapping(value = "/delete_friend")
    @ResponseBody
    JSONResult deleteFriend(String friend_id) {
        if(friend_id == null) {
            return JSONResult.errorMsg("缺少friend_id");
        }
        int current_user_id = getCurrentUserId();
        int other_user_id = Integer.parseInt(friend_id);
        int min_user_id = 0;
        int max_user_id = 0;
        if(current_user_id < other_user_id) {
            min_user_id = current_user_id;
            max_user_id = other_user_id;
        } else {
            min_user_id = other_user_id;
            max_user_id = current_user_id;
        }
        iUser.deleteFriend(min_user_id,max_user_id);
        return JSONResult.ok();
    }
    @RequestMapping(value = "/confirm_friend")
    @ResponseBody
    JSONResult confirmFriend(String add_user_id,String type) throws IOException {
        if(add_user_id == null && type == null) {
            return JSONResult.errorMsg("缺少 add_user_id 或 type");
        }

        int current_user_id = getCurrentUserId();
        int other_user_id = Integer.parseInt(add_user_id);
        int min_user_id = 0;
        int max_user_id = 0;
        if(current_user_id < other_user_id) {
            min_user_id = current_user_id;
            max_user_id = other_user_id;
        } else {
            min_user_id = other_user_id;
            max_user_id = current_user_id;
        }
        int time = (int) (new Date().getTime()/1000);

        iUser.updateFriendState(min_user_id,max_user_id,time);
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
        List<UserFriend> friendList =  iUser.newFriendByPhone(user_id);
        List<Integer> list = new ArrayList<>();
        Map<Integer,String> m_state = new HashMap<>();
        Map<Integer,Integer> m_time = new HashMap<>();
        for (UserFriend item : friendList) {
            if(!item.getTo_user_id().contains(String.valueOf(user_id) + ",")) {
                continue;
            }
            int temp_id = 0;
            if(item.getUser_id() == user_id) {
                temp_id = item.getAdd_user_id();
            } else {
                temp_id = item.getUser_id();
            }
            m_time.put(temp_id,item.getCreate_time());
            list.add(temp_id);
            if(item.getState() == 1) {
                m_state.put(temp_id,"1");
            } else if(item.getState() == 2) {
                m_state.put(temp_id,"2");
            }
        }
        if(list.size() == 0) {
            return JSONResult.ok(new ArrayList<>());
        }

        List<UserInfo> user_list = iUser.getUserByIds(list);
        for (UserInfo info : user_list) {
            if(m_time.containsKey(info.getId())) {
                info.setCreate_time(m_time.get(info.getId()));
            }
            if(m_state.containsKey(info.getId())) {
                info.setFriend_state(m_state.get(info.getId()));
            }
        }
        return JSONResult.ok(user_list);
    }
}
