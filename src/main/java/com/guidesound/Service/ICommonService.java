package com.guidesound.Service;

import com.guidesound.TempStruct.NextClassInfo;
import com.guidesound.models.ArticleInfo;
import com.guidesound.models.VideoShow;

import java.util.List;

public interface ICommonService {
    //总金额
    public void changeUserSurplusAmount(int user_id, int change_amount);
    //可提现
    public void changeUserAmount(int user_id, int change_amount);

    //更新alipay用户信息
    public void updateAliPayUserId(int user_id,String alipay_user_id);

    //增加视频扩展信息
    void improveVideoList(List<VideoShow> list_temp, int user_id);

    //增加文章扩展信息
    void improveArticleList(List<ArticleInfo> list, int user_id);

    public NextClassInfo isClassFinish(int class_id,int user_id);

}
