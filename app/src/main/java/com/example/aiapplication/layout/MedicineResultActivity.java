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
import com.example.aiapplication.medicine.service.MedicineService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.annotation.Nullable;

public class MedicineResultActivity extends AppCompatActivity implements FirebaseSuccessListener {

    private final String TAG = "MedicineResultActivity";
    private FirebaseRepository firebaseRepository;
    private MedicineService medicineService;

    private TextView company;
    private TextView name;
    private TextView info;
    private TextView dangerInfo;

    private MedicineInfo medicineInfo;
    private ImageInfo imageInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        firebaseRepository = new FirebaseRepository(this);
        medicineService = new MedicineService(getApplicationContext());

        company = findViewById(R.id.medicine_company);
        name = findViewById(R.id.medicine_name);
        info = findViewById(R.id.medicine_effect);
        dangerInfo = findViewById(R.id.dangerous);

        /*
        * TODO ::
        *  - 현재는 단순히 찍은 이미지를 ImageView로 보여주고 있는데 해당 이미지 데이터를
        *    AI로 분석하는 로직을 아래에 작성해야 한다.
        * */

        imageInfo = ImageInfo.getInstance();
        Bitmap bitmap = imageInfo.getBitmap().orElseThrow(IllegalArgumentException::new);

        ImageView imageView = findViewById(R.id.medicine_image);
        imageView.setImageBitmap(bitmap);

        /*
        * TODO :: 약 데이터 가져오면서 해야할 것
        *  - MedicineCode는 AI가 분석 후 반환하는 정보이다.
        *  - collectionName은 사용자 프로필에서 Division 정보를 사용한다.
        * */
        firebaseRepository.getMedicineInfo("651904740", "Medicine");
    }

    @Override
    public void onFirebaseDataParsed(MedicineInfo medicineInfo) {
        setTextView(company, medicineInfo.getCompany());
        setTextView(name, medicineInfo.getName());
        setTextView(info, medicineInfo.getInfo());
        setTextView(dangerInfo, medicineInfo.getDangerInfo());

        this.medicineInfo = medicineInfo;
    }

    private void setTextView(TextView view, String message) {
        view.setText(message);
    }

    private String getText(TextView view) {
        return (String) view.getText();
    }

    public void clickAddDataButton(View view) {
        Log.i(TAG, "데이터추가 Button Click");

        Bitmap bitmap = imageInfo.getBitmap().get();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        medicineService.addMedicine(medicineInfo, byteArray);
    }

    public void clickCheckButton(View view) {
        Log.i(TAG, "확인 Button Click");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
