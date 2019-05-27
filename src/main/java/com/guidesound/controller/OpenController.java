package com.guidesound.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.guidesound.Service.ICommonService;
import com.guidesound.dao.ITool;
import com.guidesound.dao.IVideo;
import com.guidesound.models.AppVersion;
import com.guidesound.models.VideoShow;
import com.guidesound.resp.ListResp;
import com.guidesound.util.JSONResult;
import com.guidesound.util.ToolsFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.tools.Tool;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/open")
public class OpenController extends BaseController {


    @Autowired
    ITool iTool;

    @Autowired
    IVideo iVideo;

    @Autowired
    ICommonService iCommonService;

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
    JSONResult version(String os_type, String os_version) {
        if (os_type == null || os_version == null) {
            return JSONResult.errorMsg("缺少参数 os_type 或 os_version");
        }
        AppVersion appVersion = iTool.getVersion();
        AppInfo appInfo = new AppInfo();
        if (os_type.equals("0")) { //android
            if (os_version.equals(appVersion.getAndroid_version())) {
                appInfo.setUpgrade_type(2);
            } else {
                appInfo.setUpgrade_type(appVersion.getAndroid_type());
            }
            appInfo.setUpgrade_message(appVersion.getAndroid_message());
            appInfo.setDownload_url(appVersion.getAndroid_download_url());

        } else { //ios

            if (os_version.equals(appVersion.getIos_version())) {
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


    @RequestMapping(value = "/feature_description")
    @ResponseBody
    JSONResult featureDescription() {

        class Description {
            String teacher_class_protocol;
            String video_class_protocol;
            String commodity_protocol;
            String shop_protocol;

            public String getTeacher_class_protocol() {
                return teacher_class_protocol;
            }

            public void setTeacher_class_protocol(String teacher_class_protocol) {
                this.teacher_class_protocol = teacher_class_protocol;
            }

            public String getVideo_class_protocol() {
                return video_class_protocol;
            }

            public void setVideo_class_protocol(String video_class_protocol) {
                this.video_class_protocol = video_class_protocol;
            }

            public String getCommodity_protocol() {
                return commodity_protocol;
            }

            public void setCommodity_protocol(String commodity_protocol) {
                this.commodity_protocol = commodity_protocol;
            }

            public String getShop_protocol() {
                return shop_protocol;
            }

            public void setShop_protocol(String shop_protocol) {
                this.shop_protocol = shop_protocol;
            }
        }

        Description description = new Description();
        description.setCommodity_protocol("功能说明：添加商品后，在上传短视频时可以选择把商品挂接上，这样在视频被观看时，有相应的商品提示，用户点击后直接跳到淘宝或京东app内的相应商品；所得收益完全属于上传者，平台不收取任何费用。");
        description.setShop_protocol("功能说明：设置店铺之后，在个人主页会有“店铺”按钮，用户点击后打开淘宝或京东app然后跳转到所设置的店铺主页；所得收益完全属于上传者，平台不收取任何费用。");
        description.setTeacher_class_protocol("1、 课堂采用实时音视频技术，老师和任何学生均可实时语音视频互动；\n" +
                "\n" +
                "2、课程分1对1和班课2种，为了提高辅导效果，班课人数最多5人；\n" +
                "\n" +
                "3、老师和学生通过手机或者电脑浏览器均可上课；\n" +
                "\n" +
                "4、平台收取课程费用的5%作为运营成本，每次课结束后课时费实时打到老师钱包账户下；\n" +
                "\n" +
                "5、钱包账户余额可随时提现到支付宝账户，提现到支付宝账户时自动扣除支付宝手续费；\n" +
                "\n" +
                "6、同时平台向购买者承诺，未上课时随时可退，以此激励老师用更高的水平、更多的耐心对待每一位学生；");
        description.setVideo_class_protocol("1、发布者要拥有对相应录播课程的发布权利；\n" +
                "\n" +
                "2、发布者要以群方式提供不少于课程总时间10%的答疑服务，若购买者对此服务投诉平台可以终止此录播课的销售；\n" +
                "\n" +
                "3、平台保证未付费用户不能观看要求付费的视频，同时平台通过技术及管理手段保障视频课程不被非法盗取；\n" +
                "\n" +
                "4、购买者对视频的观看时限为1年，同时购买后不可退款；\n" +
                "\n" +
                "5、平台收取录播课费用的5%作为运营成本，用户购买视频后平台实时把费用打到发布者钱包账户下；\n" +
                "\n" +
                "6、钱包账户余额可随时提现到支付宝账户，提现到支付宝账户时自动扣除支付宝手续费；");
        return JSONResult.ok(description);

    }


    @RequestMapping(value = "/share")
    String share(String type, String avid, ModelMap model) throws IOException {
        if (type == null || avid == null) {
            return "about";
        }
        if (type.equals("0")) { // 文章
            return "share_article";
        } else {  //视频
            VideoShow videoShow = iVideo.getVideoById(avid);
            if (videoShow != null) {
                List<VideoShow> lists = new ArrayList<>();
                lists.add(videoShow);
                iCommonService.improveVideoList(lists, 0);
                videoShow = lists.get(0);
                model.addAttribute("user_head", videoShow.getUser_head());
                model.addAttribute("user_name", videoShow.getUser_name());
                model.addAttribute("user_sing", "这里填什么？");

                model.addAttribute("pic_up_path", videoShow.getPic_up_path());
                model.addAttribute("praise_count", videoShow.getPraise_count());
                model.addAttribute("chat_count", videoShow.getChat_count());
                model.addAttribute("collection_count", videoShow.getCollection_count());
                model.addAttribute("shared_count", videoShow.getShared_count());

                model.addAttribute("video_subject", videoShow.getWatch_type_name() + videoShow.getSubject_name());
                model.addAttribute("video_user_name", "@" + videoShow.getUser_name());
                model.addAttribute("video_title", ToolsFunction.URLDecoderString(videoShow.getTitle()));

                return "share_video";
            }
        }

        return "err";

    }

}
