package com.fitlife.atfsd.ui.cardio

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.fitlife.atfsd.data.local.FitLifeDB

class CardioViewModel(application: Application):AndroidViewModel(application) {

    val exerciseDao = FitLifeDB.getInstance(application).trainingsDao()

    val testLD = exerciseDao.getTrainingsListShort(2)

}