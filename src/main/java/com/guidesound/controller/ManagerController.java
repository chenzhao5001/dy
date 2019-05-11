package com.guidesound.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidesound.Service.ILogService;
import com.guidesound.TempStruct.InfoMsg;
import com.guidesound.TempStruct.RecordVideo;
import com.guidesound.dao.*;
import com.guidesound.dao.UserCommodity;
import com.guidesound.models.*;
import com.guidesound.ret.*;
import com.guidesound.util.*;
import com.guidesound.TempStruct.ItemInfo;
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
    @Autowired
    private IOrder iOrder;
    @Autowired
    private IRecord iRecord;
    @Autowired
    private ILogService iLogService;
    int getCurrentCount() {
        int count = iOrder.getCurrentCount();
        iOrder.setCurrentCount(count + 1);
        return count;
    }
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
                VideoShow video = iVideo.getVideoById(video_id);
                UserInfo userInfo = iUser.getUser(video.getUser_id());
                if(status.equals("1")) {
                    if(type_list.contains("1")) {
                        iVideo.setPoolByVideoId(video_id,","+ video.getWatch_type());
                        VideoPool videoPool = new VideoPool();
                        videoPool.setUser_id(video.getUser_id());
                        videoPool.setSubject(video.getSubject());
                        videoPool.setUser_id(video.getUser_id());
                        videoPool.setVideo_pool(video.getWatch_type());
                        videoPool.setVideo_id(video.getId());
                        videoPool.setCreate_time((int) (new Date().getTime() / 1000));
                        iVideo.insertVideoPool(videoPool);
                        if(userInfo != null) {
                            TlsSigTest.SendMessage(userInfo.getIm_id(),"您发布的短视频“" + ToolsFunction.URLDecoderString(video.getTitle())+ "”已经通过系统审核，由于视频质量很高，已被系统推荐。");
                        }
                    } else {
                        if(userInfo != null){
                            TlsSigTest.SendMessage(userInfo.getIm_id(),"您发布的短视频“" + ToolsFunction.URLDecoderString(video.getTitle()) + "”已经通过系统审核。");
                        }
                    }
                }

            } catch (JMSException e) {
                e.printStackTrace();
            }
        } else {
            if(fail_reason == null || fail_content == null) {
                return JSONResult.errorMsg("缺少fail_reason或fail_content");
            }
            iVideo.setExamineFail(Integer.parseInt(video_id),fail_reason,fail_content);
            VideoShow video = iVideo.getVideoById(video_id);
            UserInfo userInfo = iUser.getUser(video.getUser_id());
            if(userInfo != null) {
                Map<Integer,String> reason = VideoExamine.getReason();
                if(reason.containsKey(Integer.parseInt(fail_reason))) {
                    TlsSigTest.SendMessage(userInfo.getIm_id(),"您发布的短视频“" + ToolsFunction.URLDecoderString(video.getTitle()) + "”未通过系统审核，未通过原因是" + reason.get(Integer.parseInt(fail_reason)));
                }
            }
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
            List<Integer> ask_ids = new ArrayList<>();
            for (ArticleAnswer item: answerList) {
                ask_ids.add(item.getAsk_id());
            }
            Map<Integer,ArticleInfo> articleInfoMap = new HashMap<>();
            if(ask_ids.size() > 0) {
                List<ArticleInfo> list = iArticle.getArticlebyIds(ask_ids);
                for(ArticleInfo articleInfo : list ) {
                    articleInfoMap.put(articleInfo.getId(),articleInfo);
                }

            }
            List<ArticleVerify> articleVerifies = new ArrayList<>();
            for (ArticleAnswer item: answerList) {
                ArticleVerify articleVerify = new ArticleVerify();
                articleVerify.setType(2);
                articleVerify.setArticle_id(item.getId());
                articleVerify.setArticleAid(item.getId());
                articleVerify.setArticleGrade_class(SignMap.getGradeTypeByID(0));

                String strSubject = "未知";
                if(articleInfoMap.containsKey(item.getAsk_id())) {
                    ArticleInfo articleInfo = articleInfoMap.get(item.getAsk_id());
                    if(articleInfo != null) {

                        if(articleInfo.getType() == 1) {
                            strSubject = SignMap.getSubjectTypeById(articleInfo.getSubject());
                        } else {
                            strSubject = SignMap.getSubjectTypeById(articleInfo.getAsk_subject());
                        }
                    }
                }
                articleVerify.setArticleSubject(strSubject);
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
                articleVerify.setArticleGrade_class(SignMap.getGradeTypeByID((Integer)item.getGrade()));
                articleVerify.setArticleLength(0);
                if(item.getType() == 1) {
                    articleVerify.setArticleSubject(SignMap.getSubjectTypeById(item.getSubject()));
                } else {
                    articleVerify.setArticleSubject(SignMap.getSubjectTypeById(item.getAsk_subject()));
                }

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

        ArticleInfo articleInfo = null;
        ArticleAnswer articleAnswer = null;
        UserInfo userInfo = null;
        if(type.equals("1")) {
            articleInfo = iArticle.getArticle(Integer.parseInt(article_id));
            if(articleInfo == null) {
                return JSONResult.errorMsg("文章不存在");
            }
            userInfo = iUser.getUser(articleInfo.getUser_id());
            if(userInfo == null) {
                return JSONResult.errorMsg("作者不存在");
            }
        } else {
            articleAnswer = iArticle.getAnswerById(Integer.parseInt(article_id));
            if(articleAnswer == null) {
                return JSONResult.errorMsg("答案不存在");
            }
            articleInfo = iArticle.getArticle(articleAnswer.getAsk_id());
            if(articleInfo == null) {
                return JSONResult.errorMsg("问题不存在");
            }

            userInfo = iUser.getUser(articleAnswer.getUser_id());
            if(userInfo == null) {
                return JSONResult.errorMsg("作者不存在");
            }
        }


        if(Integer.parseInt(status) == 1) {
            if(type_list == null) {
                return JSONResult.errorMsg("缺少type_list");
            }
            if(type_list.contains("1")) {

                iArticle.setPoolByArticleId(article_id,","+ articleInfo.getGrade());
                ArticlePool articlePool = new ArticlePool();
                articlePool.setUser_id(articleInfo.getUser_id());
                articlePool.setSubject(articleInfo.getSubject());
                articlePool.setArticle_pool((Integer)articleInfo.getGrade());
                articlePool.setArticle_id(articleInfo.getId());
                articlePool.setCreate_time((int) (new Date().getTime() / 1000));
                iArticle.insertArticlePool(articlePool);

                if(type.equals("1")) {
                    iArticle.setExamineSuccess(Integer.parseInt(article_id),type_list);
                    TlsSigTest.SendMessage(userInfo.getIm_id(),"您发布的文章“" + articleInfo.getHead()+ "”已经通过系统审核，由于文章质量很高已被系统推荐！");
                } else {
                    iArticle.setAnswerExamineSuccess(Integer.parseInt(article_id),type_list);
                    TlsSigTest.SendMessage(userInfo.getIm_id(),"您回答的问题“" + articleInfo.getHead()+ "”已经通过系统审核，由于文章质量很高已被系统推荐！");
                }

            } else {
                if(type.equals("1")) {
                    iArticle.setExamineSuccess(Integer.parseInt(article_id),type_list);
                    TlsSigTest.SendMessage(userInfo.getIm_id(),"您发布的文章“" + articleInfo.getHead()+ "”已经通过系统审核。");
                } else {
                    iArticle.setAnswerExamineSuccess(Integer.parseInt(article_id),type_list);
                    TlsSigTest.SendMessage(userInfo.getIm_id(),"您回答的问题“" + articleInfo.getHead()+ "”已经通过系统审核。");
                }

                if(type.equals("1")) {
                    InfoMsg infoMsg = new InfoMsg();
                    infoMsg.setMsg_type(7);
                    infoMsg.setType(1);
                    infoMsg.setId(articleInfo.getId());
                    infoMsg.setGrade(SignMap.getGradeTypeByID((Integer)articleInfo.getGrade()));
                    infoMsg.setSubject(SignMap.getSubjectTypeById(articleInfo.getSubject()));
                    infoMsg.setName(ToolsFunction.URLDecoderString(articleInfo.getHead()));
                    if(userInfo != null) {
                        infoMsg.setHead(userInfo.getHead());
                        infoMsg.setUser_name(userInfo.getName());
                    }

                    List<Integer> follows = iUser.getAllFuns(articleInfo.getUser_id());
                    List<Integer> no_send = iUser.getAllAcceptUserIds(articleInfo.getUser_id(),1);
                    for(Integer user_id : follows) {
                        if(!no_send.contains(user_id)) {
                            TlsSigTest.PushMessage(String.valueOf(user_id),infoMsg.toString());
                            iLogService.addLog("99999", "/examine_article", infoMsg.toString());
                        }
                    }
                }
            }

        } else {
            if(fail_reason == null || fail_content == null) {
                return JSONResult.errorMsg("缺少fail_reason或fail_content");
            }
            Map<Integer,String> reason = VideoExamine.getReason();
            if(type.equals("1")) {

                iArticle.setExamineFail(Integer.parseInt(article_id),fail_reason,fail_content);
                TlsSigTest.SendMessage(userInfo.getIm_id(),"您发布的文章“" + articleInfo.getHead()+ "”未通过系统审核，未通过原因是“" + reason.get(Integer.parseInt(fail_reason)) + "”");
            } else {
                iArticle.setAnswerExamineFail(Integer.parseInt(article_id),fail_reason,fail_content);
                TlsSigTest.SendMessage(userInfo.getIm_id(),"您回答的问题“" + articleInfo.getHead()+ "”未通过系统审核，未通过原因是“" + reason.get(Integer.parseInt(fail_reason)) + "”！");
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

    @RequestMapping(value = "/up_article")
    @ResponseBody
    public JSONResult upArticle(String article_id) {

//        Integer userId = getUserId();
//        if ( userId == null ) {
//            return JSONResult.errorMsg("缺少m_token");
//        }
        if(article_id == null) {
            return JSONResult.errorMsg("缺少article_id");
        }
        iArticle.upArticle(Integer.parseInt(article_id));
        return JSONResult.ok("已完成");
    }

    @RequestMapping(value = "/down_article")
    @ResponseBody
    public JSONResult downArticle(String article_id) {
//        Integer userId = getUserId();
//        if ( userId == null ) {
//            return JSONResult.errorMsg("缺少m_token");
//        }
        if(article_id == null) {
            return JSONResult.errorMsg("缺少article_id");
        }
        iArticle.downArticle(Integer.parseInt(article_id));
        return JSONResult.ok("已下架");
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

    @RequestMapping(value = "/add_pool_article")
    @ResponseBody
    JSONResult addArticlePool(String article_id,String pool_id) {
        if(article_id == null || pool_id == null) {
            return JSONResult.errorMsg("缺少article_id 或 pool_id");
        }
        ArticleInfo articleInfo = iArticle.getArticle(Integer.parseInt(article_id));
        if( articleInfo == null ) {
            return JSONResult.errorMsg("文章不存在");
        }

        String pools = (String)articleInfo.getPools();
        List<String> lists = new ArrayList<>(Arrays.asList(pools.split(",")));
        if(!lists.contains(pool_id)) {
            lists.add(pool_id);
        }
        String temp = StringUtils.join(lists, ",");
        iArticle.setPoolByArticleId(article_id,temp);


        List<ArticlePool> list = iArticle.getArticlePoolByInfo(Integer.parseInt(article_id),Integer.parseInt(pool_id));
        if(list.size() == 0) {
            ArticlePool articlePool = new ArticlePool();
            articlePool.setArticle_id(Integer.parseInt(article_id));
            articlePool.setUser_id(articleInfo.getUser_id());
            articlePool.setSubject(articleInfo.getSubject());
            articlePool.setArticle_pool(Integer.parseInt(pool_id));
            articlePool.setCreate_time((int) (new Date().getTime() / 1000));
            iArticle.insertArticlePool(articlePool);
        }
        return JSONResult.ok();
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

        List<VideoPool> list = iVideo.getVideoPoolByInfo(Integer.parseInt(video_id),Integer.parseInt(pool_id));
        if(list.size() == 0) {
            VideoShow videoShow = iVideo.getVideoById(video_id);
            if(videoShow != null) {
                VideoPool videoPool = new VideoPool();
                videoPool.setVideo_id(videoShow.getId());
                videoPool.setVideo_pool(Integer.parseInt(pool_id));
                videoPool.setSubject(videoShow.getSubject());
                videoPool.setUser_id(videoShow.getUser_id());
                videoPool.setCreate_time((int) (new Date().getTime() / 1000));
                iVideo.insertVideoPool(videoPool);
            }
        }

        return JSONResult.ok();
    }

    @RequestMapping(value = "/remove_pool_article")
    @ResponseBody
    JSONResult removeArticlePool(String article_id,String pool_id) {
        if(article_id == null || pool_id == null) {
            return JSONResult.errorMsg("缺少article_id 或 pool_id");
        }
        ArticleInfo articleInfo = iArticle.getArticle(Integer.parseInt(article_id));
        if(articleInfo == null) {
            return JSONResult.errorMsg("文章不存在..");
        }

        String pools = (String)articleInfo.getPools();
        List<String> lists = new ArrayList<>(Arrays.asList(pools.split(",")));
        if(lists.contains(pool_id)) {
            lists.remove(pool_id);
        }
        String temp = StringUtils.join(lists, ",");
        iArticle.setPoolByArticleId(article_id,temp);
        iArticle.removeArticleFromPools(Integer.parseInt(article_id),Integer.parseInt(pool_id));
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
        iVideo.removeVideoFromPools(Integer.parseInt(video_id),Integer.parseInt(pool_id));

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

    @RequestMapping(value = "/examine_common")
    @ResponseBody
    public JSONResult videoExamineCommon(String type,String uid,String item_id,String result,String failure_id,String failure_content) throws IOException {
        if(type.equals("0")) { //头像
            List<UserExamine> userExamine = iExamine.getUserExamineByInfo(Integer.parseInt(uid),Integer.parseInt(type));
            if(userExamine.size() > 0) {
                String head = userExamine.get(0).getText();
                if(Integer.parseInt(result) == 0) {
                    TlsSigTest.SendMessage(uid,"您修改的新头像已经通过系统审核。");
                    iUser.updateHead(Integer.parseInt(uid),head);
                } else {
                    TlsSigTest.SendMessage(uid,"您修改的新头像未通过系统审核，未通过原因是" +  failure_content + "”。");
                }
                iUser.updateUserHeadFlag(Integer.parseInt(uid),1);
            }
            iExamine.deleteUserExamine(Integer.parseInt(uid),0);

        } else if(type.equals("1")) {  // 昵称
            List<UserExamine> userExamine = iExamine.getUserExamineByInfo(Integer.parseInt(uid),Integer.parseInt(type));
            if(userExamine.size() > 0) {
                String name = userExamine.get(0).getText();
                if(Integer.parseInt(result) == 0) {
                    iUser.updateName(Integer.parseInt(uid),name);
                    TlsSigTest.SendMessage(uid,"您修改的新昵称：“" + name + "”已经通过系统审核。");
                } else {
                    TlsSigTest.SendMessage(uid,"您修改的新头像未通过系统审核，未通过原因是" +  failure_content + "”。");
                }
                iUser.updateUserNameFlag(Integer.parseInt(uid),1);
            }
            iExamine.deleteUserExamine(Integer.parseInt(uid),1);
        } else if(type.equals("2")) {  // 简介
            List<UserExamine> userExamine = iExamine.getUserExamineByInfo(Integer.parseInt(uid),Integer.parseInt(type));
            if(userExamine.size() > 0) {
                String introduction = userExamine.get(0).getText();
                if(Integer.parseInt(result) == 0) {
                    iUser.upIntroduction(Integer.parseInt(uid),introduction);
                    TlsSigTest.SendMessage(uid,"您修改的新用户介绍：“" + introduction + "”已经通过系统审核。");
                } else {
                    TlsSigTest.SendMessage(uid,"您修改的新用户介绍“" + introduction + "”未通过系统审核，未通过原因是" +  failure_content + "”。");
                }
                iUser.updateUserIntroduceFlag(Integer.parseInt(uid),1);
            }
            iExamine.deleteUserExamine(Integer.parseInt(uid),2);

        } else if(type.equals("3")) { //身份认证
            List<UserExamine> userExamine = iExamine.getUserExamineByInfo(Integer.parseInt(uid),Integer.parseInt(type));
            if(userExamine.size() > 0) {
                if(Integer.parseInt(result) == 0) {
                    iUser.updateAuthState(Integer.parseInt(uid),1);
                    iUser.addVideoDuration(Integer.parseInt(uid),60);
                    UserInfo userInfo = iUser.getUser(Integer.parseInt(uid));
                    int duration = userInfo.getVideo_duration() / 60;
                    TlsSigTest.SendMessage(uid,"您申请的认证“认证信息”已经通过系统审核，除真实身份体现外，同时您已经可以上传“" + duration +"”分钟的短视频了。");
                } else {
                    iUser.updateAuthState(Integer.parseInt(uid),3);
                    TlsSigTest.SendMessage(uid,"您申请的`认证“认证信息”未通过系统审核，未通过原因是“审核后台提交的结果”");
                }
            }
            iExamine.deleteUserExamine(Integer.parseInt(uid),3);

        } else if(type.equals("4")) { //设置店铺
            List<CommodityExamine> commodityExamines = iExamine.getCommodityExamineByUserId(Integer.parseInt(uid),Integer.parseInt(type));
            List<UserShop> shop_list = iUser.getShopByUserId(Integer.parseInt(uid));
            if (commodityExamines.size() > 0 && shop_list.size() > 0) {
                if(Integer.parseInt(result) == 0) {
                    iUser.updateShopbyUserId(Integer.parseInt(uid),commodityExamines.get(0).getShop_url());
                    iUser.updateShopState(Integer.parseInt(uid),1);
                    TlsSigTest.SendMessage(uid,"您发布的店铺“" + shop_list.get(0).getShop_url() + "”已经通过系统审核，快努力发高质量的视频推广您的商品吧！");
                } else {
                    iUser.updateShopState(Integer.parseInt(uid),2);
                    TlsSigTest.SendMessage(uid,"您发布的店铺“" + shop_list.get(0).getShop_url() + "”未通过系统审核，未通过原因是“" + failure_content + "”。");
                }

            }
            iExamine.deleteCommodityExamineByUserId(Integer.parseInt(uid),4);

        } else if(type.equals("5")) {  //添加商品
            List<CommodityExamine> commodityExamines = iExamine.getCommodityExamineByInfo(Integer.parseInt(item_id),5);
            if (commodityExamines.size() > 0) {
                UserCommodity userCommodity = iUser.getCommodityById(item_id);
                if(Integer.parseInt(result) == 0 && userCommodity != null) {
                    iUser.updateCommodityState(Integer.parseInt(item_id),1);
                    TlsSigTest.SendMessage(uid,"您发布的商品“" + userCommodity.getCommodity_name() + "”已经通过系统审核，快努力发高质量的视频推广您的商品吧！");
                } else {
                    iUser.updateCommodityState(Integer.parseInt(item_id),2);
                    TlsSigTest.SendMessage(uid,"您发布的商品“" + userCommodity.getCommodity_name() + "”未通过系统审核，未通过原因是“" + failure_content + "”。");
                }
            }
            iExamine.deleteCommodityExamine(Integer.parseInt(item_id),5);

        } else if(type.equals("6")) {  //辅导老师
            List<CourseExamine> courseExamine = iExamine.getCourseExamineById(Integer.parseInt(item_id),6);
            Teacher teacher = iCourse.getTeacherById(Integer.parseInt(item_id));
            if (courseExamine.size() > 0) {
                if(Integer.parseInt(result) == 0) {
                    iCourse.setTeacherState(Integer.parseInt(item_id),2);
                    TlsSigTest.SendMessage(uid,"您添加的辅导老师“" + teacher.getName() + "”已经通过系统审核，现在可以发布辅导课程了。");
                } else {
                    iCourse.setTeacherState(Integer.parseInt(item_id),3);
                    TlsSigTest.SendMessage(uid,"您添加的辅导老师“" + teacher.getName() + "”未通过系统审核，未通过原因是“" + failure_content + "”。");
                }
            }
            iExamine.deleteCourseExamine(Integer.parseInt(item_id),6);

        } else if(type.equals("7")) {  //辅导课
            List<CourseExamine> courseExamine = iExamine.getCourseExamineById(Integer.parseInt(item_id),7);
            if (courseExamine.size() > 0) {
                Course course = iCourse.getCourseById(Integer.parseInt(item_id));
                if(Integer.parseInt(result) == 0) {
                    iCourse.setCourseState(Integer.parseInt(item_id),3);
                    TlsSigTest.SendMessage(uid,"您发布的课程“" + course.getCourse_name() + "”已经通过系统审核，快努力发高质量的视频展示您自己吧！");
                    if(course.getType() == 1) {
                        ClassRoom classRoom = new ClassRoom();
                        classRoom.setUser_id(course.getUser_id());
                        classRoom.setCourse_id(course.getId());
                        classRoom.setCourse_name(course.getCourse_name());
                        classRoom.setCourse_pic(course.getCourse_pic());
                        classRoom.setTeacher_name(course.getTeacher_name());
                        classRoom.setSubject(course.getSubject());
                        classRoom.setGrade(course.getGrade());
                        classRoom.setMax_person(10);
                        classRoom.setAll_hours(course.getAll_hours());
                        classRoom.setPrice_one_hour(course.getPrice_one_hour());
                        classRoom.setAll_charge(course.getAll_charge());
                        classRoom.setRefund_rule(course.getRefund_rule());
                        classRoom.setTutor_content(course.getTutor_content());
                        classRoom.setOutline(course.getOutline());
                        classRoom.setCreate_time((int) (new Date().getTime() / 1000));
                        classRoom.setType(1);
                        classRoom.setForm(1);
                        classRoom.setWay("线上");
                        iOrder.addClassRoom(classRoom);
                        course.setId(classRoom.getClass_id());
                        course.setWay("线上");
                        iOrder.ClassRoomCourse(course);
                        iOrder.setClassRoomIsTest(1,classRoom.getClass_id());
                        int class_number = getCurrentCount();
                        iOrder.addRoomNumber(classRoom.getClass_id(), class_number);
                    }
                } else {
                    iCourse.setCourseState(Integer.parseInt(item_id),2);
                    TlsSigTest.SendMessage(uid,"您发布的课程“" + course.getCourse_name() + "”未通过系统审核，未通过原因是“" + failure_content + "”。");

                }
            }
            iExamine.deleteCourseExamine(Integer.parseInt(item_id),7);
        } else if(type.equals("9")) {
            Record record = iRecord.get(Integer.parseInt(item_id));
            if(record == null) {
                return JSONResult.errorMsg("辅导课不存在");
            }
            if(record.getRecord_course_status() != 1) {
                return JSONResult.errorMsg("此状态无法审核");
            }
            if(Integer.parseInt(result) == 0) {
                TlsSigTest.SendMessage(uid,"您发布的录播课“" + record.getRecord_course_name() + "”已经通过系统审核，快努力发高质量的视频展示您自己吧！");
                iRecord.setRecordCourseStatue(Integer.parseInt(item_id),3);
                String videos = (String) record.getVideos();
                ObjectMapper mapper_temp = new ObjectMapper();
                List<RecordVideo> recordVideoList = null;
                try {
                    JavaType javaType = getCollectionType(ArrayList.class, RecordVideo.class);
                    recordVideoList = mapper_temp.readValue((String) record.getVideos(), javaType);
                    for(RecordVideo recordVideo : recordVideoList) {

                        try {
                            if(recordVideo.getWonderful_part() != null) {
                                WonderfulPart wonderfulPart = recordVideo.getWonderful_part();
                                TestRecordCourse testRecordCourse = new TestRecordCourse();
                                testRecordCourse.setUser_id(record.getUser_id());
                                testRecordCourse.setRecord_course_id(record.getRecord_course_id());
                                testRecordCourse.setClass_NO(recordVideo.getClass_number());
                                testRecordCourse.setClass_name(recordVideo.getClass_title());
                                testRecordCourse.setClass_url(recordVideo.getClass_url());
                                testRecordCourse.setTime_start(wonderfulPart.getTime_start());
                                testRecordCourse.setTime_end(wonderfulPart.getTime_end());
                                testRecordCourse.setPicture(wonderfulPart.getPicture());
                                iRecord.addTestRecordCourse(testRecordCourse);
                            }
                        } catch (Exception e) {
                            System.out.println(1111);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                TlsSigTest.SendMessage(uid,"您发布的录播课“" + record.getRecord_course_name() + "”没有通过系统审核");
                iRecord.setRecordCourseStatue(Integer.parseInt(item_id),2);
            }

        }
        return JSONResult.ok();
    }
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
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
            authentication.setAchivement(userExamine.getAchievement());
            authentication.setConfirmation_pic(userExamine.getConfirmation_letter());
            authentication.setEnterprise_card(userExamine.getLicense());
            authentication.setGraduation_card(userExamine.getGraduation_card());
            authentication.setIdentity_card(userExamine.getIdentity_card());
            authentication.setIdentity_info(userExamine.getAuth_info());
            authentication.setShop_card(userExamine.getShop_prove());
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


    @RequestMapping("/course_examine")
    @ResponseBody
    JSONResult getCourseExamine() {
        List<CourseExamine> list = iExamine.getCourseExamine();
        return JSONResult.ok(list);
    }

    @RequestMapping("/record_examine")
    @ResponseBody
    JSONResult getRecordExamine() {
//        TlsSigTest.sendGroupMsg("1000000129","test123");
        List<Record> records = iRecord.getRecordByStatus(1);
        List<RecordExamine> list = new ArrayList<>();
        for(Record item : records) {
            RecordExamine temp = new RecordExamine();
            temp.setGrade_id((Integer)item.getGrade());
            temp.setGrade(SignMap.getGradeTypeByID((Integer)item.getGrade()));
            temp.setSubject_id((Integer)item.getSubject());
            temp.setSubject(SignMap.getGradeTypeByID((Integer)item.getSubject()));
            temp.setPrice(item.getPrice());
            temp.setRecord_course_id(item.getRecord_course_id());
            temp.setRecord_course_name(item.getRecord_course_name());
            temp.setRecord_course_pic(item.getRecord_course_pic());
            temp.setRecord_course_status(item.getRecord_course_status());
            temp.setVideo_count(item.getVideo_count());
            temp.setUid(item.getUser_id());
            list.add(temp);
        }
        return JSONResult.ok(list);
    }
}


