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

import javax.security.auth.Subject;
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
    private IVerifyCode iVerifyCode;
    @Autowired
    private IUser iUser;
    @Autowired
    private IVideo iVideo;
    @Autowired
    private IArticle iArticle;

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

        List<UserInfo> userList = iUser.getUserByUnionid(unionid);
        UserInfo user = null;
        if(userList.isEmpty()) {
            user = new UserInfo();
            user.setUnionid(unionid);
            user.setName(name);
            user.setHead(head);
            user.setCreate_time((int) (new Date().getTime() / 1000));
            user.setLevel(1);
            user.setBackground_url("http://background-1257964795.cos.ap-beijing.myqcloud.com/main_background.jpg");
            iUser.addUserByUnionid(user);
//            user.setToken(TockenUtil.makeTocken(user.getId()));
        } else {
            user = userList.get(0);
        }
        user.setToken(TockenUtil.makeTocken(user.getId()));

        String token = TockenUtil.makeTocken(user.getId());

        int funCount = iUser.getFunsById(String.valueOf(user.getId()));
        int followCount = iUser.getFollowById(String.valueOf(user.getId()));
        int praiseCount = iUser.getPraiseById(String.valueOf(user.getId()));
        int videoCount = iVideo.getVideoByUserId(String.valueOf(user.getId()));
        int articleCount =  iArticle.getCountByUserId(String.valueOf(user.getId()));

        user.setFuns_counts(funCount);
        user.setFollow_count(followCount);
        user.setPraise_count(praiseCount);
        user.setVideo_count(videoCount);
        user.setArticle_count(articleCount);
        user.setCreate_time(user.getCreate_time());


        //种cookie
        Cookie cookie = new Cookie("token",token);//创建新cookie
        cookie.setPath("/");//设置作用域
        response.addCookie(cookie);//将cookie添加到response的cookie数组中返回给客户端

        return JSONResult.ok(user);
    }

    @RequestMapping(value = "/identifying_code")
    @ResponseBody
    public JSONResult getIdentifyingCode(String phone) {
        if(phone == null || !ToolsFunction.isNumeric(phone) || phone.length() != 11) {
            return JSONResult.build(201,"参数错误",null);
        }

//        "【导音教育】您的验证码是: 5678"

//        String code = ToolsFunction.getNumRandomString(6);
//        String content =  "【北京导音教育科技有限公司】您的验证码是: " + code;
//        ToolsFunction.sendSMS(phone,content);
        int time = (int) (new Date().getTime() / 1000);
        iVerifyCode.addVerifyCode(phone,"1234",time,time);
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

        List<UserInfo> userList = iUser.getUserByPhone(phone);
        UserInfo user = null;
        if(userList.isEmpty()) {
            user = new UserInfo();
            user.setPhone(phone);
            user.setCreate_time((int) (new Date().getTime() / 1000));
            user.setLevel(1);
            user.setBackground_url("http://background-1257964795.cos.ap-beijing.myqcloud.com/main_background.jpg");
            iUser.addUserByPhone(user);
//            user.setToken(TockenUtil.makeTocken(user.getId()));
        } else {
            user = userList.get(0);
        }
        user.setToken(TockenUtil.makeTocken(user.getId()));

        int funCount = iUser.getFunsById(String.valueOf(user.getId()));
        int followCount = iUser.getFollowById(String.valueOf(user.getId()));
        int praiseCount = iUser.getPraiseById(String.valueOf(user.getId()));
        int videoCount = iVideo.getVideoByUserId(String.valueOf(user.getId()));
        int articleCount =  iArticle.getCountByUserId(String.valueOf(user.getId()));

        user.setFuns_counts(funCount);
        user.setFollow_count(followCount);
        user.setPraise_count(praiseCount);
        user.setVideo_count(videoCount);
        user.setArticle_count(articleCount);
        user.setCreate_time(user.getCreate_time());

        return JSONResult.ok(user);
    }

    /**
     *昵称登录
     */
    @RequestMapping(value = "/name_login")
    @ResponseBody
    public JSONResult loginByName(String name,String pwd) {
        if(name == null || pwd == null || !pwd.equals("90908989")) {
            return JSONResult.errorMsg("参数错误");
        }

        List<UserInfo> userList = iUser.getUserByName(name);
        UserInfo user = null;
        if(userList.isEmpty()) {
            user = new UserInfo();
            user.setName(name);
            user.setCreate_time((int) (new Date().getTime() / 1000));
            user.setLevel(2);
            user.setType(1);
            user.setBackground_url("http://background-1257964795.cos.ap-beijing.myqcloud.com/main_background.jpg");
            iUser.addUserByName(user);
            iUser.setDyId(user.getId(),10000000 + user.getId());
            user.setToken(TockenUtil.makeTocken(user.getId()));
            user.setDy_id(String.valueOf(10000000 + user.getId()));
        } else {
            user = userList.get(0);
        }
        user.setToken(TockenUtil.makeTocken(user.getId()));

        int funCount = iUser.getFunsById(String.valueOf(user.getId()));
        int followCount = iUser.getFollowById(String.valueOf(user.getId()));
        int praiseCount = iUser.getPraiseById(String.valueOf(user.getId()));
        int videoCount = iVideo.getVideoByUserId(String.valueOf(user.getId()));
        int articleCount =  iArticle.getCountByUserId(String.valueOf(user.getId()));

        user.setFuns_counts(funCount);
        user.setFollow_count(followCount);
        user.setPraise_count(praiseCount);
        user.setVideo_count(videoCount);
        user.setArticle_count(articleCount);
        user.setCreate_time(user.getCreate_time());

        return JSONResult.ok(user);
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
        int iType = Integer.parseInt(user_type);

        if(!SignMap.getUserTypeList().containsKey(Integer.parseInt(user_type))) {
            return JSONResult.errorMsg("设置的类型不存在");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        int dy_id = 0;
        if(iType == 1 ) {
            dy_id = 10009999 + currentUser.getId();
        } else if (iType == 2){
            dy_id = 20000000 + currentUser.getId();
        } else if ( iType == 3) {
            dy_id = 30000000 + currentUser.getId();
        } else if ( iType == 4 ) {
            dy_id = 40000000 + currentUser.getId();
        }
        int level = currentUser.getLevel();
        if(level < 2) {
            level = 2;
        }
        iUser.updateTypeInfo(currentUser.getId(),user_type,dy_id,level);
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

        userInfo.setSubject_name(SignMap.getSubjectTypeById(userInfo.getSubject()));
        userInfo.setGrade_name(SignMap.getGradeTypeByID(userInfo.getGrade()));
        userInfo.setGrade_level_name(SignMap.getWatchById(userInfo.getGrade_level()));
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
        int currentUserID = 0;
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    currentUserID = TockenUtil.getUserIdByTocket(token);
                }
            }
        }

        if(currentUserID == 0) {
            userInfo.setFollow(false);
        } else {
            if (iUser.getIdByUserAndFunsId(Integer.parseInt(user_id),currentUserID) == 0) {
                userInfo.setFollow(false);
            } else {
                userInfo.setFollow(true);
            }
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

        int count = iUser.isFollow(currentUser.getId(),Integer.parseInt(user_id));
        if (count > 0) {
            return JSONResult.errorMsg("此用户已经关注过");
        }

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
    public JSONResult addStudentBasicInfo(String sex,String grade) {
        if(sex == null || grade == null) {
            return JSONResult.errorMsg("缺少参数");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");

        int level = currentUser.getLevel();
        if( level < 3) {
            level = 3;
        }

        iUser.setStudentBasicInfo(currentUser.getId(),sex,grade,level);
        return JSONResult.ok();
    }

    /**
     *学生完善高级信息
     */
    @RequestMapping(value = "/update_student_high_info")
    @ResponseBody
    JSONResult addStudentHighInfo(String province,String city,String area,String phone ) {
        if( province == null || city == null || area == null || phone == null ) {
            return JSONResult.errorMsg("缺少参数");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        int level = currentUser.getLevel();
        if( level < 4) {
            level = 4;
        }
        iUser.setStudentHighInfo(currentUser.getId(),province,city,area,phone,level);
        return JSONResult.ok();
    }

    /**
     *家长完善基本信息
     */
    @RequestMapping(value = "/update_parent_basic_info")
    @ResponseBody
    JSONResult addParentBasicInfo(String sex,String grade) {
        if(sex == null || grade == null) {
            return JSONResult.errorMsg("缺少参数");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        int level = currentUser.getLevel();
        if( level < 3) {
            level = 3;
        }

        iUser.setParentBasicInfo(currentUser.getId(),sex,grade,level);
        return JSONResult.ok();
    }

    /**
     *家长完善高级信息
     */
    @RequestMapping(value = "/update_parent_high_info")
    @ResponseBody
    JSONResult addParentHighInfo(String province,String city,String area,String phone) {
        if( province == null || city == null || area == null || phone == null ) {
            return JSONResult.errorMsg("缺少参数");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        int level = currentUser.getLevel();
        if( level < 4) {
            level = 4;
        }
        iUser.setParentHighInfo(currentUser.getId(),province,city,area,phone,level);
        return JSONResult.ok();
    }

    /**
     *老师完善基本信息
     */
    @RequestMapping(value = "/update_teacher_basic_info")
    @ResponseBody
    JSONResult addTeacherBasicInfo(String sex, String subject,String grade,String province,String city,String area) {
        if(sex == null || subject == null || grade == null || province == null || city == null || area == null) {
            return JSONResult.errorMsg("缺少参数");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        int level = currentUser.getLevel();
        if( level < 3) {
            level = 3;
        }
        iUser.setTeacherBasicInfo(currentUser.getId(),sex,subject,grade,province,city,area,level);
        return JSONResult.ok();
    }

    /**
     *结构完善基本信息
     */
    @RequestMapping(value = "/update_institution_basic_info")
    @ResponseBody
    JSONResult addInstitutionHighInfo(String subject,String grade,String province,String city,String area) {
        if(province == null || city == null || subject == null || grade == null || area == null) {
            return JSONResult.errorMsg("缺少参数");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        int level = currentUser.getLevel();
        if( level < 3) {
            level = 3;
        }
        iUser.setInstitutionBasicInfo(currentUser.getId(),subject,grade,province,city,area,level);
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

        iUser.updateGrade(currentUser.getId(),Integer.parseInt(grade));
        return JSONResult.ok();
    }

    /**
     *绑定手机号
     */
    @RequestMapping(value = "/update_phone")
    @ResponseBody
    JSONResult updatePhone(String phone,String verify_code) {
        if ( phone == null || verify_code == null) {
            return JSONResult.errorMsg("缺少请求参数");
        }

        int time = (int) (new Date().getTime() / 1000) - 600;
        int count = iVerifyCode.selectCode(phone,verify_code,time);
        if(count <= 0) {
            return JSONResult.build(201,"验证码错误",null);
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");

        iUser.updatePhone(currentUser.getId(),phone);
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

        iUser.updateProvinceAndCity(currentUser.getId(),province,city,area);
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
    public JSONResult identityAuth(String pic1,String pic2,String identity_name,String identity_num) {
        if( pic1 == null || pic2== null || identity_name == null || identity_num == null) {
            return JSONResult.errorMsg("缺少参数");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        iUser.identityAuth(currentUser.getId(),pic1,pic2,identity_name,identity_num);
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

    @RequestMapping(value = "/background_url")
    @ResponseBody
    public JSONResult setBackground_url(String background_url) {
        if(background_url == null) {
            return JSONResult.errorMsg("缺少参数 background_url");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        iUser.setBackroundUrl(currentUser.getId(),background_url);
        return JSONResult.ok();

    }
}


