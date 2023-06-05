package com.example.aiapplication.layout;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aiapplication.R;
import com.example.aiapplication.layout.dialog.UserProfileDialogFragment;
import com.example.aiapplication.layout.dialog.UserProfileDialogListener;
import com.example.aiapplication.user.dao.ActiveUserProfile;
import com.example.aiapplication.user.dto.UserInfo;
import com.example.aiapplication.user.entity.User;
import com.example.aiapplication.user.service.UserService;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class UserActivity extends AppCompatActivity implements UserProfileDialogListener {

    private UserService userService;

    private ActiveUserProfile activeUserProfile;

    private final static String TAG = "UserActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        activeUserProfile = ActiveUserProfile.getInstance(this);

        Log.i(TAG, "UserActivity onCreate");
        userService = UserService.getInstance(getApplicationContext());
        userService.getUsers()
                .thenAccept(users -> {
                    runOnUiThread(() -> drawTableLayoutByUserInfo(users));});

    }

    public void drawTableLayoutByUserInfo(List<User> users) {
        Log.i(TAG, "DrawTableLayoutByUserInfo 호출");

        TableLayout tableLayout = findViewById(R.id.tableLayout);
        tableLayout.removeAllViews();

        for (User user : users) {
            TableRow tableRow = new TableRow(getApplicationContext());
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            tableRow.setClickable(true);
            tableRow.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    onTableRowClick(view, user.getId());
                }
            });

            tableRow.addView(getTextView(user.getName(), 75));
            tableRow.addView(getTextView(String.valueOf(user.getAge()), 75));
            tableRow.addView(getTextView(user.getGender(), 75));
            tableRow.addView(getTextView(user.getDivision(), 75));

            tableLayout.addView(tableRow);
        }
    }



    private TextView getTextView(String text, int width) {
        int widthInDp = (int) (width * getResources().getDisplayMetrics().density); // dp로 변환

        TextView textView = new TextView(getApplicationContext());
        textView.setLayoutParams(new TableRow.LayoutParams(widthInDp, TableRow.LayoutParams.WRAP_CONTENT));
        textView.setText(text);
        textView.setTextColor(Color.parseColor("#121111"));
        textView.setTypeface(Typeface.createFromAsset(getAssets(), "semaul.ttf"));
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
//        textView.setPadding(10, 10, 10, 10);

        return textView;
    }

    public void clickAddProfileButton(View view) {
        UserProfileDialogFragment userProfileDialogFragment = new UserProfileDialogFragment(this);
        userProfileDialogFragment.show(getSupportFragmentManager(), "user_profile_dialog");
    }


    public void onTableRowClick(View view, Long userId) {
        Log.i(TAG, "테이블 로우 클릭" + userId);
        userService.getUserById(userId)
                .thenAccept(user -> {
                    UserProfileDialogFragment userProfileDialogFragment =
                            new UserProfileDialogFragment(this, user, activeUserProfile.getActiveUserInfo().getId());
                    userProfileDialogFragment.show(getSupportFragmentManager(), "user_profile_dialog");
                });
    }

    public void onClickBackButtonAtUser(View view){
        finish();
    }

    @Override
    public void onDismissListener() {
        userService.getUsers()
                .thenAccept(users -> runOnUiThread(()-> drawTableLayoutByUserInfo(users)));
    }

    @Override
    public void saveUserInfo(UserInfo userInfo) {
        Log.i(TAG, "사용자 정보 저장");
        userService.addUser(userInfo)
                .thenCompose((Void) -> userService.getUsers())
                .thenAccept(users -> {
                    runOnUiThread(()-> drawTableLayoutByUserInfo(users));
                });
    }

    @Override
    public void updateUserInfo(UserInfo userInfo, Long userId) {
        Log.i(TAG, "사용자 정보 수정");
        if (userInfo.getActive()) {
            userService.getUserById(userId)
                    .thenAccept(user -> {
                        activeUserProfile.setActiveUserProfile(user.getId(), user.getName(), user.getAge(), user.getDivision());
                    });
        }

        userService.getUserById(userId)
                .thenApply(user -> {
                    user.update(userInfo);
                    return user;
                }).thenCompose((user)->userService.update(user))
                .thenCompose((Void) -> userService.getUsers())
                .thenAccept((users -> {
                    runOnUiThread(() -> drawTableLayoutByUserInfo(users));
                }));
    }

    @Override
    public void removeUser(Long id) {
        userService.getUserById(id)
                .thenCompose((user) -> userService.deleteUser(user))
                .thenCompose((Void) -> userService.getUsers())
                .thenAccept((users -> {
                    runOnUiThread(() -> drawTableLayoutByUserInfo(users));
                }));
    }

}
