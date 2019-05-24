package com.guidesound.TempStruct;

public class PayInfoRet {
    int type;
    int time;
    int in_or_out;
    int amount;

    CourseOrder course_order;
    Refund refund;
    TeacherClassIn teacher_class_in;
    TeacherClassOut teacher_class_out;
    VideoClassIn video_class_in;
    VideoClassOut video_class_out;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getIn_or_out() {
        return in_or_out;
    }

    public void setIn_or_out(int in_or_out) {
        this.in_or_out = in_or_out;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public CourseOrder getCourse_order() {
        return course_order;
    }

    public void setCourse_order(CourseOrder course_order) {
        this.course_order = course_order;
    }

    public Refund getRefund() {
        return refund;
    }

    public void setRefund(Refund refund) {
        this.refund = refund;
    }

    public TeacherClassIn getTeacher_class_in() {
        return teacher_class_in;
    }

    public void setTeacher_class_in(TeacherClassIn teacher_class_in) {
        this.teacher_class_in = teacher_class_in;
    }

    public TeacherClassOut getTeacher_class_out() {
        return teacher_class_out;
    }

    public void setTeacher_class_out(TeacherClassOut teacher_class_out) {
        this.teacher_class_out = teacher_class_out;
    }

    public VideoClassIn getVideo_class_in() {
        return video_class_in;
    }

    public void setVideo_class_in(VideoClassIn video_class_in) {
        this.video_class_in = video_class_in;
    }

    public VideoClassOut getVideo_class_out() {
        return video_class_out;
    }

    public void setVideo_class_out(VideoClassOut video_class_out) {
        this.video_class_out = video_class_out;
    }
}
