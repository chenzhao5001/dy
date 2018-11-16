package com.guidesound.controller;

import com.guidesound.Service.IVideoService;
import com.guidesound.dao.*;
import com.guidesound.dto.VideoDTO;
import com.guidesound.find.VideoFind;
import com.guidesound.models.*;
import com.guidesound.resp.ListResp;
import com.guidesound.util.*;
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
        String tmpPath = req.getServletContext().getRealPath("")
                + "/tmp/";
        File filePath = new File(tmpPath);
        if (!filePath.exists() && !filePath.isDirectory()) {
            filePath.mkdir();
        }
        String savaPath = tmpPath + System.currentTimeMillis() + "_" + ToolsFunction.getRandomString(4) + ".mp4";
        ToolsFunction.videoChange(videoDTO.getViedo_url(),savaPath);

        filePath = new File(savaPath);
        String url = ToolsFunction.upFileToServicer(filePath);
        Video video = new Video();
        User user = (User)req.getAttribute("user_info");
        video.setUser_id(user.getId());
        video.setTitle(videoDTO.getTitle());
        video.setSubject(Integer.parseInt(videoDTO.getSubject()));
        video.setWatch_type(Integer.parseInt(videoDTO.getWatch_type()));
        video.setContent(videoDTO.getContent());
        video.setDuration(Integer.parseInt(videoDTO.getDuration()));

        video.setPic_up_path(videoDTO.getPicture_url());
        video.setVideo_up_path(videoDTO.getViedo_url());
        video.setVideo_temp_path("");
        video.setVideo_show_path(url);

        video.setCreate_time((int) (new Date().getTime() / 1000));
        video.setUpdate_time((int) (new Date().getTime() / 1000));
        videoService.addVideo(video);
        return JSONResult.ok(url);
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
        int end = (iPage -1)*iSize + iSize;
//        if( sType == "1") {
//            List<VideoShow> list_temp  = iVideo.selectVideo(status,begin,end);
//        } else {
//
//        }
//        List<VideoShow> list_temp  = iVideo.selectVideo(status,begin,end);

        return null;
    }

    /**
     * 获取视频列表
     */
    @RequestMapping(value = "/video_list")
    public @ResponseBody
    JSONResult selectVideo(
            HttpServletRequest request,
            String status, String content, String page, String size, String s_type,String subject,String grade_class) {
        status = status == null ? "0":status;
        content = content == "" ? null:content;
        int iPage = page == null ? 1:Integer.parseInt(page);
        int iSize = size == null ? 20:Integer.parseInt(size);
        int sType = s_type == null ? 0:Integer.parseInt(s_type);

        int begin = (iPage -1)*iSize;
        int end = (iPage -1)*iSize + iSize;
        VideoFind videoFind = new VideoFind();
        videoFind.setContent(content);
        videoFind.setStatus(0);
        videoFind.setsType(1);
        videoFind.setBegin(begin);
        videoFind.setEnd(end);


        int count_temp = iVideo.findVideoCount(videoFind);
        if (count_temp == 0) {
            ListResp ret = new ListResp();
            ret.setCount(count_temp);
            ret.setList(null);
            return JSONResult.ok(ret);
        }



//        List<VideoShow> list_temp  = iVideo.selectVideo(status,begin,end);

        List<VideoShow> list_temp  = iVideo.findVideo(videoFind);


        List<Integer> idList = new ArrayList<>();
        for(VideoShow item:list_temp) {
            item.setWatch_type_name(SignMap.getWatchById(item.getWatch_type()));
            item.setSubject_name(SignMap.getSubjectTypeById(item.getSubject()));
            idList.add(item.getId());
        }
        Cookie[] cookies = request.getCookies();
        int user_id = 0;
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    user_id = TockenUtil.getUserIdByTocket(token);
                }
            }
        }
        if(user_id != 0) {
            List<Integer> videoIds = iVideo.getCollectionVideoById(user_id);
            if(videoIds != null) {
                for(VideoShow item:list_temp) {
                    if(videoIds.contains(item.getId())) {
                        item.setFollow(true);
                    }
                }
            }
        }

        List<User> heads = iVideo.getUserHeadByIds(idList);
        Map<Integer,String> headMap = new HashMap<>();
        for (User user : heads) {
            headMap.put(user.getId(),user.getHead());
        }

        for(VideoShow item:list_temp) {
            if(headMap.containsKey(item.getUser_id())) {
                item.setUser_head(headMap.get(item.getUser_id()));
            }
        }

        ListResp ret = new ListResp();
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
    public JSONResult addPraise(String video_id) {
        if(video_id == null) {
            return JSONResult.errorMsg("缺少参数");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User)request.getAttribute("user_info");
        int count = iVideoPraise.getVideoPraise(Integer.parseInt(video_id),currentUser.getId());
        if(count > 0) {
            return JSONResult.errorMsg("此用户已经评论过该视频");
        }

        iVideoPraise.addMainPraise(Integer.parseInt(video_id));
        iVideoPraise.addPraise(currentUser.getId(),Integer.parseInt(video_id),(int)(new Date().getTime() /1000),(int)(new Date().getTime() /1000));
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
        if ( count == 0) {
            iVideoCollection.addCollection(currentUser.getId(),Integer.parseInt(video_id),(int) (new Date().getTime() / 1000),0);
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
        iVideoCollection.deleteCollection(currentUser.getId(),Integer.parseInt(video_id));
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
    public @ResponseBody ServiceResponse delete(HttpServletRequest request) {
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

//    @RequestMapping(value = "/verify")
//    public String verify(ModelMap model) {
//        RepList repList = selectVideo("0","","","");
//        model.addAttribute("video_list",repList.getList());
//        return "verify";
//    }
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
