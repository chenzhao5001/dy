package com.guidesound.controller;

import com.alibaba.fastjson.JSON;
import com.guidesound.util.JSONResult;
import com.guidesound.util.TlsSigTest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;


@Controller
@RequestMapping("/message")
public class MessageController {
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
}
