package com.example.aiapplication.layout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aiapplication.R;
import com.example.aiapplication.layout.dialog.UserProfileDialogFragment;
import com.example.aiapplication.layout.dialog.UserProfileDialogListener;
import com.example.aiapplication.user.entity.User;
import com.example.aiapplication.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class UserActivity extends AppCompatActivity implements UserProfileDialogListener {

    private UserService userService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        userService = new UserService(getApplicationContext());
        drawTableLayoutByUserInfo(null);

    }


    @Override
    public void drawTableLayoutByUserInfo(User user) {
        Log.i("UserActivity Tag", "테이블 그리기 호출");

        try {
            List<User> users;
            if (user != null) {
                users = userService.addUser(user)
                        .thenCompose((Void) -> userService.getUsers()).get(5, TimeUnit.SECONDS);
            }else{
                users = userService.getUsers().get(5, TimeUnit.SECONDS);
            }
            for (User userInfo : users) {
                System.out.println("userInfo.toString() = " + userInfo.toString());
            }
            TableLayout tableLayout = findViewById(R.id.tableLayout);
            tableLayout.removeAllViews();
            addTextView(tableLayout, users);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private void addTextView(TableLayout tableLayout, List<User> users) {
        Log.i("UserActivity Tag", "TextView 추가 메서드 호출");
        for (User user : users) {
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
                    onTableRowClick(view);
                }
            });

            tableRow.addView(getTextView(user.getName(), 100));
            tableRow.addView(getTextView("25", 310)); // 나중에 프로필별 연관관계 설정을 통해 먹는 알약 개수를 알아오자 현재는 임의의 값임

            tableLayout.addView(tableRow);
        }
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
        /*
        * TODO :: 프로필을 등록할 수 있는 Dialog가 팝업된다.
        *  - 지금은 임의의 데이터를 추가시키는 로직만 추가한다.
        *  - 나중에 프론트엔드와 코드를 통합할 떄는 Dialog에 입력된 데이터를 가져와야 한다.
        * */
        UserProfileDialogFragment userProfileDialogFragment = new UserProfileDialogFragment(this);
        userProfileDialogFragment.show(getSupportFragmentManager(), "user_profile_dialog");
    }

    public void clickDeleteButton(View view) {
        /*
        * TODO :: 삭제 버튼을 누르면 리스트 옆에 삭제 버튼이 나오는 그림
        *  - 현재는 삭제 버튼을 누르면 모든 데이터가 삭제된다.
        * */
        userService.removeAll()
                .thenAccept((Void) -> drawTableLayoutByUserInfo(null));
        Toast.makeText(getApplicationContext(), "전체 데이터가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
    }

    public void onTableRowClick(View view) {
        // TODO :: 사용자 정보를 수정할 수 있는 Dialog가 팝업된다.
    }
}
