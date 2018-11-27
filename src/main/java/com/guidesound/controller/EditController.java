package com.guidesound.controller;

import com.guidesound.util.ServiceResponse;
import com.guidesound.util.ToolsFunction;
import okhttp3.*;
import org.json.JSONObject;
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
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
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
        String url = "http://" + articleBucketName + ".cos." + region + ".myqcloud.com" + "/" + key;
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
