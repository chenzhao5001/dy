package com.guidesound.dao;

import com.guidesound.models.InUser;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IInUser {
    @Select("select * from inUser where account = #{arg0}")
    List<InUser> getUserByName(String account);
}
