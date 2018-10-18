package com.guidesound.controller;

import com.guidesound.Service.IUserService;
import com.guidesound.models.User;
import com.guidesound.util.ServiceResponse;
import com.guidesound.util.TockenUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户控制器
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController{

    @Autowired
    private IUserService userService;

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



    /**
     *手机号登录
     */
    @RequestMapping(value = "/phonelogin")
    public @ResponseBody LoginRep phoneLogin(HttpServletRequest request, HttpServletResponse response) {
        String phone = request.getParameter("phone");
        String pwd = request.getParameter("pwd");
        LoginRep rep = new LoginRep();
        if(phone == null || pwd == null ) {
            rep.setCode(201);
            rep.setMsg("缺少参数");
            return rep;
        }

        List<User> userList = userService.phoneLogin(phone);
        if(userList.isEmpty()) {
            rep.setCode(202);
            rep.setMsg("手机号未注册");
            return rep;
        }
        User user = userList.get(0);
        System.out.println(user.getPwd());
        System.out.println(DigestUtils.md5Hex(pwd));
        if(!user.getPwd().equals(DigestUtils.md5Hex(pwd))) {
            rep.setCode(202);
            rep.setMsg("密码错误");
            return rep;
        }

        UserRepTemp userRepTemp = new UserRepTemp();

        String token = TockenUtil.makeTocken(user.getId());

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

    /**
     *手机号注册
     */
    @RequestMapping(value = "/phone_register")
    public @ResponseBody ServiceResponse phoneRegister(HttpServletRequest request, HttpServletResponse response) {
        String phone = request.getParameter("phone");
        String pwd = request.getParameter("pwd");

        ServiceResponse serviceResponse = new ServiceResponse();
        if(phone == null || pwd == null) {
            serviceResponse.setCode(201);
            serviceResponse.setMsg("缺失参数");
            return serviceResponse;
        }

        List<User> userList = userService.phoneLogin(phone);
        if(userList != null && !userList.isEmpty()) {
            serviceResponse.setCode(202);
            serviceResponse.setMsg("此手机号已经被注册");
            return serviceResponse;
        }

        User user = (User)request.getAttribute("user_info");

        if(user.getPhone() == "") {
            serviceResponse.setCode(203);
            serviceResponse.setMsg("已经注册过了");
            return serviceResponse;
        }

        userService.phoneRegister(user.getId(),phone,DigestUtils.md5Hex(pwd));
        serviceResponse.setCode(200);
        serviceResponse.setMsg("ok");
        return serviceResponse;
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

//        log.info("1234 info");
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
