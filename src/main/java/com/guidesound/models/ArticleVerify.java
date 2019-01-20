package com.guidesound.models;

import com.guidesound.TempStruct.ArticleInfoTemp;
import com.guidesound.TempStruct.ArticlePathTemp;
import com.guidesound.TempStruct.UserInfoTemp;

public class ArticleVerify {

    ArticleInfoTemp articleInfoTemp = new ArticleInfoTemp();
    ArticlePathTemp articlePathTemp = new ArticlePathTemp();
    UserInfoTemp userInfoTemp = new UserInfoTemp();
    int article_id;
    int type;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setArticleTitle(String title) { articleInfoTemp.setTitle(title); }
    public void setArticleSubject(String subject) { articleInfoTemp.setSubject(subject); }
    public void setArticleGrade_class(String grade_class) { articleInfoTemp.setGrade_class(grade_class); }
    public void setArticleLength(int length) { articleInfoTemp.setLength(length); }
    public void setArticleXy(String xy) { articleInfoTemp.setXy(xy); }
    public void setArticleAid(int aid) { articleInfoTemp.setAid(aid); }

    public void setShowContent(String content) {
        articlePathTemp.setContent(content);
    }
    public void setShowTitle_pic1(String title_pic1) { articlePathTemp.setTitle_pic1(title_pic1); }
    public void setShowTitle_pic2(String title_pic2) { articlePathTemp.setTitle_pic2(title_pic2); }
    public void setShowTitle_pic3(String title_pic3) { articlePathTemp.setTitle_pic3(title_pic3); }


    public void setUser_type(String type) {
        userInfoTemp.user_type = type;
    }
    public void setUser_extend(String extend) {
        userInfoTemp.user_extend = extend;
    }
    public void setUser_grade_level(String level) {
        userInfoTemp.user_grade_level = level;
    }
    public void setUser_level(String level) {
        userInfoTemp.user_level = level;
    }
    public void setUser_name(String name) {
        userInfoTemp.user_name = name;
    }
    public void setUser_subject(String subject) {
        userInfoTemp.user_subject = subject;
    }
    public void setUser_uid(String uid) { userInfoTemp.setUser_uid(uid); }


    public ArticleInfoTemp getArticleInfoTemp() {
        return articleInfoTemp;
    }

    public void setArticleInfoTemp(ArticleInfoTemp articleInfoTemp) {
        this.articleInfoTemp = articleInfoTemp;
    }

    public ArticlePathTemp getArticlePathTemp() {
        return articlePathTemp;
    }

    public void setArticlePathTemp(ArticlePathTemp articlePathTemp) {
        this.articlePathTemp = articlePathTemp;
    }

    public UserInfoTemp getUserInfoTemp() {
        return userInfoTemp;
    }

    public void setUserInfoTemp(UserInfoTemp userInfoTemp) {
        this.userInfoTemp = userInfoTemp;
    }

    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }
}
