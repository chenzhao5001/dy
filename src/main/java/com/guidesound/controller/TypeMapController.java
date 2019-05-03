package com.guidesound.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidesound.dao.ICourse;
import com.guidesound.dao.IUser;
import com.guidesound.models.User;
import com.guidesound.models.UserInfo;
import com.guidesound.util.JSONResult;
import com.guidesound.util.SignMap;
import com.guidesound.util.StorageSts;
import com.guidesound.util.ToolsFunction;
import com.qcloud.Utilities.Json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

/**
 * 用户控制器
 */
@Controller
@RequestMapping("/type")
public class TypeMapController extends BaseController {

    @Autowired
    private IUser iUser;
    @Autowired
    ICourse iCourse;
    Boolean flag = false;
    @RequestMapping(value = "/watch_type_list")
    @ResponseBody
    public JSONResult getWatchType() {
        class Item {
            int id;
            String type;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getType() {
                return type;
            }

            public void setType(String reason) {
                this.type = reason;
            }
        }

        List<Item> list = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : SignMap.getWatchList().entrySet()) {
            Item item = new Item();
            item.setId(entry.getKey());
            item.setType(entry.getValue());
            list.add(item);
        }
        return JSONResult.ok(list);
    }

    @RequestMapping(value = "/grade_type_list2")
    @ResponseBody
    public JSONResult getGradeType2() {
        return JSONResult.ok(SignMap.getGradeTypeInfo2(false));
    }

    @RequestMapping(value = "/grade_type_list3")
    @ResponseBody
    public JSONResult getGradeType3() {
        return JSONResult.ok(SignMap.getGradeTypeInfo2(true));
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
     * 获取学科类型信息接口
     */
    @RequestMapping(value = "/subject_list")
    @ResponseBody
    public JSONResult getSubjectList() {
        return JSONResult.ok(SignMap.getSubjectList());
    }

    @RequestMapping(value = "/grade_list")
    @ResponseBody
    public JSONResult getGradetList() {
        return JSONResult.ok(SignMap.getGrade());
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

    @RequestMapping(value = "/channel_list")
    @ResponseBody
    JSONResult  getChannelList() throws JsonProcessingException {
        int user_id = getCurrentUserId();
        if(user_id == 0) {
            return JSONResult.ok(SignMap.getChannelList(1,false));
        }
        int channel_stage = iUser.getVideoChannelStage(user_id);
        if(channel_stage == 101) {
            return JSONResult.ok(SignMap.getChannelList(101,false));
        } else if(channel_stage == 102){
            return JSONResult.ok(SignMap.getChannelList(102,false));
        } else {
            return JSONResult.ok(SignMap.getChannelList(channel_stage/100,false));
        }
    }


    @RequestMapping(value = "/user_grade_list")
    @ResponseBody
    JSONResult  getUserGradeList(HttpServletRequest request) {

        if(getCurrentUserId() == 0) {
            return JSONResult.ok(new ArrayList<>());
        }

        UserInfo userInfo = iUser.getUser(getCurrentUserId());
        int level = userInfo.getGrade_level();
        boolean role_flag = false;
        //教师 达人
        if(userInfo.getType() == 1 || userInfo.getType() == 4) {
            role_flag = true;
        }
        Map<Integer,String> m_temp = SignMap.getGradeByClass(level,role_flag);
        class Grade{
            int id;
            String grade;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getGrade() {
                return grade;
            }

            public void setGrade(String grade) {
                this.grade = grade;
            }
        }
        List<Grade> r_list = new ArrayList<>();
        for(Integer key:m_temp.keySet()) {
            Grade grade = new Grade();
            grade.setId(key);
            grade.setGrade(m_temp.get(key));
            r_list.add(grade);

        }
        return JSONResult.ok(r_list);
    }

}
