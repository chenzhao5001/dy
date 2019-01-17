package com.guidesound.controller;

import com.guidesound.Service.IVideoService;
import com.guidesound.dao.*;
import com.guidesound.dto.VideoDTO;
import com.guidesound.find.VideoFind;
import com.guidesound.models.*;
import com.guidesound.resp.ListResp;
import com.guidesound.util.*;
import com.qcloud.Utilities.Json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.guidesound.util.ToolsFunction.URLDecoderString;
import static com.guidesound.util.ToolsFunction.getURLEncoderString;


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

        User currentUser = (User)req.getAttribute("user_info");
        video.setSubject(currentUser.getSubject());
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

        String videoTemp = iVideo.getFinishVideoByUserGuid(user_guid);
        if(videoTemp == null) {
            videoTemp = "";
        }
        List<VideoShow> video_list = new ArrayList<>();
        List<VideoShow> all_list = new ArrayList<>();
        int grade = 0;
        int user_id = getCurrentUserId();
        if(user_id != 0) {
            UserInfo userInfo = iUser.getUser(user_id);
            grade = userInfo.getChannel_stage();
        }

        int other_grade1 = -1;
        int other_grade2 = -1;
        if(grade != 0) {
            other_grade1 = grade/100 *100 + 99;
            if(other_grade1 == 399 || other_grade1 == 499) {
                other_grade2 = 498;
            }
        }
        Collections.shuffle(video_list);
        if (channel.equals("1")) {
            all_list = iVideo.getRecommendVideo(grade,other_grade1,other_grade2);
            Collections.shuffle(all_list);
            video_list = getRecVideos(all_list,user_guid);
        } else {
            List<String> list = Arrays.asList(channel.split(","));
            all_list = iVideo.getVideoByChannel(list,grade,other_grade1,other_grade2);
            Collections.shuffle(all_list);
            video_list = getRecVideos(all_list,user_guid);
        }

        improveVideoList(video_list);
        List<Integer> videoIDs = new ArrayList<>();
        for(VideoShow item : video_list) {
            videoIDs.add(item.getId());
        }
        if(videoIDs.size() > 0) {
            iVideo.addRecommend(videoIDs);
        }
        ListResp ret = new ListResp();
        ret.setCount(video_list.size());
        ret.setList(video_list);

        return JSONResult.ok(ret);
    }

    List<VideoShow> getRecVideos(List<VideoShow> all_list,String user_guid) {
        if(all_list.size() < 1) {
            return new ArrayList<>();
        }
        List<VideoShow> video_list = new ArrayList<>();
        String videoTemp = iVideo.getFinishVideoByUserGuid(user_guid);
        ArrayList<String> arrVidoe = new ArrayList<>();
        if(videoTemp != null && !videoTemp.equals("")) {
            arrVidoe =  new ArrayList<String>(Arrays.asList(videoTemp.split(",")));
        }
        List<VideoShow> retList = new ArrayList<>();
        for (VideoShow item : all_list) {
            if(!arrVidoe.contains(item.getId())) {
                retList.add(item);
                if(retList.size() >= 20) {
                    break;
                }
            }
        }
        return retList;
    }

    List<VideoShow> getRecVideo(List<VideoShow> all_list,String user_guid) {
        List<VideoShow> video_list = new ArrayList<>();

        String videoTemp = iVideo.getPushVideoByUserGuid(user_guid);
        boolean insertFlag = false;
        if(videoTemp == null) {
            videoTemp = "";
            insertFlag = true;
        }
        ArrayList<String> arrVidoe = new ArrayList<>();
        if(!videoTemp.equals("")) {
            arrVidoe =  new ArrayList<String>(Arrays.asList(videoTemp.split(",")));
        }

        for (int i = 0; i < all_list.size(); i++) {
            if (video_list.size() == 20) {
                break;
            }
            for (int j = i; j < all_list.size(); j++) {
                if (video_list.size() == 20) {
                    break;
                }
                int temp = all_list.get(j).getId();
                if (arrVidoe.contains(String.valueOf(temp))) {
                    continue;
                }
                if (video_list.size() == 0) {
                    video_list.add(all_list.get(j));
                    arrVidoe.add(String.valueOf(all_list.get(j).getId()));
                    continue;
                }
                if (all_list.get(j).getUser_id() != video_list.get(video_list.size() -1).getUser_id()) {
                    video_list.add(all_list.get(j));
                    arrVidoe.add(String.valueOf(all_list.get(j).getId()));
                    i++;
                    if (j != i) {
                        Collections.swap(all_list, i, j);
                    }
                    continue;
                }
                if (j == all_list.size() - 1) {
                    video_list.add(all_list.get(i));
                    arrVidoe.add(String.valueOf(all_list.get(j).getId()));
                }
            }
        }

        String strVideos= StringUtils.join(arrVidoe, ",");
        strVideos += ",";
        if(insertFlag == true) {
            iVideo.insertPushVideo(user_guid,strVideos);
        } else {
            iVideo.updatePushVidoe(user_guid,strVideos);
        }

        return video_list;
    }

    /**
     * 获取视频列表
     */
    @RequestMapping(value = "/video_list")
    public @ResponseBody
    JSONResult selectVideo(
            String status,
            String content,
            String page,
            String size,
            String s_type,
            String subject,
            String grade_class,
            String video_id,
            String user_id,
            String user_name) {
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
            grade_class_list = Arrays.asList(grade_class.split(","));
        }
        ListResp ret = new ListResp();

        List<Integer> user_ids = null;
        if(user_name != null) {
            user_ids = iUser.getUserIdsByName(user_name);
            if(user_ids.size() < 1) {
                ret.setCount(0);
                ret.setList(new ArrayList<>());
                return JSONResult.ok(ret);
            }
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
        videoFind.setUser_id(user_id);
        videoFind.setVideo_id(video_id);
        videoFind.setUser_ids(user_ids);
        int count_temp = iVideo.findVideoCount(videoFind);
        if (count_temp == 0) {
            ret.setCount(0);
            ret.setList(new ArrayList<>());
            return JSONResult.ok(ret);
        }
        List<VideoShow> list_temp  = iVideo.findVideo(videoFind);
        improveVideoList(list_temp);
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
    public JSONResult addPraise(String video_id,String type) throws IOException {
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
            Video video = iVideo.getVideo(Integer.parseInt(video_id));
            TlsSigTest.PushMessage(String.valueOf(video.getUser_id()),"1");
            iVideoPraise.addMainPraise(Integer.parseInt(video_id));
            iVideoPraise.addPraise(currentUser.getId(),Integer.parseInt(video_id),(int)(new Date().getTime() /1000),(int)(new Date().getTime() /1000));

            UserAction userAction = new UserAction();
            userAction.setFrom_user_id(currentUser.getId());
            userAction.setTo_user_id(video.getUser_id());
            userAction.setType(102);
            userAction.setContent_id(Integer.parseInt(video_id));
            userAction.setCreate_time((int) (new Date().getTime() /1000));

            iUser.addUserAction(userAction);

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
    public JSONResult chatVideo(String video_id,String content) throws IOException {

        if(video_id == null || content == null) {
            return JSONResult.errorMsg("缺少参数");
        }

        System.out.println(iVideoChat);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        Video video = iVideo.getVideo(Integer.parseInt(video_id));
        TlsSigTest.PushMessage(String.valueOf(video.getUser_id()),"3");

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

        improveVideoList(list);

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
        int currentUserID = getCurrentUserId();
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

        improveVideoList(list);
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

    @RequestMapping(value = "/video_play_finish")
    @ResponseBody
    JSONResult videoPlayFinish(String user_guid,String video_id) {
        if(user_guid == null || video_id == null) {
            return JSONResult.errorMsg("缺少 user_guid 或 video_id");
        }
        List<UserPlayFinish> list = iVideo.getUserPlayInfo(user_guid);
        if(list.size() > 0) {
            UserPlayFinish userPlayFinish = list.get(0);
            String videos = userPlayFinish.getFinish_videos();
            ArrayList<String> arrVidoe = new ArrayList<>();
            if(!videos.equals("")) {
                arrVidoe =  new ArrayList<String>(Arrays.asList(videos.split(",")));
                if(!arrVidoe.contains(video_id)) {
                    arrVidoe.add(video_id);
                }
            } else {
                arrVidoe.add(video_id);
            }
            String strVideos = StringUtils.join(arrVidoe, ",");
            iVideo.upPlayFinish(userPlayFinish.getId(),strVideos);

        } else {
            iVideo.createPlayFinish(user_guid,video_id, (int) (new Date().getTime()/1000));
        }
        return JSONResult.ok();
    }

    void improveVideoList(List<VideoShow> list_temp) {

        int user_id = getCurrentUserId();
        List<Integer> idList = new ArrayList<>();
        for(VideoShow item:list_temp) {
            item.setWatch_type_name(SignMap.getGradeTypeByID(item.getWatch_type()));
            item.setSubject_name(SignMap.getSubjectTypeById(item.getSubject()));
            String video_temp = item.getVideo_show_path().replace("cos.ap-beijing","file");
//            String pic_temp = item.getPic_up_path().replace("cos.ap-beijing","file");
            String pic_temp = item.getPic_up_path().replace("cos.ap-beijing","image");
            item.setVideo_show_path(video_temp);
            item.setPic_up_path(pic_temp);
            if(item.getExamine_status() == 3) {
                item.setExamine_status(0);
            }
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
                    item.setUser_type_name(SignMap.getUserTypeById(userMap.get(item.getUser_id()).getType()));
                    item.setUser_subject(SignMap.getSubjectTypeById(userMap.get(item.getUser_id()).getSubject()));
                    item.setUser_grade(SignMap.getWatchById(userMap.get(item.getUser_id()).getGrade_level()));
                    item.setUser_level(SignMap.getUserLevelById(userMap.get(item.getUser_id()).getLevel()));

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

    /**
     * 评论视频
     */
    @RequestMapping("/comment")
    @ResponseBody
    JSONResult Comment(String video_id,String first_user_id,
                       String first_comment,String second_user_id,String second_comment) throws IOException {
        if(video_id == null || first_user_id == null || first_comment == null) {
            return JSONResult.errorMsg("缺少参数");
        }

        TlsSigTest.PushMessage(String.valueOf(first_user_id),"3");
        second_user_id = second_user_id == null ? "0" : second_user_id;
        second_comment = second_comment == null ? "" : second_comment;

        first_comment = getURLEncoderString(first_comment);
        second_comment = getURLEncoderString(second_comment);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        VideoComment videoComment = new VideoComment();
        videoComment.setVideo_id(Integer.parseInt(video_id));
        videoComment.setFirst_user_id(Integer.parseInt(first_user_id));
        videoComment.setFirst_comment(first_comment);
        videoComment.setSecond_user_id(Integer.parseInt(second_user_id));
        videoComment.setSecond_comment(second_comment);
        videoComment.setCreate_time((int)(new Date().getTime() / 1000));


        UserAction userAction = new UserAction();
        userAction.setFrom_user_id(Integer.parseInt(first_user_id));
        Video video = iVideo.getVideo(Integer.parseInt(video_id));
        if(Integer.parseInt(second_user_id) == 0) {
            userAction.setTo_user_id(video.getId());
        } else {
            userAction.setTo_user_id(Integer.parseInt(second_user_id));
        }
        userAction.setType(101);
        userAction.setContent_id(Integer.parseInt(video_id));
        userAction.setCreate_time((int) (new Date().getTime() /1000));
        userAction.setContent_url(video.getPic_up_path());
        userAction.setFirst_comment(first_comment);
        userAction.setSecond_comment(second_comment);
        iUser.addUserAction(userAction);


        iVideo.addComment(videoComment);
        iVideo.addMainComment(Integer.parseInt(video_id));
        videoComment.setFirst_comment(URLDecoderString(first_comment));
        videoComment.setSecond_comment(URLDecoderString(second_comment));
        return JSONResult.ok(videoComment);
    }

    /**
     *获得评论列表
     */
    @RequestMapping("/comment_list")
    @ResponseBody
    JSONResult getCommentList(String video_id,String page,String size) {

        if (video_id == null) {
            return JSONResult.errorMsg("缺少 video_id");
        }

        int iPage = (page == null || page.equals("")) ?1:Integer.parseInt(page);
        int iSize = (size == null || size.equals("")) ? 20:Integer.parseInt(size);
        int begin = (iPage - 1)*iSize;
        int end = iSize;

        int count = iVideo.CommentCount(Integer.parseInt(video_id));

        List<VideoComment> list = iVideo.getCommentList(Integer.parseInt(video_id),begin,end);
        if (list.size() > 0) {
            List<Integer> user_ids = new ArrayList<>();
            for (VideoComment videoComment : list) {
                videoComment.setFirst_comment(URLDecoderString(videoComment.getFirst_comment()));
                videoComment.setSecond_comment(URLDecoderString(videoComment.getSecond_comment()));
                if(!user_ids.contains(videoComment.getFirst_user_id())){
                    user_ids.add(videoComment.getFirst_user_id());
                }
                if(!user_ids.contains(videoComment.getSecond_user_id())){
                    user_ids.add(videoComment.getSecond_user_id());
                }
            }

            List<UserInfo> user_list = iUser.getUserByIds(user_ids);
            List<Integer> comment_ids = new ArrayList<>();
            int currentUserID = getCurrentUserId();
            if (currentUserID != 0) {
                comment_ids = iVideo.getPraiseComment(currentUserID);
            }
            Map<Integer,UserInfo> usersMap = new HashMap<>();
            for (UserInfo userInfo :user_list) {
                usersMap.put(userInfo.getId(),userInfo);
            }

            for (VideoComment videoComment : list) {
                if( comment_ids.contains(videoComment.getId())) {
                    videoComment.setPraise(true);
                }
                if(usersMap.get(videoComment.getFirst_user_id()) != null) {
                    videoComment.setFirst_user_head(usersMap.get(videoComment.getFirst_user_id()).getHead());
                    videoComment.setFirst_user_name(usersMap.get(videoComment.getFirst_user_id()).getName());
                } else {
                    videoComment.setFirst_user_head("");
                    videoComment.setFirst_user_name("");
                }

                if(usersMap.get(videoComment.getSecond_user_id()) != null) {
                    videoComment.setSecond_user_head(usersMap.get(videoComment.getSecond_user_id()).getHead());
                    videoComment.setSecond_user_name(usersMap.get(videoComment.getSecond_user_id()).getName());
                } else {
                    videoComment.setSecond_user_head("");
                    videoComment.setSecond_user_name("");
                }
            }

        }


        ListResp listResp = new ListResp();
        listResp.setCount(count);
        listResp.setList(list);
        return JSONResult.ok(listResp);
    }

    /**
     * 为评论点赞
     */
    @RequestMapping("/praise_comment")
    @ResponseBody
    JSONResult praiseComment(String comment_id) throws IOException {
        if (comment_id == null) {
            return JSONResult.errorMsg("缺少 comment_id 参数");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");

        if(null == iVideo.findVideoCommentPraise(currentUser.getId(),Integer.parseInt(comment_id))) {
            String first_user_id = iVideo.getUserIdByCommentId(Integer.valueOf(comment_id));
            TlsSigTest.PushMessage(first_user_id,"7");

            UserAction userAction = new UserAction();
            userAction.setFrom_user_id(Integer.parseInt(first_user_id));
            userAction.setTo_user_id(currentUser.getId());
            userAction.setType(103);
            userAction.setContent_id(Integer.parseInt(comment_id));
            userAction.setCreate_time((int) (new Date().getTime() /1000));
            iUser.addUserAction(userAction);

            iVideo.praiseVideoComment(currentUser.getId(),Integer.parseInt(comment_id), (int) (new Date().getTime() /1000));
            iVideo.praiseMainVideoComment(Integer.parseInt(comment_id));
        } else {
            return JSONResult.errorMsg("已经点过赞了");
        }
        return JSONResult.ok();
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
