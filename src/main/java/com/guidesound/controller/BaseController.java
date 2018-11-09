package com.guidesound.controller;


import com.guidesound.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BaseController {

//    protected User currentUser;
    static Logger log;
    @ModelAttribute
    public void common(HttpServletRequest request, HttpServletResponse response) {
        if(log == null ) {
            log = (Logger) LogManager.getLogger();
        }
//        currentUser = (User)request.getAttribute("user_info");
    }
}
