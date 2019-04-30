package com.guidesound.controller;

import com.guidesound.dao.*;
import com.guidesound.dto.ArticleDTO;
import com.guidesound.find.ArticleFind;
import com.guidesound.models.*;
import com.guidesound.resp.ListResp;
import com.guidesound.ret.CommodityInfo;
import com.guidesound.ret.TeacherClass;
import com.guidesound.ret.VideoClass;
import com.guidesound.util.*;
import com.qcloud.Common.Sign;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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

import static com.guidesound.util.ToolsFunction.*;

@Controller
@RequestMapping("/article")
public class ArticleController extends BaseController {

    @Autowired
    private IArticle iArticle;
    @Autowired
    private IUser iUser;

    @Autowired
    private IRecord iRecord;

    @Autowired
    private ICourse iCourse;

    @RequestMapping(value = "/add")
    @ResponseBody
    JSONResult add(@Valid ArticleDTO articleDTO, BindingResult result, HttpServletRequest request) throws IOException {
        StringBuilder msg = new StringBuilder();
        if (!ToolsFunction.paramCheck(result, msg)) {
            return JSONResult.errorMsg(msg.toString());
        }

        articleDTO.head_pic1 = articleDTO.head_pic1 == null ? "" : articleDTO.head_pic1;
        articleDTO.head_pic2 = articleDTO.head_pic2 == null ? "" : articleDTO.head_pic2;
        articleDTO.head_pic3 = articleDTO.head_pic3 == null ? "" : articleDTO.head_pic3;
        articleDTO.grade_class = articleDTO.grade_class == null ? "0" : articleDTO.grade_class;
        articleDTO.subject_class = articleDTO.subject_class == null ? "0" : articleDTO.subject_class;


        articleDTO.attachment_type = articleDTO.attachment_type == null ? 0 : articleDTO.attachment_type;
        articleDTO.attachment_id = articleDTO.attachment_id == null ? 0 : articleDTO.attachment_id;
        articleDTO.attachment_name = articleDTO.attachment_name == null ? "" : articleDTO.attachment_name;
        articleDTO.attachment_subtype = articleDTO.attachment_subtype == null ? 0 : articleDTO.attachment_subtype;


        User currentUser = (User) request.getAttribute("user_info");
        articleDTO.setSubject(String.valueOf(articleDTO.getSubject()));
        articleDTO.setContent(upStringToCloud(articleDTO.getContent()));
        articleDTO.setUser_id(currentUser.getId());
        articleDTO.setCreate_time((int) (new Date().getTime() / 1000));
        iArticle.add(articleDTO);

        return JSONResult.ok();
    }

    @RequestMapping("/finish")
    String finish(HttpServletRequest request, ModelMap mode) {

        User currentUser = (User) request.getAttribute("user_info");
        Map info = new HashMap<String, Object>();
        info.put("user", currentUser);
        info.put("subject", SignMap.getSubjectClassifyList());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        info.put("created_time", sdf.format(new Date(Long.valueOf(currentUser.getCreate_time() + "000"))));
        mode.addAllAttributes(info);
        return "edit_success";
    }

    @RequestMapping("/delete")
    @ResponseBody
    JSONResult delete(HttpServletRequest request, String article_id) {
        if (article_id == null) {
            return JSONResult.errorMsg("缺少参数 article_id");
        }
        User currentUser = (User) request.getAttribute("user_info");
        iArticle.delete(currentUser.getId(), Integer.parseInt(article_id));
        return JSONResult.ok();
    }

    /**
     * /*收藏文章
     */
    @RequestMapping("/collect")
    @ResponseBody
    JSONResult collect(HttpServletRequest request, String article_id) {
        if (article_id == null) {
            return JSONResult.errorMsg("缺少参数article_id");
        }
        User currentUser = (User) request.getAttribute("user_info");
        if (null == iArticle.findCollection(currentUser.getId(), Integer.parseInt(article_id))) {
            iArticle.addMainCollection(Integer.parseInt(article_id));
            iArticle.collect(currentUser.getId(), Integer.parseInt(article_id),
                    (int) (new Date().getTime() / 1000));
        } else {
            return JSONResult.errorMsg("此文章已经收藏过了");
        }
        return JSONResult.ok();
    }

    @RequestMapping("/collect_list")
    @ResponseBody
    JSONResult getCollectByUserId(String page, String size) {

        int iPage = (page == null || page.equals("")) ? 1 : Integer.parseInt(page);
        int iSize = (size == null || size.equals("")) ? 20 : Integer.parseInt(size);
        int begin = (iPage - 1) * iSize;
        int end = iSize;
        int count = iArticle.getCollectCountByUserid(getCurrentUserId());
        List<ArticleInfo> list = new ArrayList<>();
        if (count > 0) {
            List<Integer> ids = iArticle.getCollectIdsByUserid(getCurrentUserId(), begin, end);
            if (ids.size() > 0) {
                list = iArticle.getArticleByid(ids);
                getExtendInfo(list);
            }
        }

        ListResp listResp = new ListResp();
        listResp.setCount(count);
        listResp.setList(list);

        return JSONResult.ok(listResp);
    }

    /**
     * 取消搜藏
     */
    @RequestMapping("/cancel_collect")
    @ResponseBody
    JSONResult cancelCollect(HttpServletRequest request, String article_id) {
        if (article_id == null) {
            return JSONResult.errorMsg("缺少article_id");
        }
        User currentUser = (User) request.getAttribute("user_info");
        if (null != iArticle.findCollection(currentUser.getId(), Integer.parseInt(article_id))) {
            iArticle.cancelCollection(currentUser.getId(), Integer.parseInt(article_id));
            iArticle.cancelMainCollection(Integer.parseInt(article_id));
        }
        return JSONResult.ok();
    }

    /**
     * 给文章点赞
     */

