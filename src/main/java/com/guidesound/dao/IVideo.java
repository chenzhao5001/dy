package com.guidesound.dao;

import com.guidesound.find.VideoFind;
import com.guidesound.models.User;
import com.guidesound.models.Video;
import com.guidesound.models.VideoShow;
import com.guidesound.models.VideoInfo;
import org.apache.ibatis.annotations.Insert;
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
    void setVideoStatus(int id, int status);

    @Update("update video set deleted=1 where id=#{arg0} and user_id=#{arg1}")
    void deleteVideo(int id, int userId);

    @Select("select * from video where examine_status = #{arg0} and deleted = 0 limit #{arg1},#{arg2}")
    List<VideoShow> selectVideo(String status, int begin, int end);

    @Select("select * from video where examine_status = #{arg0} and deleted = 0 and content like title limit #{arg1},#{arg2}")
    List<VideoShow> selectVideoByContent(String status, String content, int begin, int end);

    @Select("select * from video where examine_status = #{arg0} and deleted = 0  limit #{arg1},#{arg2} order by praise_count desc")
    List<VideoShow> selectVideoOrderByPraise(String status, int begin, int end);

    @Select("select * from video where examine_status = #{arg0} and deleted = 0  limit #{arg1},#{arg2} order by play_count desc")
    List<VideoShow> selectVideoOrderByPlay(String status, String content, int begin, int end);

    @Select("select * from video where examine_status = #{arg0} and deleted = 0  limit #{arg1},#{arg2} order by create_time desc")
    List<VideoShow> selectVideoOrderByCreatetime(String status, int begin, int end);


    @Select("select * from video where examine_status = #{arg0} and deleted = 0 and #{arg1} like title limit #{arg2},#{arg3} order by praise_count desc")
    List<VideoShow> selectVideoByContentOrderByPraise(String status, String content, int begin, int end);

    @Select("select * from video where examine_status = #{arg0} and deleted = 0 and #{arg1} like title limit #{arg2},#{arg3} order by play_count desc")
    List<VideoShow> selectVideoByContentOrderByPlay(String status, String content, int begin, int end);

    @Select("select * from video where examine_status = #{arg0} and deleted = 0 and #{arg1} like title limit #{arg2},#{arg3} order by create_time desc")
    List<VideoShow> selectVideoByContentOrderByCreatetime(String status, String content, int begin, int end);

    @Update("update video set video_show_path = #{arg1} where id = #{arg0}")
    void setVideoShowPath(int id, String path);

    @Select("select count(*) from video where examine_status = #{arg0}")
    int getVideoCount(String status);

    @Select("select count(*) from video where user_id = #{arg0}")
    int getVideoByUserId(String usre_id);

    @Select("select video_id from videoCollection where user_id = #{arg0} and deleted = 0")
    List<Integer> getCollectionVideoById(int user_id);

    @Select("select video_id from videoPraise where user_id = #{arg0} and deleted = 0")
    List<Integer> getPraiseVideoById(int user_id);

    @Select("<script>"
            + "SELECT * FROM user WHERE id IN "
            + "<foreach item='item' index='index' collection='iList' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    List<User> getUserHeadByIds(@Param("iList") List<Integer> iList);

    List<VideoShow> findVideo(VideoFind videoFind);

    int findVideoCount(VideoFind videoFind);

    @Select("select * from video where examine_pesron = #{arg0} and deleted = 0 and examine_status = 0")
    List<VideoInfo> getVideoByAdminId(int admin_id);

    @Select("select * from video where examine_pesron = 0 and deleted = 0 and examine_status = 0 limit 0 ,5")
    List<VideoInfo> getExamineVideo();

    @Update("update video set examine_status = 1,type_list = #{arg1},video_show_path = #{arg2} where id = #{arg0}")
    void setExamineSucess(int id, String type_list,String url);

    @Select("select video_up_path from video where id = #{arg0}")
    String getTempVideoById(int id);

    @Update("update video set examine_status = 2,examine_reason = #{arg1},fail_content = #{arg2} where id = #{arg0}")
    void setExamineFail(int id, String fail_reson, String fail_content);

    @Update("<script>"
            + "update video set examine_pesron = #{arg1}  WHERE id IN "
            + "<foreach item='item' index='index' collection='iList' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    void setExaminePerson(@Param("iList") List<Integer> iList, int user_id);

    @Select("<script>"
            + "SELECT count(*) FROM video WHERE subject IN "
            + "<foreach item='item' index='index' collection='iList' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    int getVideoNumByChannel(@Param("iList") List<String> iList);


    @Select("<script>"
            + "SELECT * FROM video WHERE subject IN "
            + "<foreach item='item' index='index' collection='iList' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + " limit #{arg1},#{arg2}"
            + "</script>")
    List<VideoShow> getVideoByChannel(@Param("iList") List<String> iList, int begin, int end);

    @Insert("insert into videoShare (user_id,video_id,created_time) values (#{arg0},#{arg1},#{arg2})")
    void shareVideo(int user_id, int video_id, int created_time);

    @Update("update video set shared_count = shared_count + 1 where id = #{arg0}")
    void addShareCount(int video_id);

    @Select("select id from videoCollection where user_id = #{arg0} and deleted = 0")
    List<Integer> getMyCollectionIds(int user_id);

    @Select("<script>"
            + "SELECT * FROM video WHERE id IN "
            + "<foreach item='item' index='index' collection='iList' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + " limit #{arg1},#{arg2}"
            + "</script>")
    List<VideoShow> myCollection(@Param("iList") List<Integer> iList,int begin,int end);

    @Select("select count(*) from video where user_id = #{arg0} and deleted = 0")
    int getPublishVidoeCountByUserId(int user_id);
    @Select("select * from video where user_id = #{arg0} and deleted = 0 limit #{arg1},#{arg2}")
    List<VideoShow> getPublishVidoeByUserId(int user_id,int begin,int end);

}
