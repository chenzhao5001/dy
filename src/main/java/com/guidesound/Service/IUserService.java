package com.guidesound.Service;

import com.guidesound.models.User;
import com.guidesound.models.UserInfo;

import java.util.List;

public interface IUserService {

    public UserInfo getUserById(int userId);
    public void insertUser(UserInfo user);
    public List<UserInfo> getUserList(UserInfo user);
    public UserInfo updateUser(UserInfo user);
    public void deleteUser(int id);

    public UserInfo login(String uuid, String name, String head);
    public List<UserInfo> phoneLogin(String phone);
    public void phoneRegister(int id,String phone,String pwd);


    /**
     * user_id (被关注人id)  follow_user_id(关注人id)
     */
    int getFunsCount(int user_id);
    int getFollowCount(int follow_user_id);
    void cannelFollow(int user_id,int follow_user_id);
    void followUser(int user_id,int follow_user_id);

}
