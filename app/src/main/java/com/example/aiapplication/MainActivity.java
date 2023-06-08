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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.aiapplication.camera.Photo;
import com.example.aiapplication.image.ImageInfo;
import com.example.aiapplication.layout.MedicineResultActivity;
import com.example.aiapplication.layout.MyDataActivity;
import com.example.aiapplication.layout.UserActivity;
import com.example.aiapplication.layout.dialog.LoadingDialog;
import com.example.aiapplication.medicine.service.MedicineService;
import com.example.aiapplication.server.PillCodeController;
import com.example.aiapplication.server.PillCodeRequester;
import com.example.aiapplication.user.dao.ActiveUserProfile;
import com.example.aiapplication.user.dto.ActiveUserInfo;
import com.example.aiapplication.user.entity.Division;
import com.example.aiapplication.user.service.UserService;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private static final int TIME_INTERVAL = 2000;
    private long backPressedTime;

    private ActivityResultLauncher<Intent> cameraResultLauncher;
    private ActivityResultLauncher<Intent> galleryResultLauncher;

    private Photo photo;
    private ImageInfo imageInfo = ImageInfo.getInstance();

    private ActiveUserProfile activeUserProfile;
    private PillCodeController pillCodeController;

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

        MedicineService.getInstance(getApplicationContext());
        UserService.getInstance(getApplicationContext());

        activeUserProfile = ActiveUserProfile.getInstance(this);

        photo = new Photo(); // 카메라 인스턴스 생성
        pillCodeController = PillCodeRequester.getApiService();

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


        cameraResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Bitmap bitmap = photo.afterTakePicture();
                            addCameraTextMessage();
                            setImageView(bitmap);
                            imageInfo.setBitmap(bitmap);
                        }
                    }
                }
        );

        galleryResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK) {
                            Intent intent = result.getData();
                            Uri uri = intent.getData();

                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                imageInfo.setBitmap(bitmap);
                                setImageView(bitmap);
                                addCameraTextMessage();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );

        setTextViewActiveUserProfile();
    }

    private void setTextViewActiveUserProfile() {
        ActiveUserInfo activeUserInfo = activeUserProfile.getActiveUserInfo();
        TextView nameTextView = findViewById(R.id.name);
        TextView ageTextView = findViewById(R.id.age);
        TextView divisionTextView = findViewById(R.id.division);

        nameTextView.setText(activeUserInfo.getName());
        ageTextView.setText(String.valueOf(activeUserInfo.getAge()));
        divisionTextView.setText(activeUserInfo.getDivision());
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
                cameraResultLauncher.launch(intent);
            }
        }
    }

    private void addCameraTextMessage() {
        TextView textView = findViewById(R.id.camera_info);
        textView.setText("사진을 다시 찍고 싶으시면 이미지를 다시 터치해주세요");
    }

    public void clickMyDataButton(View view) {
        Intent intent = new Intent(getApplicationContext(), MyDataActivity.class);
        startActivity(intent);
    }

    public void clickGalleryButton(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.setAction(Intent.ACTION_PICK);
        galleryResultLauncher.launch(intent);
    }

    public void clickSettingButton(View view) {
        Intent intent = new Intent(getApplicationContext(), UserActivity.class);
        startActivity(intent);
    }

    public void clickImageAnalyzeButton(View view) {
        if (imageInfo.getBitmap().isPresent()) {
            Log.i("MainActivity", "결과 페이지로 INTENT 시도");

            File imageFile = imageInfo.convertBitmapToFile(getApplicationContext());
            RequestBody requestBody = RequestBody.create(imageFile, MediaType.parse("image/*"));
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), requestBody);
            Call<ResponseBody> call = pillCodeController.detectImage(imagePart);

            LoadingDialog loadingDialog = new LoadingDialog(this);
            loadingDialog.showLoadingDialog();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();
                        if (responseBody != null) {
                            try {
                                Log.i("MainActivity", "body");
                                loadingDialog.dismissLoadingDialog();

                                JSONObject jsonObject = new JSONObject(responseBody.string());
                                String medicineCode = jsonObject.getString("result");
                                String detectImageURL = jsonObject.getString("url");

                                ActiveUserInfo activeUserInfo = activeUserProfile.getActiveUserInfo();
                                Log.i("MainActivity", "Name : " + activeUserInfo.getName() + "// Age : " + activeUserInfo.getAge());
                                Intent intent = new Intent(getApplicationContext(), MedicineResultActivity.class);
                                intent.putExtra("medicineCode", medicineCode);
                                intent.putExtra("age", activeUserInfo.getAge());
                                intent.putExtra("dbKey", Division.getDBKey(activeUserInfo.getDivision()));
                                intent.putExtra("detect_image_url", detectImageURL);

                                startActivity(intent);
                            } catch (IOException | JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }else {
                        loadingDialog.dismissLoadingDialog();
                        showToastMessage("서버에서 잘못된 응답이 왔습니다.");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    loadingDialog.dismissLoadingDialog();
                    showToastMessage("서버와의 통신이 실패했습니다.");
                    t.printStackTrace();
                }
            });

        }else{
            showToastMessage("사진을 찍어야 분석이 가능합니다.");
        }
    }

    private void showToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
