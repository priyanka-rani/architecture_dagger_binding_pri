package com.pri.architecture_boilerplate.util

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    const val SERVER_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    const val CAL_OUTPUT_DATE_FORMAT = "yyyy-MM-dd"
    const val NOTIF_OUTPUT_DATE_TIME_FORMAT = "MMM dd yyyy hh:mm a"
    //const val NOTIF_INPUT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS Z"
    const val NOTIF_INPUT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS K"
    const val DATE_FORMAT_1 = "hh:mm a"
    const val DATE_FORMAT_2 = "h:mm a"
    const val DATE_FORMAT_3 = "yyyy-MM-dd"
    const val DATE_FORMAT_4 = "dd-MMMM-yyyy"
    const val DATE_FORMAT_5 = "dd MMMM yyyy"
    const val DATE_FORMAT_6 = "dd MMMM yyyy zzzz"
    const val DATE_FORMAT_7 = "EEE, MMM d, ''yy"
    const val DATE_FORMAT_8 = "yyyy-MM-dd HH:mm:ss"
    const val DATE_FORMAT_9 = "h:mm a dd MMMM yyyy"
    const val DATE_FORMAT_10 = "K:mm a, z"
    const val DATE_FORMAT_11 = "hh 'o''clock' a, zzzz"
    const val DATE_FORMAT_12 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val DATE_FORMAT_13 = "E, dd MMM yyyy HH:mm:ss z"
    const val DATE_FORMAT_14 = "yyyy.MM.dd G 'at' HH:mm:ss z"
    const val DATE_FORMAT_15 = "yyyyy.MMMMM.dd GGG hh:mm aaa"
    const val DATE_FORMAT_16 = "EEE, d MMM yyyy HH:mm:ss Z"
    const val DATE_FORMAT_17 = "yyyy-MM-dd'T'HH:mm:ss.SSS Z"
    const val DATE_FORMAT_18 = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    const val DATE_FORMAT_19 = "yyyy-MM-dd'T'HH:mm:ss.SSSz"
    const val DATE_FORMAT_20 = "dd \nMMM \nyyyy"

    @SuppressLint("SimpleDateFormat")
    val server_format = SimpleDateFormat(SERVER_FORMAT).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }


    @SuppressLint("SimpleDateFormat")
    fun getServerTimeUTC(timeStr: String): Long {
        val date: Date
        try {
            date = server_format.parse(timeStr)
            return date.time
        } catch (e: ParseException) {
            return 0
        }
    }

    fun getServerDateUTC(timeStr: String): Date? {
        return try {
            server_format.parse(timeStr)
        } catch (e: ParseException) {
            null
        }

    }

    fun parseDateOnly(timeStamp: Long): Date? {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timeStamp
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.time
    }


    fun parseServerDateOnlyUTC(date: String): Date? {
        val cal = Calendar.getInstance()
        var timeStamp: Long = 0
        try {
            timeStamp = server_format.parse(date).time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        cal.timeInMillis = timeStamp
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.time
    }

    fun parseServerDate(data: String): String {
        val sdf = SimpleDateFormat(SERVER_FORMAT, Locale.getDefault())
        var timeStamp: Long = 0
        try {
            timeStamp = sdf.parse(data).time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val currentDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return currentDateFormat.format(timeStamp)
    }

    fun parseServerDateUTC(data: String): String {
        var timeStamp: Long = 0
        try {
            timeStamp = server_format.parse(data).time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val currentDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return currentDateFormat.format(timeStamp)
    }


    fun parseServerDateByFormat(data: String, outputFormat: String): String {
        val sdf = SimpleDateFormat(SERVER_FORMAT, Locale.getDefault())
        var timeStamp: Long = 0
        try {
            timeStamp = sdf.parse(data).time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val currentDateFormat = SimpleDateFormat(outputFormat, Locale.getDefault())
        return currentDateFormat.format(timeStamp)
    }

    fun parseServerDateByFormatUTC(data: String, outputFormat: String): String {
        var timeStamp: Long = 0
        try {
            timeStamp = server_format.parse(data).time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val currentDateFormat = SimpleDateFormat(outputFormat, Locale.getDefault())
        return currentDateFormat.format(timeStamp)
    }

    fun parseDateString(data: String, inputFormat: String, outputFormat: String): String {
        val sdf = SimpleDateFormat(inputFormat, Locale.getDefault())
        var timeStamp: Long = 0
        try {
            timeStamp = sdf.parse(data).time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val currentDateFormat = SimpleDateFormat(outputFormat, Locale.getDefault())
        val dateString = currentDateFormat.format(timeStamp)
        return dateString
    }

    @SuppressLint("SimpleDateFormat")
    fun parseTimeStampToDateString(ts: Long): String {
        val date = Date()
        date.time = ts
        return SimpleDateFormat(NOTIF_OUTPUT_DATE_TIME_FORMAT).format(date)
    }

}