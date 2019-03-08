package com.guidesound.dao;

import com.guidesound.models.CommodityExamine;
import com.guidesound.models.CourseExamine;
import com.guidesound.models.UserAuth;
import com.guidesound.models.UserExamine;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IExamine {

    @Insert("insert into user_examine (user_id,auth_type,text) value (#{arg0},#{arg1},#{arg2})")
    void addUserExamine(int user_id,int auth_type,String head);

    @Insert("insert into user_examine (user_id," +
            "auth_type," +
            "type," +
            "identity_card," +
            "graduation_card," +
            "teacher_card," +
            "achievement," +
            "license," +
            "confirmation_letter," +
            "shop_prove," +
            "auth_info) value (#{arg0},#{arg1},#{arg2},#{arg3},#{arg4},#{arg5},#{arg6},#{arg7},#{arg8},#{arg9},#{arg10})")
    void addUserAuth(
            int user_id,
            int auth_type,
            String type,
            String identity_card,         //身份证
            String graduation_card,       //毕业证
            String teacher_card,          //教师证
            String achievement,           //成就
            String license,               //营业执照
            String confirmation_letter,   //确认书
            String shop_prove,             //店铺证明
            String auth_info               //认证信息
    );
    @Select("select * from user_examine")
    List<UserExamine> getUserExamine();
    @Insert("insert into commodity_examine (user_id,type,commodity_id,shop_url) value (#{arg0},#{arg1},#{arg2},#{arg3})")
    void addCommodityExamine(int user_id,int type,int commodity_id, String shop_url);


    @Select("select * from course_examine")
    List<CourseExamine> getCourseExamine();

    @Insert("insert into course_examine (type,uid,item_id,item_sub_type) value (#{type},#{uid},#{item_id},#{item_sub_type})")
    void addCourseExamine(CourseExamine courseExamine);

    @Select("select * from user_examine where user_id = #{arg0} and auth_type = #{arg1}")
    List<UserExamine> getUserExamineByInfo(int user_id,int auth_type);
    @Delete("delete from user_examine where user_id = #{arg0} and auth_type = #{arg1}")
    void deleteUserExamine(int user_id,int auth_type);

    @Delete("delete from user_examine where user_id = #{arg0} and auth_type = #{arg1}")
    void deleteCommodityExamine(int commodity_id,int auth_type);

    @Select("select * from commodity_examine")
    List<CommodityExamine> getCommodityExamine();

    @Select("select * from commodity_examine where user_id = #{arg0} and auth_type = #{arg1}")
    List<CommodityExamine> getCommodityExamineByInfo(int user_id,int auth_type);

    @Select("select * from course_examine where item_id = #{arg0} and type = #{arg1}")
    List<CourseExamine> getCourseExamineById(int item_id,int type);

    @Delete("delete from course_examine where item_id = #{arg0} and type = #{arg1}")
    void deleteCourseExamine(int item_id,int type);

}
