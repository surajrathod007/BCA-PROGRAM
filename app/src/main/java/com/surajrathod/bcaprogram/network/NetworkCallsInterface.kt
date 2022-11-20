package com.surajrathod.bcaprogram.network

import com.surajrathod.bcaprogram.model.AppUpdate
import com.surajrathod.bcaprogram.model.ProgramEntity
import com.surajrathod.bcaprogram.model.Quote
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://programming-quotes-api.herokuapp.com/quotes/"
interface NetworkCallsInterface {

    @GET("programs/specific")
    fun fetchPrograms(@Query("sem")sem : String,@Query("sub") sub : String,@Query("unit")unit : String) : Call<List<ProgramEntity>>

    @GET("get")
    fun checkForUpdates() : Call<AppUpdate>

    @GET("programs")
    fun getData() : Call<List<ProgramEntity>>

    @GET("random")
    fun getQuote() : Call<Quote>

}
