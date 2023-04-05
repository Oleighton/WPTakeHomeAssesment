package com.example.wptakehome;

import java.util.HashMap;
import java.util.Map;

public class UserStoredInfo {
    private static UserStoredInfo sInstance;
    private HashMap<String, String> mData;

    private UserStoredInfo() {
        mData = new HashMap<>();
    }

    public static UserStoredInfo getInstance() {
        if (sInstance == null) {
            sInstance = new UserStoredInfo();
        }
        return sInstance;
    }

    public HashMap<String, String> getData() {
        return mData;
    }
    public void addUser(User user) {
        mData.put(user.getName(), user.getLoginTime());
    }
    public boolean containsUser(String username) {
        Map<String, String> data = UserStoredInfo.getInstance().getData();
        return data.containsKey(username);
    }
    public void setData(HashMap<String, String> newData) {
        mData = newData;
    }


}
