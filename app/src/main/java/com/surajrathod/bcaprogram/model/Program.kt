package com.surajrathod.bcaprogram.model

import java.io.Serializable

data class Program (
    val id : Int,
    val title : String,
    val content : String = "",
    val sem : String = "",
    val sub : String = "",
    val unit : String = ""
):Serializable