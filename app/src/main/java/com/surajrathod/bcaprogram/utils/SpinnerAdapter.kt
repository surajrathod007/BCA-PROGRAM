package com.surajrathod.bcaprogram.utils

import android.app.Activity
import android.content.Context
import android.widget.ArrayAdapter
import androidx.appcompat.R
import kotlinx.android.synthetic.main.fragment_dashboard.*

class SpinnerAdapter {
    fun createAdapter(context : Activity,list : Array<String>) : ArrayAdapter<String> {
        return ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item,list)
    }
}

//support_simple_spinner_dropdown_item