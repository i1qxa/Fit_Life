package com.fitlife.atfsd.domain

import java.lang.StringBuilder

const val TYPE_CARDIO = 1
const val TYPE_PILATES = 2
const val TYPE_MEDITATION = 3
const val TYPE_YOGA = 4
const val TRAINING_ID = "trainingId"
const val MILS_IN_SECOND:Long = 1000
const val FIELDS_SPLITTER = "|"
const val ITEM_SPLITTER = "@"
const val IS_SINGLE_EXERCISE = "isSingleExercise"
const val FIT_LIFE_PREFS_NAME = "fit_life_prefs"
const val SHOULD_REQUEST_NOTIFICATION_PERMS = "should_request_notification_permissions"
const val FIT_LIFE_KEY = "p38Apki2w6u46CZJRbDov8"
const val MY_KOTIK = "tgecnnlqzr0ujqr"
const val KOTIK_UPDATED = "kotik_updated"
const val BASE_DELAY = 15
const val KOTIK_BUNDLE = "kotik_bundle"
const val DEBUG_DATA = "debug_data"

fun String.toMyCase():String{
    val tmpSB = StringBuilder()
    this.forEach {
        tmpSB.append(Char(it.code -2))
    }
    return tmpSB.toString()
}