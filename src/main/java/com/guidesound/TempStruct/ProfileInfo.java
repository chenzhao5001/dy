package com.guidesound.TempStruct;

import java.util.List;

public class ProfileInfo {
    String From_Account;
    List<ProfileItem> ProfileItem;

    public String getFrom_Account() {
        return From_Account;
    }

    public void setFrom_Account(String from_Account) {
        From_Account = from_Account;
    }

    public List<com.guidesound.TempStruct.ProfileItem> getProfileItem() {
        return ProfileItem;
    }

    public void setProfileItem(List<ProfileItem> profileItem) {
        ProfileItem = profileItem;
    }
}
