package com.example.aiapplication.layout;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aiapplication.R;

public class MyDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydata);
        /*
        * TODO ::
        *  - 페이지가 로드되면 Medicine 데이터를 불러와서 리스트로 보여주는 로직이 필요
        * */
    }

    public void clickAddCustomDataButton(View view) {
        /*
        * TODO :: 약 정보 직접 추가 기능, 이름, 날짜, 약 코드, 복용여부 등등
        * */
    }

    public void clickCompareButton(View view) {
        /*
        * TODO ::  병용여부 판단
        *  - 기존에 저장된 데이터를 여러개 선택하고 이들의 조합으로 병용여부를 보여줘야 한다.
        * */
    }

    public void onTableRowClick(View view) {
    }
}
