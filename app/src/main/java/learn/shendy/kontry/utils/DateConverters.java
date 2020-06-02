package learn.shendy.kontry.utils;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverters {
    @TypeConverter
    public static Date toDate(long timestamp) {
        return new Date(timestamp);
    }

    @TypeConverter
    public static long toTimestamp(Date date) {
        return date.getTime();
    }
}
