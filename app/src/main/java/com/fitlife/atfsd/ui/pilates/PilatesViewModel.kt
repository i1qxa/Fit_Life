package com.fitlife.atfsd.ui.pilates

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.fitlife.atfsd.data.local.FitLifeDB
import com.fitlife.atfsd.domain.TYPE_PILATES

class PilatesViewModel(application: Application):AndroidViewModel(application) {

    val trainingDao = FitLifeDB.getInstance(application).trainingsDao()

    val trainingTypeCommonInfo = trainingDao.getCommonDataForExerciseType(TYPE_PILATES)

    val cardioTrainings = trainingDao.getTrainingsListShort(TYPE_PILATES)

}