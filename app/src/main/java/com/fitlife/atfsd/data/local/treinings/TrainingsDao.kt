package com.fitlife.atfsd.data.local.treinings

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingsDao {

    @Query("SELECT * FROM treinings WHERE exercise_type_id =:exerciseType")
    fun getTrainingsList(exerciseType:Int):Flow<List<Treinings>>

}