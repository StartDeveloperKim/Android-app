package com.example.aiapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.aiapplication.camera.Photo;
import com.example.aiapplication.image.ImageInfo;
import com.example.aiapplication.layout.MedicineResultActivity;
import com.example.aiapplication.layout.MyDataActivity;
import com.example.aiapplication.layout.UserActivity;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private static final int TIME_INTERVAL = 2000;
    private long backPressedTime;

    private ActivityResultLauncher<Intent> resultLauncher;

    private Photo photo;
    private ImageInfo imageInfo = ImageInfo.getInstance();

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();

        if (backPressedTime + TIME_INTERVAL > currentTime) {
            super.onBackPressed();
            finish();
        }else{
            showToastMessage("뒤로 버튼을 두 번 눌러주세요");
        }

        backPressedTime = currentTime;

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photo = new Photo(); // 카메라 인스턴스 생성

        //권한 체크
        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("카메라 권한이 필요합니다.")
                .setDeniedMessage("거부하셨습니다.")
                .setPermissions(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

        if (imageInfo.getBitmap().isPresent()) {
            setImageView(imageInfo.getBitmap().get());
        }


        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Bitmap bitmap = photo.afterTakePicture();
                            /*
                            * TODO :: 2023-05-21
                            *  1. 여기서 찍은 이미지를 AI에 넘겨줘야 한다.
                            *  2. 지금은 촬영 후 결과 페이지에 bitmap 데이터를 넘겨주기만 한다. => MedicineResultActivity
                            *   - 후보1 : 안드로이드 앱에 AI 모델을 삽입한다.
                            *   - 후보2 : HTTP 통신 프로토콜을 활용해서 서버에 띄어져있는 AI 모델에게 사진을 전달한 후 DB에서 정보를 가져온다.
                            * */
                            setImageView(bitmap);
                            imageInfo.setBitmap(bitmap);
                        }
                    }
                }
        );

    }

    private void setImageView(Bitmap bitmap) {
        ImageView imageView = findViewById(R.id.iv_result);
        imageView.setImageBitmap(bitmap);
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            showToastMessage("권한이 허용되었습니다.");
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            showToastMessage("권한이 거부되었습니다.");
        }
    };



    public void clickCameraButton(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = photo.createImageFile(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            } catch (IOException e) {

            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), photoFile);
                photo.setPhotoUri(photoUri);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                resultLauncher.launch(intent);
            }
        }
    }

    public void clickMyDataButton(View view) {
        Intent intent = new Intent(getApplicationContext(), MyDataActivity.class);
        startActivity(intent);
    }

    public void clickSettingButton(View view) {
        Intent intent = new Intent(getApplicationContext(), UserActivity.class);
        startActivity(intent);
    }

    public void clickImageAnalyzeButton(View view) {
        if (imageInfo.getBitmap().isPresent()) {
            Log.i("MainActivity", "결과 페이지로 INTENT 시도");

            Intent intent = new Intent(getApplicationContext(), MedicineResultActivity.class);
            startActivity(intent);
        }else{
            showToastMessage("사진을 찍어야 분석이 가능합니다.");
        }
    }

    private void showToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
