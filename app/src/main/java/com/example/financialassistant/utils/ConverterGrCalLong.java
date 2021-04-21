package com.example.financialassistant.utils;

import androidx.room.TypeConverter;

import java.util.Date;
import java.util.GregorianCalendar;

public class ConverterGrCalLong {

    @TypeConverter
    public static GregorianCalendar fromTimestamp(Long value) {
        if (value == null)
            return null;
        else {
            GregorianCalendar temp = new GregorianCalendar();
            temp.setTimeInMillis(value);
            return temp;
        }
    }

    @TypeConverter
    public static Long dateToTimestamp(GregorianCalendar date) {
        return date == null ? null : date.getTimeInMillis();
    }
}
