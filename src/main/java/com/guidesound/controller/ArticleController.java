package com.guidesound.controller;

import com.guidesound.dao.IArticle;
import com.guidesound.dto.ArticleDTO;
import com.guidesound.dto.VideoDTO;
import com.guidesound.util.JSONResult;
import com.guidesound.util.ToolsFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping("/article")
public class ArticleController extends BaseController {

    @Autowired
    private IArticle iArticle;

    @RequestMapping(value = "/add")
    @ResponseBody
    JSONResult add(@Valid ArticleDTO articleDTO, BindingResult result) {
        StringBuilder msg = new StringBuilder();
        if(!ToolsFunction.paramCheck(result,msg)) {
            return JSONResult.errorMsg(msg.toString());
        }
        if(articleDTO.getHead_pic2() == null) {
            articleDTO.setHead_pic2("");
        }
        if(articleDTO.getHead_pic3() == null) {
            articleDTO.setHead_pic3("");
        }

        articleDTO.setUser_id(currentUser.getId());
        articleDTO.setCreate_time((int)(new Date().getTime() / 1000));
        iArticle.add(articleDTO);

        return JSONResult.ok();
    }

    @RequestMapping("/delete")
    @ResponseBody
    JSONResult delete(String article_id) {
        if(article_id == null) {
            return JSONResult.errorMsg("缺少参数 article_id");
        }
        iArticle.delete(currentUser.getId(),Integer.parseInt(article_id));
        return JSONResult.ok();
    }


    @RequestMapping("/collect")
    @ResponseBody
    JSONResult collect(String article_id) {
        if(article_id == null ) {
            return JSONResult.errorMsg("缺少参数article_id");
        }
        if(null == iArticle.findCollection(currentUser.getId(),Integer.parseInt(article_id))) {
            iArticle.addMainCollection(Integer.parseInt(article_id));
            iArticle.collect(currentUser.getId(),Integer.parseInt(article_id),
                    (int)(new Date().getTime() / 1000));
        }
        return JSONResult.ok();
    }

    @RequestMapping("/cancelCollect")
    JSONResult cancelCollect(String article_id) {
        if(article_id == null) {
            return JSONResult.errorMsg("缺少article_id");
        }
        if(null == iArticle.findCollection(currentUser.getId(),Integer.parseInt(article_id))) {

        }

        return null;
    }

    JSONResult praise(String article_id) {
        return  null;
    }

    JSONResult Comment(String article_id,String content) {
        return null;
    }

    JSONResult getListByUserid(String user_id) {
        return null;
    }

}
