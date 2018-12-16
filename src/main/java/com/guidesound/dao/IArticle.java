package com.guidesound.dao;

import com.guidesound.dto.ArticleDTO;
import com.guidesound.find.ArticleFind;
import com.guidesound.models.*;
import org.apache.ibatis.annotations.*;

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

    @Insert("insert into answerChat (answer_id,first_user_id,first_comment,second_user_id,second_comment,create_time) " +
            "value (#{answer_id},#{first_user_id},#{first_comment},#{second_user_id},#{second_comment},#{create_time})")
    void addAnswerComment(AnswerComment answerComment);
    @Update("update articleAnswer set comment_count = comment_count + 1 where id = #{arg0}")
    void addMainAnswerComment(int article);



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

    @Select("select count(*) from answerChat where answer_id = #{arg0} and deleted = 0")
    int AnswerCommentCount(int article_id);


    @Select("select * from articleChat where article_id = #{arg0} and deleted = 0 order by create_time desc limit #{arg1},#{arg2}")
    List<ArticleComment> getCommentList(int article_id, int begin, int end);

    @Select("select * from answerChat where answer_id = #{arg0} and deleted = 0 order by create_time desc limit #{arg1},#{arg2}")
    List<AnswerComment> getAnswerCommentList(int answer_id, int begin, int end);

    @Select("select content from article where id = #{arg0}")
    String getContentById(int article_id);

    @Select("select content_url from articleAnswer where id = #{arg0}")
    String getAnswerContentById(int answer_id);

    @Insert("insert into article (head,user_id,head_pic1,head_pic2,head_pic3,create_time,type) value (#{arg0},#{arg1},#{arg2},#{arg3},#{arg4},#{arg5},2)")
    void addAsk(String title,int user_id,String pic1,String pic2,String pic3,int create_tile);

    @Insert("insert into articleAnswer (user_id,ask_id,abstract_info,pic1_url,pic2_url,pic3_url,content_url,create_time) value (#{arg0},#{arg1},#{arg2},#{arg3},#{arg4},#{arg5},#{arg6},#{arg7})")
    void addAnswer(int user_id,int ask_id,String t_abstract,
                   String pic1_url,String pic2_url,String pic3_url,
                   String content_url,int create_time);

    @Update("update article set answer_count = answer_count + 1 where id = #{arg0}")
    void addAnswerMainCount(int article_id);

    @Select("select article_id from articleCollection where user_id = #{arg0} and deleted = 0")
    List<Integer> getArticleListByUserId(int user_id);

    @Select("select article_id from articlePraise where user_id = #{arg0} and deleted = 0")
    List<Integer> getPraiseListByUserId(int user_id);

    @Select("select id from articleChatPraise where user_id = #{arg0} and comment_id = #{arg1} and deleted = 0")
    Integer findArticcleCommentPraise(int user_id,int article_id);

    @Insert("insert into articleChatPraise (user_id,comment_id,create_time) value (#{arg0},#{arg1},#{arg2})")
    void praiseArticcleComment(int user_id,int commment_id,int create_time);
    @Update("update articleChat set praise_count = praise_count + 1 where id = #{arg0}")
    void praiseMainArticcleComment(int commment_id);

    @Select("select comment_id from articleChatPraise where user_id = #{arg0} and deleted = 0")
    List<Integer> getPraiseCommentArticle(int user_id);

    @Select("select id from articleAnswerPraise where user_id = #{arg0} and answer_id = #{arg1} and deleted = 0")
    Integer findArticleAnswerPraise(int user_id,int article_id);
    @Insert("insert into articleAnswerPraise (user_id,answer_id,create_time) value (#{arg0},#{arg1},#{arg2})")
    void praiseArticleAnswer(int user_id,int answer_id,int create_time);
    @Update("update articleAnswer set praise_count = praise_count + 1 where id = #{arg0}")
    void praiseMainArticleAnswer(int answer_id);


    @Select("select id from answerChatPraise where user_id = #{arg0} and answer_chat_id = #{arg1} and deleted = 0")
    Integer findAnswerChatPraise(int user_id,int article_id);
    @Insert("insert into answerChatPraise (user_id,answer_chat_id,create_time) value (#{arg0},#{arg1},#{arg2})")
    void praiseAnswerChat(int user_id,int answer_chat_id,int create_time);
    @Update("update answerChat set praise_count = praise_count + 1 where id = #{arg0}")
    void praiseMainAnswerChat(int answer_chat_id);


    @Select("select count(*) from articleAnswer where ask_id = #{arg0}")
    int answerCount(int ask_id);

    @Select("select * from articleAnswer where ask_id = #{arg0} limit #{arg1},#{arg2}")
    List<ArticleAnswer> answerList(int ask_id,int begin,int end);

    @Select("select answer_id from articleAnswerPraise where user_id = #{arg0} and deleted = 0")
    List<Integer> getAnswerPraise(int user_id);

    @Select("select answer_id from articleAnswerCollection where user_id = #{arg0} and deleted = 0")
    List<Integer> getUserCollection(int user_id);

    @Select("select * from article where examine_person = #{arg0} and deleted = 0 and examine_status = 0")
    List<ArticleInfo> getArticleByAdminId(int admin_id);

    @Select("select * from article where examine_person = 0 and deleted = 0 and examine_status = 0 limit 0 ,5")
    List<ArticleInfo> getExamineArticle();

    @Update("update article set examine_status = 1,type_list = #{arg1} where id = #{arg0}")
    void setExamineSuccess(int id, String type_list);

    @Update("update article set examine_status = 2,examine_reason = #{arg1},fail_content = #{arg2} where id = #{arg0}")
    void setExamineFail(int id, String fail_reson, String fail_content);

    @Update("<script>"
            + "update article set examine_person = #{arg1}  WHERE id IN "
            + "<foreach item='item' index='index' collection='iList' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    void setExaminePerson(@Param("iList") List<Integer> iList, int user_id);

    @Select("select count(*) from articleAnswerCollection where user_id = #{arg0} and answer_id = #{arg1}")
    int getAnswerCollection(int user_id,int answer_id);
    @Update("insert into articleAnswerCollection (user_id,answer_id,create_time) value (#{arg0},#{arg1},#{arg2})")
    void collectAnswer(int user_id,int answer_id,int create_time);
}
