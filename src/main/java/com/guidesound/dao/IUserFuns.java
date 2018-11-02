package com.guidesound.dao;

import com.guidesound.models.UserFuns;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IUserFuns {
    public int getUserFunsNum(int userId);
    public int addUserFuns(UserFuns userFuns);
    public int deleteUserFuns(int userId,int funsId);
    public List<UserFuns> getUserFunsInfo(int userId,int funsId);

    @Select("select id from userFuns where user_id = #{arg0} and funs_user_id = #{arg1} and deleted = 0")
    public int getIdByUserAndFunsId(int userId,int funsId);
}
