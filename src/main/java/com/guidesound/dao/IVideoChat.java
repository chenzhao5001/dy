package com.guidesound.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

public interface IVideoChat {

    @Insert("insert videoChat (user_id,video_id,chat_content,create_time)" +
            " value (#{arg0},#{arg1},#{arg2},#{arg3})")
    void chatVideo(int user_id,int video_id,String content,int time);

    @Update("update video set chat_count = chat_count + 1 where id = #{video_id}")
    void chatMainVideo(int video_id);
}
