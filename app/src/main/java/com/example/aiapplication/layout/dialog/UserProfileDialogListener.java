package com.example.aiapplication.layout.dialog;

import com.example.aiapplication.user.dto.UserInfo;

public interface UserProfileDialogListener {

    void onDismissListener();

    void saveUserInfo(UserInfo userInfo);

    void updateUserInfo(UserInfo UserInfo, Long userId);

    void removeUser(Long id);
}
