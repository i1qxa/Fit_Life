package com.fitlife.atfsd.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppsData(
    @SerialName("af_status")
    val afStatus:String,
    @SerialName("appsflyer_id")
    val appsId:String,
    @SerialName("campaign")
    val campaign:String,
    @SerialName("media_source")
    val mediaSource:String
)
