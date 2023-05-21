package com.example.aiapplication.layout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aiapplication.MainActivity;
import com.example.aiapplication.R;
import com.example.aiapplication.image.ImageInfo;

public class MedicineResultActivity extends AppCompatActivity {

    private final String TAG = "MedicineResultActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Log.i(TAG, "약 결과 페이지로 INTENT");
        /*
        * TODO ::
        *  - 현재는 단순히 찍은 이미지를 ImageView로 보여주고 있는데 해당 이미지 데이터를
        *    AI로 분석하는 로직을 아래에 작성해야 한다.
        * */
        Bitmap bitmap = ImageInfo.getInstance()
                .getBitmap().orElseThrow(IllegalArgumentException::new);

        ImageView imageView = findViewById(R.id.medicine_image);
        imageView.setImageBitmap(bitmap);
    }

    public void clickAddDataButton(View view) {
        /*
        * TODO:: 휴대폰 내부 저장소 RoomDB에 전달된 데이터를 저장하는 로직을 작성해야 한다.
        * */
        Log.i(TAG, "데이터추가 Button Click");
    }

    public void clickCheckButton(View view) {
        /*
        * TODO:: 확인 버튼을 누르면 메인엑티비티로 넘어간다.
        * */
        Log.i(TAG, "확인 Button Click");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
