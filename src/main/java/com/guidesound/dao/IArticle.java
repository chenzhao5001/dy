package com.guidesound.dao;

import org.apache.ibatis.annotations.Select;

public interface IArticle {

    @Select("select count(*) from article where user_id = #{arg0}")
    int getCountByUserId(String user_id);
}
