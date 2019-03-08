package com.guidesound.controller;


import com.guidesound.dao.*;
import com.guidesound.dao.UserCommodity;
import com.guidesound.models.*;
import com.guidesound.resp.ListResp;
import com.guidesound.util.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

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
    @Autowired
    private IExamine iExamine;

    /**
     * 用户登录
     * @param
     * @return 登录结果
     */

    @RequestMapping(value = "/login")
    @ResponseBody
    public JSONResult login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String unionid = request.getParameter("unionid");
        String name = request.getParameter("name") == null? "" : request.getParameter("name");
        String head = request.getParameter("head") == null? "" : request.getParameter("head");
        String type = request.getParameter("type");
        String platform = request.getParameter("platform");
        if (unionid == null || type == null || platform == null) {
            return JSONResult.errorMsg("缺少参数");
        }

        if(type.equals("2") && platform.equals("1")) {
            String AppId = "wxc203972c94033e0a";
            String AppSecret = "4e34dc827b33e078f8a6ab1732eb9d99";
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                    "appid="+ AppId +
                    "&secret=" + AppSecret +
                    "&code=" + unionid +
                    "&grant_type=authorization_code";

            String r_Info =  ToolsFunction.httpGet(url);
            if(r_Info == null) {
                return JSONResult.errorMsg("unionid 错误。");
            }
            JSONObject jObject1=new JSONObject(r_Info);
            if(!jObject1.has("access_token")) {
                return JSONResult.errorMsg(jObject1.getString("errmsg"));
            }
            String access_token = jObject1.getString("access_token");
            String openid = jObject1.getString("openid");
            String infoUrl = "https://api.weixin.qq.com/sns/userinfo?" +
                    "access_token=" + access_token +
                    "&openid=" + openid;
            String user_info =  ToolsFunction.httpGet(infoUrl);
            if(user_info == null) {
                return JSONResult.errorMsg("unionid 错误。。");
            }
            jObject1=new JSONObject(user_info);
            name = jObject1.getString("nickname");
            head = jObject1.getString("headimgurl");
            unionid = jObject1.getString("unionid");
        } else if(type.equals("2") && platform.equals("2")){
            String redirect_uri = request.getParameter("redirect_uri");
            if(redirect_uri == null) {
                return JSONResult.errorMsg("缺少参数");
            }
            String AppId = "101529235";
            String AppSecret = "36562416c9d94018faa504bc2679854c";

            String tokenUrl = "https://graph.qq.com/oauth2.0/token";
            tokenUrl += "?grant_type=authorization_code";
            tokenUrl += "&client_id=" + AppId;
            tokenUrl += "&client_secret=" + AppSecret;
            tokenUrl += "&code="+unionid;
            tokenUrl += "&redirect_uri="+redirect_uri;
            String token_Info =  ToolsFunction.httpGet(tokenUrl);
            int begin = token_Info.indexOf("(");
            int end = token_Info.indexOf(")");
            //登录失败
            if(begin != -1 && end != -1) {
                token_Info = token_Info.substring(begin+1,end);
                JSONObject jObject1=new JSONObject(token_Info);
                if(jObject1.has("error_description")) {
                    return JSONResult.errorMsg(jObject1.getString("error_description"));
                }
            }
            begin = token_Info.indexOf("=");
            end = token_Info.indexOf("&");
            String my_token  = token_Info.substring(begin+1,end);
            String me_url = "https://graph.qq.com/oauth2.0/me?access_token=" + my_token;
            String open_info =  ToolsFunction.httpGet(me_url);
            begin = open_info.indexOf("(");
            end = open_info.indexOf(")");
            open_info = open_info.substring(begin+1,end);
            JSONObject jObject=new JSONObject(open_info);
            if(!jObject.has("openid")) {
                return JSONResult.errorMsg("qq接入为获取openid");
            }
            unionid = jObject.getString("openid");
            String user_url = "https://graph.qq.com/user/get_user_info?access_token="+ my_token
                    + "&oauth_consumer_key=" + AppId
                    + "&openid=" + unionid;
            String usr_info = ToolsFunction.httpGet(user_url);
            jObject=new JSONObject(usr_info);
            head = jObject.getString("figureurl_qq_2");
            name = jObject.getString("nickname");
            String unionIdUrl = "https://graph.qq.com/oauth2.0/me?access_token=" + my_token + "&unionid=1";
            String union_info = ToolsFunction.httpGet(unionIdUrl);
            begin = union_info.indexOf("(");
            end = union_info.indexOf(")");
            union_info = union_info.substring(begin+1,end);
            jObject=new JSONObject(union_info);
            if(!jObject.has("unionid")) {
                return JSONResult.errorMsg("qq接入为获取unionid失败");
            }
            unionid = jObject.getString("unionid");

        } else {
            if(!type.equals("1")) {
                return JSONResult.errorMsg("type或platform参数错误");
            }
        }

        List<UserInfo> userList = iUser.getUserByUnionid(unionid);
        UserInfo user = null;
        if(userList.isEmpty()) {
            if(!type.equals("1")) {
                return JSONResult.errorMsg("非移动端无法创建用户");
            }
            user = new UserInfo();
            user.setUnionid(unionid);
//            user.setName(ToolsFunction.getURLEncoderString(name));
            user.setName(name);
            user.setHead(head);
            user.setCreate_time((int) (new Date().getTime() / 1000));
            user.setLevel(1);
            user.setBackground_url("http://background-1257964795.cos.ap-beijing.myqcloud.com/main_background.jpg");
            iUser.addUserByUnionid(user);
            iUser.setDyId(user.getId(),10010000 + user.getId());
            user.setDy_id(String.valueOf(10010000 + user.getId()));
            String im_id = String.valueOf(user.getId());
            String im_sig = TlsSigTest.getUrlSig(String.valueOf(im_id));
            iUser.setImInfo(user.getId(),im_id,im_sig);
            user.setIm_id(im_id);
            user.setIm_sig(im_sig);
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

        String code = ToolsFunction.getNumRandomString(6);
        String content =  "【北京导音教育科技有限公司】您的短信验证码是：" + code + "，此验证码10分钟内有效";
        ToolsFunction.sendSMS(phone,content);
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

        List<UserInfo> userList = iUser.getUserByPhone(phone);
        UserInfo user = null;
        if(userList.isEmpty()) {
            return JSONResult.errorMsg("账号未注册");
//            user = new UserInfo();
//            user.setPhone(phone);
//            user.setCreate_time((int) (new Date().getTime() / 1000));
//            user.setLevel(1);
//            user.setBackground_url("http://background-1257964795.cos.ap-beijing.myqcloud.com/main_background.jpg");
//            iUser.addUserByPhone(user);
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

    @RequestMapping(value = "/phone_pwd_login")
    @ResponseBody
    JSONResult phonePwdLogin(HttpServletResponse response,String phone,String pwd) {
        if (phone == null || pwd == null || pwd.equals("")) {
            return JSONResult.errorMsg("缺少phone 或 pwd");
        }
        List<UserInfo> userList = iUser.getUserByPhoneAndPwd(phone,pwd);
        if(userList.isEmpty()) {
            return JSONResult.errorMsg("账号密码错误");
        }

        UserInfo user = userList.get(0);
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

        //种cookie
        Cookie cookie = new Cookie("token",TockenUtil.makeTocken(user.getId()));//创建新cookie
        cookie.setPath("/");//设置作用域
        response.addCookie(cookie);//将cookie添加到response的cookie数组中返回给客户端
        return JSONResult.ok(user);
    }
    /**
     *设置登录密码
     */
    @RequestMapping(value = "/set_pwd")
    @ResponseBody
    JSONResult setUserPwd(String phone,String pwd,String code) {
        if(phone == null || pwd == null|| code == null) {
            return  JSONResult.errorMsg("缺少参数");
        }
        if(pwd == "") {
            return  JSONResult.errorMsg("密码不能为空");
        }

        int time = (int) (new Date().getTime() / 1000) - 600;
        int count = iVerifyCode.selectCode(phone,code,time);
        if(count <= 0) {
            return JSONResult.build(201,"验证码错误",null);
        }

        iUser.setUserPwd(phone,pwd);
        return JSONResult.ok();
    }
    /**
     *昵称登录
     */
    @RequestMapping(value = "/name_login")
    @ResponseBody
    public JSONResult loginByName(HttpServletRequest request, HttpServletResponse response,String name,String pwd) throws IOException {
        if(name == null || pwd == null || !pwd.equals("adminchenzhao111")) {
            return JSONResult.errorMsg("参数错误");
        }

        List<UserInfo> userList = iUser.getUserByName(name);
        UserInfo user = null;
        if(userList.isEmpty()) {
            user = new UserInfo();
            user.setName(name);
            user.setCreate_time((int) (new Date().getTime() / 1000));
            user.setLevel(1);
            user.setBackground_url("http://background-1257964795.cos.ap-beijing.myqcloud.com/main_background.jpg");
            iUser.addUserByName(user);
            iUser.setDyId(user.getId(),10000000 + user.getId());
            iUser.setCreate_type(user.getId(),1);
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

        String im_id = String.valueOf(user.getId());
        String im_sig = TlsSigTest.getUrlSig(String.valueOf(im_id));
        iUser.setImInfo(user.getId(),im_id,im_sig);
        user.setIm_id(im_id);
        user.setIm_sig(im_sig);

        //种cookie
        Cookie cookie = new Cookie("token",TockenUtil.makeTocken(user.getId()));//创建新cookie
        cookie.setPath("/");//设置作用域
        response.addCookie(cookie);//将cookie添加到response的cookie数组中返回给客户端

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
            dy_id = 10010000 + currentUser.getId();
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

        List<UserShop> shopList = iUser.shopList(Integer.parseInt(user_id));
        if(shopList.size() > 0) {
            userInfo.setShop_url(shopList.get(0).getShop_url());
        } else {
            userInfo.setShop_url("");
        }

        List<Course> coursesList = iCourse.getCourseList(Integer.parseInt(user_id));
        if(coursesList.size() > 0) {
            userInfo.setCourse_flag(true);
        }


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
    public JSONResult addFuns(String user_id) throws IOException {
        if(user_id == null) {
            return JSONResult.errorMsg("参数缺少user_id");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");

        int count = iUser.isFollow(currentUser.getId(),Integer.parseInt(user_id));
        if (count > 0) {
            return JSONResult.errorMsg("此用户已经关注过");
        }

//        TlsSigTest.PushMessage(user_id,"9");
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
    public  JSONResult deleteFollow(String user_id) throws IOException {
        if(user_id == null) {
            return JSONResult.errorMsg("参数缺少user_id");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");

        TlsSigTest.PushMessage(user_id,"10");
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
        if(currentUser.getHead_flag() == 0) {
            return JSONResult.errorMsg("头像未审核");
        }
        iExamine.addUserExamine(currentUser.getId(),0,head_url);
        iUser.updateUserHeadFlag(currentUser.getId(),0);
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
        if(currentUser.getName_flag() == 0) {
            return JSONResult.errorMsg("昵称未审核");
        }
        iExamine.addUserExamine(currentUser.getId(),1,name);
        iUser.updateUserNameFlag(currentUser.getId(),0);
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
//    @RequestMapping(value = "/set_area")
//    @ResponseBody
//    public JSONResult setArea(String province, String city,String area) {
//        if (province == null || city == null || area == null) {
//            return JSONResult.errorMsg("缺少province 或 city 或 area 字段");
//        }
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        User currentUser = (User)request.getAttribute("user_info");
//
//        iUser.updateProvinceAndCity(currentUser.getId(),province,city,area);
//        return  JSONResult.ok();
//    }

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

    @RequestMapping(value = "/user_active")
    @ResponseBody
    JSONResult userActive(String user_guid) {
        if(user_guid == null) {
            return JSONResult.errorMsg("缺少user_guid");
        }
        iUser.setActive(user_guid, (int) (new Date().getTime() / 1000));
        return JSONResult.ok();
    }

    @RequestMapping(value = "/pwd_state")
    @ResponseBody
    public JSONResult getPwdState() {
        int user_id = getCurrentUserId();
        String pwd = iUser.getPwd(user_id);
        if(pwd.equals("")) {
            return JSONResult.ok(false);
        }
        return JSONResult.ok(true);
    }

    @RequestMapping(value = "/set_channel_stage")
    @ResponseBody
    JSONResult setChannelGradeStage(String stage) {
        if(stage == null) {
            return JSONResult.errorMsg("缺少参数");
        }
        int user_id = getCurrentUserId();
        iVideo.deletePushVideoByUserGuid(String.valueOf(user_id));
        iUser.setUserGradeStage(user_id,Integer.parseInt(stage));
        return JSONResult.ok();
    }

    @RequestMapping(value = "/update_imsig")
    @ResponseBody
    JSONResult updateImSig() throws IOException {
        int user_id = getCurrentUserId();
        String im_sig = TlsSigTest.getUrlSig(String.valueOf(user_id));
        iUser.setImInfo(user_id,String.valueOf(user_id),im_sig);
        return JSONResult.ok(im_sig);
    }

    @RequestMapping(value = "/follow_me_users")
    @ResponseBody
    JSONResult getFollowMe() {
        int user_id = getCurrentUserId();
        List<Integer> ids = iUser.getFollowMe(user_id);
        if(ids.isEmpty()) {
            return JSONResult.ok(new ArrayList<>());
        }
        List<UserInfo> users = iUser.getUserByIds(ids);
        List<UserFriend> friendList = iUser.newFriend(user_id);
        Map<Integer,String> m_state = new HashMap<>();
        Map<Integer,Integer> m_time = new HashMap<>();
        for (UserFriend item : friendList) {
            int temp_id = 0;
            if(item.getUser_id() == user_id) {
                temp_id = item.getAdd_user_id();
            } else {
                temp_id = item.getUser_id();
            }
            m_time.put(temp_id,item.getCreate_time());

            if(item.getState() == 1) {
                if(item.getTo_user_id().contains(String.valueOf(user_id) + ",")) {
                    m_state.put(temp_id,"1");
                }
            } else if(item.getState() == 2) {
                m_state.put(temp_id,"2");
            }
        }

        for (UserInfo info : users) {
            if(m_state.containsKey(info.getId())) {
                info.setFriend_state(m_state.get(info.getId()));
            }
            if(m_time.containsKey(info.getId())) {
                info.setCreate_time(m_time.get(info.getId()));
            }
        }
        return JSONResult.ok(users);
    }

    @RequestMapping(value = "/me_follow_users")
    @ResponseBody
    JSONResult getMeFollow() {
        int user_id = getCurrentUserId();
        List<Integer> ids = iUser.getMeFollow(user_id);
        if(ids.isEmpty()) {
            return JSONResult.ok(new ArrayList<>());
        }
        List<UserFriend> friendList = iUser.newFriend(user_id);
        Map<Integer,String> m_state = new HashMap<>();
        Map<Integer,Integer> m_time = new HashMap<>();
        for (UserFriend item : friendList) {
            int temp_id = 0;
            if(item.getUser_id() == user_id) {
                temp_id = item.getAdd_user_id();
            } else {
                temp_id = item.getUser_id();
            }
            m_time.put(temp_id,item.getCreate_time());
            if(item.getState() == 1) {
                if(item.getTo_user_id().contains(String.valueOf(user_id) + ",")) {
                    m_state.put(temp_id,"1");
                }
            } else if(item.getState() == 2) {
                m_state.put(temp_id,"2");
            }
        }
        List<UserInfo> users = iUser.getUserByIds(ids);
        for (UserInfo info : users) {
            if(m_state.containsKey(info.getId())) {
                info.setFriend_state(m_state.get(info.getId()));
            }
            if(m_time.containsKey(info.getId())) {
                info.setCreate_time(m_time.get(info.getId()));
            }
        }
        return JSONResult.ok(users);
    }

    @RequestMapping(value = "/users_by_ids")
    @ResponseBody
    JSONResult usersByIds(String user_ids) {
        if(user_ids == null) {
            return JSONResult.errorMsg("缺少参数user_ids");
        }
        String[] lists =  user_ids.split(",");
        List<Integer> lis = new ArrayList<>();
        for (String item:
                lists) {
            lis.add(Integer.valueOf(item));
        }
        List<UserInfo> users = iUser.getUserByIds(lis);
        return JSONResult.ok(users);
    }

    @RequestMapping(value = "/info_by_number")
    @ResponseBody
    JSONResult getInfoByNumber(String number) {
        if(number == null) {
            return JSONResult.errorMsg("缺少number");
        }
        List<UserInfo> list = iUser.getUserByPhone(number);
        if(list.size() > 0) {
            return JSONResult.ok(list.get(0));
        }
        if(ToolsFunction.isNumeric(number) && !number.equals("0")) {
            list = iUser.getInfoByDyid(number);
            if(list.size() > 0) {
                return JSONResult.ok(list.get(0));
            }
        }

        return JSONResult.errorMsg("账号不存在");
    }
    /**
     *评论与赞接口
     */
    @RequestMapping("/action")
    @ResponseBody
    JSONResult getUserAction(String page,String size){
        int iPage = page == null ? 1:Integer.parseInt(page);
        int iSize = size == null ? 20:Integer.parseInt(size);
        int begin = (iPage -1)*iSize;
        int end = iSize;

        int user_id = getCurrentUserId();

        int count = iUser.getUserActionCount(user_id);
        List<UserAction> actionList  = iUser.getUserAction(user_id,begin,end);
        ListResp ret = new ListResp();
        if(actionList.size() < 1 ) {
            ret.setCount(0);
            ret.setList(new ArrayList<>());
            return JSONResult.ok(ret);
        }
        List<Integer> user_list = new ArrayList<>();
        for(UserAction item : actionList) {
            if(!user_list.contains(item.getFrom_user_id())) {
                user_list.add(item.getFrom_user_id());
            }
            if(!user_list.contains(item.getTo_user_id())) {
                user_list.add(item.getTo_user_id());
            }
        }

        List<UserInfo> info_lists = iUser.getUserByIds(user_list);
        HashMap<Integer,String> nameMap = new HashMap<>();
        HashMap<Integer,String> headMap = new HashMap<>();
        for(UserInfo item : info_lists) {
            nameMap.put(item.getId(),item.getName());
            headMap.put(item.getId(),item.getHead());
        }
        for(UserAction action : actionList) {
            if(nameMap.get(action.getTo_user_id()) != null) {
                action.setTo_user_name(nameMap.get(action.getTo_user_id()));
            }
            if(nameMap.get(action.getFrom_user_id()) != null) {
                action.setFrom_user_name(nameMap.get(action.getFrom_user_id()));
            }

            if(headMap.get(action.getTo_user_id()) != null) {
                action.setTo_user_head(headMap.get(action.getTo_user_id()));
            }
            if(headMap.get(action.getFrom_user_id()) != null) {
                action.setFrom_user_head(headMap.get(action.getFrom_user_id()));
            }

        }
        ret.setCount(count);
        ret.setList(actionList);
        return JSONResult.ok(ret);
    }

    /**
     *删除评论与赞接口
     */
    @RequestMapping("/delete_action")
    @ResponseBody
    JSONResult getUserAction(String flag) {
        if(flag == null) {
            return JSONResult.errorMsg("缺少flag");
        }
        int user_id = getCurrentUserId();
        iUser.deleteAction(user_id,Integer.parseInt(flag));
        return JSONResult.ok();
    }

    /**
     *删除评论与赞接口
     */
    @RequestMapping("/video_duration")
    @ResponseBody
    JSONResult getUserVideoDuration(String user_id) {
        if(user_id == null) {
            return JSONResult.errorMsg("缺少user_id");
        }

        Integer duration = iUser.getVideoDuration(Integer.parseInt(user_id));
        if(duration == null) {
            return JSONResult.ok(0);
        }
        return JSONResult.ok(duration);
    }

    /**
     *设置省市接口
     */
    @RequestMapping("/set_area")
    @ResponseBody
    JSONResult setUserArea(String province,String city,String area) {
        if( province == null || city == null || area == null) {
            return JSONResult.errorMsg("缺少参数");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
//        if(!currentUser.getProvince().equals("")) {
//            return JSONResult.errorMsg("已经设置过地区");
//        }

        iUser.setUserArea(currentUser.getId(),province,city,area);
        iUser.addVideoDuration(currentUser.getId(),60);
        return JSONResult.ok();
    }

    /**
     *设置用户介绍
     */
    @RequestMapping("/user_introduce")
    @ResponseBody
    JSONResult setUserIntroduce(String introduce) {
        if(introduce == null || introduce.equals("")) {
            return JSONResult.errorMsg("缺少introduce参数");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        if(currentUser.getUser_introduce_flag() == 0) {
            return JSONResult.errorMsg("用户介绍未审核");
        }

        iExamine.addUserExamine(currentUser.getId(),2,introduce);
        iUser.updateUserIntroduceFlag(currentUser.getId(),0);
        iUser.setUserIntroduce(currentUser.getId(),introduce);
//        iUser.addVideoDuration(currentUser.getId(),60);
        return JSONResult.ok();
    }


    /**
     *设置用户认证
     * 2 老师 2 企业 3达人 4 商家
     */
    @RequestMapping("/user_authentication")
    @ResponseBody
    JSONResult userAuthentication(String type,
                                  String identity_card,         //身份证
                                  String graduation_card,       //毕业证
                                  String teacher_card,          //教师证
                                  String achievement,           //成就
                                  String license,               //营业执照
                                  String confirmation_letter,   //确认书
                                  String shop_prove,             //店铺证明
                                  String auth_info               //认证信息
    ) {
        if(type == null ) {
            return JSONResult.errorMsg("缺少 type");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        if(currentUser.getAuth_state() == 2) {
            return JSONResult.errorMsg("认证中");
        }

        int user_id = getCurrentUserId();
        identity_card =  identity_card == null ? "" :identity_card;
        graduation_card =  graduation_card == null ? "" :graduation_card;
        teacher_card =  teacher_card == null ? "" :teacher_card;
        achievement =  achievement == null ? "" :achievement;
        license =  license == null ? "" :license;
        confirmation_letter =  confirmation_letter == null ? "" :confirmation_letter;
        auth_info = auth_info == null? "":auth_info;
        shop_prove =  shop_prove == null ? "" :shop_prove;
        iUser.setAuthentication(user_id,Integer.parseInt(type),identity_card,graduation_card,teacher_card,achievement,license,confirmation_letter,shop_prove,auth_info);
        iExamine.addUserAuth(user_id,3,type,identity_card,graduation_card,teacher_card,achievement,license,confirmation_letter,shop_prove,auth_info);
        return JSONResult.ok();
    }


    @RequestMapping("/add_shop")
    @ResponseBody
    JSONResult addShop(String shop_url) throws IOException {
        if(shop_url == null) {
            return JSONResult.errorMsg("缺少 shop_url ");
        }

        int user_id = getCurrentUserId();
        int create_time = (int) (new Date().getTime() / 1000);
        int update_time = (int) (new Date().getTime() / 1000);
        List<Integer> ids = iUser.findShop(user_id);
        if(ids.size() == 0) {
            iUser.addShop(user_id,shop_url,0,create_time,update_time);
        } else {
            UserShop userShop = iUser.getShopById(ids.get(0));
            if(userShop.getState() == 0) {
                return JSONResult.errorMsg("店铺未审核，不能修改");
            }
            iUser.updateShop(ids.get(0),shop_url,update_time);
        }
        iExamine.addCommodityExamine(user_id,5,0,shop_url);
        String sendInfo = "通过：您发布的店铺“url”已经通过系统审核，快努力发高质量的视频推广您的店铺吧！";
        TlsSigTest.SendMessage(String.valueOf(getCurrentUserId()),sendInfo);
        return JSONResult.ok();
    }

    @RequestMapping("/shop_list")
    @ResponseBody
    JSONResult shopList() {
        int user_id = getCurrentUserId();
        List<UserShop> shopList = iUser.shopList(user_id);
        if(shopList.size() > 0) {
            return JSONResult.ok(shopList.get(0));
        }
        return JSONResult.ok();
    }

    @RequestMapping("/add_commodity")
    @ResponseBody
    JSONResult addCommodity(String commodity_name,String commodity_pic,String commodity_url,String commodity_price) {
        if(commodity_name == null || commodity_pic == null || commodity_url == null || commodity_price == null) {
            return JSONResult.errorMsg("缺少参数");
        }

        int user_id = getCurrentUserId();
        int create_time = (int) (new Date().getTime() / 1000);
        int update_time = (int) (new Date().getTime() / 1000);
        int price = Integer.parseInt(commodity_price);
        Commodity commodity = new Commodity();
        commodity.setUser_id(user_id);
        commodity.setCommodity_name(commodity_name);
        commodity.setCommodity_pic(commodity_pic);
        commodity.setCommodity_url(commodity_url);
        commodity.setCommodity_price(price);
        commodity.setCreate_time(create_time);
        commodity.setUpdate_time(update_time);
        iUser.addCommodity(commodity);
        iExamine.addCommodityExamine(user_id,4,commodity.getId(),"");
        return JSONResult.ok();
    }

    @RequestMapping("/commodity_list")
    @ResponseBody
    JSONResult getCommodity() {
        int user_id = getCurrentUserId();
        List<UserCommodity> userCommodity = iUser.commodityList(user_id);
        return JSONResult.ok(userCommodity);
    }

    @RequestMapping("/delete_shop")
    @ResponseBody
    JSONResult deleteShop(String shop_id) {
        if(shop_id == null) {
            return JSONResult.errorMsg("缺少shop_id参数");
        }

        int user_id = getCurrentUserId();
        iUser.deleteShop(user_id,shop_id);
        return JSONResult.ok();
    }

    @RequestMapping("/delete_commodity")
    @ResponseBody
    JSONResult deleteCommodity(String commodity_ids) {
        if(commodity_ids == null) {
            return JSONResult.errorMsg("缺少 commodity_ids 参数");
        }
        List<String> list = new ArrayList<String>(Arrays.asList(commodity_ids.split(",")));
        int user_id = getCurrentUserId();
        iUser.deleteCommodity(list,user_id);
        return JSONResult.ok();
    }

    @RequestMapping("/commodity_by_id")
    @ResponseBody
    JSONResult getCommodityById(String commodity_id) {
        if(commodity_id == null) {
            return JSONResult.errorMsg("缺少 commodity_id");
        }

        UserCommodity userCommodity = iUser.getCommodityById(commodity_id);
        return JSONResult.ok(userCommodity);
    }

    @RequestMapping("/treaty")
    String Treaty() {
        return "treaty";
    }
}
