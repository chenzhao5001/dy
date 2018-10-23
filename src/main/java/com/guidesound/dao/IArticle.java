package com.guidesound.dao;

import com.guidesound.dto.ArticleDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface IArticle {

    @Select("select count(*) from article where user_id = #{arg0}")
    int getCountByUserId(String user_id);

    @Insert("insert into article (user_id,head,head_pic1,head_pic2,head_pic3,content,create_time)" +
            "value (#{user_id},#{head},#{head_pic1},#{head_pic2},#{head_pic3},#{content},#{create_time})")
    void add(ArticleDTO articleDTO);

    @Update("update article set deleted = 1 where id = #{arg1} and user_id = #{arg0}")
    void delete(int user_id,int article_id);

    @Select("select id from articleCollection where deleted = 0 and user_id = #{arg0} and article_id = #{arg1}")
    Integer findCollection(int user_id,int article_id);

    @Insert("insert into articleCollection (user_id,article_id,create_time}) value (#{arg0},#{arg1},#{arg2})")
    void collect(int user_id,int article_id,int time);

    @Update("update article set collection_count = collection_count + 1 where id = #{arg0}")
    void addMainCollection(int article_id);

    @Update("update article set collection_count = collection_count - 1 where id = #{arg0}")
    void cancelCollection(int article_id);

    @Update("update articleCollection set deleted = 1 where user_id = #{arg0} and #{arg1}")
    void cancelMainCollection(int user_id,int article_id);

    @Select("select id from articlePraise where user_id = #{arg0} and article = #{arg1} and deleted = 0")
    Integer findPraise(int user_id,int article_id);
    @Update("update article set praise_count = praise_count + 1 where id = #{arg0}")
    void addMainPraise(int article_id);
    @Insert("insert into articlePraise (user_id,article_id,create_time) value (#{arg0},#{arg1},#{arg2})")
    void addPraise(int user_id,int article_id,int time);

    void addComment(int user_id,int article,String comment,int time);
    @Update("update article set chat_count = chat_count + 1 where id = #{arg0}")
    void addMainComment(int article);
    void deleteComment(int user_id,int article_id);
    void reduceMainComment(int article_id);
    void deleteCommentById(int article);
    List<Object> getCollectList(int article_id);


    
}
