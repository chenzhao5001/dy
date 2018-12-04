package com.guidesound.controller;

import com.guidesound.dao.ICourse;
import com.guidesound.dao.IUser;
import com.guidesound.find.IntroductionInfo;
import com.guidesound.models.InUser;
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
    @Autowired
    private IUser iUser;

    @RequestMapping("/add_course")
    @ResponseBody
    public JSONResult addCourse(String course_name,
                             String subject,
                             String grade_level,
                             String method,
                             String parents_state,
                             String price,
                             String course_area,
                                String test_method,
                                String test_duration,
                                String test_price,
                                String max_student,
                                String all_duration,
                                String all_price,
                                String test_live_time,
                                String course_introduction,
                                String course_outline,
                                String teacher,
                                String teacher_introduction
                                ) {
        String name = (course_name == null) ? "" : course_name;
        int i_subject = (subject == null) ? 0 : Integer.parseInt(subject);
        int i_grade_level = (grade_level == null) ? 0 : Integer.parseInt(grade_level);
        int i_method = (method == null) ? 0 : Integer.parseInt(method);
        int i_price = (price == null) ? 0 : Integer.parseInt(price);
        String area = (course_area == null) ? "" : course_area;
        int i_parents_state = (parents_state == null) ? 0:Integer.parseInt(parents_state);
        int i_test_method = (test_method == null) ? 0: Integer.parseInt(test_method);
        int i_test_duration = (test_duration == null) ? 0:Integer.parseInt(test_duration);

        int i_test_price = (test_price == null) ? 0:Integer.parseInt(test_price);
        int i_max_student = (max_student == null) ? 0:Integer.parseInt(max_student);
        int i_all_hours = (all_duration == null) ? 0:Integer.parseInt(all_duration);
        int i_all_price = (all_price == null) ? 0:Integer.parseInt(all_price);
        int i_test_live_time = (test_live_time == null) ? 0:Integer.parseInt(test_live_time);

        course_introduction = (course_introduction == null) ? "":course_introduction;
        course_outline = (course_outline == null) ? "":course_outline;
        teacher = (teacher == null) ? "":teacher;
        teacher_introduction = (teacher_introduction == null) ? "":teacher_introduction;

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User) request.getAttribute("user_info");

        int time = (int) (new Date().getTime() / 1000);
//        iCourse.add1v1(currentUser.getId(),name,i_subject,i_grade_level,i_method,i_price,pic,area,time);
        return JSONResult.ok();

    }


    @RequestMapping("/up_introduction_pic")
    @ResponseBody
    public JSONResult upIntroductionPic(String introduction_pic) {
        if(introduction_pic == null) {
            return JSONResult.errorMsg("缺少参数");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User) request.getAttribute("user_info");
        iUser.upIntroductionPic(currentUser.getId(),introduction_pic);
        return JSONResult.ok();
    }

    @RequestMapping("/up_introduction")
    @ResponseBody
    public JSONResult upIntroduction(String introduction) {
        if(introduction == null) {
            return JSONResult.errorMsg("缺少参数");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User) request.getAttribute("user_info");
        iUser.upIntroduction(currentUser.getId(),introduction);
        return JSONResult.ok();
    }

    @RequestMapping("/introduction_info")
    @ResponseBody
    public JSONResult getIntroductionInfo(String user_id) {
        if(user_id == null) {
            return JSONResult.errorMsg("缺少参数");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User) request.getAttribute("user_info");
        IntroductionInfo introductionInfo = iUser.getIntroductionInfo(currentUser.getId());
        return JSONResult.ok(introductionInfo);
    }
}
