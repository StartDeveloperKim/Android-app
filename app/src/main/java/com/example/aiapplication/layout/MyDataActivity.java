package com.example.aiapplication.layout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aiapplication.R;
import com.example.aiapplication.medicine.entity.Medicine;
import com.example.aiapplication.medicine.service.MedicineService;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class MyDataActivity extends AppCompatActivity {

    private MedicineService medicineService;

    private static final String TAG = "MyDataActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydata);

        medicineService = new MedicineService(getApplicationContext());
        medicineService.getMedicines()
                .thenAccept(medicines -> {
                    drawTableLayoutByMedicineInfo(medicines);});
    }

    private void drawTableLayoutByMedicineInfo(List<Medicine> medicines) {
        Log.i(TAG, "drawTableLayoutByMedicineInfo");

        TableLayout tableLayout = findViewById(R.id.tableLayout);
        tableLayout.removeAllViews();

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

            tableRow.addView(getTextView(medicine.getCreateDate().format(DateTimeFormatter.ISO_DATE), 128));
            tableRow.addView(getTextView(medicine.getName(), 285));

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

    public void onMyDataTableRowClick(View view, Long id) {
        Log.i(TAG, id + " : 클릭");
        /*
        * TODO :: 다이어로그에 약 정보를 넘겨야 한다.
        * */
    }
}
