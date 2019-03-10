package com.guidesound.dao;

import com.guidesound.dto.OrderClassDTO;
import org.apache.ibatis.annotations.Insert;

public interface IOrder {

    @Insert("insert into order " +
            "(type," +
            "course_name," +
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
            "outline) values " +
            "(#{type}," +
            "#{course_name}," +
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
    void addClassOrder(OrderClassDTO orderClassDTO);
}
