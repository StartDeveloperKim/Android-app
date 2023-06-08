package com.example.aiapplication.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aiapplication.R;
import com.example.aiapplication.medicine.dto.MedicineAlarmDto;
import com.example.aiapplication.medicine.service.MedicineService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class AlarmActivity extends AppCompatActivity {

    private AlarmSharedPreferences alarmSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        alarmSharedPreferences = new AlarmSharedPreferences(this);

        Intent intent = getIntent();
        List<MedicineAlarmDto> medicineList = (List<MedicineAlarmDto>) intent.getSerializableExtra("medicine_list");

        String[] medicineArray = medicineList.stream()
                .map(MedicineAlarmDto::getName)
                .toArray(String[]::new);

        setSpinner(medicineArray);

        if (medicineArray.length > 0) {
            TimePicker timepicker = findViewById(R.id.timePicker);
            alarmSharedPreferences.setTimePickerByLastAlarm(timepicker, medicineList.get(0).getId());
        }
    }

    private void setSpinner(String[] medicineArray) {
        Spinner spinner = (Spinner) findViewById(R.id.medicine_list);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, medicineArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedMedicine = medicineArray[position];
                Toast.makeText(getApplicationContext(), "[ " + selectedMedicine + " ]약이 선택되었습니다", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void clickAddAlarmButton(View view) {
        TimePicker picker=findViewById(R.id.timePicker);
        Spinner spinner = findViewById(R.id.medicine_list);

        int hour = picker.getHour();
        int minute = picker.getMinute();

        // 현재 지정된 시간으로 알람 시간 설정
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // 이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        Date currentDateTime = calendar.getTime();
        String medicine = spinner.getSelectedItem().toString();

        String date_text = new SimpleDateFormat("h : mm a ", Locale.getDefault()).format(currentDateTime);
        Toast.makeText(getApplicationContext(),"[ " + medicine + " ]" + date_text + "으로 알람이 설정되었습니다.", Toast.LENGTH_SHORT).show();

        alarmSharedPreferences.setTimePickerByLastAlarm(picker, calendar.getTimeInMillis());

        diaryNotification(calendar, medicine);
    }

    private void diaryNotification(Calendar calendar, String medicine)
    {
        PackageManager pm = this.getPackageManager();
        ComponentName receiver = new ComponentName(this, DeviceBootReceiver.class);

        Log.d("Moon","Bootreceiver됨." + receiver);

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.putExtra("chosenMedicine", medicine);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

//        //  Preference에 설정한 값 저장
//        SharedPreferences.Editor editor = getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
//        editor.putLong("nextNotifyTime", (long) calendar.getTimeInMillis());
//        editor.apply();

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }

        // 부팅 후 실행되는 리시버 사용가능하게 설정
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
}
