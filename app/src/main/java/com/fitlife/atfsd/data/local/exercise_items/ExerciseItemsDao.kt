package com.fitlife.atfsd.data.local.exercise_items

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseItemsDao {

    @Query("SELECT * FROM exerciseitems WHERE training_id =:trainingId")
    fun getExercisesForTraining(trainingId:Int):Flow<List<ExerciseItems>>

}