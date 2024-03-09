package com.fitlife.atfsd.data.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fitlife.atfsd.data.local.exercise_items.ExerciseItems
import com.fitlife.atfsd.data.local.exercise_items.ExerciseItemsDao
import com.fitlife.atfsd.data.local.exercise_types.ExerciseTypesDB
import com.fitlife.atfsd.data.local.exercise_types.ExerciseTypesDao
import com.fitlife.atfsd.data.local.treinings.TrainingsDao
import com.fitlife.atfsd.data.local.treinings.Treinings
import kotlinx.coroutines.InternalCoroutinesApi

@Database(
    entities = [
        ExerciseItems::class,
        ExerciseTypesDB::class,
        Treinings::class
    ], exportSchema = false,
    version = 1
)
abstract class FitLifeDB : RoomDatabase() {

    abstract fun exerciseItemsDao():ExerciseItemsDao
    abstract fun exerciseTypesDao(): ExerciseTypesDao

    abstract fun trainingsDao(): TrainingsDao

    companion object {
        private var INSTANCE: FitLifeDB? = null
        private val LOCK = Any()
        private const val DB_NAME = "fit_life_db"

        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(application: Application): FitLifeDB {
            INSTANCE?.let {
                return it
            }
            kotlinx.coroutines.internal.synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    FitLifeDB::class.java,
                    DB_NAME
                )
                    .createFromAsset("fit_life_db.db")
                    .build()
                INSTANCE = db
                return db
            }
        }
    }


}