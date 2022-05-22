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

    val semList = arrayOf("Sem 1","Sem 2","Sem 3","Sem 4","Sem 5","Sem 6")
    val unitList = arrayOf("Unit 1","Unit 2","Unit 3","Unit 4")
    val subjectmutableMap = mutableMapOf<Int,Array<String>>()

    init {
        createSubjectsMutableMap()
    }

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
    fun addToSubjectSMutableMap(key : Int,list:Array<String>){
        subjectmutableMap[key] = list
    }
    fun createSubjectsMutableMap(){
        subjectmutableMap[1] = arrayOf("IPLC","HTML")
        subjectmutableMap[2] = arrayOf("ACP","DBMS 1","HTML/JS")
        subjectmutableMap[3] = arrayOf("OOCP","DS_ALGO")
        subjectmutableMap[4] = arrayOf("CJ","DBMS 2","WPC#")
        subjectmutableMap[5] = arrayOf("PYTHON","ASP.NET")
        subjectmutableMap[6] = arrayOf("WEB APP DEV")
    }
}