package com.guidesound.dao;

import com.guidesound.models.Video;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface IVideo {
    void addVideo(Video video);
    List<Video> getVideoList(int userId);
    Video getVideo(int id);

    @Update("update video set examine_status = #{arg1} where id=#{arg0}")
    void setVideoStatus(int id,int status);
    @Update("update video set deleted=1 where id=#{arg0} and user_id=#{arg1}")
    void deleteVideo(int id,int userId);

    @Select("select * from video where examine_status = #{arg0}")
    List<Video> selectVideo(int status);

    @Update("update video set video_show_path = #{arg1} where id = #{arg0}")
    void setVideoShowPath(int id,String path);
}
