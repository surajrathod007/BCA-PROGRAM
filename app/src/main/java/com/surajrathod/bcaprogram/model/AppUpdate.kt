package com.surajrathod.bcaprogram.model

data class AppUpdate(
    val id : Int,
    val version : Float,
    val link : String,
    val message : String = ""
)
