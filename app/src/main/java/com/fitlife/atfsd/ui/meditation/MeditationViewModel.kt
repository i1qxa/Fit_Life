package com.fitlife.atfsd.ui.meditation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.fitlife.atfsd.data.local.FitLifeDB
import com.fitlife.atfsd.domain.TYPE_MEDITATION

class MeditationViewModel(application: Application):AndroidViewModel(application) {

    val trainingDao = FitLifeDB.getInstance(application).trainingsDao()

    val trainingTypeCommonInfo = trainingDao.getCommonDataForExerciseType(TYPE_MEDITATION)

    val cardioTrainings = trainingDao.getTrainingsListShort(TYPE_MEDITATION)

}