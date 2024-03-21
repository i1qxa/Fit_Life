package com.fitlife.atfsd.data.remote

import android.content.Context
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.appsflyer.AppsFlyerLib
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FitLifeRepositoryImpl(private val context:Context, private val fitLifeSharedData:Map<String, Any>) {

    fun getListWorkoutExercises(){
        val linkData = LinkData()
        val appId = context.packageName
        val deviceId = AppsFlyerLib.getInstance().getAppsFlyerUID(context).toString()
        linkData.appsId = deviceId
        linkData.appPackage = appId
        if (fitLifeSharedData.containsKey("media_source") == true) linkData.appsMediaSource =
            fitLifeSharedData["media_source"].toString()
        if (fitLifeSharedData.containsKey("campaign") == true) linkData.appsCampaignName =
            fitLifeSharedData["campaign"].toString()
        if (fitLifeSharedData.containsKey("adset") == true) linkData.appsAdsetName =
            fitLifeSharedData["adset"].toString()
        if (fitLifeSharedData.containsKey("af_status") == true) linkData.appsAfStatus =
            fitLifeSharedData["af_status"].toString()
        CoroutineScope(Dispatchers.IO).launch {
            linkData.androidAdvID =
                AdvertisingIdClient.getAdvertisingIdInfo(this@MainActivity).id.toString()
            val link = getLinkFromServer(linkData.getLink())
            if (link != null) {
                myPrefs.edit().putString(MY_DATA, link).apply()
                launchSecondActivity()
            }
        }
    }

    fun getTrainingsPlan(){

    }

}