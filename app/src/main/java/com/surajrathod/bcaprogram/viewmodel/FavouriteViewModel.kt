package com.surajrathod.bcaprogram.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.surajrathod.bcaprogram.room.ProgramDatabase
import com.surajrathod.bcaprogram.room.ProgramEntity

class FavouriteViewModel(val context: Context) : ViewModel() {
    private val _favProgramsList = MutableLiveData<MutableList<ProgramEntity>>(mutableListOf())
    val favProgramsList : LiveData<MutableList<ProgramEntity>>
        get() = _favProgramsList
    lateinit var favDb : ProgramDatabase
    init {
        setUpDataBase(context)
    }
    private fun clearList() = _favProgramsList.value?.clear()
    private fun refresh(){
        _favProgramsList.value = _favProgramsList.value
    }
    private fun setUpDataBase(context : Context){
         favDb = ProgramDatabase.getDatabase(context)
    }

    suspend fun toggleFavourite(programId: Int){
        if(!favDb.programDao().isFav(programId)){
           val data = _favProgramsList.value?.find { programId == it.id }
            favDb.programDao().insert(data!!)
        }else{
            favDb.programDao().removeFav(programId)
        }
    }

    fun getAllPrograms(viewLifecycleOwner: LifecycleOwner){
        clearList()
        favDb.programDao().getAllProgram().observe(viewLifecycleOwner, Observer{
            _favProgramsList.value?.addAll(it)
        })
    }
}