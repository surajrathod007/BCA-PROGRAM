package com.surajrathod.bcaprogram.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.surajrathod.bcaprogram.model.Program
import com.surajrathod.bcaprogram.network.NetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProgramViewModel : ViewModel() {
    private val _programsList = MutableLiveData<MutableList<Program>>(mutableListOf())
    val programsList : LiveData<MutableList<Program>>
    get() = _programsList

    private fun clearPrograms() = _programsList.value?.clear()

    private fun refresh(){
        _programsList.value = _programsList.value
    }

    fun getRemotePrograms(sem : String, sub : String, unit : String){

        val response = NetworkService.networkInstance.fetchPrograms(sem, sub, unit)
//        println("Response is $response")
        response.enqueue(object : Callback<List<Program>>{
            override fun onResponse(call: Call<List<Program>>, response: Response<List<Program>>) {
                response.body()?.let {
                    clearPrograms()
                    _programsList.value?.addAll(it)
                    refresh()
//                    println("Body is $it")
                }

            }
            override fun onFailure(call: Call<List<Program>>, t: Throwable) {
                Log.e(TAG, "Retrofit Failure : $t", )
                _programsList.value?.add(Program(404,"Empty"))
                refresh()
//                println("Failure is $t")
            }
        })
    }
}