package com.guidesound.controller;


import com.guidesound.Service.ILogService;
import com.guidesound.dao.ICourse;
import com.guidesound.util.SignMap;
import com.guidesound.util.TlsSigTest;
import com.guidesound.util.TockenUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import okhttp3.OkHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

//@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BaseController {
    static String articleBucketName;
    static String videoBucketName;
    static String region;
    static COSClient cosClient = null;
    static OkHttpClient okHttpClient;

    double platformCostRatio = 0.95;
    double alipayCostRatio = 0.994;

    static {
        articleBucketName = "pic-article-1257964795";
        videoBucketName = "video-1257964795";
        region = "ap-beijing";
        COSCredentials cred = new BasicCOSCredentials("AKIDkIbfU4YZXUDgttF7MPDl36vUw9E6o7GK", "zjHchX8UbSCj9MM7ORFo8uUpwoUw9ltq");
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        cosClient = new COSClient(cred, clientConfig);
        okHttpClient = new OkHttpClient();
    }

    static Logger log;
    @Autowired
    ICourse iCourse;

    @Autowired
    ILogService iLogService;


    BaseController() {

    }
    @ModelAttribute
    public void common(HttpServletRequest request, HttpServletResponse response) {
        if(log == null ) {
            SignMap.Init(iCourse.getSubject());
            log = (Logger) LogManager.getLogger();
        }
    }

//    User getCurrentUser() {
//        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        return (User)req.getAttribute("user_info");
//    }

    int getCurrentUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        int currentUserID = 0;
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    currentUserID = TockenUtil.getUserIdByTocket(token);
                    break;
                }
            }
        }
        return currentUserID;
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
    String upUserHeadAndName(String im_id,String head,String name) throws IOException {
        iLogService.addLog("im_设置","im_id = " + im_id + ",head = " + head + ",name = " + name,"");
        return TlsSigTest.setUserHeadAndName(im_id,head,name);
    }
}
