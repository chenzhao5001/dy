package com.guidesound.dao;

import com.guidesound.models.User;
import com.guidesound.models.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface IUser {

    public void insertUser(User user);

    public User getUser(int id);

    public List<User> getListByUnionid(String unionid);

    @Select("select * from user where phone = #{arg0}")
    public List<User> getUserByPhone(String phone);

    @Update("update user set phone = #{arg1},pwd = #{arg2} ,status = 1 where id = #{arg0}")
    public void phoneRegister(int id,String phone,String pwd);

    @Update("update user set status = #{arg1} where id= #{arg0}")
    public void updateStatus(int id,int status);
    @Update("update user set type = #{arg1} where id= #{arg0}")
    public void updateType(int id,String type);

    @Insert("insert into user (phone) values (#{phone})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    public void addUserByPhone(User user);

    @Select("select * from user where id = #{arg0}")
    public UserInfo getUserInfo(String id);

    @Insert("insert into userFuns (user_id,funs_user_id,create_time) values (#{arg1},#{arg0},#{arg2})")
    public void followUser(int id,int user_id,int create_time);

    @Update("update userFuns set deleted = 1,update_time = #{arg2} where user_id= #{arg1} and funs_user_id = #{arg0}")
    public void cancelFollow(int id,int user_id,int update_time);

}

