package com.guidesound.controller;

import com.alibaba.fastjson.JSONObject;
import com.guidesound.dao.IInUser;
import com.guidesound.dao.IVideo;
import com.guidesound.models.InUser;
import com.guidesound.models.User;
import com.guidesound.util.JSONResult;
import com.guidesound.util.TockenUtil;
import com.guidesound.util.VerifyCodeUtils;
import com.guidesound.util.VideoExamine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class ManagerController {

    @Autowired
    private IInUser iInUser;
    private IVideo iVideo;
    @RequestMapping(value = "/login")
    @ResponseBody
    JSONResult logIn(HttpServletRequest request, HttpServletResponse response) {

        String account = request.getParameter("account");
        String pwd = request.getParameter("pwd");
        if(account == null || pwd == null) {
            return JSONResult.errorMsg("缺少参数");
        }

        List<InUser> inUser = iInUser.getUserByName(account);
        if (inUser.size() == 0 ) {
            return JSONResult.errorMsg("账号密码错误");
        }

        String temp = inUser.get(0).getPwd();
        if(!temp.equals(pwd)) {
            return JSONResult.errorMsg("账号密码错误");
        }

        String token = TockenUtil.makeTocken(inUser.get(0).getId());
        token = "m_token=" + token;
        return JSONResult.ok(token);
    }

    @RequestMapping(value = "/code")
    void identifyingCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        HttpSession session = request.getSession(true);
        session.removeAttribute("verCode");
        session.setAttribute("verCode", verifyCode.toLowerCase());

        int w = 100, h = 40;
        VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
    }

    @RequestMapping(value = "/fail_reason_list")
    @ResponseBody
    JSONResult failReasonList() {
        Map<Integer,String> reason = VideoExamine.getReason();
        class Item {
            int id;
            String reason;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }
        }

        List<Item> list = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : reason.entrySet()) {
            Item item = new Item();
            item.setId(entry.getKey());
            item.setReason(entry.getValue());
            list.add(item);
        }
        return JSONResult.ok(list);
    }

    @RequestMapping(value = "/current_video_list")
    @ResponseBody
    JSONResult currentVideoList(HttpServletRequest request, HttpServletResponse response) {
        Integer userId = getUserId();
        if ( userId == null ) {
            return JSONResult.errorMsg("缺少m_token");
        }

        iVideo.setVideoShowPath();





        return null;
    }

    @RequestMapping(value = "/examine_video")
    @ResponseBody
    JSONResult examineVideo(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }



    Integer getUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("m_token")) {
                    String token = cookie.getValue();
                    int user_id = TockenUtil.getUserIdByTocket(token);
                    return user_id;
                }
            }
        }
        return null;
    }
}
