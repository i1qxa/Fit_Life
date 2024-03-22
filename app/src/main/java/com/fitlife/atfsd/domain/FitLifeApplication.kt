package com.fitlife.atfsd.domain

import android.app.Application
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.fitlife.atfsd.data.local.FitLifeDB
import com.fitlife.atfsd.data.remote.FitLifeRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FitLifeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val db = FitLifeDB.getInstance(this).trainingsDao()
        val conversionDataListener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(anyMutableMap: MutableMap<String, Any>?) {
                if (anyMutableMap != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        if (db.getAmountOfTrainingsForTrainingType(1) > -1) {
                            val fitLifeRepo = FitLifeRepositoryImpl(this@FitLifeApplication.applicationContext, anyMutableMap)
                            fitLifeRepo.getListWorkoutExercises()
                        }
                    }
                }
            }

            override fun onConversionDataFail(error: String?) {

            }

            override fun onAppOpenAttribution(data: MutableMap<String, String>?) {
            }

            override fun onAttributionFailure(error: String?) {

            }
        }

        AppsFlyerLib.getInstance().apply {
            init(FIT_LIFE_KEY, conversionDataListener, this@FitLifeApplication)
            start(this@FitLifeApplication, FIT_LIFE_KEY, object :
                AppsFlyerRequestListener {
                override fun onSuccess() {
                }

                override fun onError(p0: Int, p1: String) {

                }
            }
            )
        }
    }
}