package com.fitlife.atfsd.domain

import android.icu.util.Calendar
import java.text.SimpleDateFormat

data class TrainingWithCommonData(
    val id: Int,
    val name: String,
    val amountExercises: Int,
    val totalTime: Int,
) {
    val totalTimeFormatted:String
        get() = getTimeFormatted()
    private fun getTimeFormatted(): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = (totalTime*1000).toLong()
        val formatter = SimpleDateFormat("HH:MM")
        return formatter.format(calendar)
    }
}
