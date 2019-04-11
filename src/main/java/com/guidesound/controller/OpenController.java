package com.guidesound.controller;

import com.guidesound.dao.ITool;
import com.guidesound.models.AppVersion;
import com.guidesound.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/open")
public class OpenController {


    @Autowired
    ITool iTool;

    class AppInfo {
        int upgrade_type;
        String upgrade_message;
        String download_url;

        public int getUpgrade_type() {
            return upgrade_type;
        }

        public void setUpgrade_type(int upgrade_type) {
            this.upgrade_type = upgrade_type;
        }

        public String getUpgrade_message() {
            return upgrade_message;
        }

        public void setUpgrade_message(String upgrade_message) {
            this.upgrade_message = upgrade_message;
        }

        public String getDownload_url() {
            return download_url;
        }

        public void setDownload_url(String download_url) {
            this.download_url = download_url;
        }
    }

    @RequestMapping(value = "/version")
    @ResponseBody
    JSONResult version(String os_type,String os_version) {
        if(os_type == null || os_version == null) {
            return JSONResult.errorMsg("缺少参数 os_type 或 os_version");
        }
        AppVersion appVersion = iTool.getVersion();
        AppInfo appInfo = new AppInfo();
        if(os_type.equals("0")) { //android
            if(os_version.equals(appVersion.getAndroid_version())) {
                appInfo.setUpgrade_type(2);
            } else {
                appInfo.setUpgrade_type(appVersion.getAndroid_type());
            }
            appInfo.setUpgrade_message(appVersion.getAndroid_message());
            appInfo.setDownload_url(appVersion.getAndroid_download_url());

        } else { //ios

            if(os_version.equals(appVersion.getIos_version())) {
                appInfo.setUpgrade_type(2);
            } else {
                appInfo.setUpgrade_type(appVersion.getIos_type());
            }
            appInfo.setUpgrade_message(appVersion.getIos_message());
            appInfo.setDownload_url(appVersion.getIos_download_url());
        }

        return JSONResult.ok(appInfo);
    }

    @RequestMapping(value = "/trade_description")
    String tradeDescription() {
        return "tradeDescription";
    }

    @RequestMapping(value = "/shop_description")
    String shopDescription() {
        return "shopDescription";
    }

    @RequestMapping(value = "/report_description")
    String reportDescription() {
        return "reportDescription";
    }


    @RequestMapping(value = "/course_description")
    String courseDescription() {
        return "courseDescription";
    }

    @RequestMapping(value = "/about")
    String about() {
        return "about";
    }
    @RequestMapping(value = "/privacy_policy")
    String privacyPolicy() {
        return "privacyPolicy";
    }

    @RequestMapping(value = "/user_policy")
    String userPolicy() {
        return "userPolicy";
    }




}
