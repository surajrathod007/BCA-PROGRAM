package com.surajrathod.bcaprogram.viewmodel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.surajrathod.bcaprogram.model.ProgramEntity
import com.surajrathod.bcaprogram.room.ProgramDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavouriteViewModel() : ViewModel() {
    private val _favProgramsList = MutableLiveData<MutableList<ProgramEntity>>(mutableListOf())
    val favProgramsList : LiveData<MutableList<ProgramEntity>>
        get() = _favProgramsList
    private lateinit var favDb : ProgramDatabase

    private fun clearList() = _favProgramsList.value?.clear()
    private fun refresh(){
        _favProgramsList.value = _favProgramsList.value
    }
     fun setUpDataBase(context : Context){
         favDb = ProgramDatabase.getDatabase(context)
    }

    suspend fun toggleFavourite(programEntity : ProgramEntity){
        val id = programEntity.id
        if(!favDb.programDao().isFav(id)){
            favDb.programDao().insert(programEntity)
            _favProgramsList.value?.add(programEntity)
        }else{
            favDb.programDao().removeFav(id)
            _favProgramsList.value?.remove(programEntity)
        }
    }
    fun isFav(id : Int) = favDb.programDao().isFav(id)

    fun getAllPrograms(viewLifecycleOwner: LifecycleOwner){
        favDb.programDao().getAllProgram().observe(viewLifecycleOwner, Observer{
            clearList()
            _favProgramsList.value?.addAll(it)
            refresh()
        })
    }
}