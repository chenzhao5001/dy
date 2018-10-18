package com.guidesound.controller;


import com.guidesound.dao.IParent;
import com.guidesound.dao.IStudent;
import com.guidesound.dao.ITeacher;
import com.guidesound.dao.IUser;
import com.guidesound.models.Parent;
import com.guidesound.models.Student;
import com.guidesound.models.Teacher;
import com.guidesound.models.User;
import com.guidesound.util.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
@RequestMapping("/register")
public class Register extends BaseController {

    @Autowired
    private IParent iParent;
    @Autowired
    private ITeacher iTeacher;
    @Autowired
    private IStudent iStudent;
    @Autowired
    private IUser iUser;
    @RequestMapping(value = "/student")

    public @ResponseBody ServiceResponse student(HttpServletRequest request, HttpServletResponse response)
    {
        String grade = request.getParameter("grade");
        ServiceResponse serviceResponse = new ServiceResponse();
        if (grade == null) {
            serviceResponse.setCode(201);
            serviceResponse.setMsg("缺少参数");
            return serviceResponse;
        }


        User user = (User)request.getAttribute("user_info");
        if(user.getStatus() != 1) {
            serviceResponse.setCode(202);
            serviceResponse.setMsg("只有 status=1 才能进行此步骤");
            return serviceResponse;
        }

        iUser.updateStatus(user.getId(),2);
        iUser.updateType(user.getId(),1);

        Student student = new Student();
        student.setUser_id(user.getId());
        student.setGrade(Integer.parseInt(grade));
        student.setCreate_time((int) (new Date().getTime() / 1000));
        student.setUpdate_time((int) (new Date().getTime() / 1000));
        iStudent.add(student);

        serviceResponse.setCode(200);
        serviceResponse.setMsg("ok");
        return serviceResponse;
    }

    @RequestMapping(value = "/teacher")
    public @ResponseBody ServiceResponse teacher(HttpServletRequest request, HttpServletResponse response) {

        String subject = request.getParameter("subject");
        String rank = request.getParameter("rank");
        ServiceResponse serviceResponse = new ServiceResponse();
        if (subject == null || rank == null) {
            serviceResponse.setCode(201);
            serviceResponse.setMsg("缺少参数");
            return serviceResponse;
        }

        User user = (User)request.getAttribute("user_info");
        System.out.println(user.getStatus());
        if(user.getStatus() != 1) {
            serviceResponse.setCode(202);
            serviceResponse.setMsg("只有 status=1 才能进行此步骤");
            return serviceResponse;
        }

        iUser.updateStatus(user.getId(),2);
        iUser.updateType(user.getId(),2);


        Teacher teacher = new Teacher();
        teacher.setUser_id(user.getId());
        teacher.setSubject(Integer.parseInt(subject));
        teacher.setSubject(Integer.parseInt(rank));
        teacher.setCreate_time((int) (new Date().getTime() / 1000));
        teacher.setUpdate_time((int) (new Date().getTime() / 1000));
        iTeacher.add(teacher);

        serviceResponse.setCode(200);
        serviceResponse.setMsg("ok");
        return serviceResponse;
    }

    @RequestMapping(value = "/parent")
    public @ResponseBody ServiceResponse parent(HttpServletRequest request, HttpServletResponse response) {
        String grade = request.getParameter("grade");
        ServiceResponse serviceResponse = new ServiceResponse();
        if (grade == null) {
            serviceResponse.setCode(201);
            serviceResponse.setMsg("缺少参数");
            return serviceResponse;
        }

        User user = (User)request.getAttribute("user_info");
        if(user.getStatus() != 1) {
            serviceResponse.setCode(202);
            serviceResponse.setMsg("只有 status=1 才能进行此步骤");
            return serviceResponse;
        }


        iUser.updateStatus(user.getId(),2);
        iUser.updateType(user.getId(),3);

        Parent parent = new Parent();
        parent.setUser_id(user.getId());
        parent.setGrade(Integer.parseInt(grade));
        parent.setCreate_time((int) (new Date().getTime() / 1000));
        parent.setUpdate_time((int) (new Date().getTime() / 1000));
        iParent.add(parent);

        serviceResponse.setCode(200);
        serviceResponse.setMsg("ok");
        return serviceResponse;
    }
}
