package com.guidesound.TempStruct;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ImGroupMsg {
    String GroupId;
    String Random;
    String From_Account;
    List<ImGroupMsgInfo> MsgBody;

    public String getFrom_Account() {
        return From_Account;
    }

    public void setFrom_Account(String from_Account) {
        From_Account = from_Account;
    }

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
