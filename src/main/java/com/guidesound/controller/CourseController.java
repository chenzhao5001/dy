package com.guidesound.controller;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidesound.TempStruct.ClassTime;
import com.guidesound.TempStruct.CourseOutline;
import com.guidesound.dao.ICourse;
import com.guidesound.dao.IExamine;
import com.guidesound.dao.IUser;
import com.guidesound.dto.Course1V1DTO;
import com.guidesound.dto.CourseClassDTO;
import com.guidesound.dto.TeacherDTO;
import com.guidesound.find.IntroductionInfo;
import com.guidesound.models.Course;
import com.guidesound.models.CourseExamine;
import com.guidesound.models.Teacher;
import com.guidesound.models.User;
import com.guidesound.ret.Course1V1;
import com.guidesound.ret.CourseClass;
import com.guidesound.ret.CourseItem;
import com.guidesound.ret.TeacherItem;
import com.guidesound.util.JSONResult;
import com.guidesound.util.SignMap;
import com.guidesound.util.ToolsFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static com.guidesound.util.SignMap.*;


@Controller
@RequestMapping("/course")
public class CourseController extends BaseController{

    @Autowired
    private ICourse iCourse;
    @Autowired
    private IUser iUser;
    @Autowired
    private IExamine iExamine;


    @RequestMapping("/add_1v1_course")
    @ResponseBody
    public JSONResult add_1v1_course(@Valid Course1V1DTO course1V1DTO,BindingResult result) {
        log.info(course1V1DTO);
        StringBuilder msg = new StringBuilder();
        if(!ToolsFunction.paramCheck(result,msg)) {
            return JSONResult.errorMsg(msg.toString());
        }
        Course course = new Course();
        course.setUser_id(getCurrentUserId());
        course.setId(Integer.parseInt(course1V1DTO.getCourse_id()));
        course.setType(0);
        course.setCourse_pic(course1V1DTO.getCourse_pic());
        course.setCourse_name(course1V1DTO.getCourse_name());
        course.setSubject(Integer.parseInt(course1V1DTO.getSubject()));
        course.setGrade(Integer.parseInt(course1V1DTO.getGrade()));
        course.setForm(Integer.parseInt(course1V1DTO.getForm()));
        course.setPrice_one_hour(Integer.parseInt(course1V1DTO.getPrice_one_hour()));
        course.setArea_service(course1V1DTO.getArea_service());
        course.setTest_form(Integer.parseInt(course1V1DTO.getTest_form()));
        course.setTest_duration(Integer.parseInt(course1V1DTO.getTest_duration()));
        course.setTest_charge(Integer.parseInt(course1V1DTO.getTest_charge()));
        course.setIntroduction_teacher(course1V1DTO.getIntroduction_teacher());
        course.setTeacher_id(Integer.parseInt(course1V1DTO.getTeacher_id()));
        course.setTeacher_name(course1V1DTO.getTeacher_name());
        course.setCourse_status(Integer.parseInt(course1V1DTO.getSave()));
        course.setCourse_content("");
        course.setOutline("");
        if(course1V1DTO.getCourse_id().equals("0")) {  //新建
            course.setCreate_time((int) (new Date().getTime() / 1000));
            iCourse.add1v1(course);
        } else {  //更新
            Course course_temp  = iCourse.getCouresByid(course.getId());
            if(course_temp != null && (course_temp.getCourse_status() == 1)) {
                return JSONResult.errorMsg("正在审核，无法修改1v1课程信息");
            }
            course.setUpdate_time((int) (new Date().getTime() / 1000));
            iCourse.update1V1(course);
        }
        if( course1V1DTO.getSave().equals("1")) {
            CourseExamine courseExamine = new CourseExamine();
            courseExamine.setType(7);
            courseExamine.setItem_id(course.getId());
            courseExamine.setUid(getCurrentUserId());
            courseExamine.setItem_sub_type(0);
            iExamine.addCourseExamine(courseExamine);
        }
        log.info("add_1v1_course ====================================");
        return JSONResult.ok();
    }


