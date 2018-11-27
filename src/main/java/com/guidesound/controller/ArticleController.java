package com.guidesound.controller;

import com.guidesound.dao.IArticle;
import com.guidesound.dao.IUser;
import com.guidesound.dto.ArticleDTO;
import com.guidesound.find.ArticleFind;
import com.guidesound.models.ArticleInfo;
import com.guidesound.models.User;
import com.guidesound.models.UserInfo;
import com.guidesound.resp.ListResp;
import com.guidesound.util.JSONResult;
import com.guidesound.util.SignMap;
import com.guidesound.util.TockenUtil;
import com.guidesound.util.ToolsFunction;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

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
//        String urlContent = ToolsFunction.upTextToServicer(articleDTO.getContent());

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss_");//设置日期格式
        String strDate = df.format(new Date());// new Date()为获取当前系统时间

        String savePath = request.getServletContext().getRealPath("");
        System.out.println(savePath);
        File file = new File(savePath);

        savePath = file.getParent() + "/file";
        File filePath = new File(savePath);
        if (!filePath.exists() && !filePath.isDirectory()) {
            filePath.mkdir();
        }

        String randStr = ToolsFunction.getRandomString(8);

        String pathFile = savePath + "/" + strDate + randStr;

        FileOutputStream fos = new FileOutputStream(pathFile);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        osw.write(articleDTO.getContent());
        osw.flush();
        fos.close();

        File localFile = new File(pathFile);

        String key = strDate + randStr;
        PutObjectRequest putObjectRequest = new PutObjectRequest(articleBucketName, key, localFile);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        localFile.delete();
        String url = "http://" + articleBucketName + ".cos." + region + ".myqcloud.com" + "/" + key;
        articleDTO.setContent(url);
        articleDTO.setUser_id(currentUser.getId());
        articleDTO.setCreate_time((int)(new Date().getTime() / 1000));
        iArticle.add(articleDTO);

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
    JSONResult getList(String page,String size,String subject,String head) {

        int iPage = (page == null || page.equals(""))  ? 1:Integer.parseInt(page);
        int iSize = (size == null || size.equals(""))  ? 20:Integer.parseInt(size);

        int begin = (iPage - 1)*iSize;
        int end = iSize;

        ArticleFind articleFind = new ArticleFind();
        articleFind.setHead(head);
        articleFind.setSubject(subject);
        articleFind.setBegin(begin);
        articleFind.setEnd(end);

        int count = iArticle.count(articleFind);
        List<ArticleInfo> list = new ArrayList<>();
        if(count > 0) {
            list = iArticle.getList(articleFind);
            getExtendInfo(list);
        }

        ListResp listResp = new ListResp();
        listResp.setCount(count);
        listResp.setList(list);
        return JSONResult.ok(listResp);
    }

    void getExtendInfo(List<ArticleInfo> list) {

        if(list.size()  < 1) {
            return;
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        int currentUserID = 0;
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    currentUserID = TockenUtil.getUserIdByTocket(token);
                    break;
                }
            }
        }

        List<Integer> user_ids = new ArrayList<>();
        for (ArticleInfo item : list) {
            item.setContent_url(request.getScheme() +"://" + request.getServerName()  + ":"
                    + request.getServerPort() + "/article/preview?article_id=" + item.getId());
            if(!user_ids.contains(item.getUser_id())) {
                user_ids.add(item.getUser_id());
            }
        }

        if(user_ids.size() > 0) {
            List<UserInfo> userList = iUser.getUserByIds(user_ids);
            Map<Integer,UserInfo> userMap = new HashMap<>();
            for (UserInfo user : userList) {
                userMap.put(user.getId(),user);
            }
            for(ArticleInfo item:list) {
                if(userMap.containsKey(item.getUser_id())) {
                    item.setUser_head(userMap.get(item.getUser_id()).getHead());
                    item.setUser_name(userMap.get(item.getUser_id()).getName());
                    item.setUser_type(userMap.get(item.getUser_id()).getType());
                    item.setUser_subject(userMap.get(item.getUser_id()).getSubject());
                    item.setUser_subject_name(SignMap.getSubjectTypeById(userMap.get(item.getUser_id()).getSubject()));
                    item.setUser_grade(userMap.get(item.getUser_id()).getGrade());
                    item.setUser_grade_name(SignMap.getGradeTypeByID(userMap.get(item.getUser_id()).getGrade()));

                    item.setUser_grade_level(userMap.get(item.getUser_id()).getGrade_level());
                    item.setUser_grade_level_name(SignMap.getWatchById(userMap.get(item.getUser_id()).getGrade_level()));
                }
            }
        }

        return;
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
        UserInfo user = iUser.getUser(1);
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
    public String articlePreview(String article_id, ModelMap mode) throws IOException {
        if(article_id == null) {
            mode.addAttribute("content","文章不存在");
            return "preview";
        }
        String url = iArticle.getContentById(Integer.parseInt(article_id));
        Request request=new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        String content = response.body().string();
        mode.addAttribute("content",content);
        return "preview";
    }



}
