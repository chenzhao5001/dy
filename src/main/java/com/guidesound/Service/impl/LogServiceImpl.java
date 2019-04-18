package com.guidesound.Service.impl;

import com.guidesound.Service.ILogService;
import com.guidesound.dao.IUser;
import com.guidesound.models.LogTmp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class LogServiceImpl implements ILogService {

    @Autowired
    IUser iUser;
    @Override
    public void addLog(String user_id, String type_info, String container) {
        LogTmp logTmp = new LogTmp();
        logTmp.setUser_id(user_id);
        logTmp.setType_info(type_info);
        logTmp.setContainer(container);
        logTmp.setCreate_time((int) (new Date().getTime() / 1000));
        iUser.AddLog(logTmp);
    }
}
