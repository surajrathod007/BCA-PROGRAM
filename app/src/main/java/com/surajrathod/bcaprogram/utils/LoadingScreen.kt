package com.surajrathod.bcaprogram.utils

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatDialog
import com.surajrathod.bcaprogram.R

class LoadingScreen(context: Context) : AppCompatDialog(context) {

    fun loadingScreen() : Dialog{
       val dialog = Dialog(context)
        dialog.setContentView(R.layout.loading_screen)
        return dialog
    }
    fun toggleDialog(dialog: Dialog){
        if(dialog.isShowing){
            dialog.hide()
        }else{
            dialog.show()
        }
    }

}