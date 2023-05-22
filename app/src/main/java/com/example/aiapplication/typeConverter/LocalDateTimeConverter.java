package com.example.aiapplication.typeConverter;

import androidx.room.TypeConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeConverter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    @TypeConverter
    public static LocalDateTime toLocalDateTime(String date) {
        return date == null ? null : LocalDateTime.parse(date, DATE_TIME_FORMATTER);
    }

    @TypeConverter
    public static String fromLocalDateTime(LocalDateTime date) {
        return date == null ? null : date.format(DATE_TIME_FORMATTER);
    }
}
