package com.guidesound.dto;

import javax.validation.constraints.NotNull;

public class TeacherDTO {
    @NotNull(message = "id:不能为空;")
    String id;
    @NotNull(message = "name:不能为空;")
    String name;
    @NotNull(message = "sex:不能为空;")
    String sex;
    @NotNull(message = "subject:不能为空;")
    String subject;
    @NotNull(message = "level:不能为空;")
    String level;
    @NotNull(message = "certificate:不能为空;")
    String certificate;
    @NotNull(message = "introduction:不能为空;")
    String introduction;
    @NotNull(message = "save:不能为空;")
    String save;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLevel() {
        return level;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }
}