    @RequestMapping("/add_class_course")
    @ResponseBody
    public JSONResult addClassCourse(@Valid CourseClassDTO courseClassDTO,BindingResult result) {
        log.info(courseClassDTO);
        StringBuilder msg = new StringBuilder();
        if(!ToolsFunction.paramCheck(result,msg)) {
            return JSONResult.errorMsg(msg.toString());
        }

        Course course = new Course();
        course.setId(Integer.parseInt(courseClassDTO.getCourse_id()));
        course.setUser_id(getCurrentUserId());
        course.setType(1);
        course.setCourse_pic(courseClassDTO.getCourse_pic());
        course.setCourse_name(courseClassDTO.getCourse_name());
        course.setSubject(Integer.parseInt(courseClassDTO.getSubject()));
        course.setGrade(Integer.parseInt(courseClassDTO.getGrade()));
        course.setForm(Integer.parseInt(courseClassDTO.getForm()));
        course.setMax_person(Integer.parseInt(courseClassDTO.getMax_person()));
        course.setAll_hours(Integer.parseInt(courseClassDTO.getAll_hours()));
        course.setAll_charge(Integer.parseInt(courseClassDTO.getAll_charge()));
        course.setForm(Integer.parseInt(courseClassDTO.getForm()));
        course.setTest_duration(Integer.parseInt(courseClassDTO.getTest_duration()));
        course.setTest_charge(Integer.parseInt(courseClassDTO.getTest_charge()));
        course.setTest_time(Integer.parseInt(courseClassDTO.getTest_time()));
        course.setCourse_content(courseClassDTO.getCourse_content());
        course.setOutline(courseClassDTO.getOutline());
        course.setIntroduction_teacher(courseClassDTO.getIntroduction_teacher());
        course.setTeacher_id(Integer.parseInt(courseClassDTO.getTeacher_id()));
        course.setTeacher_name(courseClassDTO.getTeacher_name());
        course.setCourse_status(Integer.parseInt(courseClassDTO.getSave()));

        if(courseClassDTO.getCourse_id().equals("0")) {  //新建
            iCourse.addClass(course);
        } else {  //更新
            Course course_temp  = iCourse.getCouresByid(course.getId());
            if(course_temp != null && (course_temp.getCourse_status() == 1)) {
                return JSONResult.errorMsg("正在审核，无法修改班课信息");
            }
            course.setUpdate_time((int) (new Date().getTime() /1000));
            iCourse.updateClass(course);
        }
        if(courseClassDTO.getSave().equals("1")) {
            CourseExamine courseExamine = new CourseExamine();
            courseExamine.setType(7);
            courseExamine.setItem_id(course.getId());
            courseExamine.setUid(getCurrentUserId());
            courseExamine.setItem_sub_type(1);
            iExamine.addCourseExamine(courseExamine);
        }
        log.info("add_class_course========================================================");
        return JSONResult.ok();
    }

    @RequestMapping("/up_course_pic")
    @ResponseBody
    public JSONResult updateCoursePic(String course_id,String pic) {
        if(course_id == null || pic == null) {
            return JSONResult.errorMsg("缺少course_id 或 pic");
        }
        int user_id = getCurrentUserId();
        iCourse.updateCoursePic(user_id,Integer.parseInt(course_id),pic);
        return JSONResult.ok();
    }

    @RequestMapping("/course_list")
    @ResponseBody
    public JSONResult getCourcourse_typeseList(String user_id) {
        if(user_id == null) {
            return JSONResult.errorMsg("缺少 user_id");
        }

        List<Course> list = iCourse.getCourseList(Integer.parseInt(user_id));
        List<CourseItem> course_list = new ArrayList<>();
        for(Course course : list) {
            CourseItem courseItem = new CourseItem();
            courseItem.setCourse_id(course.getId());
            courseItem.setCourse_pic(course.getCourse_pic());
            courseItem.setCourse_status(course.getCourse_status());

            if(course.getType() == 1) {
                String outLine = course.getOutline();
                ObjectMapper mapper = new ObjectMapper();
                try {
                    List<CourseOutline> beanList = mapper.readValue(outLine, new TypeReference<List<CourseOutline>>() {
                        });
                    if(beanList.size() > 0) {
                        CourseOutline lastClassTime = beanList.get(beanList.size() -1);
                        if(lastClassTime.getClass_time() + lastClassTime.getClass_hours()*3600 < new Date().getTime()/1000) {
                            if(course.getCourse_status() == 3) {
                                iCourse.setCourseState(course.getId(),4);
                                courseItem.setCourse_status(4);
                            }
                        }

                    }
                } catch (IOException e) {
                    courseItem.setCourse_status(-1);
                }
            }
            courseItem.setCourse_name(course.getCourse_name());
            courseItem.setForm(getCourseFormById(course.getForm()));
            courseItem.setCourse_type(course.getType());
            courseItem.setCourse_type_name(course.getType() == 0 ? "1对1授课 " : "班课");
            courseItem.setSubject(SignMap.getSubjectTypeById(course.getSubject()));
            courseItem.setGrade(SignMap.getGradeTypeByID(course.getGrade()));
            if(course.getType() == 0) {
                courseItem.setPrice(course.getPrice_one_hour());
            } else {
                courseItem.setPrice(course.getAll_charge());
            }
            course_list.add(courseItem);
        }
        return JSONResult.ok(course_list);
    }

