package com.guidesound.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidesound.util.JSONResult;
import com.guidesound.util.SignMap;
import com.guidesound.util.ToolsFunction;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;

/**
 * 用户控制器
 */
@Controller
@RequestMapping("/type")
public class TypeMapController {

    @RequestMapping(value = "/watch_type_list")
    @ResponseBody
    public JSONResult getWatchType() {
        return JSONResult.ok(SignMap.getWatchList());
    }

    @RequestMapping(value = "/grade_type_list")
    @ResponseBody
    public JSONResult getGradeType() {
        return JSONResult.ok(SignMap.getGradeTypeInfo());
    }

    /**
     * 获得用户类型列表
     */
    @RequestMapping(value = "/user_type_list")
    @ResponseBody
    public JSONResult getUserType() {
        return JSONResult.ok(SignMap.getUserTypeList());
    }

    /**
     * 获取学科类别接口
     */
    @RequestMapping(value = "/subject_classify_list")
    @ResponseBody
    public JSONResult getSubjectClassifyList() {
        return JSONResult.ok(SignMap.getSubjectClassifyList());
    }

    /**
     * 获取学科类型信息接口
     */
    @RequestMapping(value = "/subject_type_list")
    @ResponseBody
    public JSONResult getSubjectTypeList() {
        return JSONResult.ok(SignMap.getSubjectTypeInfo());
    }

    /**
     * 获取城市列表接口
     */
    @RequestMapping(value = "/area_list")
    @ResponseBody
    public JSONResult getCityList() throws IOException {

        String fileContent = "";
        String filePath = "city-list.txt";
        Resource resource1 = new ClassPathResource(filePath);
        File f = resource1.getFile();

        if (f.isFile() && f.exists()) {
            InputStreamReader read = new InputStreamReader(new FileInputStream(f), "UTF-8");
            BufferedReader reader = new BufferedReader(read);
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent += line;
            }
            read.close();
        }

        String str = fileContent.replace("\t","");

        ObjectMapper mapper = new ObjectMapper();
        Object object = mapper.readValue(str, Object.class);

        return JSONResult.ok(object);
    }

}
