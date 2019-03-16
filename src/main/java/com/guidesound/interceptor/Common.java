package com.guidesound.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.guidesound.Service.IUserService;
import com.guidesound.dao.IUser;
import com.guidesound.models.User;
import com.guidesound.models.UserInfo;
import com.guidesound.util.TockenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;


public class Common implements HandlerInterceptor {

    @Autowired
    private IUser iUser;

    private  static Logger logger = LoggerFactory.getLogger(Common.class);

    public  String getBodyString(ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            logger.warn("处理异常",e);
        }  finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.warn("处理异常",e);
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.warn("处理异常",e);
                }
            }
        }
        return sb.toString();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//        String str_req = getBodyString(request);
//        System.out.println(str_req);
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    int user_id = TockenUtil.getUserIdByTocket(token);
                    User user = iUser.getUserById(user_id);
                    if(user == null) {
                        response.setContentType("text/json; charset=utf-8");
                        PrintWriter out = response.getWriter();
                        JSONObject jsonobj = new JSONObject();
                        jsonobj.put("code", 203);
                        jsonobj.put("msg", "token错误");
                        out = response.getWriter();
                        out.println(jsonobj);
                        return false;
                    }
                    request.setAttribute("user_info",user);
                    return true;
                }
            }
        }

        response.setContentType("text/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        JSONObject jsonobj = new JSONObject();
        jsonobj.put("code", 202);
        jsonobj.put("msg", "缺少token");
        out = response.getWriter();
        out.println(jsonobj);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        int a = response.getStatus();
        System.out.println(a);
        if(response.getStatus()==500){
            String temp = response.getOutputStream().toString();
            System.out.println(temp);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }
}
