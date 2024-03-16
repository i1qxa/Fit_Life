package com.fitlife.atfsd.ui.training

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.fitlife.atfsd.data.local.FitLifeDB
import com.fitlife.atfsd.data.local.exercise_items.ExerciseItems
import kotlinx.coroutines.flow.Flow

class TrainingViewModel(application: Application) : AndroidViewModel(application) {

    private val exerciseDao = FitLifeDB.getInstance(application).exerciseItemsDao()

    lateinit var exerciseList: Flow<List<ExerciseItems>>

    fun setupTrainingId(trainingId: Int, isSingleExercise: Boolean) {
        exerciseList = if (isSingleExercise) {
            exerciseDao.getExercise(trainingId)
        } else {
            exerciseDao.getExercisesForTraining(trainingId)
        }
    }

}