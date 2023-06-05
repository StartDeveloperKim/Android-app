package com.example.aiapplication.layout;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aiapplication.R;
import com.example.aiapplication.firebase.FirebaseRepository;
import com.example.aiapplication.firebase.FirebaseSuccessListener;
import com.example.aiapplication.image.ImageInfo;
import com.example.aiapplication.medicine.dto.MedicineInfo;
import com.example.aiapplication.medicine.service.MedicineService;

import java.io.ByteArrayOutputStream;

import javax.annotation.Nullable;

public class MedicineResultActivity extends AppCompatActivity implements FirebaseSuccessListener {

    private final String TAG = "MedicineResultActivity";
    private FirebaseRepository firebaseRepository;
    private MedicineService medicineService;

    private TextView companyTextView;
    private TextView codeTextView;
    private TextView nameTextView;
    private TextView infoTextView;
    private TextView dangerInfoTextView;

    private MedicineInfo medicineInfo;
    private ImageInfo imageInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        firebaseRepository = new FirebaseRepository(this);
        medicineService = MedicineService.getInstance(getApplicationContext());

        companyTextView = findViewById(R.id.company);
        codeTextView = findViewById(R.id.code);
        nameTextView = findViewById(R.id.name);
        infoTextView = findViewById(R.id.info);
        dangerInfoTextView = findViewById(R.id.danger_info);

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
        setTextView(companyTextView, medicineInfo.getCompany());
        setTextView(nameTextView, medicineInfo.getName());
        setTextView(infoTextView, medicineInfo.getInfo());
        setTextView(dangerInfoTextView, medicineInfo.getDangerInfo());

        this.medicineInfo = medicineInfo;
    }

    private void setTextView(TextView view, String message) {
        view.setText(message);
    }

    public void clickAddDataButton(View view) {
        Bitmap bitmap = imageInfo.getBitmap().get();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
        byte[] byteArray = stream.toByteArray();

        medicineService.addMedicine(medicineInfo, byteArray);

        Toast.makeText(getApplicationContext(), "데이터가 추가되었습니다.", Toast.LENGTH_SHORT).show();
    }

    public void onClickBackButtonAtResult(View view) {
        finish();
    }
}
