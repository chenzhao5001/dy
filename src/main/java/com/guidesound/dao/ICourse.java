package com.guidesound.dao;

import com.guidesound.dto.Course1V1DTO;
import com.guidesound.dto.CourseClassDTO;
import com.guidesound.models.Course;
import com.guidesound.models.Subject;
import com.guidesound.models.Teacher;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ICourse {

    @Insert("insert into course (user_id,course_name,course_type,grade_level,method,price,course_pic,test_method,test_duration,test_price,create_time) " +
            "value (#{user_id},#{course_name},1,#{grade_level},#{method},#{price},#{course_pic},#{test_method},#{test_duration},#{test_price},#{create_time})")
    void add1v1(Course1V1DTO course1V1DTO);

    @Insert("insert into course (" +
            "user_id," +
            "course_type," +
            "course_name," +
            "grade_level," +
            "course_pic," +
            "max_person," +
            "all_duration," +
            "all_price," +
            "test_time," +
            "test_duration," +
            "test_price," +
            "create_time) " +

            "value (" +
            "#{user_id}," +
            "2," +
            "#{course_name}," +
            "#{grade_level}," +
            "#{course_pic}," +
            "#{max_person}," +
            "#{all_duration}," +
            "#{test_time}," +
            "#{test_duration}," +
            "#{test_price}," +
            "#{test_price}," +
            "#{create_time})")
    void addClass(CourseClassDTO courseClassDTO);

    @Update("update course set course_pic = #{arg2} where user_id = #{arg0} and id = #{arg1}")
    void updateCoursePic(int user_id,int course_id,String pic);
    @Select("select * from course where user_id = #{arg0}")
    List<Course> getCourseList(int user_id);

    @Update("update course set course_outline = #{arg1}, over_time = #{arg2} where id = #{arg0}")
    void setCourseOutline(int course_id,String outline,int over_time);

    @Select("select course_outline from course where id = #{arg0}")
    String getCourseOutline(int course_id);


    @Select("select * from subject_config")
    List<Subject> getSubject();

    @Insert("insert into teacher (name,sex,subject,level,introduction,user_id,create_time,update_time) value (#{name},#{sex},#{subject},#{level},#{introduction},#{user_id},#{create_time},#{update_time})")
    void addTeacher(Teacher teacher);
    @Update("update teacher set name = #{name},sex = #{sex},subject = #{subject},level = #{level},introduction = #{introduction},update_time = #{update_time} where id = #{id}")
    void updateTeacher(Teacher teacher);
}
