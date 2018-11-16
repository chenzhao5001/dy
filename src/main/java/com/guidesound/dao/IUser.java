package com.guidesound.dao;

import com.guidesound.models.User;
import com.guidesound.models.UserInfo;
import com.guidesound.models.VideoUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface IUser {

    public void insertUser(User user);

    public User getUser(int id);

    public List<User> getListByUnionid(String unionid);

    @Select("select * from user where phone = #{arg0}")
    public List<User> getUserByPhone(String phone);

    @Select("select * from user where name = #{arg0}")
    public List<User> getUserByName(String name);

    @Update("update user set phone = #{arg1},pwd = #{arg2} ,status = 1 where id = #{arg0}")
    public void phoneRegister(int id,String phone,String pwd);

    @Update("update user set status = #{arg1} where id= #{arg0}")
    public void updateStatus(int id,int status);
    @Update("update user set type = #{arg1} ,level = 2 where id= #{arg0} and level = 1")
    public void updateType(int id,String type);

    @Insert("insert into user (phone) values (#{phone})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    public void addUserByPhone(User user);

    @Insert("insert into user (name,create_time) values (#{name},#{create_time})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    public void addUserByName(User user);


    @Select("select * from user where id = #{arg0}")
    public UserInfo getUserInfo(String id);

    @Insert("insert into userFuns (user_id,funs_user_id,create_time) values (#{arg1},#{arg0},#{arg2})")
    public void followUser(int id,int user_id,int create_time);

    @Update("update userFuns set deleted = 1,update_time = #{arg2} where user_id= #{arg1} and funs_user_id = #{arg0}")
    public void cancelFollow(int id,int user_id,int update_time);

    @Insert("insert into userPraise (user_id,praise_user_id,create_time) values (#{arg1},#{arg0},#{arg2})")
    public void praiseUser(int id,int user_id,int create_time);

    @Select("select count(*) from userFuns where user_id = #{arg0}")
    public int getFunsById(String user_id);
    @Select("select count(*) from userFuns where funs_user_id = #{arg0}")
    public int getFollowById(String user_id);

    @Select("select count(id) from userFuns where user_id = #{arg0} and funs_user_id = #{arg1} and deleted = 0")
    public int getIdByUserAndFunsId(int userId,int funsId);

    @Select("select count(*) from userPraise where user_id = #{arg0}")
    public int getPraiseById(String user_id);
    @Select("select id from student where user_id = #{arg0}")
    public int findStudentInfoId(String user_id);

    @Update("update user set head = #{arg1} where id = #{arg0}")
    public void updateHead(int id,String head);
    @Update("update user set sign_name = #{arg1} where id = #{arg0}")
    public void updateSignName(int id,String signName);

    @Update("update user set name = #{arg1} where id = #{arg0}")
    public void updateName(int id,String name);

    @Update("update user set sex = #{arg1} where id = #{arg0}")
    public void updateSex(int id,int sex);

    @Update("update user set grade = #{arg1} where id = #{arg0}")
    public void updateGrade(int id,int grade);

    @Update("update user set phone = #{arg1} where id = #{arg0}")
    public void updatePhone(int id,String phone);

    @Update("update user set province = #{arg1},city = #{arg2},area = #{arg3} where id = #{arg0}")
    public void updateProvinceAndCity(int id,String province,String city,String area);

    @Update("update user set subject = #{arg1} where id = #{arg0}")
    public void updateSubject(int id,int subject);

    @Update("update user set grade_level = #{arg1},level = 3 where id = #{arg0}")
    public void updateGradeLevel(int id,int grade);

    @Update("update user set teach_age = #{arg1} where id = #{arg0}")
    public void updateTeachAge(int id,int age);

    @Update("update user set identity_auth_pic1 = #{arg1},identity_auth_pic2 = #{arg2}  where id = #{arg0}")
    public void identityAuth(int id,String pic1,String pic2);

    @Update("update user set education_auth_pic1 = #{arg1},education_auth_pic2 = #{arg2}  where id = #{arg0}")
    public void educationAuth(int id,String pic1,String pic2);

    @Update("update user set qualification_pic1 = #{arg1},qualification_pic2 = #{arg2}  where id = #{arg0}")
    public void qualificationAuth(int id,String pic1,String pic2);

    @Update("update user set juridical_pic1 = #{arg1},juridical_pic2 = #{arg2}  where id = #{arg0}")
    public void juridicalAuth(int id,String pic1,String pic2);
    @Update("update user set business_pic1 = #{arg1},business_pic2 = #{arg2}  where id = #{arg0}")
    public void businessAuth(int id,String pic1,String pic2);

    @Update("update user set company_name = #{arg1} where id = #{arg0}")
    public void setCompanyName(int user_id,String companyName);


    @Update("update user set sex = #{arg1},grade = #{arg2},level = #{arg3} where id = #{arg0}")
    public void setStudentBasicInfo(int id,String sex,String grade,int level);

    @Update("update user set province = #{arg1},city = #{arg2},area = #{arg3},phone = #{arg4},level = #{arg5} where id = #{arg0}")
    public void setStudentHighInfo(int id,String province,String city,String area,String phone,int level);

    @Update("update user set sex = #{arg1},grade = #{arg2},level = #{arg3} where id = #{arg0}")
    public void setParentBasicInfo(int id,String sex,String grade,int level);

    @Update("update user set province = #{arg1},city = #{arg2},area = #{arg3},phone = #{arg4},level = #{arg5} where id = #{arg0}")
    public void setParentHighInfo(int id,String province,String city,String area,String phone,int level);

    @Update("update user set sex = #{arg1},subject = #{arg2},grade = #{arg3},province = #{arg4},city = #{arg5}, area = #{arg6},level = #{arg7} where id = #{arg0}")
    public void setTeacherBasicInfo(int id,String sex,String subject,String grade,String province,String city,String area,int level);
    @Update("update user set subject = #{arg1},grade = #{arg2},province = #{arg3},city = #{arg4},area = #{arg5},level = #{arg6} where id = #{arg0}")
    public void setInstitutionBasicInfo(int id,String subject,String grade,String province,String city,String area,int level);

    @Select("<script>"
            + "SELECT * FROM user WHERE id IN "
            + "<foreach item='item' index='index' collection='iList' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    public List<VideoUser> getUserInfoByIds(@Param("iList") List<Integer> iList);

}

