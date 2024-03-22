package com.fitlife.atfsd.data.remote

import com.fitlife.atfsd.domain.MY_KOTIK
import com.fitlife.atfsd.domain.toMyCase
import java.lang.StringBuilder

data class LinkData(
    var appPackage: String? = null,
    var androidAdvID: String? = null,
    var appsMediaSource: String? = null,
    var appsCampaignName: String? = "None",
    var appsAdsetName: String? = "null",
    var appsAfStatus: String? = null,
    var appsId: String? = null,
    var domain: String = MY_KOTIK.toMyCase()
) {

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
            append(appsMediaSource)
            append("&utm_campaign=")
            append(appsCampaignName)
            append("&utm_content=")
            append(appsAdsetName)
            append("&utm_medium=")
            append(appsAfStatus)
            append("&utm_term=")
            append(appsId)
            return sb.toString()
        }
    }

}
