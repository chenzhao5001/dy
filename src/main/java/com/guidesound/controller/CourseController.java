package com.guidesound.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidesound.TempStruct.CourseOutline;
import com.guidesound.dao.ICourse;
import com.guidesound.dao.IUser;
import com.guidesound.dto.Course1V1DTO;
import com.guidesound.dto.CourseClassDTO;
import com.guidesound.dto.TeacherDTO;
import com.guidesound.dto.VideoDTO;
import com.guidesound.find.IntroductionInfo;
import com.guidesound.models.Course;
import com.guidesound.models.InUser;
import com.guidesound.models.Teacher;
import com.guidesound.models.User;
import com.guidesound.util.JSONResult;
import com.guidesound.util.ToolsFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/course")
public class CourseController extends BaseController{

    @Autowired
    private ICourse iCourse;
    @Autowired
    private IUser iUser;

    @RequestMapping("/add_1v1_course")
    @ResponseBody
    public JSONResult add_1v1_course(@Valid Course1V1DTO course1V1DTO,BindingResult result) {

        StringBuilder msg = new StringBuilder();
        if(!ToolsFunction.paramCheck(result,msg)) {
            return JSONResult.errorMsg(msg.toString());
        }

        course1V1DTO.setUser_id(getCurrentUserId());
        course1V1DTO.setCreate_time((int) (new Date().getTime()/1000));
        iCourse.add1v1(course1V1DTO);
        return JSONResult.ok();
    }


    @RequestMapping("/add_class_course")
    @ResponseBody
    public JSONResult addClassCourse(@Valid CourseClassDTO courseClassDTO,BindingResult result) {

        StringBuilder msg = new StringBuilder();
        if(!ToolsFunction.paramCheck(result,msg)) {
            return JSONResult.errorMsg(msg.toString());
        }
        courseClassDTO.setUser_id(getCurrentUserId());
        courseClassDTO.setCreate_time((int) (new Date().getTime()/1000));
        iCourse.addClass(courseClassDTO);
        return JSONResult.ok();
    }

    @RequestMapping("/up_course_pic")
    @ResponseBody
    public JSONResult updateCoursePic(String course_id,String pic) {
        if(course_id == null || pic == null) {
            return JSONResult.errorMsg("缺少course_id 或 pic");
        }
        int user_id = getCurrentUserId();
        iCourse.updateCoursePic(user_id,Integer.parseInt(course_id),pic);
        return JSONResult.ok();
    }

    @RequestMapping("/course_list")
    @ResponseBody
    public JSONResult getCourseList(String user_id) {
        if(user_id == null) {
            return JSONResult.errorMsg("缺少 user_id");
        }
        List<Course> list = iCourse.getCourseList(Integer.parseInt(user_id));
        return JSONResult.ok(list);
    }

    @RequestMapping("/set_outline")
    @ResponseBody
    public JSONResult setCourseOutline(String course_id,String outline) throws IOException {

        if(course_id == null || outline == null) {
            return JSONResult.errorMsg("缺少 course_id 获 outline");
        }
        ObjectMapper mapper = new ObjectMapper();
        List<CourseOutline> beanList = mapper.readValue(outline, new TypeReference<List<CourseOutline>>() {});
        if(beanList.size() < 1) {
            return JSONResult.errorMsg("格式错误");
        }
        CourseOutline lastItem = beanList.get(beanList.size() -1);
        int overTime = lastItem.getTime() + 3600*lastItem.getDuration();
        String content  = mapper.writeValueAsString(beanList);
        iCourse.setCourseOutline(Integer.parseInt(course_id),content,overTime);
        return JSONResult.ok();
    }


    @RequestMapping("/get_outline")
    @ResponseBody
    public JSONResult setCourseOutline(String course_id) throws IOException {
        if(course_id == null) {
            return JSONResult.errorMsg("缺少course_id");
        }
        ObjectMapper mapper = new ObjectMapper();
        String outLine = iCourse.getCourseOutline(Integer.parseInt(course_id));
        List<CourseOutline> beanList = mapper.readValue(outLine, new TypeReference<List<CourseOutline>>() {});
        return JSONResult.ok(beanList);
    }

    @RequestMapping("/up_introduction_pic")
    @ResponseBody
    public JSONResult upIntroductionPic(String introduction_pic) {
        if(introduction_pic == null) {
            return JSONResult.errorMsg("缺少参数");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User) request.getAttribute("user_info");
        if(currentUser.getType() != 1) {
            return JSONResult.errorMsg("不是老师");
        }
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

        if(currentUser.getType() != 1) {
            return JSONResult.errorMsg("不是老师");
        }
        iUser.upIntroduction(currentUser.getId(),introduction);
        return JSONResult.ok();
    }

    @RequestMapping("/introduction_info")
    @ResponseBody
    public JSONResult getIntroductionInfo(String user_id) {

        if(user_id == null) {
            return JSONResult.errorMsg("缺少 user_id");
        }
        IntroductionInfo introductionInfo = iUser.getIntroductionInfo(Integer.parseInt(user_id));
        return JSONResult.ok(introductionInfo);
    }


    @RequestMapping("/add_teacher")
    @ResponseBody
    public JSONResult addTeacher(@Valid TeacherDTO teacherDTO, BindingResult result) {

        StringBuilder msg = new StringBuilder();
        if(!ToolsFunction.paramCheck(result,msg)) {
            return JSONResult.errorMsg(msg.toString());
        }
        Teacher teacher = new Teacher();
        teacher.setId(Integer.parseInt(teacherDTO.getId()));
        teacher.setName(teacherDTO.getName());
        teacher.setSex(Integer.parseInt(teacherDTO.getSex()));
        teacher.setSubject(Integer.parseInt(teacherDTO.getSubject()));
        teacher.setLevel(Integer.parseInt(teacherDTO.getLevel()));
        teacher.setIntroduction(teacherDTO.getIntroduction());
        teacher.setUser_id(getCurrentUserId());
        if(teacherDTO.getId().equals("0")) {  //新增
            teacher.setCreate_time((int) (new Date().getTime() /1000));
            teacher.setUpdate_time((int) (new Date().getTime() /1000));
            iCourse.addTeacher(teacher);
        } else { //修改
            teacher.setUpdate_time((int) (new Date().getTime() /1000));
            iCourse.updateTeacher(teacher);
        }
        return JSONResult.ok();
    }
}
