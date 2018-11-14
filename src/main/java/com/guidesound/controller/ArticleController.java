package com.guidesound.controller;

import com.guidesound.dao.IArticle;
import com.guidesound.dao.IUser;
import com.guidesound.dto.ArticleDTO;
import com.guidesound.models.ArticleInfo;
import com.guidesound.models.User;
import com.guidesound.resp.ListResp;
import com.guidesound.util.JSONResult;
import com.guidesound.util.SignMap;
import com.guidesound.util.TockenUtil;
import com.guidesound.util.ToolsFunction;
import org.omg.CORBA.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/article")
public class ArticleController extends BaseController {

    @Autowired
    private IArticle iArticle;
    @Autowired
    private IUser iUser;

    @RequestMapping(value = "/add")
    @ResponseBody
    JSONResult add(@Valid ArticleDTO articleDTO, BindingResult result,HttpServletRequest request) throws IOException {
        StringBuilder msg = new StringBuilder();
        if(!ToolsFunction.paramCheck(result,msg)) {
            return JSONResult.errorMsg(msg.toString());
        }

        User currentUser = (User)request.getAttribute("user_info");
        String urlContent = ToolsFunction.upTextToServicer(articleDTO.getContent());
        articleDTO.setContent(urlContent);
        articleDTO.setUser_id(currentUser.getId());
        articleDTO.setCreate_time((int)(new Date().getTime() / 1000));
        iArticle.add(articleDTO);

//        String contextpath = request.getScheme() +"://" + request.getServerName() + ":" +request.getServerPort();
//        contextpath += "/article/preview?article_id=";
//        contextpath += articleDTO.getArticle_id();
        return JSONResult.ok("/article/finish");
    }

    @RequestMapping("/finish")
    String finish(HttpServletRequest request,ModelMap mode) {

        User currentUser = (User)request.getAttribute("user_info");
        Map info = new HashMap<String,Object>();
        info.put("user",currentUser);
        info.put("subject", SignMap.getSubjectClassifyList());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        info.put("created_time", sdf.format(new Date(Long.valueOf(currentUser.getCreate_time()+"000"))));
        mode.addAllAttributes(info);
        return "edit_success";
    }

    @RequestMapping("/delete")
    @ResponseBody
    JSONResult delete(HttpServletRequest request,String article_id) {
        if(article_id == null) {
            return JSONResult.errorMsg("缺少参数 article_id");
        }
        User currentUser = (User)request.getAttribute("user_info");
        iArticle.delete(currentUser.getId(),Integer.parseInt(article_id));
        return JSONResult.ok();
    }

    /**
    /*收藏文章
     */
    @RequestMapping("/collect")
    @ResponseBody
    JSONResult collect(HttpServletRequest request,String article_id) {
        if(article_id == null ) {
            return JSONResult.errorMsg("缺少参数article_id");
        }
        User currentUser = (User)request.getAttribute("user_info");
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
    JSONResult cancelCollect(HttpServletRequest request,String article_id) {
        if(article_id == null) {
            return JSONResult.errorMsg("缺少article_id");
        }
        User currentUser = (User)request.getAttribute("user_info");
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
    JSONResult praise(HttpServletRequest request,String article_id) {
        if (article_id == null) {
            JSONResult.errorMsg("缺少参数 article_id");
        }

        User currentUser = (User)request.getAttribute("user_info");
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
    JSONResult Comment(HttpServletRequest request,String article_id,String comment) {
        if(article_id == null || comment == null) {
            return JSONResult.errorMsg("缺少参数");
        }
        User currentUser = (User)request.getAttribute("user_info");
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
    JSONResult getListByUserId(HttpServletRequest request,String user_id,String page,String size) {
        if (user_id == null) {
            return JSONResult.errorMsg("user_id");
        }

        int iPage = page == null ?0:Integer.parseInt(page);
        int iSize = size == null ?0:Integer.parseInt(size);
        int begin = (iPage - 1)*iSize;
        int end = (iPage - 1)*iSize + 20;

        User currentUser = (User)request.getAttribute("user_info");
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
    public String articleEdit(HttpServletRequest request, HttpServletResponse response, ModelMap mode) {
        //种cookie
        User user = iUser.getUser(1);
        String token = TockenUtil.makeTocken(user.getId());
        Cookie cookie = new Cookie("token",token);//创建新cookie
        cookie.setPath("/");//设置作用域



        Map info = new HashMap<String,Object>();
        info.put("user",user);
        info.put("subject", SignMap.getSubjectClassifyList());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        info.put("created_time", sdf.format(new Date(Long.valueOf(user.getCreate_time()+"000"))));

        mode.addAllAttributes(info);
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
