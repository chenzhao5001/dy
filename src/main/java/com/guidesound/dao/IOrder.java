package com.guidesound.dao;

import com.guidesound.dto.Order1V1DTO;
import com.guidesound.dto.OrderClassDTO;
import com.guidesound.models.ClassRoom;
import com.guidesound.models.OrderInfo;
import com.guidesound.ret.ClassOrder;
import com.guidesound.ret.Order1V1;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IOrder {

    @Insert("insert into user_order " +
            "(type," +
            "course_owner_id," +
            "course_id," +
            "course_name," +
            "course_pic," +
            "teacher_id," +
            "teacher_name," +
            "student_id," +
            "student_name," +
            "subject," +
            "grade," +
            "form," +
            "way," +
            "max_person," +
            "all_hours," +
            "price_one_hour," +
            "all_charge," +
            "create_time," +
            "refund_rule," +
            "tutor_content," +
            "outline) value " +
            "(#{type}," +
            "#{user_id}," +
            "#{course_id}," +
            "#{course_name}," +
            "#{course_pic}," +
            "#{teacher_id}," +
            "#{teacher_name}," +
            "#{student_id}," +
            "#{student_name}," +
            "#{subject}," +
            "#{grade}," +
            "#{form}," +
            "#{way}," +
            "#{max_person}," +
            "#{all_hours}," +
            "#{price_one_hour}," +
            "#{all_charge}," +
            "#{create_time}," +
            "#{refund_rule}," +
            "#{tutor_content}," +
            "#{outline})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void addClassOrder(OrderClassDTO orderClassDTO);

    @Insert("insert into user_order " +
            "(type," +
            "course_owner_id," +
            "course_id," +
            "course_name," +
            "course_pic," +
            "teacher_id," +
            "teacher_name," +
            "student_id," +
            "student_name," +
            "subject," +
            "grade," +
            "form," +
            "way," +
            "all_hours," +
            "price_one_hour," +
            "all_charge," +
            "refund_rule," +
            "outline," +
            "create_time," +
            "tutor_content) value " +
            "(#{type}," +
            "#{user_id}," +
            "#{course_id}," +
            "#{course_name}," +
            "#{course_pic}," +
            "#{teacher_id}," +
            "#{teacher_name}," +
            "#{student_id}," +
            "#{student_name}," +
            "#{subject}," +
            "#{grade}," +
            "#{form}," +
            "#{way}," +
            "#{all_hours}," +
            "#{price_one_hour}," +
            "#{all_charge}," +
            "#{refund_rule}," +
            "#{outline}," +
            "#{create_time}," +
            "#{tutor_content})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void add1v1Order(Order1V1DTO order1V1DTO);

    @Select("select * from user_order where id = #{arg0}")
    ClassOrder getClassOrderById(int order_id);
    @Select("select * from user_order where id = #{arg0}")
    Order1V1 get1v1OrderById(int order_id);

    @Select("select * from user_order where id = #{arg0}")
    OrderInfo getUserByOrderId(int order_id);

    @Insert("insert into class_room (user_id,course_id) value (#{user_id},#{course_id})")
    void addClassRoom(ClassRoom classRoom);

    @Select("select * from class_room where course_id = #{arg0}")
    List<ClassRoom> getClassRoomByCourseId(int course_id);
}
