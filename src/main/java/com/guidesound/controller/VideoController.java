package com.guidesound.controller;

import com.guidesound.Service.IVideoService;
import com.guidesound.dao.IVideo;
import com.guidesound.models.User;
import com.guidesound.models.Video;
import com.guidesound.util.ServiceResponse;
import com.guidesound.util.ToolsFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
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
    ServiceResponse addVideo(HttpServletRequest request) throws IOException {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        String title = multipartRequest.getParameter("title");
        String subject = multipartRequest.getParameter("subject");
        String watch_type = multipartRequest.getParameter("watch_type");
        String content = multipartRequest.getParameter("content");
        MultipartFile picture = multipartRequest.getFile("picture");
        MultipartFile viedo = multipartRequest.getFile("video");


        if (
                title == null
                        || subject == null
                        || watch_type == null
                        || viedo == null
                        || picture == null
                        || content == null
                ) {

            ServiceResponse rsp = new ServiceResponse();
            String temp= "缺少参数";
            if(title == null) {
                temp += " title";
            }
            if(subject == null) {
                temp += " subject";
            }
            if(watch_type == null) {
                temp += " watch_type";
            }
            if(viedo == null) {
                temp += " viedo";
            }

            if(picture == null) {
                temp += " picture";
            }
            if(content == null) {
                temp += " content";
            }

            rsp.msg = temp;
            rsp.code = 201;
            return rsp;
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss_");//设置日期格式
        String strDate = df.format(new Date());// new Date()为获取当前系统时间

        String savePath = multipartRequest.getServletContext().getRealPath("");
        System.out.println(savePath);
        File file = new File(savePath);
        savePath = file.getParent() + "/upload";
//        savePath = savePath.substring(0,savePath.lastIndexOf("webapps") + 8) + "upload";


        System.out.println(savePath);
        File filePath = new File(savePath);
        if (!filePath.exists() && !filePath.isDirectory()) {
            filePath.mkdir();
        }
        String pathPic = savePath + "/" + strDate + picture.getOriginalFilename();
        String pathVideo = savePath + "/" + strDate + viedo.getOriginalFilename();
        System.out.println(pathPic);
        System.out.println(pathVideo);
        //上传
        picture.transferTo(new File(pathPic));
        viedo.transferTo(new File(pathVideo));

        Video video = new Video();
        User user = (User)multipartRequest.getAttribute("user_info");
        video.setUser_id(user.getId());
        video.setTitle(title);
        video.setSubject(Integer.parseInt(subject));
        video.setWatch_type(Integer.parseInt(watch_type));
        video.setContent(content);

        String picNetPath = multipartRequest.getContextPath();
        System.out.println(picNetPath);
        System.out.println(123);
        String showPicPath = "http://" + request.getServerName()
                + ":"+ request.getServerPort()
//                + multipartRequest.getContextPath()
                + "/upload"
                + "/" + strDate
                + picture.getOriginalFilename();
        String videoPicPath = "http://" + request.getServerName()
                + ":"+ request.getServerPort()
//                + multipartRequest.getContextPath()
                + "/upload"
                + "/" + strDate
                + viedo.getOriginalFilename();
        video.setPic_up_path(showPicPath);
        video.setVideo_up_path(videoPicPath);
        video.setVideo_temp_path(pathVideo);

        video.setCreate_time((int) (new Date().getTime() / 1000));
        video.setUpdate_time((int) (new Date().getTime() / 1000));
        videoService.addVideo(video);

        ServiceResponse rsp = new ServiceResponse();
        rsp.msg = "新增视频完成";
        rsp.code = 200;
        rsp.data = videoPicPath;
        return rsp;
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

    @RequestMapping(value = "/verify_list")
    public @ResponseBody RepList   selectVideo(String status) {
        RepList repList = new RepList();
        if(status == null) {
            status = "0";
        }
        List<Video> list = iVideo.selectVideo(Integer.parseInt(status));
        repList.setCode(200);
        repList.setMsg("ok");
        repList.setList(list);
        return repList;
    }


    @RequestMapping(value = "/verify")
    public String verify(ModelMap model) {
        RepList repList = selectVideo("0");
        model.addAttribute("video_list",repList.getList());
        return "verify";
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
