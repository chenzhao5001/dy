package com.guidesound.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


public interface IVideoCollection {
    @Insert("Insert into videoCollection (user_id,video_id,create_time,update_time) value (#{arg0},#{arg1},#{arg2},#{arg3})")
    void addCollection(int user_id,int video_id,int create_time,int update_time);
    @Update("update video set collection_count = collection_count + 1  where id = #{arg0}")
    void addMainCollection(int video_id);

    @Update("update videoCollection set deleted = 1 where user_id = #{arg0} and video_id = #{arg1}")
    void deleteCollection(int user_id,int video_id);
    @Update("update video set collection_count = collection_count -1  where id = #{arg0}")
    void deleteMainCollection(int video_id);

    @Select("select count(*) from videoCollection where video_id=#{arg0} and user_id=#{arg1} and deleted = 0")
    int getVideoCollection(int video_id,int user_id);


}
