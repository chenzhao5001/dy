package com.guidesound.util;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class VideoExamine {

    static private Map<Integer,String> failReason;

    static {
        failReason = new TreeMap<>();
        failReason.put(101,"视频内容包含个人联系方式");
        failReason.put(102,"视频内容有明显的广告宣传行为");
        failReason.put(103,"视频内容和本平台整体风格不一致");
        failReason.put(104,"视频内容与您的辅导学科不符");
        failReason.put(105,"视频内容与标题不一致");
        failReason.put(106,"视频内容涉黄");
        failReason.put(107,"审核员手动输入其它原因");
    }

    public static Map<Integer,String> getReason() {
        return failReason;
    }
}
