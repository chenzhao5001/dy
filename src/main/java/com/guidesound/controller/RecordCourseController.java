package com.guidesound.controller;

import com.guidesound.dao.IRecord;
import com.guidesound.dto.RecordDTO;
import com.guidesound.models.Record;
import com.guidesound.util.JSONResult;
import com.guidesound.util.ToolsFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/record")
public class RecordCourseController extends BaseController {

    @Autowired
    IRecord iRecord;

    @RequestMapping("/add")
    @ResponseBody
    JSONResult add(@Valid RecordDTO recordDTO, BindingResult result) {
        StringBuilder msg = new StringBuilder();
        if(!ToolsFunction.paramCheck(result,msg)) {
            return JSONResult.errorMsg(msg.toString());
        }
        recordDTO.setUser_id(getCurrentUserId());
        if(recordDTO.getSave().equals("0")) {
            recordDTO.setRecord_course_status(0);
        } else {
            recordDTO.setRecord_course_status(1);
        }

        if(recordDTO.getRecord_course_id().equals("0")) {
            recordDTO.setCreate_time((int) (new Date().getTime() / 1000));
            iRecord.add(recordDTO);
        } else {
            recordDTO.setUpdate_time((int) (new Date().getTime() / 1000));
            iRecord.update(recordDTO);
        }
        return JSONResult.ok();
    }

    @RequestMapping("/delete")
    @ResponseBody
    JSONResult delete(String record_course_id) {
        if(record_course_id == null ) {
            return JSONResult.errorMsg("缺少 record_course_id 参数");
        }
        iRecord.delete(getCurrentUserId(),Integer.parseInt(record_course_id));
        return JSONResult.ok();
    }

    @RequestMapping("/list")
    @ResponseBody
    JSONResult list(String who) {
        if(who == null) {
            return JSONResult.errorMsg("缺少参数");
        }
//        List<Record> list = iRecord.list();
        return JSONResult.ok();
    }

    @RequestMapping("/get_by_id")
    @ResponseBody
    JSONResult getById(String record_course_id) {
        return JSONResult.ok();
    }
}