    @RequestMapping("/set_outline")
    @ResponseBody
    public JSONResult setCourseOutline(String course_id,String outline) throws IOException {

        if(course_id == null || outline == null) {
            return JSONResult.errorMsg("缺少 course_id 获 outline");
        }
        ObjectMapper mapper = new ObjectMapper();
        List<CourseOutline> beanList = mapper.readValue(outline, new TypeReference<List<CourseOutline>>() {});
        if(beanList.size() < 1) {
            return JSONResult.errorMsg("格式错误");
        }
        CourseOutline lastItem = beanList.get(beanList.size() -1);
//        int overTime = lastItem.getTime() + 3600*lastItem.getDuration();
        String content  = mapper.writeValueAsString(beanList);
//        iCourse.setCourseOutline(Integer.parseInt(course_id),content,overTime);
        return JSONResult.ok();
    }


    @RequestMapping("/get_outline")
    @ResponseBody
    public JSONResult setCourseOutline(String course_id) throws IOException {
        if(course_id == null) {
            return JSONResult.errorMsg("缺少course_id");
        }
        ObjectMapper mapper = new ObjectMapper();
        String outLine = iCourse.getCourseOutline(Integer.parseInt(course_id));
        List<CourseOutline> beanList = mapper.readValue(outLine, new TypeReference<List<CourseOutline>>() {});
        return JSONResult.ok(beanList);
    }

    @RequestMapping("/up_introduction_pic")
    @ResponseBody
    public JSONResult upIntroductionPic(String introduction_pic) {
        if(introduction_pic == null) {
            return JSONResult.errorMsg("缺少参数");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User) request.getAttribute("user_info");
        if(currentUser.getType() != 1) {
            return JSONResult.errorMsg("不是老师");
        }
        iUser.upIntroductionPic(currentUser.getId(),introduction_pic);
        return JSONResult.ok();
    }

