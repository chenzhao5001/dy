package com.guidesound.dao;

import com.guidesound.models.Course;
import com.guidesound.models.Subject;
import com.guidesound.models.Teacher;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ICourse {

    @Insert("insert into course " +
            "(course_pic," +
            "user_id," +
            "course_name," +
            "subject," +
            "grade," +
            "form," +
            "price_one_hour," +
            "area_service," +
            "test_form," +
            "test_duration," +
            "test_charge," +
            "introduction_teacher," +
            "type," +
            "create_time," +
            "update_time)"  +

            "value (" +
            "#{course_pic}," +
            "#{user_id}," +
            "#{course_name}," +
            "#{subject}," +
            "#{grade}," +
            "#{form}," +
            "#{price_one_hour}," +
            "#{area_service}," +
            "#{test_form}," +
            "#{test_duration}," +
            "#{test_charge}," +
            "#{introduction_teacher}," +
            "#{type}," +
            "#{create_time}," +
            "#{update_time})")
    void add1v1(Course course);

    @Update("update course set " +
            "course_pic = #{course_pic}," +
            "course_name = #{course_name}," +
            "subject = #{subject}," +
            "grade = #{grade}," +
            "form = #{form}," +
            "price_one_hour = #{price_one_hour}," +
            "area_service = #{area_service}," +
            "test_form = #{test_form}," +
            "test_duration = #{test_duration}," +
            "test_charge = #{test_charge}," +
            "introduction_teacher = #{introduction_teacher}," +
            "update_time = #{update_time} where id = #{id}")
    void update1V1(Course course);
    @Insert("insert into course (" +
            "user_id," +
            "type," +
            "course_pic," +
            "course_name," +
            "subject," +
            "grade," +
            "form," +
            "max_person," +
            "all_hours," +
            "all_charge," +
            "test_form," +
            "test_duration," +
            "test_charge," +
            "course_content," +
            "outline," +
            "introduction_teacher," +
            "create_time) " +

            "value (" +
            "#{user_id}," +
            "2," +
            "#{course_pic}," +
            "#{course_name}," +
            "#{subject}," +
            "#{grade}," +
            "#{form}," +
            "#{max_person}," +
            "#{all_hours}," +
            "#{all_charge}," +
            "#{test_form}," +
            "#{test_duration}," +
            "#{test_charge}," +
            "#{course_content}," +
            "#{outline}," +
            "#{introduction_teacher}," +
            "#{create_time})")
    void addClass(Course course);

    @Update("update course set " +
            "course_pic = #{course_pic}," +
            "course_name = #{course_name}," +
            "subject = #{subject}," +
            "grade = #{grade}," +
            "form = #{form}," +
            "max_person = #{max_person}," +
            "all_hours = #{all_hours}," +
            "all_charge = #{all_charge}," +
            "test_form = #{test_form}," +
            "test_duration = #{test_duration}," +
            "test_charge = #{test_charge}," +

            "course_content = #{course_content}," +
            "outline = #{outline}," +
            "introduction_teacher = #{introduction_teacher}," +
            "update_time = #{update_time} where id = #{id}")
    void updateClass(Course course);

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

    @Insert("insert into teacher (name,sex,subject,level,certificate,introduction,user_id,create_time,update_time) value (#{name},#{sex},#{subject},#{level},#{introduction},#{certificate},#{user_id},#{create_time},#{update_time})")
    void addTeacher(Teacher teacher);
    @Update("update teacher set name = #{name},sex = #{sex},subject = #{subject},level = #{level},certificate = #{certificate},introduction = #{introduction},update_time = #{update_time} where id = #{id}")
    void updateTeacher(Teacher teacher);

    @Select("select * from teacher where user_id = #{arg0}")
    List<Teacher> getTeacherList(int user_id);
}
