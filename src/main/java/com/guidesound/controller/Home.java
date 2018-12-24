package com.guidesound.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/home")
public class Home {
    @RequestMapping(value = "")
    public void home(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.getRequestDispatcher("/").forward(request,response);
        String path = request.getScheme() + "://" + request.getServerName() + "?" + request.getQueryString();
        response.sendRedirect(path);
    }
}
