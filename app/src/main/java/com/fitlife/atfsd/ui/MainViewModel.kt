package com.fitlife.atfsd.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fitlife.atfsd.data.local.FitLifeDB
import com.fitlife.atfsd.data.local.exercise_items.ExerciseItems
import com.fitlife.atfsd.data.local.remote.yoga.PoseCategory
import com.fitlife.atfsd.data.local.remote.yoga.PoseCategoryTranslated
import com.fitlife.atfsd.data.local.remote.yoga.PoseItem
import com.fitlife.atfsd.data.local.remote.yoga.PoseTranslated
import com.fitlife.atfsd.data.local.remote.yoga.YogaService
import com.fitlife.atfsd.data.local.treinings.Treinings
import com.fitlife.atfsd.domain.BASE_DELAY
import com.fitlife.atfsd.domain.FIT_LIFE_PREFS_NAME
import com.fitlife.atfsd.domain.ITEM_SPLITTER
import com.fitlife.atfsd.domain.KOTIK_UPDATED
import com.fitlife.atfsd.domain.TYPE_YOGA
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val fitLifePrefs = application.baseContext.getSharedPreferences(FIT_LIFE_PREFS_NAME, Context.MODE_PRIVATE)

    private val trainingDao = FitLifeDB.getInstance(application).trainingsDao()

    private val exerciseDao = FitLifeDB.getInstance(application).exerciseItemsDao()

    val errorRequest = MutableLiveData<Boolean>()

    private val yogaService = YogaService.getInstance()

    val internetStatus = MutableLiveData<Boolean>()

    val kotikExistLD = MutableLiveData<String?>()

    fun changeInternetStatus(status:Boolean){
        internetStatus.value = status
    }

    fun checkKotikExist(){
        viewModelScope.launch {
            var isKotikExist = false
            var kotikStep = BASE_DELAY
            while (!isKotikExist && kotikStep>0){
                val kotik = fitLifePrefs.getString(KOTIK_UPDATED, null)
                if (kotik!=null){
                    isKotikExist = true
                    kotikExistLD.postValue(kotik)
                }
                kotikStep--
                delay(1000)
            }
            if (!isKotikExist){
                kotikExistLD.postValue(null)
            }
        }
    }

    fun checkForUpdatesYoga() {
//        viewModelScope.launch {
//            if (trainingDao.getAmountOfTrainingsForTrainingType(TYPE_YOGA) < 2) {
//                getYogaExercises()
//            }
//        }
    }

    private suspend fun getYogaExercises() {
        val response = yogaService.getYogaResponse()
        if (response.isSuccessful) {
            val poseCategoriesTranslated = mutableListOf<PoseCategory>()
            val yogaRemoteData = response.body()
            val yogaEncoded = StringBuilder()
            yogaRemoteData?.forEach {
                yogaEncoded.append(it.getDataForTranslate())
                yogaEncoded.append(ITEM_SPLITTER)
            }
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.RUSSIAN)
                .build()
            val englishRussianTranslator = Translation.getClient(options)
            val conditions = DownloadConditions.Builder()
                .build()
            englishRussianTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    val translator = Translation.getClient(options)
                    translator.translate(yogaEncoded.toString())
                        .addOnSuccessListener { translatedCollection ->
                            translatedCollection.split(ITEM_SPLITTER).forEach {
                                if (it.isNotEmpty()) {
                                    val tmpTranslatedItem =
                                        PoseCategoryTranslated.getFromString(it)
                                    val poseCategoryEnglishList =
                                        yogaRemoteData?.filter { it.id == tmpTranslatedItem?.id }
                                    if (poseCategoryEnglishList?.isNotEmpty() == true) {
                                        val poseCategoryEnglish = poseCategoryEnglishList[0]
                                        if (poseCategoryEnglish != null && tmpTranslatedItem != null) {
                                            poseCategoriesTranslated.add(
                                                poseCategoryEnglish.copy(
                                                    categoryName = tmpTranslatedItem.categoryName,
                                                    categoryDescription = tmpTranslatedItem.categoryDescription,
                                                    poses = translatePoses(
                                                        poseCategoryEnglish.poses,
                                                        tmpTranslatedItem.poses
                                                    )
                                                )
                                            )
                                        }
                                    }

                                }

                            }
                            val listTrainings = poseCategoriesTranslated.map {
                                Treinings(
                                    0,
                                    TYPE_YOGA,
                                    it.categoryName
                                )
                            }
                            viewModelScope.launch {
                                trainingDao.fetchYogaTrainings(listTrainings)
                                val listOfIds =
                                    trainingDao.getTrainingIdsForTrainingType(TYPE_YOGA)
                                val listOfExercises = mutableListOf<ExerciseItems>()
                                poseCategoriesTranslated.forEach { poseCategory ->
                                    val trainingId =
                                        listOfIds.filter { it.name == poseCategory.categoryName }
                                            ?.get(0)?.id
                                    poseCategory.poses.forEach { poseItem ->
                                        if (trainingId != null) {
                                            listOfExercises.add(
                                                ExerciseItems(
                                                    0,
                                                    TYPE_YOGA,
                                                    poseItem.englishName?:"Йога",
                                                    poseItem.poseDescription,
                                                    poseItem.urlPng?:"1",
                                                    trainingId,
                                                    90,
                                                    Random.nextInt(9)
                                                )
                                            )
                                        }
                                    }
                                }
                                exerciseDao.fetchListOfExercises(listOfExercises)
                            }
                        }
                }
                .addOnFailureListener { exception ->
                }
        } else {
            errorRequest.postValue(true)
        }
    }

    private fun translatePoses(
        englishList: List<PoseItem>,
        translatedPoses: List<PoseTranslated>
    ): List<PoseItem> {
        val answer = mutableListOf<PoseItem>()
        try {
            englishList.forEach { englishItem ->
                val translatedPoseList = translatedPoses.filter { it.poseId == englishItem.id }
                if (translatedPoseList.isNotEmpty()) {
                    val translatedPoseItem = translatedPoseList[0]
                    val poseName = translatedPoseItem.poseName.ifBlank { "Йога" }
                    answer.add(
                        englishItem.copy(
                            englishName = poseName,
                            poseDescription = translatedPoseItem.poseDescription?:"Йога"
                        )
                    )
                }
            }
            return answer
        } catch (e: Exception) {
            return emptyList()
        }

    }


}