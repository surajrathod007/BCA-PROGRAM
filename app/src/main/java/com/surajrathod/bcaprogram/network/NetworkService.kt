package com.surajrathod.bcaprogram.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService {
    val networkInstance : NetworkCallsInterface
    init {
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        networkInstance = retrofit.create(NetworkCallsInterface::class.java)
    }
}