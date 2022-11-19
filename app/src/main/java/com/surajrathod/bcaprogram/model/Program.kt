package com.surajrathod.bcaprogram.model

data class Program(
    val sub: String = "",
    val unit: String = "",
    val sem: String = "",
    val id: Int,
    val title: String,
    val content: String = ""
)