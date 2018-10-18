package com.guidesound.dao;

import com.guidesound.models.Student;
import org.apache.ibatis.annotations.Insert;

public interface IStudent {
    @Insert("insert into student " +
            "(user_id,grade,create_time,update_time) values (#{user_id},#{grade},#{create_time},#{update_time})")
    void add(Student student);
}
