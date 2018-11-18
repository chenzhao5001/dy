package com.guidesound.dao;

import org.apache.ibatis.annotations.Insert;

public interface ICourse {

    @Insert("insert into course (user_id,course_name,subject,grade_level,method,price,pictures,area) " +
            "value (#{arg0},#{arg1},#{arg2},#{arg3},#{arg4},#{arg5},#{arg6},#{arg7})")
    void add1v1(int user_id,String name,int i_subject,int i_grade_level,int i_method,int i_price,String pic,String area,int create_time);
}
