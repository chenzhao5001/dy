package com.guidesound.dao;

import com.guidesound.models.Video;
import com.guidesound.models.VideoShow;
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

    @Select("select * from video where examine_status = #{arg0} limit #{arg1},#{arg2}")
    List<VideoShow> selectVideo(String status, int begin, int end);

    @Update("update video set video_show_path = #{arg1} where id = #{arg0}")
    void setVideoShowPath(int id,String path);

    @Select("select count(*) from video where examine_status = #{arg0}")
    int getVideoCount(String status);
}
