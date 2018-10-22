package com.guidesound.dao;

import com.guidesound.models.PraiseCount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface IVideoPraise {

    @Insert("Insert into videoPraise (user_id,video_id,create_time,update_time) value (#{arg0},#{arg1},#{arg2},#{arg3})")
    void addPraise(int user_id,int video_id,int create_time,int update_time);
    @Update("update video set praise_count = praise_count + 1 where id = #{video_id}")
    void addMainPraise(int video_id);

    @Select("select count(*) from videoPraise where video_id=#{arg0} and user_id=#{arg1}")
    int getVideoPraise(int video_id,int user_id);

    @Select("<script>"
            + "SELECT video_id,count(*) as count FROM videoPraise WHERE video_id IN "
            + "<foreach item='item' index='index' collection='iList' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "GROUP BY video_id"
            + "</script>")
    public List<PraiseCount> praiseCount(@Param("iList") List<Integer> iList);
}
