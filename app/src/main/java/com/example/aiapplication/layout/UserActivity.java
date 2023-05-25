package com.example.aiapplication.layout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aiapplication.R;
import com.example.aiapplication.layout.dialog.UserProfileDialogFragment;
import com.example.aiapplication.layout.dialog.UserProfileDialogListener;
import com.example.aiapplication.user.dto.UserInfo;
import com.example.aiapplication.user.entity.User;
import com.example.aiapplication.user.service.UserService;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class UserActivity extends AppCompatActivity implements UserProfileDialogListener {

    private UserService userService;

    private final static String TAG = "UserActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        userService = new UserService(getApplicationContext());
        userService.getUsers()
                .thenAccept(users -> { drawTableLayoutByUserInfo(users);});

    }

    public void drawTableLayoutByUserInfo(List<User> users) {
        Log.i(TAG, "DrawTableLayoutByUserInfo 호출");

        TableLayout tableLayout = findViewById(R.id.tableLayout);
        tableLayout.removeAllViews();

        for (User user : users) {
            System.out.println("user.toString() = " + user.toString());

            TableRow tableRow = new TableRow(getApplicationContext());
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            tableRow.setClickable(true);
            tableRow.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    /*
                     * TODO :: 2023-05-22
                     *  - 테이블 로우를 클릭하면 다이어로그가 뜬다.
                     *  - 해당 다이어로그에는 로우의 사용자정보를 조회하여 표시된다.
                     * */
                    onTableRowClick(view, user.getId());
                }
            });

            TextView nameTextView = getTextView(user.getName(), 100);
            TextView ageTextView = getTextView("25", 310);

            tableRow.addView(nameTextView);
            tableRow.addView(ageTextView); // 나중에 프로필별 연관관계 설정을 통해 먹는 알약 개수를 알아오자 현재는 임의의 값임

            tableLayout.addView(tableRow);
        }
        System.out.println("TableLayout 그리기 상태체크");
    }

    private TextView getTextView(String text, int width) {
        int widthInDp = (int) (width * getResources().getDisplayMetrics().density); // dp로 변환

        TextView textView = new TextView(getApplicationContext());
        textView.setLayoutParams(new TableRow.LayoutParams(widthInDp, TableRow.LayoutParams.WRAP_CONTENT));
        textView.setText(text);
        textView.setPadding(0, 0, 0, 0);

        return textView;
    }

    public void clickAddProfileButton(View view) {
        UserProfileDialogFragment userProfileDialogFragment = new UserProfileDialogFragment(this);
        userProfileDialogFragment.show(getSupportFragmentManager(), "user_profile_dialog");
    }

    public void clickDeleteButton(View view) {
        /*
        * TODO :: 삭제 버튼을 누르면 리스트 옆에 삭제 버튼이 나오는 그림
        *  - 현재는 삭제 버튼을 누르면 모든 데이터가 삭제된다.
        * */
        userService.removeAll()
                .thenAccept((Void) -> onDismissListener());
        Toast.makeText(getApplicationContext(), "전체 데이터가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
    }

    public void onTableRowClick(View view, Long userId) {
        Log.i(TAG, "테이블 로우 클릭" + userId);
        userService.getUserById(userId)
                .thenAccept(user -> {
                    UserProfileDialogFragment userProfileDialogFragment = new UserProfileDialogFragment(this, user);
                    userProfileDialogFragment.show(getSupportFragmentManager(), "user_profile_dialog");
                });
    }

    @Override
    public void onDismissListener() {
        userService.getUsers()
                .thenAccept(users -> drawTableLayoutByUserInfo(users));
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

}
