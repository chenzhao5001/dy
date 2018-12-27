package com.guidesound.controller;

import com.guidesound.util.ToolsFunction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/")
public class Root {
    @RequestMapping(value = "")
    @ResponseBody
    String root(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/index111.html").forward(request,response);
        String me_url = "https://daoyinjiaoyu.com/user_dy/user" +  "?" + request.getQueryString();
        String open_info =  ToolsFunction.httpGet(me_url);
        return open_info;
    }
}
