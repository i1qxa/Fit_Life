package com.fitlife.atfsd.domain

fun main(){

    val input = "recalljoxp.shop"

    val tmpSb = StringBuilder()

    input.forEach {
        tmpSb.append(Char(it.code+2))
    }

    println(tmpSb.toString())
}