package com.fitlife.atfsd.ui.cardio

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.fitlife.atfsd.data.local.FitLifeDB
import com.fitlife.atfsd.domain.TYPE_CARDIO

class CardioViewModel(application: Application):AndroidViewModel(application) {

    val trainingDao = FitLifeDB.getInstance(application).trainingsDao()

    val trainingTypeCommonInfo = trainingDao.getCommonDataForExerciseType(TYPE_CARDIO)

    val cardioTrainings = trainingDao.getTrainingsListShort(TYPE_CARDIO)

}