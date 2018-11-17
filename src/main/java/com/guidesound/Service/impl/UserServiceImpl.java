package com.guidesound.Service.impl;

import com.guidesound.Service.IUserService;
import com.guidesound.dao.IUser;
import com.guidesound.dao.IUserFollow;
import com.guidesound.dao.IUserFuns;
import com.guidesound.models.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;


@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUser user;
    @Autowired
    private IUserFuns iUserFuns;
    @Autowired
    private IUserFollow iUserFollow;

    @Override
    public UserInfo getUserById(int userId) {
        return this.user.getUser(userId);
    }

    @Override
    public void insertUser(UserInfo user) {
        this.user.insertUser(user);
    }

    @Override
    public List<UserInfo> getUserList(UserInfo user) {
        return null;
    }

    @Override
    public UserInfo updateUser(UserInfo user) {
        return null;
    }

    @Override
    public void deleteUser(int id) {

    }

    @Override
    public UserInfo login(String uuid, String name, String head) {
        List<UserInfo> userList = this.user.getListByUnionid(uuid);
        if(null == userList || userList.size() == 0 ){
            UserInfo u_temp = new UserInfo();
            u_temp.setUnionid(uuid);
            u_temp.setName(name);
            u_temp.setHead(head);
            u_temp.setPhone("");
            u_temp.setType(0);
            u_temp.setStatus(0);
            u_temp.setLevel(1);
            u_temp.setSign_name("");
            u_temp.setSex(0);
            u_temp.setTeach_age(0);
            u_temp.setCreate_time((int) (new Date().getTime() / 1000));
            this.user.insertUser(u_temp);
            return u_temp;
        }
        return userList.get(0);
    }

    @Override
    public List<UserInfo> phoneLogin(String phone) {
        return user.getUserByPhone(phone);
    }

    @Override
    public int getFunsCount(int user_id) {
        return iUserFollow.getFunsCount(user_id);
    }

    @Override
    public int getFollowCount(int follow_user_id) {
        return iUserFollow.getFollowCount(follow_user_id);
    }


    @Override
    public void cannelFollow(int user_id, int follow_user_id) {
        iUserFollow.cannelFollow(user_id,follow_user_id);
    }

    @Override
    public void followUser(int user_id, int follow_user_id) {
        if(iUserFollow.getFollowInfo(user_id,follow_user_id) <= 0) {
            iUserFollow.followUser(user_id,follow_user_id);
        }
    }

    @Override
    public void phoneRegister(int id,String phone, String pwd) {
        user.phoneRegister(id,phone,pwd);
    }
}
