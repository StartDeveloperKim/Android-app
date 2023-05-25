package com.example.aiapplication.layout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aiapplication.MainActivity;
import com.example.aiapplication.R;
import com.example.aiapplication.firebase.FirebaseRepository;
import com.example.aiapplication.firebase.FirebaseSuccessListener;
import com.example.aiapplication.image.ImageInfo;
import com.example.aiapplication.medicine.dto.MedicineInfo;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import javax.annotation.Nullable;

public class MedicineResultActivity extends AppCompatActivity implements FirebaseSuccessListener {

    private final String TAG = "MedicineResultActivity";
    private FirebaseRepository firebaseRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        firebaseRepository = new FirebaseRepository(this);

        /*
        * TODO ::
        *  - 현재는 단순히 찍은 이미지를 ImageView로 보여주고 있는데 해당 이미지 데이터를
        *    AI로 분석하는 로직을 아래에 작성해야 한다.
        * */
        Bitmap bitmap = ImageInfo.getInstance()
                .getBitmap().orElseThrow(IllegalArgumentException::new);

        ImageView imageView = findViewById(R.id.medicine_image);
        imageView.setImageBitmap(bitmap);

        firebaseRepository.getMedicineInfo("651904740", "Medicine");
    }

    private void setTextView(TextView view, String message) {
        view.setText(message);
    }

    @Override
    public void onFirebaseDataParsed(MedicineInfo medicineInfo) {
        TextView company = findViewById(R.id.medicine_company);
        TextView name = findViewById(R.id.medicine_name);
        TextView info = findViewById(R.id.medicine_effect);
        TextView dangerInfo = findViewById(R.id.dangerous);

        setTextView(company, medicineInfo.getCompany());
        setTextView(name, medicineInfo.getName());
        setTextView(info, medicineInfo.getInfo());
        setTextView(dangerInfo, medicineInfo.getDangerInfo());
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
