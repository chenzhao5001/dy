package com.guidesound.Service;

import com.guidesound.dao.IUser;
import org.springframework.beans.factory.annotation.Autowired;

public interface ILogService {
    public void addLog(String user_id,String type_info,String container);

}
