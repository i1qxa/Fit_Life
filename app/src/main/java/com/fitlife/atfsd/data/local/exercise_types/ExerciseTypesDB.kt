package com.fitlife.atfsd.data.local.exercise_types

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ExerciseTypes")
data class ExerciseTypesDB(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name:String
)
