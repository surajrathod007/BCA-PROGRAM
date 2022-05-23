package com.surajrathod.bcaprogram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.surajrathod.bcaprogram.model.ProgramEntity
import com.surajrathod.bcaprogram.network.NetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProgramViewModel : ViewModel() {
    private val _programsList = MutableLiveData<MutableList<ProgramEntity>>(mutableListOf())
    val programsList : LiveData<MutableList<ProgramEntity>>
    get() = _programsList
    val curSemester = MutableLiveData<String>("Sem 1")
    val curSubject = MutableLiveData<String>("IPLC")
    val curUnit = MutableLiveData<String>("Unit 1")

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
        response.enqueue(object : Callback<List<ProgramEntity>>{
            override fun onResponse(call: Call<List<ProgramEntity>>, response: Response<List<ProgramEntity>>) {
                response.body()?.let {
                    clearPrograms()
                    _programsList.value?.addAll(it)
                    refresh()
//                    println("Body is $it")
                }

            }
            override fun onFailure(call: Call<List<ProgramEntity>>, t: Throwable) {
                clearPrograms()
                refresh()
                println("Failure is $t")
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