package com.intentaction.reminder.db.converters

// Example type converter
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromZonedDateTime(zonedDateTime: ZonedDateTime?): String? {
        return zonedDateTime?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toZonedDateTime(data: String?): ZonedDateTime? {
        return if (data == null) null else ZonedDateTime.parse(data, DateTimeFormatter.ISO_ZONED_DATE_TIME)
    }
}