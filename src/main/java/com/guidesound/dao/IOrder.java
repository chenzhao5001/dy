package com.guidesound.dao;

import com.guidesound.TempStruct.ClassTime;
import com.guidesound.dto.Order1V1DTO;
import com.guidesound.dto.OrderClassDTO;
import com.guidesound.models.*;
import com.guidesound.ret.ClassOrder;
import com.guidesound.ret.Order1V1;
import org.apache.ibatis.annotations.*;

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
    OrderInfo getOrderById(int order_id);
    @Select("select * from user_order where id = #{arg0} and student_id = #{arg1}")
    OrderInfo getUserByOrderIdAndUserId(int order_id,int user_id);

    @Update("update user_order set order_status = #{arg1} where id = #{arg0}")
    void setOrderStatus(int order_id,int state);

    @Insert("insert into class_room (user_id,course_id,create_time,all_hours,type,all_charge,price_one_hour) " +
            "value (#{user_id},#{course_id},#{create_time},#{all_hours},#{type},#{all_charge},#{price_one_hour})")
    @Options(useGeneratedKeys=true, keyProperty="class_id", keyColumn="class_id")
    void addClassRoom(ClassRoom classRoom);

    @Update("update class_room set room_number = #{arg1} where class_id = #{arg0}")
    void addRoomNumber(int class_id,int room_number);

    @Select("select * from class_room where course_id = #{arg0}")
    List<ClassRoom> getClassRoomByCourseId(int course_id);



    @Update("update class_room set new_class_time = #{arg1} where class_id = #{arg0}")
    void setClassTime(int class_id,String class_time);

    @Update("update class_time set status = #{arg3} where class_id = #{arg0} and student_id = #{arg1} and begin_time = #{arg2}")
    void setClassTimeStatus(int class_id,int student_id, int begin_time,int status);
    @Select("select * from class_time where order_id = #{arg0} and student_id = #{arg1} and begin_time = #{arg2}")
    List<ClassTimeInfo> getClassTimeStatus(int order_id,int student_id,int begin_time);

    @Update("update class_room set outline = #{arg1} where class_id = #{arg0}")
    void setClassRoomOutLine(int class_id,String out_line);

    @Update("update user_order set outline = #{arg1} where id = #{arg0}")
    void setOrderOutline(int order,String outline);
    @Select("select new_class_time from class_room where class_id = #{arg0} and user_id = #{arg1}")
    String getClassTime(int class_id,int user_id);
    @Select("select outline from user_order where id = #{arg0}")
    String getOutlinefromOrder(int order);

    @Select("select * from class_room where class_id = #{arg0}")
    ClassRoom getClassRoomById(int class_id);

    @Delete("delete from class_time where class_id = #{arg0} and create_time > #{arg0}")
    void deleteClassTime(int class_id,int time);

    @Update("update class_room set course_name = #{course_name}," +
            "course_name = #{course_name}," +
            "course_pic = #{course_pic}," +
            "teacher_name = #{teacher_name}," +
            "subject = #{subject}," +
            "grade = #{grade}," +
            "form = #{type}," +
            "way = #{way}," +
            "max_person = #{max_person}," +
            "price_one_hour = #{price_one_hour}," +
            "all_charge = #{all_charge}," +
            "refund_rule = #{refund_rule}," +
            "outline = #{outline}," +
            "type = #{type}," +
            "tutor_content = #{tutor_content}" +
            " where class_id = #{id}")
    void ClassRoomCourse(Course course);

    @Insert("insert into student_class (user_id,teacher_id,course_id,class_id,order_id,create_time,update_time)" +
            " value (#{user_id},#{teacher_id},#{course_id},#{class_id},#{order_id},#{create_time},#{update_time})")
    void addStudentClass(StudentClass studentClass);

    @Select("select * from student_class where user_id = #{arg0} and class_id = #{arg1}")
    List<StudentClass> getStudentClassByInfo(int user_id,int class_id);

    @Select("select * from student_class where teacher_id = #{arg0} and class_id = #{arg1}")
    List<StudentClass> getStudentClassByTeacherId(int teacher_id,int class_id);


    @Select("select * from student_class where course_id = #{arg0}")
    List<StudentClass> getStudentClassByCourseId(int course_id);

    @Select("select * from student_class where user_id = #{arg0}")
    List<StudentClass> getStudentClassByUserId(int user_id);

    @Select("select * from student_class where class_id = #{arg0}")
    List<StudentClass> getStudentClassByClassId(int class_id);


    @Select("select * from student_class where order_id = #{arg0}")
    List<StudentClass> getStudentClassByOrder(int order_id);

    @Select("<script>"
            + "SELECT * FROM class_room WHERE class_id IN "
            + "<foreach item='item' index='index' collection='iList' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    List<ClassRoom> getClassInfoByIds(@Param("iList") List<Integer> iList);

    @Select("select * from class_room where user_id = #{arg0}")
    List<ClassRoom> getClassInfoByUserId(int user_id);

    @Select("select * from class_room where type = #{arg0}")
    List<ClassRoom> getAllClassRoom(int type);

    @Select("select * from class_room where user_id = #{arg0}")
    List<ClassRoom> getClassRoomByUserId(int user_id);
    @Insert("insert into class_time (class_id,order_id,teacher_id,student_id,begin_time,end_time,status)" +
            " value(#{class_id},#{order_id},#{teacher_id},#{student_id},#{begin_time},#{end_time},#{status})")
    void addClassTime(ClassTimeInfo classTime);

    @Select("select * from class_time where order_id = #{arg0} and student_id = #{arg1} and begin_time < #{arg2}")
    List<ClassTimeInfo> getClassTimeByInfo(int order_id, int student_id, int time);


    @Select("select * from class_time where order_id = #{arg0} and student_id = #{arg1} and begin_time < #{arg2} and status = 1")
    List<ClassTimeInfo> getTrueClassTimeByInfo(int order_id, int student_id, int time);


    @Select("select * from class_time where class_id = #{arg0} and student_id = #{arg1} and teacher_id = #{arg3} and begin_time < #{arg2}")
    List<ClassTimeInfo> getTeacherClassTimeByInfo(int class_id, int student_id, int time,int teacher_id);

    @Select("select * from class_time where class_id = #{arg0} and student_id = #{arg1} and teacher_id = #{arg3} and begin_time < #{arg2} and status = 1")
    List<ClassTimeInfo> getTeacherTrueClassTimeByInfo(int class_id, int student_id, int time,int teacher_id);

    @Update("update user_order set refund_amount = #{arg1},submit_time = #{arg2} where id = #{arg0}")
    void setRefundAmount(int order_id,int money,int time);

    @Select("select * from teacher_enter_info where class_id = #{arg0} and #{arg1}")
    List<TeacherEnterInfo> getTeacherEnterInfo(int class_id,int user_id);

    @Insert("insert into teacher_enter_info (teacher_id,class_id,class_nunber,create_time) value (#{arg0},#{arg1},#{arg2},#{arg3})")
    void setTeacherEnterInfo(int teacher_id,int class_id,int class_nunber,int create_time);

    @Update("update user_order set class_id = #{arg1} where id = #{arg0}")
    void addOrderClassId(int order,int class_id);

    @Select("select count(*) from user_order where class_id = #{arg0} and refund_amount != 0")
    int getReturnOrderByClassId(int class_id);

}
