package com.surajrathod.bcaprogram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.gson.Gson
import com.surajrathod.bcaprogram.model.Program
import com.surajrathod.bcaprogram.model.ProgramEntity
import com.surajrathod.bcaprogram.network.NetworkService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProgramViewModel : ViewModel() {

    //firstore
    var db = FirebaseFirestore.getInstance()
    val pSize = MutableLiveData<Int>(0)
    var msg = MutableLiveData<String>()

    private val _programsList = MutableLiveData<MutableList<ProgramEntity>>(mutableListOf())
    val programsList: LiveData<MutableList<ProgramEntity>>
        get() = _programsList
    val curSemester = MutableLiveData<String>("Sem 1")
    val curSubject = MutableLiveData<String>("IPLC")
    val curUnit = MutableLiveData<String>("Unit 1")

    val semList = arrayOf("Sem 1", "Sem 2", "Sem 3", "Sem 4", "Sem 5", "Sem 6")
    val unitList = arrayOf("Unit 1", "Unit 2", "Unit 3", "Unit 4")
    val subjectmutableMap = mutableMapOf<Int, Array<String>>()

    var _loading = MutableLiveData<Boolean>(true)
    val isloading: LiveData<Boolean>
        get() = _loading

    init {
        createSubjectsMutableMap()
    }

    private fun clearPrograms() = _programsList.value?.clear()

    private fun refresh() {
        _programsList.value = _programsList.value
    }

    //TODO : Remove this later , no need to do server call
    fun getRemotePrograms(sem: String, sub: String, unit: String) {

        _loading.postValue(true)

        val response = NetworkService.networkInstance.fetchPrograms(sem, sub, unit)
        println("Response is $response")
        response.enqueue(object : Callback<List<ProgramEntity>> {
            override fun onResponse(
                call: Call<List<ProgramEntity>>,
                response: Response<List<ProgramEntity>>
            ) {
                println("Inner Response is $response")
                response.body()?.let {
                    clearPrograms()
                    _programsList.value?.addAll(it)
                    _loading.postValue(false)
                    refresh()
//                    println("Body is $it")
                }

            }

            override fun onFailure(call: Call<List<ProgramEntity>>, t: Throwable) {
                clearPrograms()
                refresh()
                _loading.postValue(false)
                println("Failure is $t")
            }
        })


    }


    //TODO : List of json can not convert to objects, done it manually
    fun getFirestorePrograms(sem: String, sub: String, unit: String) {
        try {
            _loading.postValue(true)
            val programCol = db.collection("newPrograms")
            programCol.whereEqualTo("sem", sem).whereEqualTo("sub", sub).whereEqualTo("unit", unit)
                .get().addOnSuccessListener {
                    val mList = mutableListOf<ProgramEntity>()
                    //val j = it.documents[0].data
                    for(j in it.documents){
                        val p = ProgramEntity(
                            id = j!!["id"].toString().toInt(),
                            title = j["title"].toString(),
                            content = j["content"].toString(),
                            sem = j["sem"].toString(),
                            sub = j["sub"].toString(),
                            unit = j["unit"].toString()
                        )
                        mList.add(p)
                    }
                    //msg.postValue(mList.size.toString())
                    //clearPrograms()
                    _programsList.postValue(mList)
                    _loading.postValue(false)
                    //refresh()
                }.addOnFailureListener {
                    msg.postValue("Failure"+it.message.toString())
                    clearPrograms()
                    refresh()
                    _loading.postValue(false)
                }
        } catch (e: Exception) {
            msg.postValue(e.message)
        }

    }

    //TODO : Remove this later , no need to do server call
    fun getAllPrograms(){
        try{
            val r = NetworkService.networkInstance.getData()
            r.enqueue(object : Callback<List<ProgramEntity>?> {
                override fun onResponse(
                    call: Call<List<ProgramEntity>?>,
                    response: Response<List<ProgramEntity>?>
                ) {
                    var count = 0
                    val plist = response.body()!!
                    /*
                    plist.forEach {
                        val document = db.collection("newPrograms").document(it.id.toString())
                        document.set(it).addOnSuccessListener {
                            count++
                        }
                    }
                     */
                    val c = db.collection("newPrograms").get().addOnSuccessListener {
                        msg.postValue("Postgress : ${plist.size} FireStore : ${it.size()} ")
                    }
                }
                override fun onFailure(call: Call<List<ProgramEntity>?>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }catch (e : Exception){
            msg.postValue(e.message.toString())
        }
    }

    fun addToSubjectSMutableMap(key: Int, list: Array<String>) {
        subjectmutableMap[key] = list
    }

    fun createSubjectsMutableMap() {
        subjectmutableMap[1] = arrayOf("IPLC", "HTML")
        subjectmutableMap[2] = arrayOf("ACP", "DBMS 1", "HTML/JS")
        subjectmutableMap[3] = arrayOf("OOCP", "DS_ALGO")
        subjectmutableMap[4] = arrayOf("CJ", "DBMS 2", "WPC#")
        subjectmutableMap[5] = arrayOf("PYTHON", "ASP.NET")
        subjectmutableMap[6] = arrayOf("WEB APP DEV")
    }
}