package com.guidesound.TempStruct;

public class PayInfoRet {
    int type;
    int time;
    int in_or_out;
    int amount;

    CourseOrder courseOrder;
    Refund refund;
    TeacherClassIn teacherClassIn;
    TeacherClassOut teacherClassOut;
    VideoClassIn videoClassIn;
    VideoClassOut videoClassOut;

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

    public CourseOrder getCourseOrder() {
        return courseOrder;
    }

    public void setCourseOrder(CourseOrder courseOrder) {
        this.courseOrder = courseOrder;
    }

    public Refund getRefund() {
        return refund;
    }

    public void setRefund(Refund refund) {
        this.refund = refund;
    }

    public TeacherClassIn getTeacherClassIn() {
        return teacherClassIn;
    }

    public void setTeacherClassIn(TeacherClassIn teacherClassIn) {
        this.teacherClassIn = teacherClassIn;
    }

    public TeacherClassOut getTeacherClassOut() {
        return teacherClassOut;
    }

    public void setTeacherClassOut(TeacherClassOut teacherClassOut) {
        this.teacherClassOut = teacherClassOut;
    }

    public VideoClassIn getVideoClassIn() {
        return videoClassIn;
    }

    public void setVideoClassIn(VideoClassIn videoClassIn) {
        this.videoClassIn = videoClassIn;
    }

    public VideoClassOut getVideoClassOut() {
        return videoClassOut;
    }

    public void setVideoClassOut(VideoClassOut videoClassOut) {
        this.videoClassOut = videoClassOut;
    }
}
