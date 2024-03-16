package com.fitlife.atfsd.data.local.remote.yoga

import android.util.Log
import com.fitlife.atfsd.domain.FIELDS_SPLITTER
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

const val POSE_VALUE_SPLITTER = "^"
const val POSE_ITEM_SPLITTER = "`"
@Serializable
data class PoseCategory(
    val id:Int,
    @SerialName("category_name")
    val categoryName:String,
    @SerialName("category_description")
    val categoryDescription:String,
    val poses:List<PoseItem>,

    ){
    var isExpanded = false
    var position = 0
    var currentPosIndex = 0
    var favoriteId:Long = -1


    fun getDataForTranslate():String{
        val answer = StringBuilder()
        answer.append(id)
        answer.append(" ")
        answer.append(FIELDS_SPLITTER)
        answer.append(" ")
        answer.append(categoryName)
        answer.append(" ")
        answer.append(FIELDS_SPLITTER)
        answer.append(" ")
        answer.append(categoryDescription)
        answer.append(" ")
        answer.append(FIELDS_SPLITTER)
        answer.append(" ")
        poses.forEach { poseItem ->
            answer.append(poseItem.id)
            answer.append(" ")
            answer.append(POSE_VALUE_SPLITTER)
            answer.append(" ")
            answer.append(poseItem.englishName)
            answer.append(" ")
            answer.append(POSE_VALUE_SPLITTER)
            answer.append(" ")
            answer.append(poseItem.poseDescription)
            answer.append(" ")
            answer.append(POSE_ITEM_SPLITTER)
            answer.append(" ")
        }
        Log.d("ENCODED_STR",answer.toString())
        return answer.toString()
    }

}
