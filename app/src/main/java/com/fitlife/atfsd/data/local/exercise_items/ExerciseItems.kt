package com.fitlife.atfsd.data.local.exercise_items

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ExerciseItems")
data class ExerciseItems(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    @ColumnInfo(name = "type_id")
    val typeId:Int,
    val name:String,
    val description:String,
    val logo:String,
    @ColumnInfo(name="training_id")
    val trainingId:Int,
    val duration:Int,
    val repeat:Int,
)
