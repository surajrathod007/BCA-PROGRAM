package com.surajrathod.bcaprogram.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.surajrathod.bcaprogram.R
import com.surajrathod.bcaprogram.databinding.ActivityDescriptionBinding
import com.surajrathod.bcaprogram.model.Program
import com.surajrathod.bcaprogram.room.ProgramEntity
import com.surajrathod.bcaprogram.viewmodel.FavouriteViewModel
import kotlinx.android.synthetic.main.activity_description.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class DescriptionActivity : AppCompatActivity() {

    lateinit var favViewModel : FavouriteViewModel

    lateinit var binding: ActivityDescriptionBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDescriptionBinding.inflate(layoutInflater)

        setContentView(binding.root)
        //setContentView(R.layout.activity_description)


        val data = intent.getSerializableExtra("program") as Program


        favViewModel = ViewModelProvider(this@DescriptionActivity).get(FavouriteViewModel()::class.java)
        favViewModel.setUpDataBase(this)
        //favViewModel.favDb.programDao().isFav(data.id).toString()

        GlobalScope.launch {
            println(favViewModel.toggleFavourite(ProgramEntity(data.id,data.title,data.content,data.sem,data.sub,data.unit)))
        }


        txtTitle.text = data.title.toString()
        txtSem.text = data.sem.toString()
        txtSub.text = data.sub.toString()
        txtUnit.text = data.unit.toString()
        txtProgram.text = data.content.toString()

        println(data.toString())

        setSupportActionBar(favToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}