package com.example.aiapplication.user.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.aiapplication.user.dto.ActiveUserInfo;

public class ActiveUserProfile {

    /*
     * TODO ::
     *  - User 활성화 순서
     *  1. 현재 활성화된 User의 정보를 프로필 Dialog를 띄울 때 전송한다.
     *  2. 이 정보가 현재 User의 정보와 동일하다면 check를 Ture로 돌린다.
     * */
    private static ActiveUserProfile activeUserProfile;
    private final SharedPreferences sharedPreferences;

    private String SHARED_PREFERENCES = "active_user";

    private final String ACTIVE_USER_ID = "id";
    private final String ACTIVE_USER_NAME = "name";
    private final String ACTIVE_USER_DIVISION = "division";
    private final String ACTIVE_USER_AGE = "age";

    private ActiveUserProfile(Context context) {
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }


    public static ActiveUserProfile getInstance(Context context) {
        if (activeUserProfile == null) {
            return new ActiveUserProfile(context);
        }
        return activeUserProfile;
    }

    public void setActiveUserProfile(Long id, String name, int age, String division) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(ACTIVE_USER_ID, id);
        editor.putString(ACTIVE_USER_NAME, name);
        editor.putInt(ACTIVE_USER_AGE, age);
        editor.putString(ACTIVE_USER_DIVISION, division);

        editor.apply();
    }

    public ActiveUserInfo getActiveUserInfo() {
//        return sharedPreferences.getLong(ACTIVE_USER, 0);
        return new ActiveUserInfo(
                sharedPreferences.getLong(ACTIVE_USER_ID, -1),
                sharedPreferences.getString(ACTIVE_USER_NAME, "No Active User"),
                sharedPreferences.getInt(ACTIVE_USER_AGE, -1),
                sharedPreferences.getString(ACTIVE_USER_DIVISION, "No Active User")
        );
    }
}
