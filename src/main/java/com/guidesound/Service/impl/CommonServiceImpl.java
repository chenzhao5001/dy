package com.guidesound.Service.impl;

import com.guidesound.Service.ICommonService;
import com.guidesound.dao.IUser;
import com.guidesound.models.UserAmount;
import com.guidesound.models.UserSurplusAmount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommonServiceImpl implements ICommonService {

    @Autowired
    IUser iUser;


    @Override
    public void changeUserSurplusAmount(int user_id, int change_amount) {
        List<UserSurplusAmount> lists =  iUser.getUserSurplusAmount(user_id);
        if(lists.size() == 0) {
            iUser.InsertUserSurplusAmount(user_id,change_amount);
        } else {
            UserSurplusAmount userSurplusAmount = lists.get(0);
            int amount = userSurplusAmount.getAmount() + change_amount;
            iUser.updateUserSurplusAmount(user_id,amount);
        }
    }

    @Override
    public void changeUserAmount(int user_id, int change_amount) {

        List<UserAmount> lists =  iUser.getUserAmount(user_id);
        if(lists.size() == 0) {
            int time = (int) (new Date().getTime() / 1000);
            iUser.InsertUserAmount(user_id,change_amount,time,time);
        } else {
            UserAmount userAmount = lists.get(0);
            int amount = userAmount.getAmount() + change_amount;
            iUser.updateUserAmount(user_id,amount);
        }
    }

    public void updateAliPayUserId(int user_id,String alipay_user_id) {
        String alipay_user_id_temp = iUser.getAliPayUserId(user_id);
        if(alipay_user_id_temp == null || alipay_user_id_temp.equals("")) {
            iUser.setAliPayUserId(user_id,alipay_user_id);
        }

    }


}
