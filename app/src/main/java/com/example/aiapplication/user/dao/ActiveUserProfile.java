package com.example.aiapplication.user.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.aiapplication.user.entity.User;

import java.util.HashSet;
import java.util.Set;

public class ActiveUserProfile {

    /*
     * TODO ::
     *  - User 활성화 순서
     *  1. 현재 활성화된 User의 정보를 프로필 Dialog를 띄울 때 전송한다.
     *  2. 이 정보가 현재 User의 정보와 동일하다면 check를 Ture로 돌린다.
     * */
    private static ActiveUserProfile activeUserProfile;
    private final SharedPreferences sharedPreferences;

    private final String ACTIVE_USER = "active_user";

    private ActiveUserProfile(Context context) {
        this.sharedPreferences = context.getSharedPreferences(ACTIVE_USER, Context.MODE_PRIVATE);
    }


    public static ActiveUserProfile getInstance(Context context) {
        if (activeUserProfile == null) {
            return new ActiveUserProfile(context);
        }
        return activeUserProfile;
    }

    public void setActiveUserProfileId(Long userId) {
        Set<String> userInfo = new HashSet<>();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(ACTIVE_USER, userId);
        editor.apply();
    }

    public Long getActiveUserProfileId() {
        return sharedPreferences.getLong(ACTIVE_USER, 0);
    }
}
