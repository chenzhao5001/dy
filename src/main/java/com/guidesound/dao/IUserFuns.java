package com.guidesound.dao;

import com.guidesound.models.UserFuns;
import java.util.List;

public interface IUserFuns {
    public int getUserFunsNum(int userId);
    public int addUserFuns(UserFuns userFuns);
    public int deleteUserFuns(int userId,int funsId);
    public List<UserFuns> getUserFunsInfo(int userId,int funsId);
}