    @RequestMapping("/up_introduction")
    @ResponseBody
    public JSONResult upIntroduction(String introduction) {
        if(introduction == null) {
            return JSONResult.errorMsg("缺少参数");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User currentUser = (User) request.getAttribute("user_info");

        if(currentUser.getType() != 1) {
            return JSONResult.errorMsg("不是老师");
        }
        iUser.upIntroduction(currentUser.getId(),introduction);
        return JSONResult.ok();
    }

    @RequestMapping("/introduction_info")
    @ResponseBody
    public JSONResult getIntroductionInfo(String user_id) {

        if(user_id == null) {
            return JSONResult.errorMsg("缺少 user_id");
        }
        IntroductionInfo introductionInfo = iUser.getIntroductionInfo(Integer.parseInt(user_id));
        return JSONResult.ok(introductionInfo);
    }


    @RequestMapping("/add_teacher")
    @ResponseBody
    public JSONResult addTeacher(@Valid TeacherDTO teacherDTO, BindingResult result) {

        StringBuilder msg = new StringBuilder();
        if(!ToolsFunction.paramCheck(result,msg)) {
            return JSONResult.errorMsg(msg.toString());
        }
        Teacher teacher = new Teacher();
        teacher.setId(Integer.parseInt(teacherDTO.getId()));
        teacher.setName(teacherDTO.getName());
        teacher.setSex(Integer.parseInt(teacherDTO.getSex()));
        teacher.setSubject(Integer.parseInt(teacherDTO.getSubject()));
        teacher.setLevel(Integer.parseInt(teacherDTO.getLevel()));
        teacher.setCertificate(teacherDTO.getCertificate());
        teacher.setIntroduction(teacherDTO.getIntroduction());
        teacher.setUser_id(getCurrentUserId());
        teacher.setStatus(Integer.parseInt(teacherDTO.getSave()));
        if(teacherDTO.getId().equals("0")) {  //新增
            teacher.setCreate_time((int) (new Date().getTime() /1000));
            teacher.setUpdate_time((int) (new Date().getTime() /1000));
            iCourse.addTeacher(teacher);

        } else { //修改
            Teacher teacher_temp = iCourse.getTeacherById(teacher.getId());
            if(teacher_temp != null && (teacher_temp.getStatus() == 1)) {
                return JSONResult.errorMsg("正在审核，无法修改老师信息");
            }
            teacher.setUpdate_time((int) (new Date().getTime() /1000));
            iCourse.updateTeacher(teacher);
        }
        //添加审核
        if (teacherDTO.getSave().equals("1")) {
            CourseExamine courseExamine = new CourseExamine();
            courseExamine.setType(6);
            courseExamine.setItem_id(teacher.getId());
            courseExamine.setUid(getCurrentUserId());
            courseExamine.setItem_sub_type(0);
            iExamine.addCourseExamine(courseExamine);
        }
        return JSONResult.ok();
    }

    @RequestMapping("/teacher_list")
    @ResponseBody
    JSONResult getTeacherList(String user_id) {

        if(user_id == null) {
            return JSONResult.errorMsg("缺少 user_id");
        }
        List<Teacher> teacherList = iCourse.getTeacherList(Integer.parseInt(user_id));
        List<TeacherItem> list = new ArrayList<>();
        for(Teacher item: teacherList) {
            TeacherItem teacherItem = new TeacherItem();
            teacherItem.setId(item.getId());
            teacherItem.setStatus(item.getStatus());
            teacherItem.setName(item.getName());

            teacherItem.setSex(SignMap.getSexById(item.getSex()));
            teacherItem.setSubject(SignMap.getSubjectTypeById(item.getSubject()));
            teacherItem.setSubject_id(item.getSubject());
            teacherItem.setLevel(SignMap.getWatchById(item.getLevel()));
            teacherItem.setLevel_id(item.getLevel());
            teacherItem.setCertificate(item.getCertificate());
            teacherItem.setIntroduction(item.getIntroduction());
            list.add(teacherItem);
        }
        return JSONResult.ok(list);
    }

    @RequestMapping("/get_1v1_course")
    @ResponseBody
    JSONResult get1V1Course(String course_id) {
        if(course_id == null) {
            return JSONResult.errorMsg("缺少 course_id");
        }

        Course course = iCourse.getCourseById(Integer.parseInt(course_id));
        if(course == null) {
            return JSONResult.ok(null);
        }


        Course1V1 course1V1 = new Course1V1();
        Integer im_id = iUser.getImIdByUserId(course.getUser_id());
        if(im_id != null) {
            course1V1.setIm_id(im_id);
        }
        course1V1.setCourse_id(course.getId());
        course1V1.setCourse_status(course.getCourse_status());
        course1V1.setCourse_type(course.getType());
        course1V1.setCourse_type_name(getCourseTypeNameById(course.getType()));
        course1V1.setCourse_pic(course.getCourse_pic());
        course1V1.setCourse_name(course.getCourse_name());
        course1V1.setSubject(SignMap.getSubjectTypeById(course.getSubject()));
        course1V1.setGrade(SignMap.getGradeTypeByID(course.getGrade()));
        course1V1.setForm(getCourseFormById(course.getForm()));
        course1V1.setPrice_one_hour(course.getPrice_one_hour());
        course1V1.setArea_service(course.getArea_service());
        course1V1.setTest_form(getCourseFormById(course.getTest_form()));
        course1V1.setTest_duration(course.getTest_duration());
        course1V1.setTest_charge(course.getTest_charge());
        course1V1.setIntroduction_teacher(course.getIntroduction_teacher());
        course1V1.setTeacher_id(course.getTeacher_id());
        course1V1.setTeacher_name(course.getTeacher_name());

        return JSONResult.ok(course1V1);
    }

    @RequestMapping("/get_class_course")
    @ResponseBody
    JSONResult getClassCourse(String course_id) {
        if(course_id == null) {
            return JSONResult.errorMsg("缺少 course_id");
        }
        Course course = iCourse.getCourseById(Integer.parseInt(course_id));
        if(course == null) {
            return JSONResult.ok(null);
        }
        CourseClass courseClass = new CourseClass();
        Integer im_id = iUser.getImIdByUserId(course.getUser_id());
        if(im_id != null) {
            courseClass.setIm_id(im_id);
        }

        courseClass.setCourse_id(course.getId());
        courseClass.setCourse_status(course.getCourse_status());
        courseClass.setCourse_type(course.getType());
        courseClass.setCourse_type_name(getCourseTypeNameById(course.getType()));
        courseClass.setCourse_pic(course.getCourse_pic());
        courseClass.setCourse_name(course.getCourse_name());

        courseClass.setSubject(SignMap.getSubjectTypeById(course.getSubject()));
        courseClass.setGrade(SignMap.getGradeTypeByID(course.getGrade()));
        courseClass.setForm(getCourseFormById(course.getForm()));

        courseClass.setMax_person(course.getMax_person());
        courseClass.setAll_hours(course.getAll_hours());
        courseClass.setAll_charge(course.getAll_charge());

        courseClass.setTest_time(course.getTest_time());
        courseClass.setTest_duration(course.getTest_duration());
        courseClass.setTest_charge(course.getTest_charge());

        courseClass.setCourse_content(course.getCourse_content());

        ObjectMapper mapper = new ObjectMapper();
        List<CourseOutline> beanList = null;
        try {
            beanList = mapper.readValue(course.getOutline(), new TypeReference<List<CourseOutline>>() {});
            courseClass.setOutline(beanList);
        } catch (IOException e) {
            e.printStackTrace();
            courseClass.setOutline("json 格式错误");
        }

        courseClass.setIntroduction_teacher(course.getIntroduction_teacher());
        courseClass.setTeacher_id(course.getTeacher_id());
        courseClass.setTeacher_name(course.getTeacher_name());

        return JSONResult.ok(courseClass);
    }

    @RequestMapping("/down_course")
    @ResponseBody
    JSONResult downCourse(String course_id) {
        if(course_id == null) {
            return JSONResult.errorMsg("缺少 course_id");
        }
        iCourse.updateCourseType(Integer.parseInt(course_id),5);
        return JSONResult.ok();
    }

    @RequestMapping("/delete_course")
    @ResponseBody
    JSONResult deleteCourse(String course_id) {
        if(course_id == null) {
            return JSONResult.errorMsg("缺少 course_id");
        }
        iCourse.deleteCourse(Integer.parseInt(course_id));
        return JSONResult.ok();
    }

    @RequestMapping("/teacher_by_id")
    @ResponseBody
    JSONResult teacherByid(String teacher_id) {
        if (teacher_id == null) {
            return JSONResult.errorMsg("缺少 teacher_id");
        }
        Teacher teacher = iCourse.getTeacherById(Integer.parseInt(teacher_id));
        if(teacher == null) {
            return JSONResult.errorMsg("老师信息不存在");
        }

        TeacherItem teacherItem = new TeacherItem();
        teacherItem.setId(teacher.getId());
        teacherItem.setStatus(teacher.getStatus());
        teacherItem.setName(teacher.getName());

        teacherItem.setSex(SignMap.getSexById(teacher.getSex()));
        teacherItem.setSubject(SignMap.getSubjectTypeById(teacher.getSubject()));
        teacherItem.setSubject_id(teacher.getSubject());
        teacherItem.setLevel(SignMap.getWatchById(teacher.getLevel()));
        teacherItem.setLevel_id(teacher.getLevel());
        teacherItem.setCertificate(teacher.getCertificate());
        teacherItem.setIntroduction(teacher.getIntroduction());

        return JSONResult.ok(teacherItem);
    }

    @RequestMapping("/delete_teacher")
    @ResponseBody
    JSONResult deleteTeacher(String teacher_id) {
        if (teacher_id == null) {
            return JSONResult.errorMsg("缺少 teacher_id");
        }
        iCourse.deleteTeacher(Integer.parseInt(teacher_id));

        return JSONResult.ok();

    }

}
