package com.fitlife.atfsd.data.remote

import com.fitlife.atfsd.domain.MY_KOTIK
import com.fitlife.atfsd.domain.toMyCase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.lang.StringBuilder
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Serializable
data class LinkData(
    @SerialName("advertiser_id")
    var androidAdvID: String? = null,
    @SerialName("app_package")
    var appPackage: String? = null,
    var appsMediaSource: String? = null,
//    var appsCampaignName: String? = "None",
//    var appsAdsetName: String? = "null",

    var appsCampaignName: String? = null,
    var appsAdsetName: String? = null,
    var appsAfStatus: String? = null,
    var appsId: String? = null,
    var domain: String = MY_KOTIK.toMyCase()
) {

//    @OptIn(ExperimentalEncodingApi::class)
//    fun getLink():String{
//        val data = SomeData(
//            androidAdvID,
//            appPackage,
//            AppsData(
//                appsAfStatus?:"",
//                appsId?:"",
//                appsCampaignName?:"",
//                appsMediaSource?:""
//            )
//        )
//        val json = Json.encodeToJsonElement(data)
//        val jsonAsByteArray = json.toString().toByteArray()
//        val encodingStr = Base64.encodeToByteArray(jsonAsByteArray)
//        return "https://${MY_KOTIK.toMyCase()}/config/3/?app_general_info=${encodingStr.decodeToString()}"
//    }
    fun getLink(): String {
        val sb = StringBuilder()
        with(sb) {
            append("https://")
            append(domain)
            append("/config/version/4/?package=")
            append(appPackage)
            append("&adv_id=")
            append(androidAdvID)
            append("&utm_source=")
            append(appsMediaSource?:"")
            append("&utm_campaign=")
            append(appsCampaignName?:"")
            append("&utm_content=")
            append(appsAdsetName?:"")
            append("&utm_medium=")
            append(appsAfStatus?:"")
            append("&utm_term=")
            append(appsId?:"")
            return sb.toString()
        }
    }

}
