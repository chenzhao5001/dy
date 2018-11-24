package com.guidesound.controller;

import com.alibaba.fastjson.JSONObject;
import com.guidesound.dao.IInUser;
import com.guidesound.dao.IUser;
import com.guidesound.dao.IVideo;
import com.guidesound.models.*;
import com.guidesound.util.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.jms.*;
import javax.rmi.CORBA.Util;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;
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
        if( userId  == - 1) {
            return JSONResult.errorMsg("m_token 错误");
        }
        List<VideoInfo> videoList = iVideo.getVideoByAdminId(userId);
        if(videoList.size() > 0) {
            return JSONResult.ok(getVideoShow(videoList));
        }

        videoList  = iVideo.getExamineVideo();
        if(videoList.size() > 0) {
            ArrayList<Integer> list = new ArrayList<>();
            for (VideoInfo item : videoList) {
                list.add(item.getId());
            }

            iVideo.setExaminePerson(list,userId);
            return JSONResult.ok(getVideoShow(videoList));
        }


        return JSONResult.ok(new ArrayList<>());
    }

    @RequestMapping(value = "/examine_video")
    @ResponseBody
    JSONResult examineVideo(String video_id,String status,String type_list,String fail_reason,String fail_content) throws IOException, InterruptedException, JMSException {

        Integer userId = getUserId();
        if ( userId == null ) {
            return JSONResult.errorMsg("缺少m_token");
        }

        if ( video_id == null || status == null ) {
            return JSONResult.errorMsg("缺少status 或 video_id");
        }
        if(Integer.parseInt(status) == 1) {
            if(type_list == null) {
                return JSONResult.errorMsg("缺少type_list");
            }

            MessageProducer messageProducer = null;
            Session session = null;

            String mqUrl = "tcp://139.199.112.147:61616";
            String qName = "examine";
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(mqUrl);
            try {
                Connection connection = connectionFactory.createConnection();
                connection.start();
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination destination = session.createQueue(qName);
                messageProducer = session.createProducer(destination);
                if(messageProducer != null || messageProducer != null) {
                    TextMessage textMessage = session.createTextMessage(String.valueOf(video_id));
                    messageProducer.send(textMessage);
                }
                connection.close();
                iVideo.setExamineLoading(Integer.parseInt(video_id),type_list);

            } catch (JMSException e) {
                e.printStackTrace();
            }

//            String tempUrl = iVideo.getTempVideoById(Integer.parseInt(video_id));
//            String url = ToolsFunction.changeVideo(tempUrl);
        } else {
            if(fail_reason == null || fail_content == null) {
                return JSONResult.errorMsg("缺少fail_reason或fail_content");
            }
            iVideo.setExamineFail(Integer.parseInt(video_id),fail_reason,fail_content);
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
                    token = URLDecoder.decode(token);
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
