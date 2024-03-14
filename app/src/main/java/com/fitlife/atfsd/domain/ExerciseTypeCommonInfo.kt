package com.fitlife.atfsd.domain

data class ExerciseTypeCommonInfo(
    val amountExercises:Int,
    val totalDuration:Int,
){

    val totalTimeFormatted:String
        get() = getTimeFormatted()
    private fun getTimeFormatted(): String {
        val mils = (totalDuration*1000).toLong()
        val hours = mils / (1000 * 60 * 60)
        val minutes = (mils % (1000 * 60 * 60)) / (1000 * 60)

        return String.format("%02d:%02d", hours, minutes)
    }

}
