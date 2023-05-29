package com.example.aiapplication.layout;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import com.example.aiapplication.R;
import com.example.aiapplication.layout.dialog.MedicineDialogFragment;
import com.example.aiapplication.layout.dialog.MedicineDialogListener;
import com.example.aiapplication.medicine.entity.Medicine;
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

        TableLayout tableLayout = findViewById(R.id.myData_tableLayout);
        tableLayout.removeAllViews(); // 테이블 자식 컴포넌트 전체 삭제


        addTableHeader(tableLayout); // 테이블 헤더 추가

        for (Medicine medicine : medicines) {
            TableRow tableRow = new TableRow(getApplicationContext());
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            tableRow.setClickable(true);
            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onMyDataTableRowClick(view, medicine.getId());
                }
            });

            tableRow.addView(getTextView(medicine.getCreateDate().format(DateTimeFormatter.ISO_DATE), "Data"));
            tableRow.addView(getTextView(medicine.getName(), "Data"));
            tableRow.addView(getCheckBox(medicine.getCode()));

            tableLayout.addView(tableRow);
        }
    }

    private void addTableHeader(TableLayout tableLayout) {
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));

        headerRow.addView(getTextView("날 짜", "Header"));
        headerRow.addView(getTextView("약 명", "Header"));
        headerRow.addView(getTextView("선 택", "Header"));

        tableLayout.addView(headerRow);
    }

    private TextView getTextView(String text, String type) {
        TextView textView = new TextView(getApplicationContext());
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(3, 10, 5, 10);


        if (type.equals("Header")) {
            textView.setTextSize(18);
            textView.setTypeface(null, Typeface.BOLD);
        }else {
            textView.setWidth(350);
            textView.setTextSize(16);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setMaxLines(1);
        }
        return textView;
    }

    /* TODO :: 체크박스 가운데 정렬 필요 보기 불편함*/
    private CheckBox getCheckBox(String medicineCode) {
        CheckBox checkBox = new CheckBox(this);
        checkBox.setGravity(Gravity.CENTER);
        checkBox.setPadding(30, 10, 30, 10);

        checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                checkedMedicine.add(medicineCode);
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
