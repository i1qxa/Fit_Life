package com.fitlife.atfsd.data.local.remote.yoga

data class PoseTranslated(
    val poseId:Int,
    val poseName:String,
    val poseDescription:String,
){
    companion object{
        fun getFromString(encodedPos:String): PoseTranslated?{
            val encodedPoseAsList = encodedPos.split(POSE_VALUE_SPLITTER)
            return try {
                PoseTranslated(
                    encodedPoseAsList[0].trim().toInt(),
                    encodedPoseAsList[1],
                    encodedPoseAsList[2]
                )
            }catch (e:Exception){
                null
            }
        }
    }
}
