package com.guidesound.dao;

import com.guidesound.models.Parent;
import org.apache.ibatis.annotations.Insert;

public interface IParent {

    @Insert("insert into parent " +
            "(user_id,grade,create_time,update_time) values (#{user_id},#{grade},#{create_time},#{update_time})")
    void add(Parent parent);

}
