package com.guidesound.controller;

import com.guidesound.dao.IRecord;
import com.guidesound.dto.RecordDTO;
import com.guidesound.util.JSONResult;
import com.guidesound.util.ToolsFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

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

        if(recordDTO.getRecord_course_id().equals("0")) {
            iRecord.add(recordDTO);
        } else {

        }
        return JSONResult.ok();
    }
}
