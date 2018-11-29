package com.guidesound.dao;

import com.guidesound.dto.ArticleDTO;
import com.guidesound.find.ArticleFind;
import com.guidesound.models.ArticleInfo;
import com.guidesound.models.ArticleComment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface IArticle {

    @Select("select count(*) from article where user_id = #{arg0}")
    int getCountByUserId(String user_id);

    @Insert("insert into article (user_id,head,head_pic1,head_pic2,head_pic3,content,subject_class,subject,grade_class,grade, create_time)" +
            "value (#{user_id},#{head},#{head_pic1},#{head_pic2},#{head_pic3},#{content},#{subject_class},#{subject},#{grade_class},#{grade},#{create_time})")
    @Options(useGeneratedKeys=true, keyProperty="article_id", keyColumn="id")
    void add(ArticleDTO articleDTO);

    @Update("update article set deleted = 1 where id = #{arg1} and user_id = #{arg0}")
    void delete(int user_id,int article_id);

    @Select("select id from articleCollection where user_id = #{arg0} and article_id = #{arg1} and deleted = 0")
    Integer findCollection(int user_id,int article_id);

    @Insert("insert into articleCollection (user_id,article_id,create_time) value (#{arg0},#{arg1},#{arg2})")
    void collect(int user_id,int article_id,int time);

    @Update("update article set collection_count = collection_count + 1 where id = #{arg0}")
    void addMainCollection(int article_id);

    @Update("update article set collection_count = collection_count - 1 where id = #{arg0}")
    void cancelMainCollection(int article_id);

    @Update("update articleCollection set deleted = 1 where user_id = #{arg0} and article_id = #{arg1}")
    void cancelCollection(int user_id,int article_id);

    @Select("select id from articlePraise where user_id = #{arg0} and article_id = #{arg1} and deleted = 0")
    Integer findPraise(int user_id,int article_id);
    @Update("update article set praise_count = praise_count + 1 where id = #{arg0}")
    void addMainPraise(int article_id);
    @Insert("insert into articlePraise (user_id,article_id,create_time) value (#{arg0},#{arg1},#{arg2})")
    void addPraise(int user_id,int article_id,int time);

    @Insert("insert into articleChat (article_id,first_user_id,first_comment,second_user_id,second_comment,create_time) " +
            "value (#{article_id},#{first_user_id},#{first_comment},#{second_user_id},#{second_comment},#{create_time})")
    void addComment(ArticleComment articleComment);
    @Update("update article set comment_count = comment_count + 1 where id = #{arg0}")
    void addMainComment(int article);

    @Update("update articleChat set deleted = 1 where user_id = #{arg0} and article_id = #{arg1}")
    void deleteComment(int user_id,int article_id);
    @Update("update article set chat_count = chat_count - 1 where id = #{arg0}")
    void reduceMainComment(int article_id);

//    @Select("select count(*) from article where deleted = 0")
    int count(ArticleFind articleFind);
//    @Select("select * from article where deleted = 0 limit #{arg0},#{arg1}")
    List<ArticleInfo> getList(ArticleFind articleFind);

    @Select("select count(*) from article where user_id = #{arg0} and deleted = 0")
    int countByUserID(int user_id);
    @Select("select * from article where user_id = #{arg0} and deleted = 0 limit #{arg1},#{arg2}")
    List<ArticleInfo> getListById(int user_id,int begin,int end);

    @Select("select count(*) from articleChat where article_id = #{arg0} and deleted = 0")
    int CommentCount(int article_id);

    @Select("select * from articleChat where article_id = #{arg0} and deleted = 0 limit #{arg1},#{arg2}")
    List<ArticleComment> getCommentList(int article_id, int begin, int end);

    @Select("select content from article where id = #{arg0}")
    String getContentById(int article_id);

    @Insert("insert into article (head,user_id,head_pic1,head_pic2,head_pic3,create_time,type) value (#{arg0},#{arg1},#{arg2},#{arg3},#{arg4},#{arg5},2)")
    void addAsk(String title,int user_id,String pic1,String pic2,String pic3,int create_tile);

    @Insert("insert into articleAnswer (user_id,ask_id,abstract,pic1_url,pic2_url,pic3_url,content_url,create_time) value (#{arg0},#{arg1},#{arg2},#{arg3},#{arg4},#{arg5},#{arg6},#{arg7})")
    void addAnswer(int user_id,int ask_id,String t_abstract,
                   String pic1_url,String pic2_url,String pic3_url,
                   String content_url,int create_time);

    @Update("update article set answer_count = answer_count + 1 where id = #{arg0}")
    void addAnswerMainCount(int article_id);

    @Select("select article_id from articleCollection where user_id = #{arg0} and deleted = 0")
    List<Integer> getArticleListByUserId(int user_id);

    @Select("select article_id from articlePraise where user_id = #{arg0} and deleted = 0")
    List<Integer> getPraiseListByUserId(int user_id);

}
