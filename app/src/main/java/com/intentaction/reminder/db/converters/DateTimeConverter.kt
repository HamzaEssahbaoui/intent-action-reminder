package com.intentaction.reminder.db.converters

import android.content.Context
import android.os.Build
import android.text.format.DateFormat
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DateTimeConverter private constructor() {

    companion object {
        private const val FORMAT_TIME_12_HOUR = "h:mm a"
        private const val FORMAT_TIME_24_HOUR = "H:mm"
        private const val FORMAT_DATE_TIME_12_HOUR_SHORT = "M-d, h:mm a"
        private const val FORMAT_DATE_TIME_24_HOUR_SHORT = "M-d, H:mm"
        private const val FORMAT_DATE_TIME_WITH_YEAR_12_HOUR_SHORT = "M-d-yy, h:mm a"
        private const val FORMAT_DATE_TIME_WITH_YEAR_24_HOUR_SHORT = "M-d-yy, H:mm"
        private const val FORMAT_DATE_TIME_12_HOUR = "MMMM d, h:mm a"
        private const val FORMAT_DATE_TIME_24_HOUR = "MMMM d, H:mm"
        private const val FORMAT_DATE_TIME_WITH_YEAR_12_HOUR = "MM d yyyy, h:mm a"
        private const val FORMAT_DATE_TIME_WITH_YEAR_24_HOUR = "Mm d yyyy, H:mm"

        @RequiresApi(Build.VERSION_CODES.O)
        fun getTimeFormatter(context: Context): DateTimeFormatter {
            val pattern = if (DateFormat.is24HourFormat(context))
                FORMAT_TIME_24_HOUR
            else FORMAT_TIME_12_HOUR

            return DateTimeFormatter.ofPattern(pattern)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getDateTimeFormatter(
            context: Context,
            isShort: Boolean = false,
            withYear: Boolean = false
        ): DateTimeFormatter {
            val is24Hour: Boolean = DateFormat.is24HourFormat(context)

            val pattern = if (isShort) {
                if (withYear) {
                    if (is24Hour)
                        FORMAT_DATE_TIME_WITH_YEAR_24_HOUR_SHORT
                    else FORMAT_DATE_TIME_WITH_YEAR_12_HOUR_SHORT
                } else {
                    if (is24Hour)
                        FORMAT_DATE_TIME_24_HOUR_SHORT
                    else FORMAT_DATE_TIME_12_HOUR_SHORT
                }
            } else {
                if (withYear) {
                    if (is24Hour)
                        FORMAT_DATE_TIME_WITH_YEAR_24_HOUR
                    else FORMAT_DATE_TIME_WITH_YEAR_12_HOUR
                } else {
                    if (is24Hour)
                        FORMAT_DATE_TIME_24_HOUR
                    else FORMAT_DATE_TIME_12_HOUR
                }
            }

            return DateTimeFormatter.ofPattern(pattern)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        @TypeConverter
        fun fromLocalDate(localDate: LocalDate?): String? {
            return if (localDate != null)
                DateTimeFormatter.ISO_LOCAL_DATE.format(localDate)
            else null
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        @TypeConverter
        fun fromZonedDateTimeToMillis(zonedDateTime: ZonedDateTime?): Long? {
            return zonedDateTime?.toInstant()?.toEpochMilli()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        @TypeConverter
        fun fromMillisToZoneDateTime(millis: Long?, zoneId: ZoneId = ZoneId.systemDefault()): ZonedDateTime? {
            return millis?.let {
                Instant.ofEpochMilli(it).atZone(zoneId)
            }
        }


        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        @TypeConverter
        fun toLocalDate(date: String?): LocalDate? {
            return if (date.isNullOrEmpty()) null
            else LocalDate.parse(date)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        @TypeConverter
        fun toZonedDateTime(time: String?): ZonedDateTime? {
            return if (time.isNullOrEmpty()) null
            else ZonedDateTime.parse(time)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        @TypeConverter
        fun fromZonedDateTime(zonedDateTime: ZonedDateTime?): String? {
            return if (zonedDateTime != null)
                DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(zonedDateTime)
            else null
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        @TypeConverter
        fun toLocalTime(time: String?): LocalTime? {
            return if (time.isNullOrEmpty()) null
            else LocalTime.parse(time)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        @TypeConverter
        fun fromLocalTime(time: LocalTime?): String? {
            return DateTimeFormatter.ISO_LOCAL_TIME.format(time)
        }
    }

}