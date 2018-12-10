package com.guidesound.controller;

import com.guidesound.Service.IVideoService;
import com.guidesound.dao.*;
import com.guidesound.dto.VideoDTO;
import com.guidesound.find.VideoFind;
import com.guidesound.models.*;
import com.guidesound.resp.ListResp;
import com.guidesound.util.*;
//import com.qcloud.Utilities.Json.JSONObject;
import com.qcloud.Utilities.Json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 视频控制器
 */

@Controller
@RequestMapping("/video")
public class VideoController extends BaseController {

    @Autowired
    private IVideoService videoService;
    @Autowired
    private IVideo iVideo;
    @Autowired
    private IVideoPlay iVideoPlay;
    @Autowired
    private IVideoPraise iVideoPraise;
    @Autowired
    private IVideoChat iVideoChat;
    @Autowired
    private IVideoCollection iVideoCollection;
    @Autowired
    private IUser iUser;


    /**
     * 视频上传
     */
    @RequestMapping(value = "/add")
    public @ResponseBody
    JSONResult addVideo(@Valid VideoDTO videoDTO,BindingResult result) throws IOException {

        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            String err = "";
            for (ObjectError error : errors) {
                err += error.getDefaultMessage();
            }
            return JSONResult.errorMsg(err);
        }
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Video video = new Video();
        User user = (User)req.getAttribute("user_info");
        video.setUser_id(user.getId());
        video.setTitle(videoDTO.getTitle());
        video.setSubject(Integer.parseInt(videoDTO.getSubject()));
        video.setWatch_type(Integer.parseInt(videoDTO.getWatch_type()));
        video.setContent(videoDTO.getContent());
        video.setDuration(Integer.parseInt(videoDTO.getDuration()));

        video.setResolution_w(Integer.parseInt(videoDTO.getResolution_w()));
        video.setResolution_h(Integer.parseInt(videoDTO.getResolution_h()));

        video.setPic_up_path(videoDTO.getPicture_url());
        video.setVideo_up_path(videoDTO.getViedo_url());
        video.setVideo_temp_path("");
        video.setVideo_show_path("");
        video.setPic_cut_path(videoDTO.getPic_cut_url());

