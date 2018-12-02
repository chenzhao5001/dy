package com.guidesound.controller;

import com.guidesound.dao.ICourse;
import com.guidesound.models.User;
import com.guidesound.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@RequestMapping("/course")
public class CourseController {


    @Autowired
    private ICourse iCourse;

    @RequestMapping("/add_1v1")
    @ResponseBody
    public JSONResult add1v1(String course_name, String subject, String grade_level, String method, String price, String pictures, String course_area) {
        String name = (course_name == null) ? "" : course_name;
        int i_subject = (subject == null) ? 0 : Integer.parseInt(subject);
        int i_grade_level = (grade_level == null) ? 0 : Integer.parseInt(grade_level);
        int i_method = (method == null) ? 0 : Integer.parseInt(method);
        int i_price = (price == null) ? 0 : Integer.parseInt(price);
        String pic = (pictures == null) ? "" : pictures;
        String area = (course_area == null) ? "" : course_area;

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User) request.getAttribute("user_info");

        int time = (int) (new Date().getTime() / 1000);
        iCourse.add1v1(currentUser.getId(),name,i_subject,i_grade_level,i_method,i_price,pic,area,time);
        return JSONResult.ok();

    }

    @RequestMapping("/add_class")
    @ResponseBody
    public JSONResult addClass() {

        return JSONResult.ok();
    }

    @RequestMapping("/up_show_pic")
    @ResponseBody
    public JSONResult upUserShowPic(String user_id,String pic) {
        if(user_id == null || pic == null) {
            return JSONResult.errorMsg("缺少参数");
        }
        return JSONResult.ok();
    }

    @RequestMapping("/up_introduction")
    @ResponseBody
    public JSONResult upUserIntroduction(String user_id,String introduction) {
        if(user_id == null || introduction == null) {
            return JSONResult.errorMsg("缺少参数");
        }
        return JSONResult.ok();
    }
}
