package com.fitlife.atfsd.data.local.treinings

import androidx.room.Dao
import androidx.room.Query
import com.fitlife.atfsd.domain.TrainingWithCommonData
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingsDao {

    @Query("SELECT * FROM treinings WHERE exercise_type_id =:exerciseType")
    fun getTrainingsList(exerciseType:Int):Flow<List<Treinings>>

    @Query("select tr.id as id, tr.name as name, count(ei.name) as amountExercises," +
            " sum(ei.repeat * ei.duration) as totalTime" +
            " from treinings as tr left join exerciseitems as ei on tr.id = ei.training_id " +
            "where tr.exercise_type_id = :exerciseType group by tr.id")
    fun getTrainingsListShort(exerciseType:Int):Flow<List<TrainingWithCommonData>>

}