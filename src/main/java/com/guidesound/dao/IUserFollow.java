package com.guidesound.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


public interface IUserFollow {

    /**
     * 查询用户粉丝数量
     * */
    @Select("select count(*) from userFollow where user_id = #{arg0}")
    int getFunsCount(int user_id);

    /**
     * 从查询关注数量
     */
    @Select("select count(*) from userFollow where follow_user_id = #{arg0}")
    int getFollowCount(int follow_user_id);

    /**
     * 取消对某人的关注
     */
    @Update("update userFollow set deleted = 1 where user_id = #{arg0} and follow_user_id = #{arg1}")
    void cannelFollow(int user_id,int follow_user_id);

    /**
     * 关注用户
     */
    @Insert("insert into userFollow (user_id,follow_user_id) value(#{arg0},#{arg1})")
    void followUser(int user_id,int follow_user_id);

    /**
     * 查询关注是否存在
     */
    @Select("select count(*) from userFollow where user_id = #{arg0} and follow_user_id = #{arg1}")
    int getFollowInfo(int user_id,int follow_user_id);

}
