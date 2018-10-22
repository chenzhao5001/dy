package com.guidesound.dao;

import com.guidesound.models.PlayCount;
import org.apache.ibatis.annotations.*;

import java.util.List;


public interface IVideoPlay {

    @Insert("Insert into videoPlay (user_id,video_id,create_time,update_time) value (#{arg0},#{arg1},#{arg2},#{arg3})")
    void addPlay(int user_id,int video_id,int create_time,int update_time);
    @Update("update video set play_count = play_count + 1 where id = #{video_id}")
    void addMainPlay(int video_id);


    @Select("<script>"
            + "SELECT video_id,count(*) as count FROM videoPlay WHERE video_id IN "
            + "<foreach item='item' index='index' collection='iList' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "GROUP BY video_id"
            + "</script>")
    public List<PlayCount> playCount(@Param("iList") List<Integer> iList);

}
