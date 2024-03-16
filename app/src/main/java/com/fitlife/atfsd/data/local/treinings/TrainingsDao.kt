package com.fitlife.atfsd.data.local.treinings

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fitlife.atfsd.domain.ExerciseTypeCommonInfo
import com.fitlife.atfsd.domain.TrainingWithCommonData
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingsDao {

    @Query("SELECT * FROM treinings WHERE exercise_type_id =:exerciseType")
    fun getTrainingsList(exerciseType:Int):Flow<List<Treinings>>

    @Query("select tr.id as id, tr.name as name, count(ei.name) as amountExercises," +
            " sum(ei.repeat * ei.duration) as totalTime, ei.logo as logo" +
            " from treinings as tr left join exerciseitems as ei on tr.id = ei.training_id " +
            "where tr.exercise_type_id = :exerciseType group by tr.id")
    fun getTrainingsListShort(exerciseType:Int):Flow<List<TrainingWithCommonData>>

    @Query("select count(ei.name) as amountExercises," +
            " sum(ei.repeat * ei.duration) as totalDuration" +
            " from treinings as tr left join exerciseitems as ei on tr.id = ei.training_id " +
            "where tr.exercise_type_id = :exerciseType group by tr.exercise_type_id LIMIT 1")
    fun getCommonDataForExerciseType(exerciseType:Int):Flow<ExerciseTypeCommonInfo>

    @Query("SELECT count(*) FROM Treinings WHERE exercise_type_id =:trainingType")
    suspend fun getAmountOfTrainingsForTrainingType(trainingType:Int):Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun fetchYogaTrainings(listTrainings:List<Treinings>)

    @Query("SELECT name, id FROM treinings WHERE exercise_type_id =:trainingType")
    suspend fun getTrainingIdsForTrainingType(trainingType:Int):List<TrainingIds>

}