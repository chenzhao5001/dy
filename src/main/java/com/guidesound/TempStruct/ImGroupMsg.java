package com.guidesound.TempStruct;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ImGroupMsg {
    String GroupId;
    String Random;
    List<ImGroupMsgInfo> MsgBody;

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public String getRandom() {
        return Random;
    }

    public void setRandom(String random) {
        Random = random;
    }

    public List<ImGroupMsgInfo> getMsgBody() {
        return MsgBody;
    }

    public void setMsgBody(List<ImGroupMsgInfo> msgBody) {
        MsgBody = msgBody;
    }
}
