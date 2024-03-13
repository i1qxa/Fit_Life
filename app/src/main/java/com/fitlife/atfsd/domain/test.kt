package com.fitlife.atfsd.domain

import android.icu.util.Calendar
import java.text.SimpleDateFormat

fun main(){

        val timeInSeconds = readln().toInt()

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = (timeInSeconds*1000).toLong()

        val formatter = SimpleDateFormat("HH:MM")
        println(formatter.format(calendar))

}