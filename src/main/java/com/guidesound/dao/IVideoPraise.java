package com.guidesound.dao;

import com.guidesound.models.PraiseCount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IVideoPraise {

    @Insert("Insert into videoPraise (user_id,video_id,create_time,update_time) value (#{arg0},#{arg1},#{arg2},#{arg3})")
    void addPraise(int user_id,int video_id,int create_time,int update_time);

    @Select("<script>"
            + "SELECT video_id,count(*) as count FROM videoPraise WHERE video_id IN "
            + "<foreach item='item' index='index' collection='iList' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "GROUP BY video_id"
            + "</script>")
    public List<PraiseCount> praiseCount(@Param("iList") List<Integer> iList);
}
