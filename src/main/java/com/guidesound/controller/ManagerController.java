package com.guidesound.controller;

import com.alibaba.fastjson.JSONObject;
import com.guidesound.dao.IInUser;
import com.guidesound.dao.IUser;
import com.guidesound.dao.IVideo;
import com.guidesound.models.*;
import com.guidesound.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.rmi.CORBA.Util;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class ManagerController {

    @Autowired
    private IInUser iInUser;
    @Autowired
    private IVideo iVideo;
    @Autowired
    private IUser iUser;
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
        List<VideoInfo> videoList = iVideo.getVideoByAdminId(userId);
        if(videoList.size() > 0) {
            return JSONResult.ok(getVideoShow(videoList));
        }

        videoList  = iVideo.getExamineVideo();
        if(videoList.size() > 0) {
            return JSONResult.ok(getVideoShow(videoList));
        }


        return JSONResult.ok(new ArrayList<>());
    }

    @RequestMapping(value = "/examine_video")
    @ResponseBody
    JSONResult examineVideo(String video_id,String reason_id,String reason_content) {
        Integer userId = getUserId();
        if ( userId == null ) {
            return JSONResult.errorMsg("缺少m_token");
        }

        if(video_id == null || reason_id == null || reason_content == null) {
            return JSONResult.errorMsg("缺少参数");
        }

        return JSONResult.ok();
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

    List<VideoVerify> getVideoShow(List<VideoInfo> videoList ) {
        if(videoList.size() > 0) {
            List<Integer> userIds = new ArrayList<>();
            for (VideoInfo item : videoList) {
                userIds.add(item.getUser_id());
            }
            List<VideoUser> userList = iUser.getUserInfoByIds(userIds);

            Map<Integer,VideoUser> mUser = new Hashtable<>();
            for(VideoUser item : userList) {
                mUser.put(item.getId(),item);
            }

            List<VideoVerify> videoVerifies = new ArrayList<>();
            for (VideoInfo item: videoList) {
                VideoVerify videoVerify = new VideoVerify();
                videoVerify.setVideo_id(item.getId());
                videoVerify.setVideo_title(item.getTitle());
                videoVerify.setVideo_duration(item.getDuration() + "秒");
                videoVerify.setVideo_pic_up_path(item.getPic_up_path());
                videoVerify.setVideo_video_up_path(item.getVideo_up_path());
                videoVerify.setVideo_resolution(item.getResolution());
                videoVerify.setVideo_subject(SignMap.getSubjectTypeById(item.getSubject()));
                videoVerify.setVideo_watch_type(SignMap.getWatchById(item.getWatch_type()));

                VideoUser videoUser = mUser.get(item.getUser_id());
                if(videoUser != null) {
                    videoVerify.setUser_type(SignMap.getUserTypeById(videoUser.getType()));
                    videoVerify.setUser_extend("");
                    videoVerify.setUser_grade_level(SignMap.getWatchById(videoUser.getGrade_level()));
                    videoVerify.setUser_level(SignMap.getUserLevelById(videoUser.getLevel()));
                    videoVerify.setUser_name(videoUser.getName());
                    videoVerify.setUser_subject(SignMap.getSubjectTypeById(videoUser.getSubject()));
                }
                videoVerifies.add(videoVerify);
            }
            return videoVerifies;
        }
        return null;
    }
}
