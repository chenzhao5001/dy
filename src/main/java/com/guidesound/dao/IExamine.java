package com.guidesound.dao;

import com.guidesound.models.CommodityExamine;
import com.guidesound.models.CourseExamine;
import com.guidesound.models.UserExamine;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IExamine {

    @Insert("insert into user_examine (user_id,auth_type,text) value (#{arg0},#{arg1},#{arg2})")
    void addUserExamine(int user_id,int auth_type,String head);
    @Select("select * from user_examine")
    List<UserExamine> getUserExamine();
    @Insert("insert into commodity_examine (user_id,type,commodity_id,shop_url) value (#{arg0},#{arg1},#{arg2},#{arg3})")
    void addCommodityExamine(int user_id,int type,int commodity_id, String shop_url);
    @Select("select * from commodity_examine")
    List<CommodityExamine> getCommodityExamine();

    @Select("select * from course_examine")
    List<CourseExamine> getCourseExamine();

    @Insert("insert into course_examine (type,uid,item_id,item_sub_type) value (#{type},#{uid},#{item_id},#{item_sub_type})")
    void addCourseExamine(CourseExamine courseExamine);

    @Select("select * from user_examine where user_id = #{arg0} and auth_type = 0")
    List<UserExamine> getHeadExamine(int user_id,int auth_type);
}
