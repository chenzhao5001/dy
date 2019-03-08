package com.guidesound.dao;

import com.guidesound.find.IntroductionInfo;
import com.guidesound.models.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface IUser {

    public void insertUser(UserInfo user);


    @Select("select * from user where id = #{arg0}")
    public UserInfo getUser(int id);

    @Select("select * from user where id = #{arg0}")
    public User getUserById(int id);

    public List<UserInfo> getListByUnionid(String unionid);

    @Select("select * from user where phone = #{arg0}")
    public List<UserInfo> getUserByPhone(String phone);
    @Select("select * from user where dy_id = #{arg0}")
    List<UserInfo> getInfoByDyid(String dy_id);


    @Select("select * from user where unionid = #{arg0}")
    public List<UserInfo> getUserByUnionid(String phone);

    @Select("select * from user where phone = #{arg0} and pwd = #{arg1}")
    public List<UserInfo> getUserByPhoneAndPwd(String phone,String pwd);


    @Select("select * from user where name = #{arg0}")
    public List<UserInfo> getUserByName(String name);

    @Select("select id from user where name = #{arg0}")
    public List<Integer> getUserIdsByName(String name);

    @Select("select id from user where name like CONCAT('%',#{arg0},'%')")
    public List<Integer> getUserIdsByName2(String name);


    @Update("update user set phone = #{arg1},pwd = #{arg2} ,status = 1 where id = #{arg0}")
    public void phoneRegister(int id,String phone,String pwd);

    @Update("update user set status = #{arg1} where id= #{arg0}")
    public void updateStatus(int id,int status);
    @Update("update user set type = #{arg1} where id= #{arg0}")
    public void updateType(int id,String type);

    @Update("update user set type = #{arg1},dy_id = #{arg2},level = #{arg3} where id= #{arg0}")
    public void updateTypeInfo(int id,String type,int dy_id,int level);

    @Insert("insert into user (phone,create_time) values (#{phone},#{create_time})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    public void addUserByPhone(UserInfo user);

    @Insert("insert into user (unionid,name,head,create_time) values (#{unionid},#{name},#{head},#{create_time})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    public void addUserByUnionid(UserInfo user);

    @Update("update user set im_id = #{arg1}, im_sig = #{arg2} where id = #{arg0}")
    void setImInfo(int user_id,String im_id,String im_sig);

    @Insert("insert into user (name,create_time,level,type) values (#{name},#{create_time},#{level},#{type})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    public void addUserByName(UserInfo user);


    @Select("select * from user where id = #{arg0}")
    public UserInfo getUserInfo(String id);

    @Select("select count(*) from userFuns where user_id = #{arg1} and funs_user_id = #{arg0} and deleted = 0")
    int isFollow(int id,int user_id);

    @Insert("insert into userFuns (user_id,funs_user_id,create_time) values (#{arg1},#{arg0},#{arg2})")
    public void followUser(int id,int user_id,int create_time);

    @Update("update userFuns set deleted = 1,update_time = #{arg2} where user_id= #{arg1} and funs_user_id = #{arg0}")
    public void cancelFollow(int id,int user_id,int update_time);

    @Select("select user_id from userFuns where funs_user_id = #{arg0} and deleted = 0")
    public List<Integer> getFollowUsers(int user_id);

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

    @Update("update user set identity_auth_pic1 = #{arg1},identity_auth_pic2 = #{arg2},identity_name = #{arg3},identity_num = #{arg4} where id = #{arg0}")
    public void identityAuth(int id,String pic1,String pic2,String identity_name,String identity_num);

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

    @Update("update user set background_url = #{arg1} where id = #{arg0}")
    public void setBackroundUrl(int user_id,String backroundUrl);

    @Update("update user set sex = #{arg1},grade = #{arg2},level = #{arg3} where id = #{arg0}")
    public void setStudentBasicInfo(int id,String sex,String grade,int level);

    @Update("update user set province = #{arg1},city = #{arg2},area = #{arg3},phone = #{arg4},level = #{arg5} where id = #{arg0}")
    public void setStudentHighInfo(int id,String province,String city,String area,String phone,int level);

    @Update("update user set sex = #{arg1},grade = #{arg2},level = #{arg3} where id = #{arg0}")
    public void setParentBasicInfo(int id,String sex,String grade,int level);

    @Update("update user set province = #{arg1},city = #{arg2},area = #{arg3},phone = #{arg4},level = #{arg5} where id = #{arg0}")
    public void setParentHighInfo(int id,String province,String city,String area,String phone,int level);

    @Update("update user set sex = #{arg1},subject = #{arg2},grade_level = #{arg3},province = #{arg4},city = #{arg5}, area = #{arg6},level = #{arg7} where id = #{arg0}")
    public void setTeacherBasicInfo(int id,String sex,String subject,String grade,String province,String city,String area,int level);
    @Update("update user set subject = #{arg1},grade_level = #{arg2},province = #{arg3},city = #{arg4},area = #{arg5},level = #{arg6} where id = #{arg0}")
    public void setInstitutionBasicInfo(int id,String subject,String grade,String province,String city,String area,int level);

    @Select("<script>"
            + "SELECT * FROM user WHERE id IN "
            + "<foreach item='item' index='index' collection='iList' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    public List<VideoUser> getUserInfoByIds(@Param("iList") List<Integer> iList);

    @Update("update user set dy_id = #{arg1} where id = #{arg0}")
    public void setDyId(int id,int dyId);

    @Update("update user set create_type = #{arg1} where id = #{arg0}")
    public void setCreate_type(int id,int create_type);


    @Select("<script>"
            + "SELECT * FROM user WHERE id IN "
            + "<foreach item='item' index='index' collection='iList' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    List<UserInfo> getUserByIds(@Param("iList") List<Integer> iList);

    @Update("update user set introduction_pic = #{arg1} where id = #{arg0}")
    void upIntroductionPic(int user_id,String pic);

    @Update("update user set introduction = #{arg1} where id = #{arg0}")
    void upIntroduction(int user_id,String introduction);
    @Select("select * from user where id = #{arg0}")
    IntroductionInfo getIntroductionInfo(int user_id);

    @Insert("insert into userActive  (user_guid,create_time) values (#{arg0},#{arg1})")
    void setActive(String user_guid,int time);

    @Select("select count(*) from userActive where create_time > #{arg0}")
    int getActiveUserByTime(int time);

    @Update("update user set pwd = #{arg1},pwd_state = 1 where phone = #{arg0}")
    void setUserPwd(String phone,String pwd);

    @Select("select pwd from user where id = #{arg0}")
    String getPwd(int user_id);

    @Delete("delete from user where id = #{arg0}")
    void deleteUser(int user);

    @Update("update user set channel_stage = #{arg1} where id = #{arg0}")
    void setUserGradeStage(int user_id,int grade_stage);

    @Select("select channel_stage from user where id = #{arg0}")
    int getChannelStage(int user_id);

    @Select("select user_id from userFuns where funs_user_id = #{arg0} and deleted = 0")
    public List<Integer> getMeFollow(int user_id);
    @Select("select funs_user_id from userFuns where user_id = #{arg0} and deleted = 0")
    public List<Integer> getFollowMe(int user_id);

    @Update("update user_friend set type = #{arg2},to_user_id = #{arg3} where user_id = #{arg0} and add_user_id =#{arg1}")
    public void updateAddFriendType(int user_id,int add_user_id,int type,String to_user_id);

    @Update("update user_friend set to_user_id = #{arg2} where user_id = #{arg0} and add_user_id =#{arg1}")
    void updateToUser(int user_id,int add_user_id,String to_user_id);

    @Insert("insert into user_friend (user_id,add_user_id,type,state,create_time,to_user_id) values (#{arg0},#{arg1},#{arg2},1,#{arg3},#{arg4})")
    public void addFriend(int user_id,int add_user_id,int type,int time,String to_user_id);

    @Select("select * from user_friend where user_id = #{arg0} and add_user_id =#{arg1}")
    public List<UserFriend> findFriend(int user_id,int add_user_id);

    @Update("update user_friend set state = 2,create_time = #{arg2} where user_id = #{arg0} and add_user_id = #{arg1}")
    public void updateFriendState(int user_id,int add_user_id,int time);

    @Delete("delete from user_friend where user_id = #{arg0} and add_user_id = #{arg1}")
    public void deleteFriend(int user_id,int add_user_id);

    @Select("select * from user_friend where user_id = #{arg0} or add_user_id = #{arg0}")
    public List<UserFriend> newFriend(int user_id);

//    @Select("select * from user_friend where add_user_id = #{arg0} and type = 1")
//    public List<UserFriend> newFriendOther(int user_id);


    @Select("select * from user_friend where (user_id = #{arg0} or add_user_id = #{arg0}) and type = 2")
    public List<UserFriend> newFriendByPhone(int user_id);

    @Insert("insert into user_action (" +
            "from_user_id," +
            "to_user_id," +
            "type," +
            "content_id," +
            "content_url," +
            "first_comment," +
            "second_comment," +
            "flag," +
            "create_time) values " +
            "(#{from_user_id}," +
            "#{to_user_id}," +
            "#{type}," +
            "#{content_id}," +
            "#{content_url}," +
            "#{first_comment}," +
            "#{second_comment}," +
            "#{flag}," +
            "#{create_time})")
    public void addUserAction(UserAction userAction);

    @Select("select count(*) from user_action where to_user_id = #{arg0}")
    int getUserActionCount(int user_id);
    @Select("select * from user_action where to_user_id = #{arg0} limit #{arg1},#{arg2}")
    public List<UserAction> getUserAction(int user_id,int begin,int end);

    @Delete("delete from user_action where to_user_id = #{arg0} and flag = #{arg1}")
    public void deleteAction(int user_id,int flag);

    @Select("select video_duration from user where id = #{arg0}")
    public Integer getVideoDuration(int user_id);

    @Update("update user set video_duration = #{arg1} + video_duration WHERE id= #{arg0}")
    public void addVideoDuration(int user_id,int duration);

    @Update("update user set province = #{arg1},city = #{arg2},area = #{arg3} WHERE id= #{arg0}")
    public void setUserArea(int user_id,String province,String city,String area);

    @Update("update user set user_introduce = #{arg1} where id = #{arg0}")
    void setUserIntroduce(int user_id,String introduce);

    @Update("update user set type = #{arg1}," +
            "auth_state = 2," +
            "identity_card = #{arg2}," +
            "graduation_card = #{arg3}," +
            "teacher_card = #{arg4}," +
            "achievement = #{arg5}," +
            "license = #{arg6}," +
            "confirmation_letter = #{arg7}," +
            "shop_prove = #{arg8}," +
            "auth_info = #{arg9}" +
            " where id = #{arg0}")
    void setAuthentication(int uesr_id,
                           int type,
                           String identity_card,         //身份证
                           String graduation_card,       //毕业证
                           String teacher_card,          //教师证
                           String achievement,           //成就
                           String license,               //营业执照
                           String confirmation_letter,   //确认书
                           String shop_prove,             //店铺证明
                           String auth_info
    );

    @Insert("insert into user_shop (user_id,shop_url,state,create_time,update_time) values (#{arg0},#{arg1},#{arg2},#{arg3},#{arg4})")
    void addShop(int user_id, String shop_url,int state,int create_time,int update_time);

    @Update("update user_shop set shop_url = #{arg1},update_time = #{arg2},state = 0 where id = #{arg0}")
    void updateShop(int id,String shop_url,int update_time);

    @Select("select id from user_shop where user_id = #{arg0}")
    List<Integer> findShop(int user_id);

    @Select("select * from user_shop where id = #{arg0}")
    UserShop getShopById(int id);

    @Select("select * from user_shop where user_id = #{arg0}")
    List<UserShop> shopList(int user_id);

    @Insert("insert into user_commodity (user_id,commodity_name,commodity_pic,commodity_url,commodity_price,create_time,update_time) values(#{user_id},#{commodity_name},#{commodity_pic},#{commodity_url},#{commodity_price},#{create_time},#{update_time})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void addCommodity(Commodity commodity);

    @Update("update user_commodity set " +
            "commodity_name = {#commodity_name}," +
            "commodity_pic = {#commodity_pic}," +
            "commodity_url = {#commodity_url}," +
            "commodity_price = {#commodity_price}," +
            "state = 0," +
            " where id = {#id}")
    void updateCommodity(Commodity commodity);

    @Update("update user_commodity set state = {#arg1} where id = {#arg0}")
    void updateCommodityState(int id,int state);

    @Select("select * from user_commodity where user_id = #{arg0}")
    List<UserCommodity> commodityList(int user_id);

    @Update("update user_shop set shop_url = #{arg1} where user_id = #{arg0}")
    void updateShopbyUserId(int user_id,String shop_url);
    @Delete("delete  from user_shop where user_id = #{arg0} and id = #{arg1}")
    void deleteShop(int user_id,String shop_id);

    @Select("<script>"
            + "delete from user_commodity WHERE id IN "
            + "<foreach item='item' index='index' collection='iList' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + " and user_id = #{arg1}"
            + "</script>")
    void deleteCommodity(@Param("iList") List<String> iList,int user_id);

    @Select("select * from user_commodity where id = #{arg0}")
    UserCommodity getCommodityById(String commodity_id);

    @Update("update user set head_flag = #{arg1} where id = #{arg0}")
    void updateUserHeadFlag(int user_id,int flag);
    @Update("update user set name_flag = #{arg1} where id = #{arg0}")
    void updateUserNameFlag(int user_id,int flag);
    @Update("update user set user_introduce_flag = #{arg1} where id = #{arg0}")
    void updateUserIntroduceFlag(int user_id,int flag);

    @Update("update user set auth_state = #{arg1} where id = #{arg0}")
    void updateAuthState(int user_id,int flag);

}

