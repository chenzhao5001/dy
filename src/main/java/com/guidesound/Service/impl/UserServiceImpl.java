package com.guidesound.Service.impl;

import com.guidesound.Service.IUserService;
import com.guidesound.dao.IUser;
import com.guidesound.dao.IUserFollow;
import com.guidesound.dao.IUserFuns;
import com.guidesound.models.User;
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
    public User getUserById(int userId) {
        return this.user.getUser(userId);
    }

    @Override
    public void insertUser(User user) {
        this.user.insertUser(user);
    }

    @Override
    public List<User> getUserList(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(int id) {

    }

    @Override
    public User login(String uuid,String name,String head) {
        List<User> userList = this.user.getListByUnionid(uuid);
        if(null == userList || userList.size() == 0 ){
            User u_temp = new User();
            u_temp.setUnionid(uuid);
            u_temp.setName(name);
            u_temp.setHead(head);
            u_temp.setPhone("");
            u_temp.setPwd("");
            u_temp.setType(0);
            u_temp.setStatus(0);
            u_temp.setFollow_num(0);
            u_temp.setFuns_num(0);
            u_temp.setSign_name("");
            u_temp.setSex(0);
            u_temp.setTeach_age(0);
            u_temp.setCreate_time((int) (new Date().getTime() / 1000));
            u_temp.setUpdate_time((int) (new Date().getTime() / 1000));
            this.user.insertUser(u_temp);
            return u_temp;
        }
        return userList.get(0);
    }

    @Override
    public List<User> phoneLogin(String phone) {
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
