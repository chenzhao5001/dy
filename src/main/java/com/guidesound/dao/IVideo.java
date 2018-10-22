package com.guidesound.dao;

import com.guidesound.models.User;
import com.guidesound.models.Video;
import com.guidesound.models.VideoShow;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface IVideo {
    void addVideo(Video video);
    List<Video> getVideoList(int userId);
    Video getVideo(int id);

    @Update("update video set examine_status = #{arg1} where id=#{arg0}")
    void setVideoStatus(int id,int status);
    @Update("update video set deleted=1 where id=#{arg0} and user_id=#{arg1}")
    void deleteVideo(int id,int userId);

    @Select("select * from video where examine_status = #{arg0} and deleted = 0 limit #{arg1},#{arg2}")
    List<VideoShow> selectVideo(String status, int begin, int end);

    @Update("update video set video_show_path = #{arg1} where id = #{arg0}")
    void setVideoShowPath(int id,String path);

    @Select("select count(*) from video where examine_status = #{arg0}")
    int getVideoCount(String status);

    @Select("select count(*) from video where user_id = #{arg0}")
    int getVideoByUserId(String usre_id);

    @Select("select video_id from video_collection where user_id = #{arg0}")
    List<Integer> getCollectionVideoById(int user_id);

    @Select("<script>"
            + "SELECT id, head FROM user WHERE id IN "
            + "<foreach item='item' index='index' collection='iList' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    List<User> getUserHeadByIds(@Param("iList") List<Integer> iList);

}
