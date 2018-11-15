package com.guidesound.controller;

import com.guidesound.Service.IUserService;
import com.guidesound.dao.*;
import com.guidesound.dto.StudentInfoDTO;
import com.guidesound.models.Student;
import com.guidesound.models.User;
import com.guidesound.models.UserInfo;
import com.guidesound.resp.UserResp;
import com.guidesound.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 用户控制器
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController{

    @Autowired
    private IUserService userService;
    @Autowired
    private IVerifyCode iVerifyCode;
    @Autowired
    private IUser iUser;
    @Autowired
    private IVideo iVideo;
    @Autowired
    private IArticle iArticle;
    @Autowired
    private IStudent iStudent;

    /**
     * 用户登录
     * @param
     * @return 登录结果
     */

    @RequestMapping(value = "/login")
    @ResponseBody
    public JSONResult login(HttpServletRequest request, HttpServletResponse response) {

        String unionid = request.getParameter("unionid");
        String name = request.getParameter("name");
        String head = request.getParameter("head");
        if (unionid == null || name == null || head == null) {
            return JSONResult.errorMsg("缺少参数");
        }

        User user = userService.login(unionid,name,head);
        String token = TockenUtil.makeTocken(user.getId());

        UserResp userResp = new UserResp();
        userResp.setId(user.getId());
        userResp.setToken(token);
        userResp.setPhone(user.getPhone());
        userResp.setName(user.getName());
        userResp.setHead(user.getHead());
        userResp.setType(user.getType());
        userResp.setLevel(user.getLevel());
        userResp.setStatus(user.getStatus());
        userResp.setSign_name(user.getSign_name());
        userResp.setTeach_age(user.getTeach_age());

        int funCount = iUser.getFunsById(String.valueOf(user.getId()));
        int followCount = iUser.getFollowById(String.valueOf(user.getId()));
        int praiseCount = iUser.getPraiseById(String.valueOf(user.getId()));
        int videoCount = iVideo.getVideoByUserId(String.valueOf(user.getId()));
        int articleCount =  iArticle.getCountByUserId(String.valueOf(user.getId()));

        userResp.setFuns_counts(funCount);
        userResp.setFollow_count(followCount);
        userResp.setPraise_count(praiseCount);
        userResp.setVideo_count(videoCount);
        userResp.setArticle_count(articleCount);
        userResp.setCreate_time(user.getCreate_time());


        //种cookie
        Cookie cookie = new Cookie("token",token);//创建新cookie
        cookie.setPath("/");//设置作用域
        response.addCookie(cookie);//将cookie添加到response的cookie数组中返回给客户端

        return JSONResult.ok(userResp);
    }

    @RequestMapping(value = "/identifying_code")
    @ResponseBody
    public JSONResult getIdentifyingCode(String phone) {
        if(phone == null || !ToolsFunction.isNumeric(phone) || phone.length() != 11) {
            return JSONResult.build(201,"参数错误",null);
        }
        ///
        //请求调用第三方发送第三方接口，发送短信
        ///

        String code = "1234";
        int time = (int) (new Date().getTime() / 1000);
        iVerifyCode.addVerifyCode(phone,code,time,time);
        return JSONResult.ok();
    }


    /**
     *手机号登录
     */
    @RequestMapping(value = "/phone_login")
    public @ResponseBody JSONResult phoneLogin(HttpServletRequest request) {
        String phone = request.getParameter("phone");
        String code = request.getParameter("code");

        if(phone == null || code == null || !ToolsFunction.isNumeric(phone) || phone.length() != 11) {
            return JSONResult.build(201,"参数错误",null);
        }

        int time = (int) (new Date().getTime() / 1000) - 600;
        int count = iVerifyCode.selectCode(phone,code,time);
        if(count <= 0) {
            return JSONResult.build(201,"验证码错误",null);
        }

        List<User> userList = iUser.getUserByPhone(phone);
        User user = null;
        if(userList.isEmpty()) {
            user = new User();
            user.setPhone(phone);
            user.setCreate_time((int) (new Date().getTime() / 1000));
            user.setUpdate_time((int) (new Date().getTime() / 1000));
            user.setLevel(1);
            iUser.addUserByPhone(user);
//            user.setToken(TockenUtil.makeTocken(user.getId()));
        } else {
            user = userList.get(0);
        }
        user.setToken(TockenUtil.makeTocken(user.getId()));
        UserResp userResp = new UserResp();
        userResp.setId(user.getId());
        userResp.setToken(TockenUtil.makeTocken(user.getId()));
        userResp.setUnionid(user.getUnionid());
        userResp.setPhone(user.getPhone());
        userResp.setName(user.getName());
        userResp.setHead(user.getHead());
        userResp.setType(user.getType());
        userResp.setLevel(user.getLevel());
        userResp.setStatus(user.getStatus());
        userResp.setSign_name(user.getSign_name());
        userResp.setTeach_age(user.getTeach_age());

        int funCount = iUser.getFunsById(String.valueOf(user.getId()));
        int followCount = iUser.getFollowById(String.valueOf(user.getId()));
        int praiseCount = iUser.getPraiseById(String.valueOf(user.getId()));
        int videoCount = iVideo.getVideoByUserId(String.valueOf(user.getId()));
        int articleCount =  iArticle.getCountByUserId(String.valueOf(user.getId()));

        userResp.setFuns_counts(funCount);
        userResp.setFollow_count(followCount);
        userResp.setPraise_count(praiseCount);
        userResp.setVideo_count(videoCount);
        userResp.setArticle_count(articleCount);
        userResp.setCreate_time(user.getCreate_time());

        return JSONResult.ok(userResp);
    }

    /**
     *昵称登录
     */
    @RequestMapping(value = "/name_login")
    @ResponseBody
    public JSONResult loginByName(String name) {
        if(name == null) {
            return JSONResult.errorMsg("缺少name参数");
        }

        List<User> userList = iUser.getUserByName(name);
        User user = null;
        if(userList.isEmpty()) {
            user = new User();
            user.setName(name);
            user.setCreate_time((int) (new Date().getTime() / 1000));
            user.setUpdate_time((int) (new Date().getTime() / 1000));
            user.setLevel(1);
            iUser.addUserByName(user);
            user.setToken(TockenUtil.makeTocken(user.getId()));
        } else {
            user = userList.get(0);
        }
        user.setToken(TockenUtil.makeTocken(user.getId()));

        UserResp userResp = new UserResp();
        userResp.setId(user.getId());
        userResp.setToken(TockenUtil.makeTocken(user.getId()));
        userResp.setUnionid(user.getUnionid());
        userResp.setPhone(user.getPhone());
        userResp.setName(user.getName());
        userResp.setHead(user.getHead());
        userResp.setType(user.getType());
        userResp.setLevel(user.getLevel());
        userResp.setStatus(user.getStatus());
        userResp.setSign_name(user.getSign_name());
        userResp.setTeach_age(user.getTeach_age());

        int funCount = iUser.getFunsById(String.valueOf(user.getId()));
        int followCount = iUser.getFollowById(String.valueOf(user.getId()));
        int praiseCount = iUser.getPraiseById(String.valueOf(user.getId()));
        int videoCount = iVideo.getVideoByUserId(String.valueOf(user.getId()));
        int articleCount =  iArticle.getCountByUserId(String.valueOf(user.getId()));

        userResp.setFuns_counts(funCount);
        userResp.setFollow_count(followCount);
        userResp.setPraise_count(praiseCount);
        userResp.setVideo_count(videoCount);
        userResp.setArticle_count(articleCount);
        userResp.setCreate_time(user.getCreate_time());

        return JSONResult.ok(userResp);
    }

    /**
     * 用户退出
     * @return
     */
    @RequestMapping(value = "/logout")
    public  @ResponseBody ServiceResponse logout(HttpServletRequest request, HttpServletResponse response) {
        ServiceResponse rep = new ServiceResponse();

//        System.out.println(currentUser);
//        rep.setCode(200);
        rep.setMsg("用户退出");
        return rep;
    }

    /**
     * 获得用户类型列表
     */
    @RequestMapping(value = "/type_list")
    @ResponseBody
    public JSONResult getUserType() {
        return JSONResult.ok(SignMap.getUserTypeList());
    }

    /**
     * 设置用户类型
     */
    @RequestMapping(value = "/set_type")
    @ResponseBody
    public JSONResult setUserType(String user_type) {
        if(user_type == null) {
            return JSONResult.errorMsg("需要user_type");
        }

        if(!SignMap.getUserTypeList().containsKey(Integer.parseInt(user_type))) {
            return JSONResult.errorMsg("设置的类型不存在");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        iUser.updateType(currentUser.getId(),user_type);
        return JSONResult.ok();
    }


    /**
     *息获得用户信息
     */
    @RequestMapping(value = "/info")
    @ResponseBody
    public JSONResult getUserInfoById(String user_id) {

        if(user_id == null) {
            return JSONResult.errorMsg("需要user_id");
        }
        UserInfo userInfo = iUser.getUserInfo(user_id);

        if (userInfo == null) {
            return JSONResult.errorMsg("此用户不存在");
        }
        int funCount = iUser.getFunsById(user_id);
        int followCount = iUser.getFollowById(user_id);
        int praiseCount = iUser.getPraiseById(user_id);
        int videoCount = iVideo.getVideoByUserId(user_id);
        int articleCount =  iArticle.getCountByUserId(user_id);

        userInfo.setFuns_counts(funCount);
        userInfo.setFollow_count(followCount);
        userInfo.setPraise_count(praiseCount);
        userInfo.setVideo_count(videoCount);
        userInfo.setArticle_count(articleCount);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");

        if (iUser.getIdByUserAndFunsId(Integer.parseInt(user_id),currentUser.getId()) == 0) {
            userInfo.setFollow(false);
        } else {
            userInfo.setFollow(true);
        }
        return JSONResult.ok(userInfo);
    }
    /**
     * 关注用户
     * @return
     */
    @RequestMapping(value = "/follow")
    @ResponseBody
    public JSONResult addFuns(String user_id) {
        if(user_id == null) {
            return JSONResult.errorMsg("参数缺少user_id");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");

        iUser.followUser(currentUser.getId(),
                Integer.parseInt(user_id),
                (int)(new Date().getTime() /1000));
        return JSONResult.ok();
    }

    /**
     *取消关注
     */
    @RequestMapping(value = "/cancel_follow")
    @ResponseBody
    public  JSONResult deleteFollow(String user_id) {
        if(user_id == null) {
            return JSONResult.errorMsg("参数缺少user_id");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");

        iUser.cancelFollow(currentUser.getId(),Integer.parseInt(user_id),(int)(new Date().getTime() /1000));
        return JSONResult.ok();
    }

    /**
     *给用户点赞
     */
    @RequestMapping(value = "/praise")
    @ResponseBody
    public JSONResult addPraise(String user_id) {
        if (user_id == null) {
            return JSONResult.errorMsg("参数缺少user_id");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");

        iUser.praiseUser(currentUser.getId(),
                Integer.parseInt(user_id),
                (int)(new Date().getTime() /1000));
        return JSONResult.ok();
    }

    /**
     *学生完善基本信息
     */
    @RequestMapping(value = "/update_student_basic_info")
    @ResponseBody
    public JSONResult addStudentBasicInfo(@Valid StudentInfoDTO studentInfoDTO, BindingResult result) {

        StringBuilder msg = new StringBuilder();
        if(!ToolsFunction.paramCheck(result,msg)) {
            return JSONResult.errorMsg(msg.toString());
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");

        Student student = new Student();
        student.setUser_id(currentUser.getId());
        student.setLevel(Integer.parseInt(studentInfoDTO.getLevel()));
        student.setGrade(Integer.parseInt(studentInfoDTO.getGrade()));
        student.setSex(Integer.parseInt(studentInfoDTO.getSex()));
        student.setCreate_time((int)(new Date().getTime() / 1000));
        student.setUpdate_time((int)(new Date().getTime() / 1000));
        Student temp = iStudent.find(currentUser.getId());
        if(temp == null) { //新建
            iStudent.add(student);
        } else { //修改
            iStudent.update(student);
        }
        return JSONResult.ok();
    }

    /**
     *修改头像
     */
    @RequestMapping(value = "/update_head")
    @ResponseBody
    JSONResult upHead(String head_url) {
        if(head_url == null) {
            return JSONResult.errorMsg("缺少参数head_url");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        iUser.updateHead(currentUser.getId(),head_url);
        return JSONResult.ok();
    }

    /**
     *修改个性签名
     */
    @RequestMapping(value = "/update_sign_name")
    @ResponseBody
    JSONResult upSignName(String sign_name) {
        if(sign_name == null) {
            return JSONResult.errorMsg("缺少参数sign_name");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        iUser.updateSignName(currentUser.getId(),sign_name);
        return JSONResult.ok();
    }

    /**
     *修改昵称
     */
    @RequestMapping(value = "/update_name")
    @ResponseBody
    JSONResult upName(String name) {
        if (name == null) {
            return JSONResult.errorMsg("缺少参数name");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        iUser.updateName(currentUser.getId(),name);
        return JSONResult.ok();
    }

    /**
     *修改性别接口
     */
    @RequestMapping(value = "/update_sex")
    @ResponseBody
    JSONResult upSex(String sex) {
        if ( sex == null ) {
            return JSONResult.errorMsg("缺少参数sex");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        iUser.updateSex(currentUser.getId(),Integer.parseInt(sex));
        return JSONResult.ok();

    }

    /**
     *修改年级接口
     */
    @RequestMapping(value = "/update_grade")
    @ResponseBody
    JSONResult updateGrade(String grade) {
        if (grade == null) {
            return JSONResult.errorMsg("缺少参数grade");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        int level = currentUser.getLevel();
        if(level < 3) {
            level = 3;
        }
        iUser.updateGrade(currentUser.getId(),Integer.parseInt(grade),level);
        return JSONResult.ok();
    }

    /**
     *绑定手机号
     */
    @RequestMapping(value = "/update_phone")
    @ResponseBody
    JSONResult updatePhone(String phone) {
        if ( phone == null ) {
            return JSONResult.errorMsg("缺少请求参数phone");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        int level = currentUser.getLevel();
        if(level < 4) {
            level = 4;
        }

        iUser.updatePhone(currentUser.getId(),phone,level);
        return JSONResult.ok();
    }

    /**
     *设置省市接口
     */
    @RequestMapping(value = "/set_area")
    @ResponseBody
    public JSONResult setArea(String province, String city,String area) {
        if (province == null || city == null || area == null) {
            return JSONResult.errorMsg("缺少province 或 city 或 area 字段");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");

        int level = currentUser.getLevel();
        if(level < 3) {
            level = 3;
        }
        iUser.updateProvinceAndCity(currentUser.getId(),province,city,area,level);
        return  JSONResult.ok();
    }

    /**
     *设置学科接口
     */
    @RequestMapping(value = "/set_subject")
    @ResponseBody

    public JSONResult setSubject(String subject) {
        if ( subject == null ) {
            return JSONResult.errorMsg("缺少subject");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        iUser.updateSubject(currentUser.getId(),Integer.parseInt(subject));
        return  JSONResult.ok();
    }

    /**
     * 设置年级阶段接口
     */

    @RequestMapping(value = "/set_grade_level")
    @ResponseBody

    public JSONResult setGradeLevel(String grade_level) {
        if( grade_level == null) {
            return JSONResult.errorMsg("缺少grade_level 参数");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        iUser.updateGradeLevel(currentUser.getId(),Integer.parseInt(grade_level));
        return JSONResult.ok();
    }

    /**
     * 设置教龄接口
     */

    @RequestMapping(value = "/set_teach_age")
    @ResponseBody
    public JSONResult setTeachAge(String teach_age) {
        if ( teach_age == null ) {
            return JSONResult.errorMsg("缺少 teach_age 参数");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");

        iUser.updateTeachAge(currentUser.getId(),Integer.parseInt(teach_age));
        return JSONResult.ok();
    }

    /**
     * 设置身份认证接口
     */
    @RequestMapping(value = "/identity_auth")
    @ResponseBody
    public JSONResult identityAuth(String pic1,String pic2) {
        if( pic1 == null || pic2== null) {
            return JSONResult.errorMsg("缺少参数");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        iUser.identityAuth(currentUser.getId(),pic1,pic2);
        return JSONResult.ok();
    }

    /**
     * 学历认证接口
     */
    @RequestMapping(value = "/education_auth")
    @ResponseBody
    public JSONResult educationAuth(String pic1,String pic2) {
        if( pic1 == null || pic2== null) {
            return JSONResult.errorMsg("缺少参数 pic1 或 pic2");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");

        iUser.educationAuth(currentUser.getId(),pic1,pic2);
        return JSONResult.ok();
    }

    /**
     * 资质认证接口
     */
    @RequestMapping(value = "/qualification_auth")
    @ResponseBody
    public JSONResult qualificationAuth(String pic1,String pic2) {
        if( pic1 == null || pic2 == null) {
            return JSONResult.errorMsg("缺少参数 pic1  或 pic2");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");

        iUser.qualificationAuth(currentUser.getId(),pic1,pic2);
        return JSONResult.ok();
    }

    /**
     * 法人认证接口
     */
    @RequestMapping(value = "/juridical_auth")
    @ResponseBody

    public JSONResult juridicalAuth(String pic1,String pic2) {
        if( pic1 == null || pic2== null) {
            return JSONResult.errorMsg("缺少参数 pic1  或 pic2");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        iUser.juridicalAuth(currentUser.getId(),pic1,pic2);
        return JSONResult.ok();
    }

    /**
     * 营业执照认证接口
     */
    @RequestMapping(value = "/business_auth")
    @ResponseBody
    public JSONResult businessAuth(String pic1,String pic2) {
        if( pic1 == null || pic2== null) {
            return JSONResult.errorMsg("缺少参数 pic1  或 pic2");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");

        iUser.businessAuth(currentUser.getId(),pic1,pic2);
        return JSONResult.ok();
    }

    /**
     * 设置公司名称
     */
    @RequestMapping(value = "/company_name")
    @ResponseBody
    public JSONResult setCompanyName(String company_name) {
        if ( company_name == null ) {
            return JSONResult.errorMsg("缺少参数 company_name");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        iUser.setCompanyName(currentUser.getId(),company_name);
        return JSONResult.ok();
    }
}


