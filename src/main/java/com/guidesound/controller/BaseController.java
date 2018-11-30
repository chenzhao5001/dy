package com.guidesound.controller;


import com.guidesound.models.User;
import com.guidesound.util.TockenUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import okhttp3.OkHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BaseController {

    static String articleBucketName;
    static String videoBucketName;
    static String region;
    static COSClient cosClient = null;
    static OkHttpClient okHttpClient;
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
    @ModelAttribute
    public void common(HttpServletRequest request, HttpServletResponse response) {
        if(log == null ) {
            log = (Logger) LogManager.getLogger();
        }
    }

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
}
