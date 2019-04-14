package com.guidesound.dao;

import com.guidesound.dto.RecordDTO;
import com.guidesound.models.Record;
import com.guidesound.models.UserRecordCourse;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface IRecord {

    @Insert("insert into record_course " +
            "(user_id," +
            "record_course_pic," +
            "record_course_name," +
            "subject," +
            "grade," +
            "video_count," +
            "price," +
            "intro_teacher_text," +
            "intro_teacher_pic," +
            "intro_course_text," +
            "intro_course_pic," +
            "videos," +
            "record_course_status," +
            "save," +
            "create_time) " +
            "value " +
            "(#{user_id}," +
            "#{record_course_pic}," +
            "#{record_course_name}," +
            "#{subject}," +
            "#{grade}," +
            "#{video_count}," +
            "#{price}," +
            "#{intro_teacher_text}," +
            "#{intro_teacher_pic}," +
            "#{intro_course_text}," +
            "#{intro_course_pic}," +
            "#{videos}," +
            "#{record_course_status}," +
            "#{save}," +
            "#{create_time})")
    void add(RecordDTO recordDTO);
    @Update("update record_course " +
            "set " +
            "record_course_pic = #{record_course_pic}," +
            "record_course_name = #{record_course_name}," +
            "subject = #{subject}," +
            "grade = #{grade}," +
            "video_count = #{video_count}," +
            "price = #{price}," +
            "intro_teacher_text = #{intro_teacher_text}," +
            "intro_teacher_pic = #{intro_teacher_pic}," +
            "intro_course_text = #{intro_course_text}," +
            "intro_course_pic = #{intro_course_pic}," +
            "videos = #{videos}," +
            "save = #{save}," +
            "update_time = #{update_time}," +
            "record_course_status = #{record_course_status} where record_course_id = #{record_course_id}")
    void update(RecordDTO recordDTO);

    @Delete("delete from record_course where user_id = #{arg0} and record_course_id = #{arg1}")
    void delete(int user_id,int record_course_id);

    @Select("select * from record_course where user_id = #{arg0}")
    List<Record> list(int user_id);

    @Select("select * from record_course where record_course_id = #{arg0}")
    Record get(int id);

    @Update("update record_course set last_class_no = #{arg1},last_class_pos = #{arg2} where record_course_id = #{arg0}")
    void report(int id,int last_class_no,int last_class_pos);

    @Select("select * from record_course where record_course_status = #{arg1}")
    List<Record> getRecordByStatus(int status);

    @Update("update record_course set record_course_status = #{arg1} where record_course_id = #{arg0}")
    void setRecordCourseStatue(int record_course_id,int record_course_status);

    @Select("select * from user_record_course where user_id = #{arg0} and user_record_course_id = #{arg1}")
    List<UserRecordCourse> getRecordByUserAndId(int user_id, int user_record_course_id );

    @Insert("insert into user_record_course (user_id,user_record_course_id) values (#{user_id},#{user_record_course_id})")
    void insertRecordCourse(UserRecordCourse userRecordCourse);

}
