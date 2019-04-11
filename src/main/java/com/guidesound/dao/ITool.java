package com.guidesound.dao;

import com.guidesound.models.AppVersion;
import org.apache.ibatis.annotations.Select;

public interface ITool {

    @Select("select * from app_version where id = 1")
    AppVersion getVersion();
}
