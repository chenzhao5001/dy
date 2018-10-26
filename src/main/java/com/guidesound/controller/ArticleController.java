package com.guidesound.controller;

import com.guidesound.dao.IArticle;
import com.guidesound.dto.ArticleDTO;
import com.guidesound.models.ArticleInfo;
import com.guidesound.resp.ListResp;
import com.guidesound.util.JSONResult;
import com.guidesound.util.ToolsFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.tools.Tool;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/article")
public class ArticleController extends BaseController {

    @Autowired
    private IArticle iArticle;

    @RequestMapping(value = "/add")
    @ResponseBody
    JSONResult add(@Valid ArticleDTO articleDTO, BindingResult result,HttpServletRequest request) throws IOException {
        StringBuilder msg = new StringBuilder();
        if(!ToolsFunction.paramCheck(result,msg)) {
            return JSONResult.errorMsg(msg.toString());
        }
        if(articleDTO.getHead_pic2() == null) {
            articleDTO.setHead_pic2("");
        }
        if(articleDTO.getHead_pic3() == null) {
            articleDTO.setHead_pic3("");
        }

        String urlContent = ToolsFunction.upTextToServicer(articleDTO.getContent());
        articleDTO.setContent(urlContent);
        articleDTO.setUser_id(currentUser.getId());
        articleDTO.setCreate_time((int)(new Date().getTime() / 1000));
        iArticle.add(articleDTO);

        String contextpath = request.getScheme() +"://" + request.getServerName() + ":" +request.getServerPort();
        contextpath += "/article/preview?article_id=";
        contextpath += articleDTO.getArticle_id();
        return JSONResult.ok(contextpath);
    }

    @RequestMapping("/delete")
    @ResponseBody
    JSONResult delete(String article_id) {
        if(article_id == null) {
            return JSONResult.errorMsg("缺少参数 article_id");
        }
        iArticle.delete(currentUser.getId(),Integer.parseInt(article_id));
        return JSONResult.ok();
    }

    /**
    /*收藏文章
     */
    @RequestMapping("/collect")
    @ResponseBody
    JSONResult collect(String article_id) {
        if(article_id == null ) {
            return JSONResult.errorMsg("缺少参数article_id");
        }
        if(null == iArticle.findCollection(currentUser.getId(),Integer.parseInt(article_id))) {
            iArticle.addMainCollection(Integer.parseInt(article_id));
            iArticle.collect(currentUser.getId(),Integer.parseInt(article_id),
                    (int)(new Date().getTime() / 1000));
        }
        return JSONResult.ok();
    }

    /**
     *取消搜藏
     */
    @RequestMapping("/cancel_collect")
    @ResponseBody
    JSONResult cancelCollect(String article_id) {
        if(article_id == null) {
            return JSONResult.errorMsg("缺少article_id");
        }
        if(null != iArticle.findCollection(currentUser.getId(),Integer.parseInt(article_id))) {
            iArticle.cancelCollection(currentUser.getId(),Integer.parseInt(article_id));
            iArticle.cancelMainCollection(Integer.parseInt(article_id));
        }
        return JSONResult.ok();
    }

    /**
     *给文章点赞
     */

    @RequestMapping("/praise")
    @ResponseBody
    JSONResult praise(String article_id) {
        if (article_id == null) {
            JSONResult.errorMsg("缺少参数 article_id");
        }

        if(null == iArticle.findPraise(currentUser.getId(),Integer.parseInt(article_id))) {
            iArticle.addPraise(currentUser.getId(),Integer.parseInt(article_id),(int)(new Date().getTime() /1000));
            iArticle.addMainPraise(Integer.parseInt(article_id));
        }
        return JSONResult.ok();
    }

    /**
     * 评论文章
     */
    @RequestMapping("/comment")
    @ResponseBody
    JSONResult Comment(String article_id,String comment) {
        if(article_id == null || comment == null) {
            return JSONResult.errorMsg("缺少参数");
        }
        iArticle.addComment(currentUser.getId()
                ,Integer.parseInt(article_id)
                ,comment
                ,(int)(new Date().getTime() / 1000));

        iArticle.addMainComment(Integer.parseInt(article_id));
        return JSONResult.ok();
    }

    /**
     *获得文章列表
     */
    @RequestMapping("/list")
    @ResponseBody
    JSONResult getList(String page,String size) {
        int iPage = page == null ?0:Integer.parseInt(page);
        int iSize = size == null ?0:Integer.parseInt(size);

        int begin = (iPage - 1)*iSize;
        int end = (iPage - 1)*iSize + iSize;
        int count = iArticle.count();
        List<ArticleInfo> list = null;
        if(count > 0) {
            list = iArticle.getList(begin,end);
        }
        ListResp listResp = new ListResp();
        listResp.setCount(count);
        listResp.setList(list);
        return JSONResult.ok(listResp);
    }

    /**
     *获得文章列表2
     */
    @RequestMapping("/list_by_userid")
    @ResponseBody
    JSONResult getListByUserId(String user_id,String page,String size) {
        if (user_id == null) {
            return JSONResult.errorMsg("user_id");
        }

        int iPage = page == null ?0:Integer.parseInt(page);
        int iSize = size == null ?0:Integer.parseInt(size);
        int begin = (iPage - 1)*iSize;
        int end = (iPage - 1)*iSize + 20;

        int count = iArticle.countByUserID(currentUser.getId());
        Object list = null;
        if(count > 0) {
            list =iArticle.getListById(currentUser.getId(),begin,end);
        }

        ListResp listResp = new ListResp();
        listResp.setCount(count);
        listResp.setList(list);
        return JSONResult.ok(listResp);
    }



    /**
     *获得文章详情
     */
    JSONResult getContent(String articel_id) {

        return null;
    }


    /**
     *获得评论列表
     */
    @RequestMapping("/comment_list")
    @ResponseBody
    JSONResult getCommentList(String article_id,String page,String size) {

        if (article_id == null) {
            return JSONResult.errorMsg("article_id");
        }

        int iPage = page == null ?0:Integer.parseInt(page);
        int iSize = size == null ?0:Integer.parseInt(size);
        int begin = (iPage - 1)*iSize;
        int end = (iPage - 1)*iSize + iSize;

        int count = iArticle.CommentCount(Integer.parseInt(article_id));
        Object list = null;
        if(count > 0) {
            list = iArticle.getCollectList(Integer.parseInt(article_id),begin,end);
        }

        ListResp listResp = new ListResp();
        listResp.setCount(count);
        listResp.setList(list);
        return JSONResult.ok(listResp);
    }


    @RequestMapping("/edit")
    public String articleEdit(HttpServletRequest request, HttpServletResponse response) {
        //种cookie
        Cookie cookie = new Cookie("token","Z3VpZGVfc291bmQ6MTk=");//创建新cookie
        cookie.setPath("/");//设置作用域
        response.addCookie(cookie);//将cookie添加到response的cookie数组中返回给客户端
        return "edit";
    }

    @RequestMapping("/preview")
    public String articlePreview(String article_id, ModelMap mode) {
        String content = iArticle.getContentById(Integer.parseInt(article_id));
        mode.addAttribute("content", ToolsFunction.httpGet(content));
        return "preview";
    }
}
