package com.example.aiapplication.layout;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.aiapplication.R;
import com.example.aiapplication.layout.dialog.MedicineDialogFragment;
import com.example.aiapplication.layout.dialog.MedicineDialogListener;
import com.example.aiapplication.medicine.entity.Medicine;
import com.example.aiapplication.medicine.service.MedicineCombinationUtil;
import com.example.aiapplication.medicine.service.MedicineService;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MyDataActivity extends AppCompatActivity implements MedicineDialogListener {

    private MedicineService medicineService;
    private List<String> checkedMedicine;

    private Button compareButton;

    private static final String TAG = "MyDataActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydata);

        medicineService = MedicineService.getInstance(getApplicationContext());
        medicineService.getMedicines()
                .thenAccept(medicines -> {
                    runOnUiThread(() -> {drawTableLayoutByMedicineInfo(medicines);});});

        checkedMedicine = new LinkedList<>();
        compareButton = findViewById(R.id.btn_compare);
    }

    private void drawTableLayoutByMedicineInfo(List<Medicine> medicines) {
        Log.i(TAG, "drawTableLayoutByMedicineInfo");

        LinearLayout parentLayout = findViewById(R.id.medicine_list);
        parentLayout.removeAllViews();


        for (Medicine medicine : medicines) {
            LinearLayout childLayout = new LinearLayout(getApplicationContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            childLayout.setLayoutParams(layoutParams);
            childLayout.setOrientation(LinearLayout.HORIZONTAL);
            childLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onMyDataTableRowClick(view, medicine.getId());
                }
            });

            childLayout.addView(getTextView(medicine.getCreateDate().format(DateTimeFormatter.ISO_DATE), 93));
            childLayout.addView(getTextView(medicine.getName(), 188));
            childLayout.addView(getCheckBox(medicine.getCode()));

            parentLayout.addView(childLayout);
        }
        Log.i(TAG, "테이블 로우 그리기 끝");
    }

    private TextView getTextView(String text, int width) {
        Log.i(TAG, "getTextView : " + text);
        TextView textView = new TextView(getApplicationContext());
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        textViewParams.width = dpToPx(width);
        textView.setLayoutParams(textViewParams);
        textView.setText(text);
        textView.setTextColor(Color.parseColor("#121111"));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(Typeface.createFromAsset(getAssets(), "semaul.ttf"));
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setMaxLines(1);

        return textView;
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private CheckBox getCheckBox(String medicineCode) {
        CheckBox checkBox = new CheckBox(this);
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        checkBox.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.checkbox_color));

        checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                checkedMedicine.add(medicineCode); // 약 코드가 저장
            }else{
                checkedMedicine.remove(medicineCode);
            }

            if (hasCheckedItems()) {
                compareButton.setEnabled(true);
            }else {
                compareButton.setEnabled(false);
            }

        });
        return checkBox;
    }

    private boolean hasCheckedItems() {
        return checkedMedicine.size() >= 2;
    }

    public void clickAddCustomDataButton(View view) {
        /*
        * TODO :: 약 정보 직접 추가 기능, 사진, 이름, 날짜는 추가하는 날짜로 설정, 약 코드, 복용여부 등등
        * */
    }

    public void clickCompareButton(View view) {
        /*
        * TODO ::  병용여부 판단
        *  - 기존에 저장된 데이터를 여러개 선택하고 이들의 조합으로 병용여부를 보여줘야 한다.
        *  - checkedMedicine 리스트에 있는 약 Code들을 조합을 이용해서 조합할 수 있는 코드 목록을 생성한다.
        *  - 그 다음 데이터베이스에 해당 코드들을 보내고 데이터를 파싱해서 Dialog로 띄운다.
        * */
        List<String> medicineCombinations = MedicineCombinationUtil.generateCombinations(checkedMedicine);
        // 여기서 medicineCombinations 정보를 파이어베이스에 전달하여 정보를 받아온다.
        // 그 후에 Dialog에 정보를 전달하여 정보를 띄운다.

    }

    public void onClickBackButton(View view) {
        finish();
    }

    public void onMyDataTableRowClick(View view, Long id) {
        Log.i(TAG, id + " : 클릭");
        try {
            Medicine medicine = medicineService.getMedicineById(id).get(5, TimeUnit.SECONDS);
            MedicineDialogFragment medicineDialogFragment = new MedicineDialogFragment(medicine, this);
            medicineDialogFragment.show(getSupportFragmentManager(), "medicine_dialog");
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteMedicineInfo(Medicine medicine) {
        medicineService.removeMedicine(medicine)
                .thenCompose((Void) -> medicineService.getMedicines())
                .thenAccept(medicines -> {
                    runOnUiThread(()-> drawTableLayoutByMedicineInfo(medicines));
                });
        Toast.makeText(getApplicationContext(), "약 정보가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
    }
}
