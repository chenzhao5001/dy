package com.guidesound.controller;

import com.guidesound.Service.IUserService;
import com.guidesound.models.User;
import com.guidesound.util.TockenUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {

    protected User currentUser;
    static Logger log;
    @ModelAttribute
    public void common(HttpServletRequest request, HttpServletResponse response) {
        if(log == null ) {
            log = (Logger) LogManager.getLogger();
        }
        currentUser = (User)request.getAttribute("user_info");
    }
}