        video.setCreate_time((int) (new Date().getTime() / 1000));
        video.setUpdate_time((int) (new Date().getTime() / 1000));
        videoService.addVideo(video);
        return JSONResult.ok();
    }

    @RequestMapping(value = "/subject_list")
    @ResponseBody
    public JSONResult getSubjectList() {
        return JSONResult.ok(SignMap.getSubjectClassifyList());
    }

    @RequestMapping(value = "/watch_type_list")
    @ResponseBody
    public JSONResult getWatchType() {
        return JSONResult.ok(SignMap.getWatchList());
    }

    @RequestMapping(value = "/find_video")
    @ResponseBody
    JSONResult findVideo(HttpServletRequest request) {
        String sType = request.getParameter("s_type");
        if(request.getParameter("s_type") == null ) {
            return JSONResult.errorMsg("缺少 s_type 参数");
        }
        String content = request.getParameter("content") == null ? "":request.getParameter("content");
        int iPage = request.getParameter("page") == null ? 1:Integer.parseInt(request.getParameter("page"));
        int iSize = request.getParameter("size") == null ? 1:Integer.parseInt(request.getParameter("size"));

        int count_temp = iVideo.getVideoCount("0");
        int begin = (iPage -1)*iSize;
        int end = iSize;
//        if( sType == "1") {
//            List<VideoShow> list_temp  = iVideo.selectVideo(status,begin,end);
//        } else {
//
//        }
//        List<VideoShow> list_temp  = iVideo.selectVideo(status,begin,end);

        return null;
    }


    @RequestMapping(value = "/list_by_channel")
    public @ResponseBody
    JSONResult selectVideoByChannel(String channel,String user_guid,String page, String size) {
        if(channel == null || user_guid == null) {
            return JSONResult.errorMsg("缺少channel 或 user_guid");
        }
        int iPage = (page == null ? 1:Integer.parseInt(page));
        int iSize = (size == null ? 20:Integer.parseInt(size));
        int begin = (iPage -1)*iSize;
        int end =  iSize;

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        int currentUserID = 0;

        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    currentUserID = TockenUtil.getUserIdByTocket(token);
                }
            }
        }

        List<VideoShow> video_list = new ArrayList<>();
        if(channel.equals("1")) {
            List<VideoShow> all_list = iVideo.getRecommendVideo();
            for (int i = 0; i < all_list.size(); i++) {
                if(video_list.size() == 0) {
                    video_list.add(all_list.get(0));
                } else {
                    for(int j = i; j < all_list.size(); j++) {
                        if(all_list.get(j).getUser_id() != video_list.get(i-1).getUser_id()) {
                            video_list.add(all_list.get(j));
                            if(j != i) {
                                Collections.swap(all_list,i,j);
                            }
                            break;
                        }
                        if(j == all_list.size() -1) {
                            video_list.add(all_list.get(i));
                        }
                    }

                }
//                if(video_list.size() == 20) {
//                    break;
//                }
            }
        } else {
            List<String> list = Arrays.asList(channel.split(","));
            int count_temp = iVideo.getVideoNumByChannel(list);
            if (count_temp == 0) {
                ListResp ret = new ListResp();
                ret.setCount(0);
                ret.setList(new ArrayList<>());
                return JSONResult.ok(ret);
            }

            video_list = iVideo.getVideoByChannel(list,begin,end);
        }


        improveVideoList(currentUserID,video_list);


        ListResp ret = new ListResp();
        ret.setCount(video_list.size());
        ret.setList(video_list);

        return JSONResult.ok(ret);
    }
    /**
     * 获取视频列表
     */
    @RequestMapping(value = "/video_list")
    public @ResponseBody
    JSONResult selectVideo(
            String status, String content, String page, String size, String s_type,String subject,String grade_class) {
        status = (status == null || status.equals("")) ? null:status;
        String title = (content == null || content.equals("")) ? null:content;
        int iPage = page == null ? 1:Integer.parseInt(page);
        int iSize = size == null ? 20:Integer.parseInt(size);
        int sType = s_type == null ? 0:Integer.parseInt(s_type);
        List<String> subject_list = null;
        List<String> grade_class_list = null;
        if(subject != null && !subject.equals("")) {
            subject_list = Arrays.asList(subject.split(","));
        }
        if(grade_class != null && !grade_class.equals("")) {
            grade_class_list = Arrays.asList(subject.split(","));
        }


        int begin = (iPage -1)*iSize;
        int end = iSize;

        VideoFind videoFind = new VideoFind();
        videoFind.setTitle(title);
        videoFind.setStatus(status);
        videoFind.setsType(1);
        videoFind.setBegin(begin);
        videoFind.setEnd(end);
        videoFind.setSubject_list(subject_list);
        videoFind.setGrade_class_list(grade_class_list);
        videoFind.setsType(sType);
        int count_temp = iVideo.findVideoCount(videoFind);
        ListResp ret = new ListResp();
        if (count_temp == 0) {
            ret.setCount(0);
            ret.setList(new ArrayList<>());
            return JSONResult.ok(ret);
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        int currentUserID = 0;

        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    currentUserID = TockenUtil.getUserIdByTocket(token);
                }
            }
        }
        List<VideoShow> list_temp  = iVideo.findVideo(videoFind);
        improveVideoList(currentUserID,list_temp);
        ret.setCount(count_temp);
        ret.setList(list_temp);
        return JSONResult.ok(ret);
    }


    @RequestMapping(value = "/add_play")
    @ResponseBody
    public JSONResult addPlay(String video_id) {
        if(video_id == null) {
            return JSONResult.errorMsg("缺少参数 video_id");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");

        iVideoPlay.addMainPlay(Integer.parseInt(video_id));
        iVideoPlay.addPlay(currentUser.getId(),Integer.parseInt(video_id),(int)(new Date().getTime() /1000),(int)(new Date().getTime() /1000));
        return JSONResult.ok();
    }

    @RequestMapping(value = "/add_praise")
    @ResponseBody
    public JSONResult addPraise(String video_id,String type) {
        if(video_id == null || type == null) {
            return JSONResult.errorMsg("缺少参数");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        int count = iVideoPraise.getVideoPraise(Integer.parseInt(video_id),currentUser.getId());

        if(type.equals("1")) {
            if(count > 0) {
                return JSONResult.errorMsg("此用户已经赞过该视频");
            }
            iVideoPraise.addMainPraise(Integer.parseInt(video_id));
            iVideoPraise.addPraise(currentUser.getId(),Integer.parseInt(video_id),(int)(new Date().getTime() /1000),(int)(new Date().getTime() /1000));
        } else if(type.equals("2")){
            if(count > 0) {
                iVideoPraise.reduceMainPraise(Integer.parseInt(video_id));
                iVideoPraise.reducePraise(currentUser.getId(),Integer.parseInt(video_id));
            }
        }
        return JSONResult.ok();
    }


    @RequestMapping(value = "/chat_video")
    @ResponseBody
    public JSONResult chatVideo(String video_id,String content) {

        if(video_id == null || content == null) {
            return JSONResult.errorMsg("缺少参数");
        }

        System.out.println(iVideoChat);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");

        iVideoChat.chatMainVideo(Integer.parseInt(video_id));
        iVideoChat.chatVideo(currentUser.getId()
                ,Integer.parseInt(video_id)
                ,content
                ,(int)(new Date().getTime()/1000) );
        return JSONResult.ok();
    }


    /**
     *获取用户上传视频列表
     */
    @RequestMapping(value = "/get_list")
    public @ResponseBody RepList  getVideoList(HttpServletRequest request) {
        String userId = request.getParameter("user_id");
        RepList rep = new RepList();
        if (userId == null) {
            rep.setMsg("缺少参数");
            rep.code = 201;
            return rep;
        }

        List<Video> list = videoService.getVideoList(Integer.parseInt(userId));
        rep.setCode(200);
        rep.setMsg("OK");
        rep.setList(list);
        return rep;
    }



    @RequestMapping(value = "/get_info")
    public @ResponseBody RepVideo  getVideo(HttpServletRequest request) {
        String id = request.getParameter("id");
        RepVideo rsp = new RepVideo();
        if(id == null) {
            rsp.msg = "缺少视频id参数";
            rsp.code = 201;
            return rsp;
        }
        Video video = videoService.getVideo(Integer.parseInt(id));
        rsp.code = 200;
        rsp.msg = "ok";
        rsp.video = video;
        return rsp;
    }

    /**
     * 收藏视频
     */
    @RequestMapping(value = "/collection")
    @ResponseBody
    public JSONResult collectionVideo(String video_id) {
        if ( video_id == null ) {
            return JSONResult.errorMsg("缺少参数video_id ");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        int count = iVideoCollection.getVideoCollection(Integer.parseInt(video_id),currentUser.getId());
        if(count > 0) {
            return JSONResult.errorMsg("此视频已经收藏过了 ");
        }
        if ( count == 0) {
            iVideoCollection.addCollection(currentUser.getId(),Integer.parseInt(video_id),(int) (new Date().getTime() / 1000),0);
            iVideoCollection.addMainCollection(Integer.parseInt(video_id));
        }

        return JSONResult.ok();
    }

    /**
     * 取消收藏
     */
    @RequestMapping(value = "/delete_collection")
    @ResponseBody
    public JSONResult deleteCollection(String video_id) {

        if ( video_id == null ) {
            return JSONResult.errorMsg("缺少参数 video_id");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        int count = iVideoCollection.getVideoCollection(Integer.parseInt(video_id),currentUser.getId());
        if(count > 0) {
            iVideoCollection.deleteCollection(currentUser.getId(),Integer.parseInt(video_id));
            iVideoCollection.deleteMainCollection(Integer.parseInt(video_id));
        }

        return JSONResult.ok();
    }

    @RequestMapping(value = "/set_status")
    public @ResponseBody ServiceResponse setVideoStatus(HttpServletRequest request) {
        System.out.println("setVideoStatus");
        String id = request.getParameter("id");
        String status = request.getParameter("status");
        ServiceResponse rsp = new ServiceResponse();
        if(id == null || status == null) {
            rsp.msg = "缺少参数";
            rsp.code = 204;
            return rsp;
        }

        if(status.equals("1")) {
            String savePath = request.getServletContext().getRealPath("");
            System.out.println(savePath);
            File file = new File(savePath);
            savePath = file.getParent() + "/video_show/";

            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss_");
            String videoName = df.format(new Date()) + java.util.UUID.randomUUID().toString() + ".mp4";
            Video video = iVideo.getVideo(Integer.parseInt(id));
            try {
                log.info("/home/ubuntu/wu/dyFFmpeg "
                        + video.getVideo_temp_path() + " " + savePath + videoName);

                ToolsFunction.exec("/home/ubuntu/wu/dyFFmpeg "
                        + video.getVideo_temp_path() + " " + savePath + videoName);

                String videoShowPath = "http://" + request.getServerName()
                        + ":"+ request.getServerPort()
                        + "/video_show/"
                        + videoName;
                iVideo.setVideoShowPath(Integer.parseInt(id),videoShowPath);
            } catch (InterruptedException e) {
                e.printStackTrace();
                rsp.msg = "视频转存失败";
                rsp.code = 202;
                return rsp;
            }
        }

        videoService.setVideoStatus(Integer.parseInt(id),Integer.parseInt(status));
        rsp.msg = "OK";
        rsp.code = 200;
        return rsp;
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public ServiceResponse delete(HttpServletRequest request) {
        String id = request.getParameter("id");
        ServiceResponse rsp = new ServiceResponse();
        if(id == null) {
            rsp.msg = "缺少视频id参数";
            rsp.code = 203;
            return rsp;
        }
        User user = (User)request.getAttribute("user_info");
        videoService.deleteVideo(Integer.parseInt(id),user.getId());
        rsp.msg = "OK";
        rsp.code = 200;
        return rsp;
    }

    /**
     * 获得热门搜索列表
     */
    @RequestMapping(value = "/hot_list")
    @ResponseBody
    public JSONResult hotSearch() {
        List<String> list = new ArrayList<>();
        list.add("小学数学");
        list.add("中学语文学");
        list.add("高中英语");
        list.add("高考数学");
        return JSONResult.ok(list);
    }

    /**
     * 获得热门搜索列表
     */
    @RequestMapping(value = "/share_video")
    @ResponseBody
    public JSONResult shareVideo(String video_id) {
        if (video_id == null) {
            return JSONResult.errorMsg("缺少video_id");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        int time = (int) (new Date().getTime() / 1000);
        iVideo.shareVideo(currentUser.getId(),Integer.parseInt(video_id),time);
        iVideo.addShareCount(Integer.parseInt(video_id));
        return JSONResult.ok();
    }

    /**
     * 收藏视频列表
     */
    @RequestMapping(value = "/collection_video")
    @ResponseBody
    public JSONResult getMyCollectionVideo(String user_id,String page,String size) {
        if(user_id == null) {
            return JSONResult.errorMsg("缺少user_id");
        }
        int iPage = page == null ? 1:Integer.parseInt(page);
        int iSize = size == null ? 20:Integer.parseInt(size);
        int begin = (iPage -1)*iSize;
        int end = iSize;
        List<Integer> vidoe_ids = iVideo.getMyCollectionIds(Integer.parseInt(user_id));
        ListResp ret = new ListResp();
        if(vidoe_ids.size() == 0) {
            ret.setCount(0);
            ret.setList(new ArrayList<>());
            return JSONResult.ok(ret);
        }
        List<VideoShow> list = iVideo.myCollection(vidoe_ids,begin,end);
        if(list.size() == 0) {
            ret.setCount(0);
            ret.setList(new ArrayList<>());
            return JSONResult.ok(ret);
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        int currentUserID = 0;
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    currentUserID = TockenUtil.getUserIdByTocket(token);
                }
            }
        }

        improveVideoList(currentUserID,list);

        ret.setCount(list.size());
        ret.setList(list);
        return JSONResult.ok(ret);
    }


    /**
     * 发布视频列表
     */
    @RequestMapping(value = "/publish_video")
    @ResponseBody
    public JSONResult getPublishVideo(String user_id,String page,String size) {

        if(user_id == null ) {
            return JSONResult.errorMsg("缺少user_id 获 state");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        int currentUserID = 0;
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    currentUserID = TockenUtil.getUserIdByTocket(token);
                }
            }
        }

        boolean flag = currentUserID == Integer.parseInt(user_id);
        int iPage = page == null ? 1:Integer.parseInt(page);
        int iSize = size == null ? 20:Integer.parseInt(size);
        int begin = (iPage -1)*iSize;
        int end = iSize;
        ListResp ret = new ListResp();
        int count = 0;
        if(flag) {
            count = iVideo.getPublishVidoeCountByUserId_2(Integer.parseInt(user_id));
        } else {
            count = iVideo.getPublishVidoeCountByUserId(Integer.parseInt(user_id));
        }
        if(count == 0) {
            ret.setCount(0);
            ret.setList(new ArrayList<>());
            return JSONResult.ok(ret);
        }
        List<VideoShow> list = null;
        if(flag) {
            list = iVideo.getPublishVidoeByUserId_2(Integer.parseInt(user_id),begin,end);
        } else {
            list = iVideo.getPublishVidoeByUserId(Integer.parseInt(user_id),begin,end);
        }

        improveVideoList(currentUserID,list);

        ret.setCount(count);
        ret.setList(list);
        return JSONResult.ok(ret);
    }

    @RequestMapping(value = "/get_up_key")
    @ResponseBody
    JSONResult getUpKey(HttpServletResponse response) {

        TreeMap<String, Object> config = new TreeMap<String, Object>();
        config.put("SecretId", "AKIDkIbfU4YZXUDgttF7MPDl36vUw9E6o7GK");
        config.put("SecretKey", "zjHchX8UbSCj9MM7ORFo8uUpwoUw9ltq");
        config.put("durationInSeconds", 1800);
        StorageSts storageSts = new StorageSts();
        JSONObject credential = storageSts.getCredential(config);
        int code = credential.getInt("code");
        if ( code != 0 ) {
            return JSONResult.errorMsg(credential.getString("codeDesc"));
        }

        class RetData {
            String tmpSecretId;
            String tmpSecretKey;
            String sessionToken;
            String expiredTime;

            public String getTmpSecretId() {
                return tmpSecretId;
            }

            public void setTmpSecretId(String tmpSecretId) {
                this.tmpSecretId = tmpSecretId;
            }

            public String getTmpSecretKey() {
                return tmpSecretKey;
            }

            public void setTmpSecretKey(String tmpSecretKey) {
                this.tmpSecretKey = tmpSecretKey;
            }

            public String getSessionToken() {
                return sessionToken;
            }

            public void setSessionToken(String sessionToken) {
                this.sessionToken = sessionToken;
            }

            public String getExpiredTime() {
                return expiredTime;
            }

            public void setExpiredTime(String expiredTime) {
                this.expiredTime = expiredTime;
            }
        }

        JSONObject retObject = credential.getJSONObject("data");
        RetData retData = new RetData();
        retData.setTmpSecretId(retObject.getJSONObject("credentials").getString("tmpSecretId"));
        retData.setTmpSecretKey(retObject.getJSONObject("credentials").getString("tmpSecretKey"));
        retData.setSessionToken(retObject.getJSONObject("credentials").getString("sessionToken"));
        retData.setExpiredTime(String.valueOf(retObject.getInt("expiredTime")));

        return JSONResult.ok(retData);
    }

    void improveVideoList(int user_id,List<VideoShow> list_temp) {
        List<Integer> idList = new ArrayList<>();
        for(VideoShow item:list_temp) {
            item.setWatch_type_name(SignMap.getGradeTypeByID(item.getWatch_type()));
            item.setSubject_name(SignMap.getSubjectTypeById(item.getSubject()));
            String video_temp = item.getVideo_show_path().replace("cos.ap-beijing","file");
//            String pic_temp = item.getPic_up_path().replace("cos.ap-beijing","file");
            String pic_temp = item.getPic_up_path().replace("cos.ap-beijing","image");
            item.setVideo_show_path(video_temp);
            item.setPic_up_path(pic_temp);
            idList.add(item.getUser_id());
        }

        if(user_id != 0) {
            List<Integer> videoIds = iVideo.getCollectionVideoById(user_id);
            if(videoIds != null) {
                for(VideoShow item:list_temp) {
                    if(videoIds.contains(item.getId())) {
                        item.setCollection(true);
                    }
                }
            }
            videoIds = iVideo.getPraiseVideoById(user_id);
            if(videoIds != null) {
                for(VideoShow item:list_temp) {
                    if(videoIds.contains(item.getId())) {
                        item.setPraise(true);
                    }
                }
            }

            List<Integer> userIds = iUser.getFollowUsers(user_id);
            if(userIds != null) {
                for(VideoShow item:list_temp) {
                    if(userIds.contains(item.getUser_id())) {
                        item.setFollow(true);
                    }
                }
            }
        }

        if (idList != null && idList.size() > 0) {
            List<User> userList = iVideo.getUserHeadByIds(idList);
            Map<Integer,User> userMap = new HashMap<>();
            for (User user : userList) {
                userMap.put(user.getId(),user);
            }
            for(VideoShow item:list_temp) {
                if(userMap.containsKey(item.getUser_id())) {
                    item.setUser_head(userMap.get(item.getUser_id()).getHead());
                    item.setUser_name(userMap.get(item.getUser_id()).getName());
                    item.setUser_type(userMap.get(item.getUser_id()).getType());
                }
            }
        }
    }

    float getRecUser() {
        int time = (int) (new Date().getTime() / 1000) - 3600*24*5;
        int activeCount = iUser.getActiveUserByTime(time);
        int upVideoCount = iVideo.getVideoCountByTime(time);
        if(upVideoCount != 0) {
            return (float) (upVideoCount*1.0 / upVideoCount);
        }
        return 0;
    }
}

class RepList {
    int code;
    String msg;
    List<Video> list;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Video> getList() {
        return list;
    }

    public void setList(List<Video> list) {
        this.list = list;
    }
}

class RepVideo {
    int code;
    String msg;
    Video video;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }
}