    @RequestMapping("/praise")
    @ResponseBody
    JSONResult praise(HttpServletRequest request, String article_id) throws IOException {
        if (article_id == null) {
            JSONResult.errorMsg("缺少参数 article_id");
        }

        User currentUser = (User) request.getAttribute("user_info");
        if (null == iArticle.findPraise(currentUser.getId(), Integer.parseInt(article_id))) {
            ArticleInfo articleInfo = iArticle.getArticle(Integer.parseInt(article_id));
            TlsSigTest.PushMessage(String.valueOf(articleInfo.getUser_id()), "2");
            iArticle.addPraise(currentUser.getId(), Integer.parseInt(article_id), (int) (new Date().getTime() / 1000));
            iArticle.addMainPraise(Integer.parseInt(article_id));

            UserAction userAction = new UserAction();
            userAction.setFrom_user_id(currentUser.getId());
            userAction.setTo_user_id(articleInfo.getUser_id());
            userAction.setType(202);
            userAction.setContent_id(Integer.parseInt(article_id));
            userAction.setContent_url(articleInfo.getHead_pic1());
            userAction.setFlag(2);
            userAction.setCreate_time((int) (new Date().getTime() / 1000));
            iUser.addUserAction(userAction);

        } else {
            return JSONResult.errorMsg("已经点过赞");
        }
        return JSONResult.ok();
    }

    /**
     * 评论文章
     */
    @RequestMapping("/comment")
    @ResponseBody
    JSONResult Comment(String article_id, String first_user_id,
                       String first_comment, String second_user_id, String second_comment) throws IOException {
        if (article_id == null || first_user_id == null || first_comment == null) {
            return JSONResult.errorMsg("缺少参数");
        }

        second_user_id = second_user_id == null ? "0" : second_user_id;
        second_comment = second_comment == null ? "" : second_comment;

        ArticleInfo articleInfo = iArticle.getArticle(Integer.parseInt(article_id));
        int to_user_id = 0;
        if (second_user_id.equals("0")) {
            to_user_id = articleInfo.getUser_id();
            TlsSigTest.PushMessage(String.valueOf(to_user_id), "4");
        } else {
            to_user_id = Integer.parseInt(second_user_id);
            TlsSigTest.PushMessage(String.valueOf(to_user_id), "6");
        }

        first_comment = getURLEncoderString(first_comment);
        second_comment = getURLEncoderString(second_comment);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        ArticleComment articleComment = new ArticleComment();
        articleComment.setArticle_id(Integer.parseInt(article_id));
        articleComment.setFirst_user_id(Integer.parseInt(first_user_id));
        articleComment.setFirst_comment(first_comment);
        articleComment.setSecond_user_id(Integer.parseInt(second_user_id));
        articleComment.setSecond_comment(second_comment);
        articleComment.setCreate_time((int) (new Date().getTime() / 1000));

        iArticle.addComment(articleComment);
        iArticle.addMainComment(Integer.parseInt(article_id));
        articleComment.setFirst_comment(URLDecoderString(first_comment));
        articleComment.setSecond_comment(URLDecoderString(second_comment));

        //评论文章
        UserAction userAction = new UserAction();
        userAction.setFrom_user_id(Integer.parseInt(first_user_id));
        if (Integer.parseInt(second_user_id) == 0) {
            userAction.setTo_user_id(articleInfo.getUser_id());
            userAction.setType(201);
        } else {
            userAction.setTo_user_id(Integer.parseInt(second_user_id));
            userAction.setType(204);
        }

        userAction.setContent_id(Integer.parseInt(article_id));
        userAction.setFlag(1);
        userAction.setCreate_time((int) (new Date().getTime() / 1000));

        userAction.setContent_url(articleInfo.getHead_pic1());
        userAction.setFirst_comment(first_comment);
        userAction.setSecond_comment(second_comment);

        iUser.addUserAction(userAction);

        return JSONResult.ok(articleComment);
    }

    /**
     * 获得id获文章
     */
    @RequestMapping("/article_by_id")
    @ResponseBody
    JSONResult getArticleById(String article_id) {
        if (article_id == null) {
            return JSONResult.errorMsg("缺少article_id");
        }

        ArticleInfo articleInfo = iArticle.getArticleById(article_id);
        if (articleInfo == null) {
            return JSONResult.errorMsg("文章不存在");
        }
        List<ArticleInfo> list = new ArrayList<>();
        list.add(articleInfo);
        getExtendInfo(list);

        ArticleInfo retArticleInfo = list.get(0);
        if (retArticleInfo.getAttachment_id() != 0) {
            if (retArticleInfo.getAttachment_type() == 0) { // 商品
                CommodityInfo commodityInfo = iUser.getCommodityInfoByid(retArticleInfo.getAttachment_id());
                if (commodityInfo != null) {
                    retArticleInfo.setCommodity(commodityInfo);
                }

            } else if (retArticleInfo.getAttachment_type() == 1) {  //录播课
                VideoClass videoClass = iRecord.getVideoClass(retArticleInfo.getAttachment_id());
                if (videoClass != null) {
                    videoClass.setSubject_id(Integer.parseInt(videoClass.getSubject()));
                    videoClass.setSubject(SignMap.getSubjectTypeById(Integer.parseInt(videoClass.getSubject())));
                    videoClass.setGrade_id(Integer.parseInt(videoClass.getGrade()));
                    videoClass.setGrade(SignMap.getGradeTypeByID(Integer.parseInt(videoClass.getGrade())));
                }
                retArticleInfo.setVideo_class(videoClass);

            } else if (retArticleInfo.getAttachment_type() == 2) { // 辅导课
                Course course = iCourse.getCouresByid(retArticleInfo.getAttachment_id());
                if (course != null) {
                    TeacherClass teacherClass = new TeacherClass();
                    teacherClass.setCourse_id(course.getId());
                    teacherClass.setCourse_name(course.getCourse_name());
                    teacherClass.setCourse_pic(course.getCourse_pic());
                    teacherClass.setCourse_status(course.getCourse_status());
                    teacherClass.setCourse_type(course.getType());
                    teacherClass.setCourse_type_name(SignMap.getCourseTypeNameById(course.getType()));
                    teacherClass.setForm(SignMap.getCourseFormById(course.getForm()));
                    teacherClass.setGrade(SignMap.getGradeTypeByID(course.getGrade()));
                    teacherClass.setPrice(course.getAll_charge());
                    teacherClass.setStudent_count(course.getMax_person());
                    teacherClass.setSubject(SignMap.getSubjectTypeById(course.getSubject()));
                    retArticleInfo.setTeacher_class(teacherClass);
                }
            }
        }
        return JSONResult.ok(list.get(0));
    }

