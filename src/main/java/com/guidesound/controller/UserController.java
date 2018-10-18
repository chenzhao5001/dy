package com.guidesound.controller;

import com.guidesound.Service.IUserService;
import com.guidesound.dao.IUser;
import com.guidesound.dao.IVerifyCode;
import com.guidesound.models.User;
import com.guidesound.util.JSONResult;
import com.guidesound.util.ServiceResponse;
import com.guidesound.util.TockenUtil;
import com.guidesound.util.ToolsFunction;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    /**
     * 用户登录
     * @param
     * @return 登录结果
     */

    @RequestMapping(value = "/login")
    public  @ResponseBody LoginRep login(HttpServletRequest request, HttpServletResponse response) {

        String unionid = request.getParameter("unionid");
        String name = request.getParameter("name");
        String head = request.getParameter("head");

        LoginRep rep = new LoginRep();
        if (unionid == null || name == null || head == null) {
            rep.setCode(201);
            rep.setMsg("缺少参数");
            return rep;
        }

        User user = userService.login(unionid,name,head);
        String token = TockenUtil.makeTocken(user.getId());
        UserRepTemp userRepTemp = new UserRepTemp();
        userRepTemp.token = token;
        userRepTemp.id = user.getId();
        userRepTemp.unionid = user.getUnionid();
        userRepTemp.name = user.getName();
        userRepTemp.head = user.getHead();
        userRepTemp.status = user.getStatus();
        rep.setCode(200);
        rep.setMsg("ok");
        rep.setData(userRepTemp);
        return rep;
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
        LoginRep rep = new LoginRep();

        if(phone == null || code == null || !ToolsFunction.isNumeric(phone) || phone.length() != 11) {
            return JSONResult.build(201,"参数错误",null);
        }

        int time = (int) (new Date().getTime() / 1000) - 600;
        int count = iVerifyCode.selectCode(phone,code,time);
        if(count <= 0) {
            return JSONResult.build(201,"验证码错误",null);
        }

        List<User> userList = iUser.getUserByPhone(phone);
        if(userList.isEmpty()) {
            User user = new User();
            user.setPhone(phone);
            user.setCreate_time((int) (new Date().getTime() / 1000));
            user.setUpdate_time((int) (new Date().getTime() / 1000));
            iUser.addUserByPhone(user);
            user.setToken(TockenUtil.makeTocken(user.getId()));
            return JSONResult.ok(user);
        }
        User user = userList.get(0);
        user.setToken(TockenUtil.makeTocken(user.getId()));
        return JSONResult.ok(user);
    }

    /**
     * 用户退出
     * @return
     */
    @RequestMapping(value = "/logout")
    public  @ResponseBody ServiceResponse logout(HttpServletRequest request, HttpServletResponse response) {
        ServiceResponse rep = new ServiceResponse();
        System.out.println(currentUser);
        rep.setCode(200);
        rep.setMsg("用户退出");

        return rep;
    }

    /**
     * 关注用户
     * @return
     */
    @RequestMapping(value = "/follow")
    public @ResponseBody ServiceResponse addFuns(HttpServletRequest request) {
        User user = (User)request.getAttribute("user_info");
        String userID = request.getParameter("user_id");
        ServiceResponse rep = new ServiceResponse();
        if(userID == null) {
            rep.setCode(201);
            rep.setMsg("缺少参数");
            return rep;
        }

        userService.followUser(Integer.parseInt(userID),user.getId());
        rep.setCode(200);
        rep.setMsg("ok");
        return rep;
    }

    /**
     *取消关注
     */
    @RequestMapping(value = "/delete_follow")
    public @ResponseBody ServiceResponse deleteFollow(HttpServletRequest request) {
        User user = (User)request.getAttribute("user_info");
        String userID = request.getParameter("user_id");
        ServiceResponse rep = new ServiceResponse();
        if(userID == null) {
            rep.setCode(201);
            rep.setMsg("缺少参数");
            return rep;
        }
        userService.cannelFollow(Integer.parseInt(userID),user.getId());
        rep.setCode(200);
        rep.setMsg("ok");
        return rep;
    }

    /**
     * 获取粉丝数目
     */

    @RequestMapping(value = "/get_funs_num")
    public @ResponseBody ServiceResponse getUserFunsNum(HttpServletRequest request) {
        String userID = request.getParameter("user_id");
        ServiceResponse rep = new ServiceResponse();
        if(userID == null) {
            rep.setCode(201);
            rep.setMsg("缺少参数");
            return rep;
        }
        int num = userService.getFunsCount(Integer.parseInt(userID));
        rep.setCode(200);
        rep.setMsg("增加funs成功");
        rep.setData(String.valueOf(num));
        return rep;
    }

    /**
     * 获取用户关注数
     */
    @RequestMapping(value = "/get_follow_num")
    public @ResponseBody ServiceResponse getFollowNum(HttpServletRequest request) {
        String userID = request.getParameter("user_id");
        ServiceResponse rep = new ServiceResponse();
        if(userID == null) {
            rep.setCode(201);
            rep.setMsg("缺少参数");
            return rep;
        }
        int num = userService.getFollowCount(Integer.parseInt(userID));
        rep.setCode(200);
        rep.setMsg("ok");
        rep.setData(String.valueOf(num));
        return rep;
    }

}

class UserRepTemp {
    String token;
    int id;
    String unionid;
    String name;
    String head;
    int status;
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
class LoginRep{
    int code;
    String msg;
    UserRepTemp data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UserRepTemp getData() {
        return data;
    }

    public void setData(UserRepTemp data) {
        this.data = data;
    }


}
