package com.guidesound.controller;

import com.guidesound.dao.IArticle;
import com.guidesound.dao.IUser;
import com.guidesound.dao.IVideo;
import com.guidesound.models.*;
import com.guidesound.util.JSONResult;
import com.guidesound.util.ServiceResponse;
import com.guidesound.util.ToolsFunction;
import com.qcloud.cos.model.DeleteObjectRequest;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;

@Controller
@RequestMapping("/edit")
public class EditController extends BaseController {

    public static final String TAG = "UploadHelper";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private final OkHttpClient client = new OkHttpClient();

    @Autowired
    IVideo iVideo;
    @Autowired
    IArticle iArticle;
    @Autowired
    IUser iUser;

    @RequestMapping(value = "/upload")
    @ResponseBody
    public Object upload(HttpServletRequest request, HttpServletResponse response) throws Exception {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile picture = multipartRequest.getFile("file");
        System.out.println(picture);
        if(picture == null) {
            ServiceResponse serviceResponse = new ServiceResponse();
            serviceResponse.setCode(201);
            serviceResponse.setMsg("缺少参数");
            serviceResponse.data = null;
            return serviceResponse;
        }


        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss_");//设置日期格式
        String strDate = df.format(new Date());// new Date()为获取当前系统时间

        String savePath = multipartRequest.getServletContext().getRealPath("");
        System.out.println(savePath);
        File file = new File(savePath);
        savePath = file.getParent() + "/file";
        File filePath = new File(savePath);
        if (!filePath.exists() && !filePath.isDirectory()) {
            filePath.mkdir();
        }
        String fileName = picture.getOriginalFilename();
        String[] strs = fileName.split("\\.");
        String randStr = ToolsFunction.getRandomString(8);
        String suffix = "";
        if (strs.length > 1) {
            suffix = strs[strs.length -1];
        }

        String pathFile = savePath + "/" + strDate + randStr + "." + suffix;
        File localFile = new File(pathFile);
        picture.transferTo(localFile);

        String key = strDate + randStr + "." + suffix;
        PutObjectRequest putObjectRequest = new PutObjectRequest(articleBucketName, key, localFile);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        localFile.delete();
        String url = "https://" + articleBucketName + ".cos." + region + ".myqcloud.com" + "/" + key;
        String ret = String.format("{\"default\" : \"%s\"}",url);
        return ret;
    }
    @RequestMapping(value = "/browse")
    public void browse(){
    }

    @RequestMapping(value = "/content")
    public String editContent(HttpServletRequest request, ModelMap model) {
        String ret = request.getParameter("editor1");
        System.out.println(ret);

        model.addAttribute("content",ret);
        return "content";
    }

    void deleteObject(String url) {
        ArrayList<String> arr = new ArrayList<String>();
        arr.add("pic-1257964795");
        arr.add("pic-article-1257964795");
        arr.add("video-1257964795");
        arr.add("video-temp-1257964795");

        int index1 = url.indexOf("//") + 2;
        int index2 = url.indexOf(".");
        int index3 = url.lastIndexOf("/") + 1;
        if(index1 < 0 || index2 < 0 || index3 < 0) {
            return;
        }

        String bucket = url.substring(index1,index2);
        if(!arr.contains(bucket))
            return;
        String key = url.substring(index3);
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket,key);
        cosClient.deleteObject(deleteObjectRequest);
    }
    @RequestMapping(value = "/delete_user")
    @ResponseBody
    JSONResult deleteUser() {
        ArrayList<String> arr = new ArrayList<String>();
        arr.add("pic-1257964795");
        arr.add("pic-article-1257964795");
        arr.add("video-1257964795");
        arr.add("video-temp-1257964795");
        int user_id = getCurrentUserId();
        if(user_id == 0) {
            return JSONResult.ok();
        }
        List<Video> videos = iVideo.getVideoList(user_id);
        for (Video video : videos) {
            deleteObject(video.getPic_up_path());
            deleteObject(video.getVideo_up_path());
            deleteObject(video.getVideo_show_path());
        }
        iVideo.deleteVideoByUser(user_id);
        List<ArticleInfo> articleInfos = iArticle.getListById(user_id,0,10000);
        for(ArticleInfo item : articleInfos) {
            deleteObject(item.getHead_pic1());
            deleteObject(item.getHead_pic1());
            deleteObject(item.getHead_pic1());
        }
        iArticle.deleteArticleByUser(user_id);
        List<ArticleAnswer> answerList = iArticle.getAnswerByUser(user_id);
        for(ArticleAnswer item : answerList) {
            deleteObject(item.getPic1_url());
            deleteObject(item.getPic2_url());
            deleteObject(item.getPic3_url());
        }
        iArticle.deleteAnswerByUser(user_id);
        UserInfo userInfo = iUser.getUser(user_id);
        if(userInfo != null) {
            deleteObject(userInfo.getHead());
        }
        iUser.deleteUser(user_id);
        return JSONResult.ok();

    }

    @RequestMapping(value = "/temp")
    @ResponseBody
    JSONResult fooTemp() {
        ArrayList<String> arr = new ArrayList<String>();
        arr.add("pic-1257964795");
        arr.add("pic-article-1257964795");
        arr.add("video-1257964795");
        arr.add("video-temp-1257964795");
        List<UserInfo> list = iVideo.FooTemp3();
        for(UserInfo item : list) {
            String temp1 = item.getHead();
            int index1 = temp1.indexOf("//") + 2;
            int index2 = temp1.indexOf(".");
            int index3 = temp1.lastIndexOf("/") + 1;
            if(index1 < 0 || index2 < 0 || index3 < 0) {
                continue;
            }
            String bucket = temp1.substring(index1,index2);
            if(!arr.contains(bucket))
                continue;
            String key = temp1.substring(index3);
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket,key);
            cosClient.deleteObject(deleteObjectRequest);
        }

        return JSONResult.ok(list);
    }
//DELETE FROM video WHERE user_id in (SELECT id FROM user where (dy_id div 10000000) != 1)
//DELETE  FROM article WHERE user_id in (SELECT id FROM user where (dy_id div 10000000) != 1)
    public void foo() {
        Map<String,String> m = new Hashtable<>();
    }

}


class EditResp {
    String fileName;
    int uploaded;
    String url;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getUploaded() {
        return uploaded;
    }

    public void setUploaded(int uploaded) {
        this.uploaded = uploaded;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
