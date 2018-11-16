package com.guidesound.controller;

import com.guidesound.dao.IInUser;
import com.guidesound.dao.IVerifyCode;
import com.guidesound.models.InUser;
import com.guidesound.util.JSONResult;
import com.guidesound.util.VerifyCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class ManagerController {

    @Autowired
    private IInUser iInUser;
    @RequestMapping(value = "/login")
    @ResponseBody
    JSONResult logIn(HttpServletRequest request, HttpServletResponse response) {

        String account = request.getParameter("account");
        String pwd = request.getParameter("pwd");
        String code = request.getParameter("code");
        if(account == null || pwd == null || code == null) {
            return JSONResult.errorMsg("缺少参数");
        }
        HttpSession session = request.getSession(true);
        String temp = (String) session.getAttribute("verCode");

//        if (temp == null || temp != code) {
//            return JSONResult.errorMsg("验证码错误");
//        }

        InUser inUser = iInUser.getUserByName(account);
        if(inUser == null || inUser.getPwd() != pwd) {
            return JSONResult.errorMsg("账号密码错误");
        }

        return null;
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

    @RequestMapping(value = "/current_video_list")
    @ResponseBody
    JSONResult currentVideoList(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @RequestMapping(value = "/new_video_list")
    @ResponseBody
    JSONResult newVideoList(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @RequestMapping(value = "/examine_video")
    @ResponseBody
    JSONResult examineVideo(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
