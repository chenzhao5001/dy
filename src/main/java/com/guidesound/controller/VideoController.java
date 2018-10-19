package com.guidesound.controller;

import com.guidesound.Service.IVideoService;
import com.guidesound.dao.IVideo;
import com.guidesound.dto.VideoDTO;
import com.guidesound.models.User;
import com.guidesound.models.Video;
import com.guidesound.models.VideoShow;
import com.guidesound.util.JSONResult;
import com.guidesound.util.ServiceResponse;
import com.guidesound.util.SignMap;
import com.guidesound.util.ToolsFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



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
        return JSONResult.ok(SignMap.getSubjectList());
    }

    @RequestMapping(value = "/watch_type_list")
    @ResponseBody
    public JSONResult getWatchType() {
        return JSONResult.ok(SignMap.getWatchList());
    }

    /**
     * 获取视频列表
     */
    @RequestMapping(value = "/video_list")
    public @ResponseBody
    JSONResult selectVideo(String status, String content, String page, String size) { ;
        status = status == null ? "0":status;
        content = content == null ? "":content;
        int iPage = page == null ? 1:Integer.parseInt(page);
        int iSize = size == null ? 20:Integer.parseInt(size);

        int count_temp = iVideo.getVideoCount(status);
        int begin = (iPage -1)*iSize;
        int end = (iPage -1)*iSize + iSize;

        List<VideoShow> list_temp  = iVideo.selectVideo(status,begin,end);
        class Temp {
            int count = count_temp;
            List<VideoShow> list = list_temp;
            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public List<VideoShow> getList() {
                return list;
            }

            public void setList(List<VideoShow> list) {
                this.list = list;
            }
        }
        return JSONResult.ok(new Temp());
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
