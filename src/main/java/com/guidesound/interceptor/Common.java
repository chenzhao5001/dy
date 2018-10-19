package com.guidesound.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.guidesound.Service.IUserService;
import com.guidesound.models.User;
import com.guidesound.util.TockenUtil;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


public class Common implements HandlerInterceptor {

    @Resource
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    int user_id = TockenUtil.getUserIdByTocket(token);
                    User user = userService.getUserById(user_id);
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
//        System.out.println("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
//        System.out.println("afterCompletion");
    }
}
