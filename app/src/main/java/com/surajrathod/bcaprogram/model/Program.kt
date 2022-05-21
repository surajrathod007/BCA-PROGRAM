package com.surajrathod.bcaprogram.model

data class Program (
    val id : Int,
    val title : String,
    val content : String = "",
    val sem : String = "",
    val sub : String = "",
    val unit : String = ""
)