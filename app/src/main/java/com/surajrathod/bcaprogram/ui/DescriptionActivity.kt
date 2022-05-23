package com.surajrathod.bcaprogram.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.surajrathod.bcaprogram.R

class DescriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        val data = intent.getSerializableExtra("program")
        println(data.toString())
    }
}