package com.surajrathod.bcaprogram.model

import alirezat775.lib.carouselview.CarouselModel

data class Book(
    val title : String="",
    val description : String="",
    val imageUrl : String="",
    val sem : String="",
    val sub : String="",
    val downloadLink : String="",
    val demoImages : List<String>? = null,
    val tags : List<String>? = null
) : java.io.Serializable
