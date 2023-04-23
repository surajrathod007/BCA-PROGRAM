package com.surajrathod.bcaprogram.model

import alirezat775.lib.carouselview.CarouselModel

class SampleModel constructor(private var imgUrl: String) : CarouselModel() {
    fun getId(): String {
        return imgUrl
    }
}