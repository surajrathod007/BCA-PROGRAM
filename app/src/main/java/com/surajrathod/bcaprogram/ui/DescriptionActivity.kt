package com.surajrathod.bcaprogram.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.surajrathod.bcaprogram.R
import com.surajrathod.bcaprogram.model.Program
import kotlinx.android.synthetic.main.activity_description.*

class DescriptionActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        val data = intent.getSerializableExtra("program") as Program

        txtTitle.text = data.title.toString()
        txtSem.text = data.sem.toString()

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