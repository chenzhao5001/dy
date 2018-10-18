package com.guidesound.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface IVerifyCode {

    @Insert("insert into verifyCode (phone,code,create_time,update_time) value(#{arg0},#{arg1},#{arg2},#{arg3})")
    public void addVerifyCode(String phone,String code,int create_time,int update_time);

    @Select("select count(*) from verifyCode where phone = #{arg0} and code = #{arg1} and create_time > #{arg2}")
    public int selectCode(String phone,String code,int time);



}
