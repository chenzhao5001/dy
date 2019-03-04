package com.guidesound.dao;

import com.guidesound.models.Course;
import com.guidesound.models.CourseExamine;
import com.guidesound.models.Subject;
import com.guidesound.models.Teacher;
import org.apache.ibatis.annotations.*;

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
            "teacher_id," +
            "teacher_name," +
            "type," +
            "course_status," +
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
            "#{teacher_id}," +
            "#{teacher_name}," +
            "#{type}," +
            "#{course_status}," +
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
            "teacher_id = #{teacher_id}," +
            "teacher_name = #{teacher_name}," +
            "course_status = #{course_status}," +
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
            "teacher_id," +
            "course_status," +
            "teacher_name," +
            "create_time) " +

            "value (" +
            "#{user_id}," +
            "#{type}," +
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
            "#{teacher_id}," +
            "#{course_status}," +
            "#{teacher_name}," +
            "#{create_time})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
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
            "teacher_id = #{teacher_id}," +
            "teacher_name = #{teacher_name}," +
            "course_status = #{course_status}," +
            "update_time = #{update_time} where id = #{id}")
    void updateClass(Course course);

    @Select("select * from course where id = #{agg0}")
    Course getCouresByid(int id);

    @Update("update course set course_pic = #{arg2} where user_id = #{arg0} and id = #{arg1}")
    void updateCoursePic(int user_id,int course_id,String pic);
    @Select("select * from course where user_id = #{arg0}")
    List<Course> getCourseList(int user_id);

    @Update("update course set course_outline = #{arg1}, over_time = #{arg2} where id = #{arg0}")
    void setCourseOutline(int course_id,String outline,int over_time);

    @Select("select course_outline from course where id = #{arg0}")
    String getCourseOutline(int course_id);

    @Select("select * from course where id = #{arg0} and type = #{arg1}")
    Course getCourseById(int course_id,int type);

    @Update("update course set type = #{arg1} where id = #{arg0}")
    void updateCourseType(int course_id,int type);

    @Delete("delete from course where id = #{arg0}")
    void deleteCourse(int course_id);

    @Update("update course set course_status = {#arg1} where id = {#arg0}")
    void setCourseState(int course_id,int course_status);


    @Select("select * from subject_config")
    List<Subject> getSubject();

    @Insert("insert into teacher (name,sex,subject,level,certificate,introduction,user_id,status,create_time,update_time) value (#{name},#{sex},#{subject},#{level},#{certificate},#{introduction},#{user_id},#{status},#{create_time},#{update_time})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void addTeacher(Teacher teacher);

    @Update("update teacher set status = #{arg1} where #{arg0}")
    void setTeacherState(int id,int state);

    @Update("update teacher set name = #{name},sex = #{sex},subject = #{subject},level = #{level},certificate = #{certificate},introduction = #{introduction},status = 1,update_time = #{update_time} where id = #{id}")
    void updateTeacher(Teacher teacher);
    @Select("select * from teacher where id = #{arg0}")
    Teacher getTeacherById(int id);

    @Select("select * from teacher where user_id = #{arg0}")
    List<Teacher> getTeacherList(int user_id);

}
