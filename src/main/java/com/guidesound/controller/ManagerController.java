package com.guidesound.controller;

import com.guidesound.dao.*;
import com.guidesound.models.*;
import com.guidesound.ret.Authentication;
import com.guidesound.ret.UserAudit;
import com.guidesound.util.*;
import com.guidesound.TempStruct.ItemInfo;
import com.qcloud.Common.Sign;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.jms.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class ManagerController extends BaseController {

    @Autowired
    private IInUser iInUser;
    @Autowired
    private IVideo iVideo;
    @Autowired
    private IUser iUser;
    @Autowired
    private IArticle iArticle;
    @Autowired
    private IExamine iExamine;
    @RequestMapping(value = "/login")
    @ResponseBody
    JSONResult logIn(HttpServletRequest request, HttpServletResponse response) {

        String account = request.getParameter("account");
        String pwd = request.getParameter("pwd");
        if(account == null || pwd == null) {
            return JSONResult.errorMsg("缺少参数");
        }

        List<InUser> inUser = iInUser.getUserByName(account);
        if (inUser.size() == 0 ) {
            return JSONResult.errorMsg("账号密码错误");
        }

        String temp = inUser.get(0).getPwd();
        if(!temp.equals(pwd)) {
            return JSONResult.errorMsg("账号密码错误");
        }

        String token = TockenUtil.makeTocken(inUser.get(0).getId());
        token = "m_token=" + token;
        return JSONResult.ok(token);
    }

    @RequestMapping(value = "/code")
    void identifyingCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        HttpSession session = request.getSession(true);
        session.removeAttribute("verCode");
        session.setAttribute("verCode", verifyCode.toLowerCase());

        int w = 100, h = 40;
        VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
    }

    @RequestMapping(value = "/fail_reason_list")
    @ResponseBody
    JSONResult failReasonList() {
        Map<Integer,String> reason = VideoExamine.getReason();
        class Item {
            int id;
            String reason;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }
        }

        List<Item> list = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : reason.entrySet()) {
            Item item = new Item();
            item.setId(entry.getKey());
            item.setReason(entry.getValue());
            list.add(item);
        }
        return JSONResult.ok(list);
    }

    @RequestMapping(value = "/current_video_list")
    @ResponseBody
    JSONResult currentVideoList(HttpServletRequest request, HttpServletResponse response) {
        Integer userId = getUserId();
        if ( userId == null ) {
            return JSONResult.errorMsg("缺少m_token");
        }
        if( userId  == - 1) {
            return JSONResult.errorMsg("m_token 错误");
        }
        List<VideoInfo> videoList = iVideo.getVideoByAdminId(userId);
        if(videoList.size() > 0) {
            return JSONResult.ok(getVideoShow(videoList));
        }

        videoList  = iVideo.getExamineVideo();
        if(videoList != null && videoList.size() > 0) {
            ArrayList<Integer> list = new ArrayList<>();
            for (VideoInfo item : videoList) {
                item.setTitle(ToolsFunction.URLDecoderString(item.getTitle()));
                list.add(item.getId());
            }
            iVideo.setExaminePerson(list,userId);
            return JSONResult.ok(getVideoShow(videoList));
        }


        return JSONResult.ok(new ArrayList<>());
    }

    @RequestMapping(value = "/examine_video")
    @ResponseBody
    JSONResult examineVideo(String video_id,String status,String type_list,String fail_reason,String fail_content) throws IOException, InterruptedException, JMSException {

        Integer userId = getUserId();
        if ( userId == null ) {
            return JSONResult.errorMsg("缺少m_token");
        }

        if ( video_id == null || status == null ) {
            return JSONResult.errorMsg("缺少status 或 video_id");
        }
        if(Integer.parseInt(status) == 1) {
            if(type_list == null) {
                return JSONResult.errorMsg("缺少type_list");
            }

            MessageProducer messageProducer = null;
            Session session = null;

            String mqUrl = "tcp://139.199.112.147:61616";
            String qName = "examine";
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(mqUrl);
            try {
                Connection connection = connectionFactory.createConnection();
                connection.start();
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination destination = session.createQueue(qName);
                messageProducer = session.createProducer(destination);
                if(messageProducer != null || messageProducer != null) {
                    TextMessage textMessage = session.createTextMessage(String.valueOf(video_id));
                    messageProducer.send(textMessage);
                }
                connection.close();
                iVideo.setExamineLoading(Integer.parseInt(video_id),type_list);

            } catch (JMSException e) {
                e.printStackTrace();
            }
        } else {
            if(fail_reason == null || fail_content == null) {
                return JSONResult.errorMsg("缺少fail_reason或fail_content");
            }
            iVideo.setExamineFail(Integer.parseInt(video_id),fail_reason,fail_content);
        }
        return JSONResult.ok();
    }



    Integer getUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("m_token")) {
                    String token = cookie.getValue();
                    token = URLDecoder.decode(token);
                    int user_id = TockenUtil.getUserIdByTocket(token);
                    return user_id;
                }
            }
        }
        return null;
    }

    List<ArticleVerify> getArticleShowByAnswers(List<ArticleAnswer> answerList) {
        if (answerList.size() > 0) {
            List<Integer> userIds = new ArrayList<>();
            for (ArticleAnswer item : answerList) {
                userIds.add(item.getUser_id());
            }
            List<VideoUser> userList = iUser.getUserInfoByIds(userIds);
            Map<Integer,VideoUser> mUser = new Hashtable<>();
            for(VideoUser item : userList) {
                mUser.put(item.getId(),item);
            }
            List<ArticleVerify> articleVerifies = new ArrayList<>();
            for (ArticleAnswer item: answerList) {
                ArticleVerify articleVerify = new ArticleVerify();
                articleVerify.setType(2);
                articleVerify.setArticle_id(item.getId());
                articleVerify.setArticleAid(item.getId());
                articleVerify.setArticleGrade_class(SignMap.getGradeTypeByID(0));
                articleVerify.setArticleLength(0);
                articleVerify.setArticleSubject(SignMap.getSubjectTypeById(0));
                articleVerify.setArticleTitle(item.getAbstract_info());
                articleVerify.setArticleXy("");

                articleVerify.setShowContent("https://daoyinjiaoyu.com/guidesound/article/answer_preview?answer_id=" + item.getId());
                articleVerify.setShowTitle_pic1(item.getPic1_url());
                articleVerify.setShowTitle_pic2(item.getPic2_url());
                articleVerify.setShowTitle_pic3(item.getPic3_url());

                VideoUser videoUser = mUser.get(item.getUser_id());
                if(videoUser != null) {
                    articleVerify.setUser_uid(String.valueOf(videoUser.getDy_id()));
                    articleVerify.setUser_type(SignMap.getUserTypeById(videoUser.getType()));
                    articleVerify.setUser_extend("");
                    articleVerify.setUser_grade_level(SignMap.getWatchById(videoUser.getGrade_level()));
                    articleVerify.setUser_level(SignMap.getUserLevelById(videoUser.getLevel()));
                    articleVerify.setUser_name(videoUser.getName());
                    articleVerify.setUser_subject(SignMap.getSubjectTypeById(videoUser.getSubject()));
                }
                articleVerifies.add(articleVerify);
            }
            return articleVerifies;
        }
        return null;
    }
    List<ArticleVerify> getArticleShow(List<ArticleInfo> articleList) {
        if (articleList.size() > 0) {
            List<Integer> userIds = new ArrayList<>();
            for (ArticleInfo item : articleList) {
                userIds.add(item.getUser_id());
            }
            List<VideoUser> userList = iUser.getUserInfoByIds(userIds);
            Map<Integer,VideoUser> mUser = new Hashtable<>();
            for(VideoUser item : userList) {
                mUser.put(item.getId(),item);
            }
            List<ArticleVerify> articleVerifies = new ArrayList<>();
            for (ArticleInfo item: articleList) {
                ArticleVerify articleVerify = new ArticleVerify();

                articleVerify.setType(1);
                articleVerify.setArticle_id(item.getId());
                articleVerify.setArticleAid(item.getId());
                articleVerify.setArticleGrade_class(SignMap.getGradeTypeByID(item.getGrade()));
                articleVerify.setArticleLength(0);
                articleVerify.setArticleSubject(SignMap.getSubjectTypeById(item.getSubject()));
                articleVerify.setArticleTitle(item.getHead());
                articleVerify.setArticleXy("");

                articleVerify.setShowContent("https://daoyinjiaoyu.com/guidesound/article/preview?article_id=" + item.getId());
                articleVerify.setShowTitle_pic1(item.getHead_pic1());
                articleVerify.setShowTitle_pic2(item.getHead_pic2());
                articleVerify.setShowTitle_pic3(item.getHead_pic3());


                VideoUser videoUser = mUser.get(item.getUser_id());
                if(videoUser != null) {
                    articleVerify.setUser_uid(String.valueOf(videoUser.getDy_id()));
                    articleVerify.setUser_type(SignMap.getUserTypeById(videoUser.getType()));
                    articleVerify.setUser_extend("");
                    articleVerify.setUser_grade_level(SignMap.getWatchById(videoUser.getGrade_level()));
                    articleVerify.setUser_level(SignMap.getUserLevelById(videoUser.getLevel()));
                    articleVerify.setUser_name(videoUser.getName());
                    articleVerify.setUser_subject(SignMap.getSubjectTypeById(videoUser.getSubject()));
                }

                articleVerifies.add(articleVerify);
            }
            return articleVerifies;
        }
        return null;
    }

    List<VideoVerify> getVideoShow(List<VideoInfo> videoList ) {
        if(videoList.size() > 0) {
            List<Integer> userIds = new ArrayList<>();
            for (VideoInfo item : videoList) {
                userIds.add(item.getUser_id());
            }
            List<VideoUser> userList = iUser.getUserInfoByIds(userIds);

            Map<Integer,VideoUser> mUser = new Hashtable<>();
            for(VideoUser item : userList) {
                mUser.put(item.getId(),item);
            }

            List<VideoVerify> videoVerifies = new ArrayList<>();
            for (VideoInfo item: videoList) {
                VideoVerify videoVerify = new VideoVerify();
                videoVerify.setVideo_id(item.getId());
                videoVerify.setVideo_title(ToolsFunction.URLDecoderString(item.getTitle()));
                videoVerify.setVideo_duration(item.getDuration() + "秒");
                videoVerify.setVideo_pic_up_path(item.getPic_up_path());
                videoVerify.setVideo_video_up_path(item.getVideo_up_path());
                videoVerify.setVideo_resolution(item.getResolution_w() + " X " + item.getResolution_h() );
                videoVerify.setVideo_subject(SignMap.getSubjectTypeById(item.getSubject()));
                videoVerify.setVideo_watch_type(SignMap.getGradeTypeByID(item.getWatch_type()));

                VideoUser videoUser = mUser.get(item.getUser_id());
                if(videoUser != null) {
                    videoVerify.setUser_type(SignMap.getUserTypeById(videoUser.getType()));
                    videoVerify.setUser_extend("");
                    videoVerify.setUser_grade_level(SignMap.getWatchById(videoUser.getGrade_level()));
                    videoVerify.setUser_level(SignMap.getUserLevelById(videoUser.getLevel()));
                    videoVerify.setUser_name(videoUser.getName());
                    videoVerify.setUser_subject(SignMap.getSubjectTypeById(videoUser.getSubject()));
                    videoVerify.setUser_uid(String.valueOf(videoUser.getDy_id()));
                }
                videoVerifies.add(videoVerify);
            }
            return videoVerifies;
        }
        return null;
    }

    @RequestMapping(value = "/article_fail_reason_list")
    @ResponseBody
    JSONResult articleFailReasonList() {

        class Item {
            int id;
            String reason;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }
        }

        Map<Integer,String> reason = VideoExamine.getReason();
        List<Item> list = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : reason.entrySet()) {
            Item item = new Item();
            item.setId(entry.getKey());
            item.setReason(entry.getValue());
            list.add(item);
        }
        return JSONResult.ok(list);
    }


    @RequestMapping(value = "/current_article_list")
    @ResponseBody
    JSONResult currentAtricleList(HttpServletRequest request, HttpServletResponse response) {
        Integer userId = getUserId();
        if ( userId == null ) {
            return JSONResult.errorMsg("缺少m_token");
        }
        if( userId  == - 1) {
            return JSONResult.errorMsg("m_token 错误");
        }

        List<ArticleInfo> articleList = iArticle.getArticleByAdminId(userId);
        if(articleList.size() > 0) {
            return JSONResult.ok(getArticleShow(articleList));
        }

        List<ArticleAnswer> answerList = iArticle.getAnswerByAdminId(userId);
        if (answerList.size() > 0) {
            return JSONResult.ok(getArticleShowByAnswers(answerList));
        }
        articleList  = iArticle.getExamineArticle();
        if(articleList.size() > 0) {
            ArrayList<Integer> list = new ArrayList<>();
            for (ArticleInfo item : articleList) {
                list.add(item.getId());
            }
            iArticle.setExaminePerson(list,userId);
            return JSONResult.ok(getArticleShow(articleList));
        }
        answerList = iArticle.getExamineAnswer();
        if (answerList.size() > 0) {
            ArrayList<Integer> list = new ArrayList<>();
            for (ArticleAnswer item : answerList) {
                list.add(item.getId());
            }
            iArticle.setAnswerExaminePerson(list,userId);
            return JSONResult.ok(getArticleShowByAnswers(answerList));
        }
        return JSONResult.ok(new ArrayList<>());
    }

    @RequestMapping(value = "/examine_article")
    @ResponseBody
    JSONResult examineArticle(String article_id,String status,String type_list,String fail_reason,String fail_content,String type) throws IOException, InterruptedException, JMSException {

        Integer userId = getUserId();
        if ( userId == null ) {
            return JSONResult.errorMsg("缺少m_token");
        }

        if ( article_id == null || status == null ) {
            return JSONResult.errorMsg("缺少status 或 article_id");
        }

        if ( type == null ) {
            return JSONResult.errorMsg("缺少type");
        }

        if(Integer.parseInt(status) == 1) {
            if(type_list == null) {
                return JSONResult.errorMsg("缺少type_list");
            }
            if(type.equals("1")) {
                iArticle.setExamineSuccess(Integer.parseInt(article_id),type_list);
            } else {
                iArticle.setAnswerExamineSuccess(Integer.parseInt(article_id),type_list);
            }

        } else {
            if(fail_reason == null || fail_content == null) {
                return JSONResult.errorMsg("缺少fail_reason或fail_content");
            }
            if(type.equals("1")) {
                iArticle.setExamineFail(Integer.parseInt(article_id),fail_reason,fail_content);
            } else {
                iArticle.setAnswerExamineFail(Integer.parseInt(article_id),fail_reason,fail_content);
            }
        }
        return JSONResult.ok();
    }

    @RequestMapping(value = "/code_loading")
    public String codeLoading() {
        return "code_loading";
    }

    @RequestMapping(value = "/loading")
    @ResponseBody
    JSONResult Loading() throws InterruptedException {
//        String temp = exec("cd /home/ubuntu/mysoft/apache-tomcat-9.0.13/webapps/public && git pull");
        String temp = exec("cd ../webapps/public/ && git pull");

        return JSONResult.ok(temp);
    }
    public static String exec(String command) throws InterruptedException {
        String returnString = "";
        Process pro = null;
        Runtime runTime = Runtime.getRuntime();
        if (runTime == null) {
            System.err.println("Create runtime false!");
        }
        try {
            pro = runTime.exec(command);
            int r = pro.waitFor();
            BufferedReader input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            PrintWriter output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                returnString = returnString + line + "\n";
            }
            input.close();
            output.close();
            pro.destroy();
        } catch (IOException ex) {
            System.out.println(ex);
            return ex.toString();
        }
        return returnString;
    }

    @RequestMapping(value = "/up_video")
    @ResponseBody
    public JSONResult upVideo(String video_id) {

        Integer userId = getUserId();
        if ( userId == null ) {
            return JSONResult.errorMsg("缺少m_token");
        }
        if(video_id == null) {
            return JSONResult.errorMsg("缺少video_id");
        }
        iVideo.UpVideo(Integer.parseInt(video_id));
        return JSONResult.ok("已完成");
    }

    @RequestMapping(value = "/down_video")
    @ResponseBody
    public JSONResult downVideo(String video_id) {
        Integer userId = getUserId();
        if ( userId == null ) {
            return JSONResult.errorMsg("缺少m_token");
        }
        if(video_id == null) {
            return JSONResult.errorMsg("缺少video_id");
        }
        iVideo.downVideo(Integer.parseInt(video_id));
        return JSONResult.ok("已下架");
    }

    @RequestMapping(value = "/pools_list")
    @ResponseBody
    public JSONResult getPoolsList() {

        List<ItemInfo> list = new ArrayList<>();
        Map<Integer,String> pool_list = SignMap.getPoolList();
        for(int id:pool_list.keySet()) {
            ItemInfo itemInfo = new ItemInfo();
            itemInfo.setId(id);
            itemInfo.setInfo(pool_list.get(id));
            list.add(itemInfo);
        }
        return JSONResult.ok(list);
    }


    @RequestMapping(value = "/top_sort_list")
    @ResponseBody
    public JSONResult getTooList() {
        List<ItemInfo> list = new ArrayList<>();
        list.add(new ItemInfo().setId(101).setInfo("点赞"));
        list.add(new ItemInfo().setId(102).setInfo("评论"));
        list.add(new ItemInfo().setId(103).setInfo("外转"));
        list.add(new ItemInfo().setId(104).setInfo("内转"));
        list.add(new ItemInfo().setId(105).setInfo("收藏"));
        list.add(new ItemInfo().setId(106).setInfo("完播"));
        list.add(new ItemInfo().setId(107).setInfo("推荐"));
        list.add(new ItemInfo().setId(108).setInfo("组数"));
        return JSONResult.ok(list);
    }

    @RequestMapping(value = "/add_pool")
    @ResponseBody
    JSONResult addPool(String video_id,String pool_id) {
        if(video_id == null || pool_id == null) {
            return JSONResult.errorMsg("缺少video_id 或 pool_id");
        }
        Video video = iVideo.getVideo(Integer.parseInt(video_id));
        if(video == null) {
            return JSONResult.errorMsg("视频不存在");
        }

        String pools = video.getPools();
        List<String> lists = new ArrayList<>(Arrays.asList(pools.split(",")));
        if(!lists.contains(pool_id)) {
            lists.add(pool_id);
        }
        String temp = StringUtils.join(lists, ",");
        iVideo.setPoolByVideoId(video_id,temp);
        return JSONResult.ok();
    }

    @RequestMapping(value = "/remove_pool")
    @ResponseBody
    JSONResult removePool(String video_id,String pool_id) {

        if(video_id == null || pool_id == null) {
            return JSONResult.errorMsg("缺少video_id 或 pool_id");
        }
        Video video = iVideo.getVideo(Integer.parseInt(video_id));
        if(video == null) {
            return JSONResult.errorMsg("视频不存在");
        }
        String pools = video.getPools();
        List<String> lists = new ArrayList<>(Arrays.asList(pools.split(",")));
        if(lists.contains(pool_id)) {
            lists.remove(pool_id);
        }
        String temp = StringUtils.join(lists, ",");
        iVideo.setPoolByVideoId(video_id,temp);
        return JSONResult.ok();
    }

    @RequestMapping(value = "/course_list")
    @ResponseBody
    public JSONResult getCourseList() {
        Map<Integer,String> course_type = SignMap.getCourseType();
        List<ItemInfo> list = new ArrayList<>();
        for (Integer key : course_type.keySet()) {
            ItemInfo itemInfo = new ItemInfo();
            itemInfo.setId(key);
            itemInfo.setInfo(course_type.get(key));
            list.add(itemInfo);
        }
        return JSONResult.ok(list);
    }

    @RequestMapping(value = "/examine_video_common")
    @ResponseBody
    public JSONResult videoExamineCommon(String type,String uid,String item_id,String result,String failure_id,String failure_content) {
        return JSONResult.ok();
    }

    @RequestMapping(value = "/user_examine")
    @ResponseBody
    public JSONResult userExamine() {

        List<UserAudit> userAuditsList = new ArrayList<>();
        List<UserExamine> list = iExamine.getUserExamine();
        for (UserExamine userExamine: list
             ) {

            UserAudit userAudit = new UserAudit();
            userAudit.setUid(userExamine.getUser_id());
            userAudit.setItem_type(userExamine.getAuth_type());
            userAudit.setText(userExamine.getText());

            Authentication authentication = new Authentication();
            authentication.setAchivement(userExamine.getAchivement());
            authentication.setConfirmation_pic(userExamine.getConfirmation_pic());
            authentication.setEnterprise_card(userExamine.getEnterprise_card());
            authentication.setGraduation_card(userExamine.getGraduation_card());
            authentication.setIdentity_card(userExamine.getIdentity_card());
            authentication.setIdentity_info(userExamine.getIdentity_info());
            authentication.setShop_card(userExamine.getShop_card());
            authentication.setTeacher_card(userExamine.getTeacher_card());
            authentication.setType(userExamine.getType());
            userAudit.setAuthentication(authentication);
            userAuditsList.add(userAudit);
        }
        return JSONResult.ok(userAuditsList);
    }

    @RequestMapping(value = "/commodity_examine")
    @ResponseBody
    public JSONResult getCommodity() {
        List<CommodityExamine> list = iExamine.getCommodityExamine();
        return JSONResult.ok(list);
    }
}


