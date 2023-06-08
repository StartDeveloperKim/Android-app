package com.example.aiapplication.alarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AlarmSharedPreferences {

    private SharedPreferences alarmDataSharedPreferences;
    private SharedPreferences dailyAlarmSharedPreferences;

    private static final String ALARM_DATA = "alarm_data";
    private static final String DAILY_ALARM = "daily_alarm";

    public AlarmSharedPreferences(Context context) {
        this.alarmDataSharedPreferences = context.getSharedPreferences(ALARM_DATA, Context.MODE_PRIVATE);
        this.dailyAlarmSharedPreferences = context.getSharedPreferences(DAILY_ALARM, Context.MODE_PRIVATE);
    }

    public void saveAlaram(Long id, long time) {
        SharedPreferences.Editor editor = alarmDataSharedPreferences.edit();
        editor.putLong(String.valueOf(id), time);
        editor.apply();
    }

    public long getAlarmData(Long id) {
        return alarmDataSharedPreferences.getLong(String.valueOf(id), -1L);
    }

    public void setTimePickerByLastAlarm(TimePicker timePicker, Long id) {
        long alarmData = getAlarmData(id);

        GregorianCalendar alarmTime = new GregorianCalendar();
        alarmTime.setTimeInMillis(alarmData);

        SimpleDateFormat HourFormat = new SimpleDateFormat("kk", Locale.getDefault());
        SimpleDateFormat MinuteFormat = new SimpleDateFormat("mm", Locale.getDefault());

        if (alarmData != -1L) {
            alarmTime.setTimeInMillis(alarmData);
            Date currentTime = alarmTime.getTime();
            int hour = Integer.parseInt(HourFormat.format(currentTime));
            int minute = Integer.parseInt(MinuteFormat.format(currentTime));

            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        }
    }
}
