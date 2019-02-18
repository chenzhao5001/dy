package com.guidesound.dao;

import com.guidesound.find.VideoFind;
import com.guidesound.models.*;
import org.apache.ibatis.annotations.*;

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

    @Select("select count(*) from video where examine_status = #{arg0} and deleted = 0")
    int getVideoCount(String status);

    @Select("select count(*) from video where user_id = #{arg0} and examine_status = 1 and deleted = 0")
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

    @Select("select * from video where id = #{arg0}")
    VideoShow getVideoById(String id);

    int findVideoCount(VideoFind videoFind);

    @Select("select * from video where examine_pesron = #{arg0} and deleted = 0 and examine_status = 0")
    List<VideoInfo> getVideoByAdminId(int admin_id);

    @Select("select * from video where examine_pesron = 0 and deleted = 0 and examine_status = 0 limit 0 ,5")
    List<VideoInfo> getExamineVideo();

    @Update("update video set examine_status = 3,type_list = #{arg1} where id = #{arg0}")
    void setExamineLoading(int id, String type_list);

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
            + "AND ( pools like CONCAT('%',#{arg1},'%')  or pools like CONCAT('%',#{arg2},'%') or pools like CONCAT('%',#{arg3},'%')) "
            + " and examine_status = 1"
            + " and type_list like '%1%'"
            + "</script>")
    List<VideoShow> getVideoByChannel(@Param("iList") List<String> iList,String grade,String other_grade1,String other_grade2);

    @Insert("insert into videoShare (user_id,video_id,created_time) values (#{arg0},#{arg1},#{arg2})")
    void shareVideo(int user_id, int video_id, int created_time);

    @Update("update video set shared_count = shared_count + 1 where id = #{arg0}")
    void addShareCount(int video_id);

    @Select("select video_id from videoCollection where user_id = #{arg0} and deleted = 0")
    List<Integer> getMyCollectionIds(int user_id);

    @Select("<script>"
            + "SELECT * FROM video WHERE id IN "
            + "<foreach item='item' index='index' collection='iList' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + " and examine_status = 1"
            + " limit #{arg1},#{arg2}"
            + "</script>")
    List<VideoShow> myCollection(@Param("iList") List<Integer> iList,int begin,int end);

    @Select("select count(*) from video where user_id = #{arg0} and examine_status = 1 or examine_status = 5 or examine_status = 6 and deleted = 0")
    int getPublishVidoeCountByUserId(int user_id);
    @Select("select count(*) from video where user_id = #{arg0} and (examine_status = 1 or examine_status = 0 or examine_status = 3 or examine_status = 5 or examine_status = 6) and deleted = 0")
    int getPublishVidoeCountByUserId_2(int user_id);
    @Select("select * from video where user_id = #{arg0} and examine_status = 1 and deleted = 0 limit #{arg1},#{arg2}")
    List<VideoShow> getPublishVidoeByUserId(int user_id,int begin,int end);

    @Select("select * from video where user_id = #{arg0} and (examine_status = 1 or examine_status = 0 or examine_status = 3) and deleted = 0 limit #{arg1},#{arg2}")
    List<VideoShow> getPublishVidoeByUserId_2(int user_id,int begin,int end);

    @Select("<script>"
            + "SELECT * FROM video WHERE 1 = 1"
            + " AND ( pools like CONCAT('%',#{arg0},'%') or pools like CONCAT('%',#{arg1},'%') or pools like CONCAT('%',#{arg2},'%')) "
            + " and examine_status = 1"
            + " and type_list like '%1%'"
            + "</script>")
    List<VideoShow> getRecommendVideo(String grade,String other_grade1,String other_grade2);

