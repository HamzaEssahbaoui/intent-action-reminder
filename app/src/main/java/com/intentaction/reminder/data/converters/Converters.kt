package com.intentaction.reminder.data.converters

// Example type converter
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Converters {

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