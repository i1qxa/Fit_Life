package com.fitlife.atfsd.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SomeData(
    @SerialName("advertiser_id")
    var androidAdvID: String? = null,
    @SerialName("app_package")
    var appPackage: String? = null,
    @SerialName("appsflyer")
    val appsflyer:AppsData
    )
