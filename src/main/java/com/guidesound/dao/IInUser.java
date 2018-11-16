package com.guidesound.dao;

import com.guidesound.models.InUser;
import org.apache.ibatis.annotations.Select;

public interface IInUser {
    @Select("select * from inUser where name = #{arg0}")
    InUser getUserByName(String name);
}
