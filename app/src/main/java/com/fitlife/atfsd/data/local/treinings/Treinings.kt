package com.fitlife.atfsd.data.local.treinings

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Treinings")
data class Treinings(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    @ColumnInfo(name = "exercise_type_id")
    val exerciseTypeId:Int,
    val name:String
)
