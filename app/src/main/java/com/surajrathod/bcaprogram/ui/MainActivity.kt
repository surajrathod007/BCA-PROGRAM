package com.surajrathod.bcaprogram.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.surajrathod.bcaprogram.BuildConfig
import com.surajrathod.bcaprogram.R
import com.surajrathod.bcaprogram.model.AppUpdate
import com.surajrathod.bcaprogram.network.NetworkService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.update_dialog_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    var thisVersion = BuildConfig.VERSION_NAME.toFloat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("CUR VERION IS $thisVersion")
       val checkUpdates = NetworkService.networkInstance.checkForUpdates()
        checkUpdates.enqueue(object : Callback<AppUpdate> {
            override fun onResponse(call: Call<AppUpdate>, response: Response<AppUpdate>) {
                println("Response is ${response.body()}")
                if(response.body()!!.version>thisVersion){
                    println("UPdate AVAIL")
                        toggleUpdateDialog()
                }
            }
            override fun onFailure(call: Call<AppUpdate>, t: Throwable) {
               println("RETROFIT ERROR WHILE CHECKING UPDATES : $t")
            }
        })
        updateBtn.setOnClickListener{
            Toast.makeText(this@MainActivity, "updating..", Toast.LENGTH_SHORT).show()
            toggleUpdateDialog()
        }
        laterBtn.setOnClickListener{
            toggleUpdateDialog()
        }
        bottomNavigationView.setupWithNavController(findNavController(R.id.fragmentContainerView))
    }

    private fun toggleUpdateDialog() {
        if(updateDialog.isVisible){
            updateDialog.visibility = GONE
            bottomNavigationView.visibility = VISIBLE

        }else{

            updateDialog.visibility = VISIBLE
            bottomNavigationView.visibility = GONE
        }
    }

}