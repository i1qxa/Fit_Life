package com.fitlife.atfsd.data.local.exercise_items

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fitlife.atfsd.domain.TrainingWithCommonData
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseItemsDao {

    @Query("SELECT * FROM exerciseitems WHERE training_id =:trainingId")
    fun getExercisesForTraining(trainingId:Int):Flow<List<ExerciseItems>>

    @Query("SELECT * FROM exerciseitems WHERE id =:exerciseId")
    fun getExercise(exerciseId:Int):Flow<List<ExerciseItems>>

    @Query("SELECT id, name, repeat as amountExercises, (repeat*duration) as totalTime, logo FROM exerciseitems WHERE name LIKE :query")
    fun getExerciseByQuery(query:String):LiveData<List<TrainingWithCommonData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun fetchListOfExercises(listYogaExercises:List<ExerciseItems>)

}