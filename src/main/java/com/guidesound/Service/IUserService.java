package com.guidesound.Service;

import com.guidesound.models.User;

import java.util.List;

public interface IUserService {

    public User getUserById(int userId);
    public void insertUser(User user);
    public List<User> getUserList(User user);
    public User updateUser(User user);
    public void deleteUser(int id);

    public User login(String uuid,String name,String head);
    public List<User> phoneLogin(String phone);
    public void phoneRegister(int id,String phone,String pwd);


    /**
     * user_id (被关注人id)  follow_user_id(关注人id)
     */
    int getFunsCount(int user_id);
    int getFollowCount(int follow_user_id);
    void cannelFollow(int user_id,int follow_user_id);
    void followUser(int user_id,int follow_user_id);

}
