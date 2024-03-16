package com.fitlife.atfsd.ui.search_fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.fitlife.atfsd.data.local.FitLifeDB

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    val exerciseDao = FitLifeDB.getInstance(application).exerciseItemsDao()

    val searchTextLD = MutableLiveData<String>()

    val exerciseListLD = searchTextLD.switchMap { query ->
        exerciseDao.getExerciseByQuery(query)
    }

    fun findExercises(query: String) {
        if (query.isNotEmpty()) {
            searchTextLD.value = "%$query%"
        }
    }

}