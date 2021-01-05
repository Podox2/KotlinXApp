package com.podorozhniak.kotlinx.practice.util

import android.content.Context
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateUtil {

    fun getDifference(context: Context): String {
        val currentDate = Calendar.getInstance().time
        //val stringDate: String = dateFormat.format(currentDate)
        val matchDateString = "15.08.2020 22:00"
        val matchDate = SimpleDateFormat("dd.MM.yyyy HH:mm").parse(matchDateString)
        return getBeautyTimeFormat(context, currentDate, matchDate!!)
    }

    private fun getBeautyTimeFormat(context: Context, currentDate: Date, mathDate: Date): String {

        var diffInMilliseconds = mathDate.time - currentDate.time

        if(diffInMilliseconds < 0) {
            return "Started"
        }

        val days = TimeUnit.MILLISECONDS.toDays(diffInMilliseconds)
        diffInMilliseconds -= 86400000 * days
        val hours = TimeUnit.MILLISECONDS.toHours(diffInMilliseconds)
        diffInMilliseconds -= 3600000 * hours
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMilliseconds)
        diffInMilliseconds -= 60000 * minutes
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMilliseconds)

        return "Remain \n ${formBeautyTime(days, hours, minutes, seconds)}"
    }

    private fun formBeautyTime(days: Long, hours: Long, minutes: Long, seconds: Long): String {
        val time = StringBuilder("")
        if (days > 1)
            time.append("$days days ")
        else time.append("$days day ")

        if(hours > 1)
        time.append("$hours hours ")
        else time.append("$hours hour ")

        if(minutes > 1)
        time.append("$minutes minutes ")
        else time.append("$minutes minute ")

        if (seconds > 1)
        time.append("$seconds seconds")
        else time.append("$seconds second")

        return time.toString()
    }
}