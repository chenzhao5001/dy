package com.guidesound.dao;

import com.guidesound.models.Teacher;
import org.apache.ibatis.annotations.Insert;

public interface ITeacher {

    @Insert("insert into teacher " +
            "(user_id,subject,rank,create_time,update_time) " +
            "values (#{user_id},#{subject},#{rank},#{create_time},#{update_time})")
    void add(Teacher teacher);
}
