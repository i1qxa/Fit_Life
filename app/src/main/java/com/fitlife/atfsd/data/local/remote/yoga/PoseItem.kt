package com.fitlife.atfsd.data.local.remote.yoga

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PoseItem(
    val id:Int,
    @SerialName("category_name")
    val categoryName:String,
    @SerialName("english_name")
    val englishName:String,
    @SerialName("sanskrit_name_adapted")
    val sanskritNameAdapted:String,
    @SerialName("sanskrit_name")
    val sanskritName:String,
    @SerialName("translation_name")
    val translationName:String,
    @SerialName("pose_description")
    val poseDescription:String,
    @SerialName("pose_benefits")
    val poseBenefits:String,
    @SerialName("url_svg")
    val urlSvg:String,
    @SerialName("url_png")
    val urlPng:String,
    @SerialName("url_svg_alt")
    val urlSvgAlt:String,
)
