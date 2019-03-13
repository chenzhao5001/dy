package com.guidesound.dao;

import com.guidesound.dto.RecordDTO;
import com.guidesound.models.Record;
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
            "update_time = #{update_time}," +
            "record_course_status = #{record_course_status} where record_course_id = #{record_course_id}")
    void update(RecordDTO recordDTO);

    @Delete("delete from record_course where user_id = #{arg0} and record_course_id = #{arg1}")
    void delete(int user_id,int record_course_id);

    @Select("select * from record_course")
    List<Record> list();

}
