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

@Controller
@RequestMapping("/edit")
public class EditController {

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
        File file = new File(savePath);
        savePath = file.getParent() + "/edit_upload";
        File filePath = new File(savePath);
        if (!filePath.exists() && !filePath.isDirectory()) {
            filePath.mkdir();
        }
        String pathPic = savePath + "/" + strDate + picture.getOriginalFilename();
//        File temp = new File(pathPic);
        picture.transferTo(new File(pathPic));
//        ToolsFunction.zoomImage(pathPic,pathPic,270,120);

        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), new File(pathPic));

        OkHttpClient okHttpClient  = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        MultipartBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("upload",strDate + ToolsFunction.getRandomString(8),fileBody)
                .addFormDataPart("sign","guide_sound")
                .build();

        Request req = new Request.Builder()
                .url("http://139.199.123.168/fileservice/upload")
                .post(formBody)
                .build();
        Response resp;
        resp = client.newCall(req).execute();
        file.delete();
        String jsonString = resp.body().string();
        System.out.println(jsonString);
        JSONObject json = new JSONObject(jsonString);
        String url = json.getString("data");

        String ret = String.format("{\"default\" : \"%s\"}",url);


        System.out.println(ret);

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