//    @Select("")
//    int foo();
    @Select("select count(*) from video where create_time > #{arg0}")
    int getVideoCountByTime(int time);

    @Select("select push_vidoes from userVideoPush where user_guid = #{arg0}")
    List<String> getPushVideoByUserGuid(String user_guid);

    @Select("select count(*) from userVideoPush where user_guid = #{arg0}")
    int getPushVideoCountByUserGuid(String user_guid);

    @Select("select finish_videos from userPlayFinish where user_guid = #{arg0}")
    String getFinishVideoByUserGuid(String user_guid);

    @Delete("delete from userVideoPush where user_guid = #{arg0}")
    void deletePushVideoByUserGuid(String user_guid);

    @Insert("insert into userVideoPush (user_guid,push_vidoes) values (#{arg0},#{arg1})")
    void insertPushVideo(String user_guid,String videos);
    @Update("update userVideoPush set push_vidoes = #{arg1} where user_guid = #{arg0}")
    void updatePushVidoe(String user_guid,String videos);

    @Select("<script>"
            + "update video set rec_count = rec_count +1  WHERE id IN "
            + "<foreach item='item' index='index' collection='iList' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    void addRecommend(@Param("iList") List<Integer> iList);

    @Select("select * from userPlayFinish where user_guid = #{arg0}")
    List<UserPlayFinish> getUserPlayInfo(String user_guid);

    @Update("update userPlayFinish set finish_videos = #{arg1} where id = #{arg0}")
    void upPlayFinish(int id,String videos);

    @Insert("insert into userPlayFinish (user_guid,finish_videos,create_time) values (#{arg0},#{arg1},#{arg2})")
    void createPlayFinish(String user_guid,String finish_videos,int create_time);

    @Select("SELECT * FROM video where user_id in (SELECT id from user WHERE dy_id div 10000 > 1000)")
    List<VideoShow> FooTemp();

    @Select("SELECT *  FROM article WHERE user_id in (SELECT id FROM user where (dy_id div 10000000) != 1)")
    List<Article> FooTemp2();

    @Select("SELECT * from user WHERE dy_id div 10000 > 1000")
    List<UserInfo> FooTemp3();

    @Select("SELECT * FROM articleAnswer where user_id in (9,219,225,149)")
    List<ArticleAnswer> FooTemp4();

    @Delete("delete from video where user_id = #{arg0}")
    void deleteVideoByUser(int user);

    @Update("update video set examine_status = 6 where id = #{arg0}")
    void UpVideo(int video_id);
    @Update("update video set examine_status = 7 where id = #{arg0}")
    void downVideo(int video_id);

    @Insert("insert into videoChat (video_id,first_user_id,first_comment,second_user_id,second_comment,create_time) " +
            "value (#{video_id},#{first_user_id},#{first_comment},#{second_user_id},#{second_comment},#{create_time})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void addComment(VideoComment videoComment);

    @Update("update video set chat_count = chat_count + 1 where id = #{arg0}")
    void addMainComment(int video_id);

    @Select("select count(*) from videoChat where video_id = #{arg0} and deleted = 0")
    int CommentCount(int article_id);

    @Select("select * from videoChat where video_id = #{arg0} and deleted = 0 order by create_time desc limit #{arg1},#{arg2}")
    List<VideoComment> getCommentList(int video_id, int begin, int end);

    @Select("select comment_id from videoChatPraise where user_id = #{arg0} and deleted = 0")
    List<Integer> getPraiseComment(int user_id);

    @Select("select id from videoChatPraise where user_id = #{arg0} and comment_id = #{arg1} and deleted = 0")
    Integer findVideoCommentPraise(int user_id,int article_id);

    @Select("select first_user_id from videoChat where id = #{arg0}")
    String getUserIdByCommentId(int id);

    @Insert("insert into videoChatPraise (user_id,comment_id,create_time) value (#{arg0},#{arg1},#{arg2})")
    void praiseVideoComment(int user_id,int commment_id,int create_time);

    @Update("update videoChat set praise_count = praise_count + 1 where id = #{arg0}")
    void praiseMainVideoComment(int commment_id);

    @Update("update video set pools = #{arg1} where id = #{arg0}")
    void setPoolByVideoId(String video_id,String pools);
}
