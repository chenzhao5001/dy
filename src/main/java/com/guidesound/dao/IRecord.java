package com.guidesound.dao;

import com.guidesound.dto.RecordDTO;
import com.guidesound.models.Record;
import com.guidesound.models.TestRecordCourse;
import com.guidesound.models.UserRecordCourse;
import com.guidesound.ret.VideoClass;
import org.apache.ibatis.annotations.*;

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

    @Select("<script>"
            + "SELECT * FROM record_course WHERE record_course_id IN "
            + "<foreach item='item' index='index' collection='iList' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    List<Record> listByIds(@Param("iList") List<Integer> iList);

    @Select("select * from record_course where record_course_id = #{arg0}")
    Record get(int id);



    @Update("update user_record_course set last_class_no = #{arg1},last_class_pos = #{arg2} where user_record_course_id = #{arg0} and user_id = #{arg3}")
    void report(int record_course_id,int last_class_no,int last_class_pos,int user_id);

    @Select("select * from record_course where record_course_status = #{arg1}")
    List<Record> getRecordByStatus(int status);

    @Update("update record_course set record_course_status = #{arg1} where record_course_id = #{arg0}")
    void setRecordCourseStatue(int record_course_id,int record_course_status);

    @Select("select * from user_record_course where user_id = #{arg0} and user_record_course_id = #{arg1}")
    List<UserRecordCourse> getRecordByUserAndId(int user_id, int user_record_course_id );

    @Insert("insert into user_record_course (user_id,user_record_course_id,create_time) values (#{user_id},#{user_record_course_id},#{create_time})")
    void insertRecordCourse(UserRecordCourse userRecordCourse);

    @Select("select * from user_record_course where user_id = #{arg0}")
    List<UserRecordCourse> getRecordByUserId(int user_id);

    @Select("select count(*) from user_record_course where user_record_course_id = #{arg0}")
    int getUserRecordCountByCourseId(int record_course_id);

    @Select("select * from user_record_course where user_record_course_id = #{arg0}")
    List<UserRecordCourse> getUserRecordByCourseId(int record_course_id);

    @Select("select * from user_record_course where user_id = #{arg0}")
    List<UserRecordCourse> getUserRecordByUserId(int user_id);

    @Select("select * from user_record_course where user_id = #{arg0}")
    List<UserRecordCourse> getUserRecordByUserIdAndCourseId(int user_id,int course_id);

    @Insert("insert into test_record_course (user_id,record_course_id,class_NO,class_url,class_name,time_start,time_end,picture) " +
            "values (#{user_id},#{record_course_id},#{class_NO},#{class_url},#{class_name},#{time_start},#{time_end},#{picture})")
    void addTestRecordCourse(TestRecordCourse testRecordCourse);

    @Select("select * from test_record_course")
    List<TestRecordCourse> getAllTestRecordCourse();

    @Select("select * from record_course where record_course_id = #{arg0}")
    VideoClass getVideoClass(int id);

    @Update("update record_course set group_id = #{arg0} where record_course_id = #{arg1}")
    void setGroupId(int groupId,int record_id);




}