    /**
     * 获得文章列表
     */
    @RequestMapping("/list")
    @ResponseBody
    JSONResult getList(String page, String size, String subject, String head, String type, String user_id) {
        return JSONResult.errorMsg("接口已经废弃");

//        int iPage = (page == null || page.equals(""))  ? 1:Integer.parseInt(page);
//        int iSize = (size == null || size.equals(""))  ? 20:Integer.parseInt(size);
//
//        int begin = (iPage - 1)*iSize;
//        int end = iSize;
//        subject = subject == null ? "0":subject;
//        type = type == null ? "0":type;
//
//        ArticleFind articleFind = new ArticleFind();
//        articleFind.setHead(head);
//        articleFind.setSubject(Integer.parseInt(subject));
//        articleFind.setBegin(begin);
//        articleFind.setEnd(end);
//        articleFind.setType(Integer.parseInt(type));
//        articleFind.setUser_id(user_id);
//        if(user_id != null && getCurrentUserId() == Integer.parseInt(user_id)) {
//            articleFind.setOwer_flag(true);
//        }
//
//        int count = iArticle.count(articleFind);
//        List<ArticleInfo> list = new ArrayList<>();
//        if(count > 0) {
//            list = iArticle.getList(articleFind);
//            getExtendInfo(list);
//        }
//
//        ListResp listResp = new ListResp();
//        listResp.setCount(count);
//        listResp.setList(list);
//        return JSONResult.ok(listResp);
    }

