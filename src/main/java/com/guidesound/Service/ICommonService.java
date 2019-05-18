package com.guidesound.Service;

public interface ICommonService {
    //总金额
    public void changeUserSurplusAmount(int user_id, int change_amount);
    //可提现
    public void changeUserAmount(int user_id, int change_amount);

}
