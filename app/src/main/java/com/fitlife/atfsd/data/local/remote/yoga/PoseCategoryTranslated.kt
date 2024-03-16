package com.fitlife.atfsd.data.local.remote.yoga

import android.util.Log
import com.fitlife.atfsd.domain.FIELDS_SPLITTER

data class PoseCategoryTranslated(
    val id:Int,
    val categoryName:String,
    val categoryDescription:String,
    val poses:List<PoseTranslated>,
){
    companion object{

        fun getFromString(encodedString: String): PoseCategoryTranslated?{
            Log.d("ENCODED_STR_TRANS",encodedString)
            val poseCategoryTranslatedAsList = encodedString.split(FIELDS_SPLITTER)
            try {
                val tmpCategoryId = poseCategoryTranslatedAsList[0].trim().toInt()
                val tmpCategoryName = poseCategoryTranslatedAsList[1]
                val tmpCategoryDescription = poseCategoryTranslatedAsList[2]
                val tmpListPoseTranslated = mutableListOf<PoseTranslated>()
                poseCategoryTranslatedAsList[3].split(POSE_ITEM_SPLITTER).forEach {
                    val tmpPose = PoseTranslated.getFromString(it)
                    if (tmpPose!=null) tmpListPoseTranslated.add(tmpPose)
                }
                return PoseCategoryTranslated(
                    tmpCategoryId,
                    tmpCategoryName,
                    tmpCategoryDescription,
                    tmpListPoseTranslated
                )
            }catch (e:Exception){
                return null
            }
        }
    }
}