    void getExtendInfo(List<ArticleInfo> list) {

        if (list.size() < 1) {
            return;
        }
        int currentUserID = getCurrentUserId();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        List<Integer> collectList = new ArrayList<>();
        List<Integer> followList = new ArrayList<>();
        List<Integer> praiseList = new ArrayList<>();
        if (currentUserID != 0) {
            collectList = iArticle.getArticleListByUserId(currentUserID);
            followList = iUser.getFollowUsers(currentUserID);
            praiseList = iArticle.getPraiseListByUserId(currentUserID);
        }


        List<Integer> user_ids = new ArrayList<>();
        for (ArticleInfo item : list) {
            if (collectList.contains(item.getId())) {
                item.setCollection(true);
            }
            if (followList.contains(item.getUser_id())) {
                item.setFollow(true);
            }
            if (praiseList.contains(item.getId())) {
                item.setPraise(true);
            }

            item.setSubject_name(SignMap.getSubjectTypeById(item.getSubject()));
            item.setContent_url(request.getScheme() + "://" + request.getServerName() + ":"
                    + request.getServerPort() + "/guidesound/article/preview?article_id=" + item.getId());
            if (!user_ids.contains(item.getUser_id())) {
                user_ids.add(item.getUser_id());
            }
        }

        if (user_ids.size() > 0) {
            List<UserInfo> userList = iUser.getUserByIds(user_ids);
            Map<Integer, UserInfo> userMap = new HashMap<>();
            for (UserInfo user : userList) {
                userMap.put(user.getId(), user);
            }
            for (ArticleInfo item : list) {
                if (userMap.containsKey(item.getUser_id())) {
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
     * 获得文章列表2
     */
    @RequestMapping("/list_by_userid")
    @ResponseBody
    JSONResult getListByUserId(HttpServletRequest request, String user_id, String page, String size) {
        if (user_id == null) {
            return JSONResult.errorMsg("user_id");
        }

        int iPage = page == null ? 1 : Integer.parseInt(page);
        int iSize = size == null ? 20 : Integer.parseInt(size);
        int begin = (iPage - 1) * iSize;
        int end = iSize;

        User currentUser = (User) request.getAttribute("user_info");
        int count = iArticle.countByUserID(currentUser.getId());
        Object list = null;
        if (count > 0) {
            list = iArticle.getListById(currentUser.getId(), begin, end);
        }

        ListResp listResp = new ListResp();
        listResp.setCount(count);
        listResp.setList(list);
        return JSONResult.ok(listResp);
    }

    /**
     * 获得文章详情
     */
    JSONResult getContent(String articel_id) {

        return null;
    }


    /**
     * 获得评论列表
     */
    @RequestMapping("/comment_list")
    @ResponseBody
    JSONResult getCommentList(String article_id, String page, String size) {

        if (article_id == null) {
            return JSONResult.errorMsg("article_id");
        }

        int iPage = (page == null || page.equals("")) ? 1 : Integer.parseInt(page);
        int iSize = (size == null || size.equals("")) ? 20 : Integer.parseInt(size);
        int begin = (iPage - 1) * iSize;
        int end = iSize;

        int count = iArticle.CommentCount(Integer.parseInt(article_id));

        List<ArticleComment> list = iArticle.getCommentList(Integer.parseInt(article_id), begin, end);
        if (list.size() > 0) {
            List<Integer> user_ids = new ArrayList<>();
            for (ArticleComment articleComment : list) {
                articleComment.setFirst_comment(URLDecoderString(articleComment.getFirst_comment()));
                articleComment.setSecond_comment(URLDecoderString(articleComment.getSecond_comment()));
                if (!user_ids.contains(articleComment.getFirst_user_id())) {
                    user_ids.add(articleComment.getFirst_user_id());
                }
                if (!user_ids.contains(articleComment.getSecond_user_id())) {
                    user_ids.add(articleComment.getSecond_user_id());
                }
            }
            List<UserInfo> user_list = iUser.getUserByIds(user_ids);
            List<Integer> comment_ids = new ArrayList<>();
            int currentUserID = getCurrentUserId();
            if (currentUserID != 0) {
                comment_ids = iArticle.getPraiseCommentArticle(currentUserID);
            }
            Map<Integer, UserInfo> usersMap = new HashMap<>();
            for (UserInfo userInfo : user_list) {
                usersMap.put(userInfo.getId(), userInfo);
            }

            for (ArticleComment articleComment : list) {
                if (comment_ids.contains(articleComment.getId())) {
                    articleComment.setPraise(true);
                }
                if (usersMap.get(articleComment.getFirst_user_id()) != null) {
                    articleComment.setFirst_user_head(usersMap.get(articleComment.getFirst_user_id()).getHead());
                    articleComment.setFirst_user_name(usersMap.get(articleComment.getFirst_user_id()).getName());
                } else {
                    articleComment.setFirst_user_head("");
                    articleComment.setFirst_user_name("");
                }

                if (usersMap.get(articleComment.getSecond_user_id()) != null) {
                    articleComment.setSecond_user_head(usersMap.get(articleComment.getSecond_user_id()).getHead());
                    articleComment.setSecond_user_name(usersMap.get(articleComment.getSecond_user_id()).getName());
                } else {
                    articleComment.setSecond_user_head("");
                    articleComment.setSecond_user_name("");
                }
            }

        }


        ListResp listResp = new ListResp();
        listResp.setCount(count);
        listResp.setList(list);
        return JSONResult.ok(listResp);
    }

    @RequestMapping("/edit")
    public String articleEdit(HttpServletRequest request, HttpServletResponse response, ModelMap mode) {
        //种cookie
//        UserInfo user = iUser.getUser(1);
//        String token = TockenUtil.makeTocken(user.getId());
//        Cookie cookie = new Cookie("token",token);//创建新cookie
//        cookie.setPath("/");//设置作用域

//        Map info = new HashMap<String,Object>();
//        info.put("user",user);
//        info.put("subject", SignMap.getSubjectClassifyList());
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        info.put("created_time", sdf.format(new Date(Long.valueOf(user.getCreate_time()+"000"))));
//
//        mode.addAllAttributes(info);
//        response.addCookie(cookie);//将cookie添加到response的cookie数组中返回给客户端
        return "edit";
    }

    @RequestMapping("/preview")
    public String articlePreview(String article_id, ModelMap mode) throws IOException {
        if (article_id == null) {
            mode.addAttribute("content", "文章不存在");
            return "preview";
        }

        String url = iArticle.getContentById(Integer.parseInt(article_id));
        if(url.equals("")) {
            return "";
        }
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        String content = response.body().string();
        mode.addAttribute("content", content);
        return "preview";
    }

    @RequestMapping("/answer_preview")
    public String answerPreview(String answer_id, ModelMap mode) throws IOException {
        if (answer_id == null) {
            mode.addAttribute("content", "答案不存在");
            return "preview";
        }
        String url = iArticle.getAnswerContentById(Integer.parseInt(answer_id));

        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        String content = response.body().string();
        mode.addAttribute("content", content);
        return "preview";
    }

    @RequestMapping("/add_ask")
    @ResponseBody
    JSONResult addAsk(String title, String pic1_url, String pic2_url, String pic3_url, String subject, String grade) {
        if (title == null || title.equals("")) {
            return JSONResult.errorMsg("缺少参数");
        }
        if (subject == null || grade == null) {
            return JSONResult.errorMsg("缺少 subject 或 grade 参数");
        }

        pic1_url = pic1_url == null ? "" : pic1_url;
        pic2_url = pic2_url == null ? "" : pic2_url;
        pic3_url = pic3_url == null ? "" : pic3_url;
        int time = (int) (new Date().getTime() / 1000);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User) request.getAttribute("user_info");

        iArticle.addAsk(title, currentUser.getId(), pic1_url, pic2_url, pic3_url, time, 2, Integer.parseInt(grade),Integer.parseInt(subject));
        return JSONResult.ok();
    }

    @RequestMapping("/add_answer")
    @ResponseBody
    JSONResult addAnswer(String ask_id, String answer_html,
                         String attachment_type, String attachment_id, String attachment_name, String attachment_subtype) throws IOException {
        if (ask_id == null || answer_html == null) {
            return JSONResult.errorMsg("缺少参数");
        }


        attachment_type = attachment_type == null ? "0" : attachment_type;
        attachment_id = attachment_id == null ? "0" : attachment_id;
        attachment_name = attachment_name == null ? "" : attachment_name;
        attachment_subtype = attachment_subtype == null ? "0" : attachment_subtype;
        Document doc = Jsoup.parse(answer_html);
        String content = "";
        if (doc.text().length() < 100) {
            content = doc.text();
        } else {
            content = doc.text().substring(0, 100);
        }
        String url = upStringToCloud(answer_html);
        List<String> pic_urls = new ArrayList<>();
        Elements imgs = doc.getElementsByTag("img");
        for (Element element : imgs) {
            String imgSrc = element.attr("abs:src");
            pic_urls.add(imgSrc);
        }
        String pic1_url = "";
        String pic2_url = "";
        String pic3_url = "";
        if (pic_urls.size() > 0 && pic_urls.size() < 3) {
            pic1_url = pic_urls.get(0);
        } else if (pic_urls.size() >= 3) {
            pic1_url = pic_urls.get(0);
            pic2_url = pic_urls.get(1);
            pic3_url = pic_urls.get(2);
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User) request.getAttribute("user_info");
        iArticle.addAnswer(currentUser.getId(), Integer.parseInt(ask_id),
                content, pic1_url, pic2_url, pic3_url,
                url, Integer.parseInt(attachment_type), Integer.parseInt(attachment_id), attachment_name, Integer.parseInt(attachment_subtype),
                (int) (new Date().getTime() / 1000));
        iArticle.addAnswerMainCount(Integer.parseInt(ask_id));
        return JSONResult.ok();
    }

    @RequestMapping("collection_answer")
    @ResponseBody
    JSONResult collectionAnswer(String answer_id, String type) {
        if (answer_id == null || type == null) {
            return JSONResult.errorMsg("缺少 answer_id 或 type 参数");
        }

        int user_id = getCurrentUserId();
        if (type.equals("1")) {
            int count = iArticle.getAnswerCollection(user_id, Integer.parseInt(answer_id));
            if (count > 0) {
                return JSONResult.errorMsg("答案已经搜藏");
            }
            iArticle.collectAnswer(user_id, Integer.parseInt(answer_id), (int) (new Date().getTime() / 1000));
        } else {
            iArticle.cancelCollectAnswer(user_id, Integer.parseInt(answer_id));
        }
        return JSONResult.ok();

    }

    @RequestMapping("/answer_list")
    @ResponseBody
    JSONResult getAnswerList(String ask_id, String page, String size) {
        if (ask_id == null) {
            return JSONResult.errorMsg("缺少 ask_id 参数");
        }

        int iPage = page == null || page.equals("") ? 1 : Integer.parseInt(page);
        int iSize = size == null || size.equals("") ? 20 : Integer.parseInt(size);
        int begin = (iPage - 1) * iSize;
        int end = iSize;

        int count = iArticle.answerCount(Integer.parseInt(ask_id));
        List<ArticleAnswer> list = new ArrayList<>();
        if (count > 0) {
            list = iArticle.answerList(Integer.parseInt(ask_id), begin, end);
        }
        getAnswerExtendInfo(list);
        int user_id = getCurrentUserId();
        List<Integer> answerList = new ArrayList<>();
        List<Integer> collectionList = new ArrayList<>();
        List<Integer> followList = new ArrayList<>();
        if (user_id != 0) {
            answerList = iArticle.getAnswerPraise(user_id);
            collectionList = iArticle.getUserCollection(user_id);
            followList = iUser.getFollowUsers(user_id);
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        for (ArticleAnswer item : list) {
            item.setContent_url(request.getScheme() + "://" + request.getServerName() + ":"
                    + request.getServerPort() + "/guidesound/article/answer_preview?answer_id=" + item.getId());
            if (answerList.contains(item.getId())) {
                item.setPraise(true);
            }
            if (collectionList.contains(item.getId())) {
                item.setCollection(true);
            }
            if (followList.contains(item.getUser_id())) {
                item.setFollow(true);
            }
        }

        ListResp listResp = new ListResp();
        listResp.setCount(count);
        listResp.setList(list);

        return JSONResult.ok(listResp);
    }

    void getAnswerExtendInfo(List<ArticleAnswer> list) {
        if (list.size() < 1) {
            return;
        }

        List<Integer> user_ids = new ArrayList<>();
        for (ArticleAnswer item : list) {
            if (!user_ids.contains(item.getUser_id())) {
                user_ids.add(item.getUser_id());
            }
        }
        if (user_ids.size() > 0) {
            List<UserInfo> userList = iUser.getUserByIds(user_ids);
            Map<Integer, UserInfo> userMap = new HashMap<>();
            for (UserInfo user : userList) {
                userMap.put(user.getId(), user);
            }
            for (ArticleAnswer item : list) {
                if (userMap.containsKey(item.getUser_id())) {
                    item.setUser_head(userMap.get(item.getUser_id()).getHead());
                    item.setUser_name(userMap.get(item.getUser_id()).getName());
                    item.setUser_type(userMap.get(item.getUser_id()).getType());
                    item.setUser_grade_level_name(SignMap.getWatchById(userMap.get(item.getUser_id()).getGrade_level()));
                }
            }
        }
        return;
    }

    String upStringToCloud(String content) throws IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss_");
        String strDate = df.format(new Date());

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String savePath = request.getServletContext().getRealPath("");
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
        osw.write(content);
        osw.flush();
        fos.close();
        File localFile = new File(pathFile);
        String key = strDate + randStr;
        PutObjectRequest putObjectRequest = new PutObjectRequest(articleBucketName, key, localFile);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        localFile.delete();
        String url = "http://" + articleBucketName + ".cos." + region + ".myqcloud.com" + "/" + key;
        return url;
    }


    /**
     * 为文章评论点赞
     */
    @RequestMapping("/praise_article_comment")
    @ResponseBody
    JSONResult praiseComment(String comment_id) throws IOException {
        if (comment_id == null) {
            return JSONResult.errorMsg("缺少 comment_id 参数");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User) request.getAttribute("user_info");

        if (null == iArticle.findArticcleCommentPraise(currentUser.getId(), Integer.parseInt(comment_id))) {
            String first_user_id = iArticle.getUserIdByCommentId(Integer.valueOf(comment_id));
            TlsSigTest.PushMessage(first_user_id, "8");
            iArticle.praiseArticcleComment(currentUser.getId(), Integer.parseInt(comment_id), (int) (new Date().getTime() / 1000));
            iArticle.praiseMainArticcleComment(Integer.parseInt(comment_id));

            UserAction userAction = new UserAction();
            userAction.setFrom_user_id(Integer.parseInt(first_user_id));
            userAction.setTo_user_id(currentUser.getId());
            userAction.setType(203);
            userAction.setContent_id(Integer.parseInt(comment_id));
            userAction.setContent_url("");
            userAction.setFlag(2);
            userAction.setCreate_time((int) (new Date().getTime() / 1000));
            iUser.addUserAction(userAction);

        } else {
            return JSONResult.errorMsg("已经点过赞了");
        }
        return JSONResult.ok();
    }

    /**
     * 为文章评论点赞
     */
    @RequestMapping("/praise_answer")
    @ResponseBody
    JSONResult praiseAnswer(String answer_id) {
        if (answer_id == null) {
            return JSONResult.errorMsg("缺少 answer_id 参数");
        }
        int user_id = getCurrentUserId();
        if (null == iArticle.findArticleAnswerPraise(user_id, Integer.parseInt(answer_id))) {
            iArticle.praiseArticleAnswer(user_id, Integer.parseInt(answer_id), (int) (new Date().getTime() / 1000));
            iArticle.praiseMainArticleAnswer(Integer.parseInt(answer_id));
        } else {
            return JSONResult.errorMsg("已经攒过了");
        }
        return JSONResult.ok();
    }


    /**
     * 获取文章频道列表
     */
    @RequestMapping("/article_channel")
    @ResponseBody
    JSONResult articleChannel() {

        int user_id = getCurrentUserId();
        if (user_id == 0) {
            return JSONResult.ok(SignMap.getChannelList(1, true));
        }
        int channel_stage = iUser.getChannelStage(user_id);
        if (channel_stage == 101) {
            return JSONResult.ok(SignMap.getChannelList(101, true));
        } else if (channel_stage == 102) {
            return JSONResult.ok(SignMap.getChannelList(102, true));
        } else {
            return JSONResult.ok(SignMap.getChannelList(channel_stage / 100, true));
        }
    }

    List<Integer> getAllPoolArticle(String user_id, String key, boolean is_new, List<Integer> pools_list) {

        List<Integer> article_ids = new ArrayList<>();
        List<ArticleIndex> videoIndex = iArticle.getArticleIndexCount(user_id, key);
        long nowTime = System.currentTimeMillis();
        int todayStartTime = (int) ((nowTime - (nowTime + TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24)) / 1000);

        if (pools_list.size() == 0) {
            if (videoIndex.size() == 0) {
                if (is_new) {
                    article_ids = iArticle.articleAllIdsInArticlePoolsToday(0, 20, todayStartTime);
                } else {
                    article_ids = iArticle.articleAllIdsInArticlePools(0, 20);
                }
                iArticle.insertArticleIndex(user_id, key, article_ids.size());
            } else {
                if (is_new) {
                    article_ids = iArticle.articleAllIdsInArticlePoolsToday(videoIndex.get(0).getIndex_count(), 20, todayStartTime);
                } else {
                    article_ids = iArticle.articleAllIdsInArticlePools(videoIndex.get(0).getIndex_count(), 20);
                }
                iArticle.updateArticleIndex(user_id, key, videoIndex.get(0).getIndex_count() + article_ids.size());
            }
        } else {
            if (videoIndex.size() == 0) {
                if (is_new) {
                    article_ids = iArticle.articleIdsByPoolsIdsInArticlePoolsToday(pools_list, 0, 20, todayStartTime);
                } else {
                    article_ids = iArticle.articleIdsByPoolsIdsInArticlePools(pools_list, 0, 20);
                }
                iArticle.insertArticleIndex(user_id, key, article_ids.size());
            } else {
                if (is_new) {
                    article_ids = iArticle.articleIdsByPoolsIdsInArticlePoolsToday(pools_list, videoIndex.get(0).getIndex_count(), 20, todayStartTime);
                } else {
                    article_ids = iArticle.articleIdsByPoolsIdsInArticlePools(pools_list, videoIndex.get(0).getIndex_count(), 20);
                }
                iArticle.updateArticleIndex(user_id, key, videoIndex.get(0).getIndex_count() + article_ids.size());
            }
        }
        if (!is_new) {
            if (article_ids.size() < 20) {
                iArticle.updateArticleIndex(user_id, key, 0);
            }
        }
        return article_ids;
    }

    List<Integer> getAllSubjectPoolArticle(String user_id, String key, boolean is_new, List<Integer> pools_list, List<Integer> subject_list) {

        List<Integer> article_ids = new ArrayList<>();
        List<ArticleIndex> articleIndex = iArticle.getArticleIndexCount(user_id, key);
        long nowTime = System.currentTimeMillis();
        int todayStartTime = (int) ((nowTime - (nowTime + TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24)) / 1000);

        if (pools_list.size() == 0) {
            if (articleIndex.size() == 0) {
                if (is_new) {
                    article_ids = iArticle.articleAllIdsInArticlePoolsBySubjectToday(subject_list, 0, 20, todayStartTime);
                } else {
                    article_ids = iArticle.articleAllIdsInArticlePoolsBySubject(subject_list, 0, 20);
                }
                iArticle.insertArticleIndex(user_id, key, article_ids.size());
            } else {
                if (is_new) {
                    article_ids = iArticle.articleAllIdsInArticlePoolsBySubjectToday(subject_list, articleIndex.get(0).getIndex_count(), 20, todayStartTime);
                } else {
                    article_ids = iArticle.articleAllIdsInArticlePoolsBySubject(subject_list, articleIndex.get(0).getIndex_count(), 20);
                }
                iArticle.updateArticleIndex(user_id, key, articleIndex.get(0).getIndex_count() + article_ids.size());
            }
        } else {
            if (articleIndex.size() == 0) {
                if (is_new) {
                    article_ids = iArticle.articleIdsByPoolsIdsInArticlePoolsBySubjectToday(subject_list, pools_list, 0, 20, todayStartTime);
                } else {
                    article_ids = iArticle.articleIdsByPoolsIdsInArticlePoolsBySubject(subject_list, pools_list, 0, 20);
                }
                iArticle.insertArticleIndex(user_id, key, article_ids.size());
            } else {
                if (is_new) {
                    article_ids = iArticle.articleIdsByPoolsIdsInArticlePoolsBySubjectToday(subject_list, pools_list, articleIndex.get(0).getIndex_count(), 20, todayStartTime);
                } else {
                    article_ids = iArticle.articleIdsByPoolsIdsInArticlePoolsBySubject(subject_list, pools_list, articleIndex.get(0).getIndex_count(), 20);
                }
                iArticle.updateArticleIndex(user_id, key, articleIndex.get(0).getIndex_count() + article_ids.size());
            }
        }
        if (!is_new) {
            if (article_ids.size() < 20) {
                iArticle.updateArticleIndex(user_id, key, 0);
            }
        }
        return article_ids;
    }


    /**
     * 获取频道文章
     */
    @RequestMapping("/channel_article")
    @ResponseBody
    JSONResult channelArticle(String channel_id, String user_guid) {

        if (channel_id == null || user_guid == null) {
            return JSONResult.errorMsg("缺少 channel_id 或 user_guid 参数");
        }

        List<Integer> article_ids = new ArrayList<>();
        int grade = 0;

        if (isNumeric(user_guid)) {
            UserInfo userInfo = iUser.getUser(Integer.parseInt(user_guid));
            if (userInfo != null) {
                grade = userInfo.getChannel_stage();
            }
        }
        int other_grade1 = 0;
        if (grade != 0) {
            other_grade1 = grade / 100 * 100 + 99;
        }
        List<Integer> videos_pool_ids = new ArrayList<>();
        if (grade != 0) {
            videos_pool_ids.add(grade);
        }
        if (other_grade1 != 0) {
            videos_pool_ids.add(other_grade1);
        }
        videos_pool_ids.add(999);

        String param = "";
        long nowTime = System.currentTimeMillis();
        int todayStartTime = (int) ((nowTime - (nowTime + TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24)) / 1000);
        if (channel_id.equals("1")) { //推荐
            if (grade == 0) {
                article_ids = getAllPoolArticle(user_guid, "all" + todayStartTime, true, new ArrayList<>());
                if (article_ids.size() < 6) {
                    List<Integer> article_ids_temp = getAllPoolArticle(user_guid, "all", false, new ArrayList<>());
                    if (article_ids_temp.size() == 0) {
                        article_ids_temp = getAllPoolArticle(user_guid, "all", false, new ArrayList<>());
                    }
                    for (int id : article_ids_temp) {
                        article_ids.add(id);
                    }
                }
            } else {
                for (int id : videos_pool_ids) {
                    param += (id + ",");
                }
                article_ids = getAllPoolArticle(user_guid, param + todayStartTime, true, videos_pool_ids);
                if (article_ids.size() < 6) {
                    List<Integer> video_ids_temp = getAllPoolArticle(user_guid, param, false, videos_pool_ids);
                    if (video_ids_temp.size() == 0) {
                        video_ids_temp = getAllPoolArticle(user_guid, param, false, videos_pool_ids);
                    }
                    for (int id : video_ids_temp) {
                        article_ids.add(id);
                    }
                }
            }
        } else { //频道
            List<String> list = Arrays.asList(channel_id.split(","));
            List<Integer> subjectList = new ArrayList<>();
            for (String id : list) {
                if (id != null && !id.equals("")) {
                    subjectList.add(Integer.parseInt(id));
                }
            }
            for (int subject : subjectList) {
                param += (subject + ",");
            }
            if (grade != 0) {
                for (int id : videos_pool_ids) {
                    param += (id + ",");
                }
            } else {
                videos_pool_ids = new ArrayList<>();
            }

            article_ids = getAllSubjectPoolArticle(user_guid, param + todayStartTime, true, videos_pool_ids, subjectList);
            if (article_ids.size() < 6) {
                List<Integer> video_ids_temp = getAllSubjectPoolArticle(user_guid, param, false, videos_pool_ids, subjectList);
                if (video_ids_temp.size() == 0) {
                    video_ids_temp = getAllSubjectPoolArticle(user_guid, param, false, videos_pool_ids, subjectList);
                }
                for (int id : video_ids_temp) {
                    article_ids.add(id);
                }
            }
        }

        ListResp ret = new ListResp();
        if (article_ids.size() > 0) {
            List<ArticleInfo> articles = iArticle.getArticlebyIds(article_ids);
            getExtendInfo(articles);
            ret.setCount(articles.size());
            ret.setList(articles);
        } else {
            ret.setCount(0);
            ret.setList(article_ids);
        }
        return JSONResult.ok(ret);
    }

    @RequestMapping("/comment_answer")
    @ResponseBody
    JSONResult commentAnswer(String answer_id, String first_user_id,
                             String first_comment, String second_user_id, String second_comment) {
        if (answer_id == null || first_user_id == null || first_comment == null) {
            return JSONResult.errorMsg("缺少参数");
        }

        second_user_id = second_user_id == null ? "0" : second_user_id;
        second_comment = second_comment == null ? "" : second_comment;

        first_comment = getURLEncoderString(first_comment);
        second_comment = getURLEncoderString(second_comment);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        AnswerComment answerComment = new AnswerComment();
        answerComment.setAnswer_id(Integer.parseInt(answer_id));
        answerComment.setFirst_user_id(Integer.parseInt(first_user_id));
        answerComment.setFirst_comment(first_comment);
        answerComment.setSecond_user_id(Integer.parseInt(second_user_id));
        answerComment.setSecond_comment(second_comment);
        answerComment.setCreate_time((int) (new Date().getTime() / 1000));

        iArticle.addAnswerComment(answerComment);
        iArticle.addMainAnswerComment(Integer.parseInt(answer_id));
        answerComment.setFirst_comment(URLDecoderString(first_comment));
        answerComment.setSecond_comment(URLDecoderString(second_comment));
        return JSONResult.ok(answerComment);
    }

    @RequestMapping("/praise_answer_comment")
    @ResponseBody
    JSONResult commentAnswerPraise(String answer_comment_id) {
        if (answer_comment_id == null) {
            return JSONResult.errorMsg("缺少 answer_comment_id 参数");
        }
        int user_id = getCurrentUserId();
        if (null == iArticle.findAnswerChatPraise(user_id, Integer.parseInt(answer_comment_id))) {
            iArticle.praiseAnswerChat(user_id, Integer.parseInt(answer_comment_id), (int) (new Date().getTime() / 1000));
            iArticle.praiseMainAnswerChat(Integer.parseInt(answer_comment_id));
        } else {
            return JSONResult.errorMsg("已经攒过了");
        }
        return JSONResult.ok();
    }

    @RequestMapping("/answer_comment_list")
    @ResponseBody
    JSONResult answerCommentList(String answer_id, String page, String size) {
        if (answer_id == null) {
            return JSONResult.errorMsg("answer_id");
        }

        int iPage = (page == null || page.equals("")) ? 1 : Integer.parseInt(page);
        int iSize = (size == null || size.equals("")) ? 20 : Integer.parseInt(size);
        int begin = (iPage - 1) * iSize;
        int end = iSize;

        int count = iArticle.AnswerCommentCount(Integer.parseInt(answer_id));

        List<AnswerComment> list = iArticle.getAnswerCommentList(Integer.parseInt(answer_id), begin, end);
        if (list.size() > 0) {
            List<Integer> user_ids = new ArrayList<>();
            for (AnswerComment answerComment : list) {
                answerComment.setFirst_comment(URLDecoderString(answerComment.getFirst_comment()));
                answerComment.setSecond_comment(URLDecoderString(answerComment.getSecond_comment()));
                if (!user_ids.contains(answerComment.getFirst_user_id())) {
                    user_ids.add(answerComment.getFirst_user_id());
                }
                if (!user_ids.contains(answerComment.getSecond_user_id())) {
                    user_ids.add(answerComment.getSecond_user_id());
                }
            }
            List<UserInfo> user_list = iUser.getUserByIds(user_ids);
            List<Integer> comment_ids = new ArrayList<>();
            int currentUserID = getCurrentUserId();
            if (currentUserID != 0) {
                comment_ids = iArticle.getPraiseAnswerCommentByUserId(currentUserID);
            }
            Map<Integer, UserInfo> usersMap = new HashMap<>();
            for (UserInfo userInfo : user_list) {
                usersMap.put(userInfo.getId(), userInfo);
            }

            for (AnswerComment answerComment : list) {

                answerComment.setFirst_comment(ToolsFunction.URLDecoderString(answerComment.getFirst_comment()));
                answerComment.setSecond_comment(ToolsFunction.URLDecoderString(answerComment.getSecond_comment()));
                if (comment_ids.contains(answerComment.getId())) {
                    answerComment.setPraise(true);
                }

                if (usersMap.get(answerComment.getFirst_user_id()) != null) {
                    answerComment.setFirst_user_head(usersMap.get(answerComment.getFirst_user_id()).getHead());
                    answerComment.setFirst_user_name(usersMap.get(answerComment.getFirst_user_id()).getName());
                } else {
                    answerComment.setFirst_user_head("");
                    answerComment.setFirst_user_name("");
                }

                if (usersMap.get(answerComment.getSecond_user_id()) != null) {
                    answerComment.setSecond_user_head(usersMap.get(answerComment.getSecond_user_id()).getHead());
                    answerComment.setSecond_user_name(usersMap.get(answerComment.getSecond_user_id()).getName());
                } else {
                    answerComment.setSecond_user_head("");
                    answerComment.setSecond_user_name("");
                }
            }

        }


        ListResp listResp = new ListResp();
        listResp.setCount(count);
        listResp.setList(list);
        return JSONResult.ok(listResp);
    }


    @RequestMapping("/my_answer")
    @ResponseBody
    JSONResult myAnswer(String page, String size) {

        int iPage = page == null || page.equals("") ? 1 : Integer.parseInt(page);
        int iSize = size == null || size.equals("") ? 20 : Integer.parseInt(size);
        int begin = (iPage - 1) * iSize;
        int end = iSize;

        int user_id = getCurrentUserId();

        int count = iArticle.myAnswerCount(user_id);
        List<ArticleAnswer> list = new ArrayList<>();
        if (count > 0) {
            list = iArticle.myAnswerList(user_id, begin, end);
        }
        getAnswerExtendInfo(list);
        List<Integer> answerList = new ArrayList<>();
        List<Integer> collectionList = new ArrayList<>();
        List<Integer> followList = new ArrayList<>();
        if (user_id != 0) {
            answerList = iArticle.getAnswerPraise(user_id);
            collectionList = iArticle.getUserCollection(user_id);
            followList = iUser.getFollowUsers(user_id);
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        for (ArticleAnswer item : list) {
            item.setContent_url(request.getScheme() + "://" + request.getServerName() + ":"
                    + request.getServerPort() + "/guidesound/article/answer_preview?answer_id=" + item.getId());
            if (answerList.contains(item.getId())) {
                item.setPraise(true);
            }
            if (collectionList.contains(item.getId())) {
                item.setCollection(true);
            }
            if (followList.contains(item.getUser_id())) {
                item.setFollow(true);
            }
        }

        ListResp listResp = new ListResp();
        listResp.setCount(count);
        listResp.setList(list);
        return JSONResult.ok(listResp);
    }

    @RequestMapping("/article_list")
    @ResponseBody
    JSONResult getArticleList(String status,
                              String content,
                              String page,
                              String size,
                              String s_type,
                              String subject,
                              String grade_class,
                              String article_id,
                              String user_id,
                              String pools,
                              String course,
                              String user_name) {

        status = (status == null || status.equals("")) ? null : status;
        String title = (content == null || content.equals("")) ? null : content;
        int iPage = page == null ? 1 : Integer.parseInt(page);
        int iSize = size == null ? 20 : Integer.parseInt(size);
        int sType = s_type == null ? 0 : Integer.parseInt(s_type);
        List<String> subject_list = null;
        List<String> grade_class_list = null;
        if (subject != null && !subject.equals("")) {
            subject_list = Arrays.asList(subject.split(","));
        }
        if (grade_class != null && !grade_class.equals("")) {
            grade_class_list = Arrays.asList(grade_class.split(","));
        }
        ListResp ret = new ListResp();

        List<Integer> user_ids = null;
        if (user_name != null) {
            user_ids = iUser.getUserIdsByName2(user_name);
            if (user_ids.size() < 1) {
                ret.setCount(0);
                ret.setList(new ArrayList<>());
                return JSONResult.ok(ret);
            }
        }
        int begin = (iPage - 1) * iSize;
        int end = iSize;

        ArticleFind articleFind = new ArticleFind();
        articleFind.setHead(title);
        articleFind.setStatus(status);
        articleFind.setsType(1);
        articleFind.setBegin(begin);
        articleFind.setEnd(end);
        articleFind.setSubject_list(subject_list);
        articleFind.setGrade_class_list(grade_class_list);
        articleFind.setsType(sType);
        articleFind.setUser_id(user_id);
        articleFind.setArticle_id(article_id);
        articleFind.setUser_ids(user_ids);

        List<String> poolList = new ArrayList<>();
        if (pools != null && !pools.equals("")) {
            String[] strarray = pools.split(",");
            for (String id : strarray) {
                if (!id.equals("")) {
                    poolList.add(id);
                }
            }
            articleFind.setPools(poolList);
        }

        List<Integer> course_type = new ArrayList<>();
        if (course != null && !course.equals("")) {
            String[] strarray = course.split(",");
            for (String id : strarray) {
                if (!id.equals("")) {
                    course_type.add(Integer.parseInt(id) - 1);
                }
            }
            articleFind.setCourse_type(course_type);
        }

        int count_temp = iArticle.findArticleCount(articleFind).size();
        if (count_temp == 0) {
            ret.setCount(0);
            ret.setList(new ArrayList<>());
            return JSONResult.ok(ret);
        }
        List<ArticleInfo> list_temp = iArticle.findArticle(articleFind);
        getExtendInfo(list_temp);
        ret.setCount(count_temp);
        ret.setList(list_temp);
        return JSONResult.ok(ret);
    }

    @RequestMapping("/up_pool")
    @ResponseBody
    JSONResult upPool() {
        List<ArticleInfo> lists = iArticle.getAll();
        for (int i = 0; i < 5; i++) {
            Collections.shuffle(lists);
        }
        for (ArticleInfo item : lists) {
            if(!item.getType_list().contains("1")) {
                continue;
            }
            ArticlePool articlePool = new ArticlePool();
            articlePool.setArticle_id(item.getId());
            articlePool.setArticle_pool(item.getGrade());
            articlePool.setUser_id(item.getUser_id());
            articlePool.setSubject(item.getSubject());
            iArticle.insertArticlePool(articlePool);
        }
        return JSONResult.ok();
    }

}
