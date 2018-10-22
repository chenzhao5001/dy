package com.guidesound.dao;

import com.guidesound.models.Student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface IStudent {
    @Insert("insert into student " +
            "(user_id,level,grade,create_time) values (#{user_id},#{level},#{grade},#{create_time})")
    void add(Student student);

    @Select("select * from student where user_id = #{arg0}")
    Student find(int id);

    @Update("update student set level = #{level},grade = #{grade},update_time = #{update_time}" +
            " where user_id = #{user_id}")
    void update(Student student);

}
