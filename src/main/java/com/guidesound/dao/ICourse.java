package com.guidesound.dao;

import com.guidesound.models.Subject;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ICourse {

    @Insert("insert into course (user_id,course_name,subject,grade_level,method,price,pictures,area) " +
            "value (#{arg0},#{arg1},#{arg2},#{arg3},#{arg4},#{arg5},#{arg6},#{arg7})")
    void add1v1(int user_id,String name,int i_subject,int i_grade_level,int i_method,int i_price,String pic,String area,int create_time);
    @Select("select * from subject_config")
    List<Subject> getSubject();
}
