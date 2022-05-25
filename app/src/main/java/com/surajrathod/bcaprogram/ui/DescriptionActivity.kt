package com.surajrathod.bcaprogram.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider

import com.surajrathod.bcaprogram.R
import com.surajrathod.bcaprogram.databinding.ActivityDescriptionBinding
import com.surajrathod.bcaprogram.model.ProgramEntity

import com.surajrathod.bcaprogram.viewmodel.FavouriteViewModel
import kotlinx.android.synthetic.main.activity_description.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DescriptionActivity : AppCompatActivity() {

    lateinit var favViewModel : FavouriteViewModel

    lateinit var binding: ActivityDescriptionBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDescriptionBinding.inflate(layoutInflater)

        setContentView(binding.root)
        //setContentView(R.layout.activity_description)


        val data = intent.getSerializableExtra("program") as com.surajrathod.bcaprogram.model.ProgramEntity

        val gk = data.content.intern()



        //webView.loadData(ans,"text/html","UTF-8")
        val s = data.content.intern()


        favViewModel = ViewModelProvider(this@DescriptionActivity).get(FavouriteViewModel()::class.java)
        favViewModel.setUpDataBase(this)
       GlobalScope.launch {  setFavIcon(data.id) }
        //favViewModel.favDb.programDao().isFav(data.id).toString()



        binding.btnFav.setOnClickListener {

            GlobalScope.launch {
                favViewModel.toggleFavourite(
                    ProgramEntity(
                        data.id,
                        data.title,
                        data.content,
                        data.sem,
                        data.sub,
                        data.unit
                    )
                )
                setFavIcon(data.id,)
            }
        }

        txtTitle.text = data.title.toString()
        txtSem.text = data.sem.toString()
        txtSub.text = data.sub.toString()
        txtUnit.text = data.unit.toString()
        txtProgram.text = gk

       // txtProgram.text = g
        println(data.toString())

        setSupportActionBar(favToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
    }

    private fun setFavIcon(data : Int){
        if(favViewModel.isFav(data)) binding.btnFav.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_favorite_24))
            else binding.btnFav.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_favorite_border_24))
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