package com.surajrathod.bcaprogram.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.getkeepsafe.taptargetview.TapTargetView
import com.surajrathod.bcaprogram.BuildConfig
import com.surajrathod.bcaprogram.R
import com.surajrathod.bcaprogram.databinding.ActivityMainBinding
import com.surajrathod.bcaprogram.databinding.UpdateDialogLayoutBinding
import com.surajrathod.bcaprogram.model.AppUpdate
import com.surajrathod.bcaprogram.network.NetworkService
import com.surajrathod.bcaprogram.utils.TapTargetMaker
import kotlinx.android.synthetic.main.activity_description.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.update_dialog_layout.*
import kotlinx.android.synthetic.main.welcome_screen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    var thisVersion = BuildConfig.VERSION_NAME.toFloat()
    val tapTargetBuilder = TapTargetMaker()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        println("CUR VERION IS $thisVersion")
        bottomNavigationView.visibility = GONE
       var intent = Intent(this,MainActivity::class.java)
       val checkUpdates = NetworkService.networkInstance.checkForUpdates()
        checkUpdates.enqueue(object : Callback<AppUpdate> {
            override fun onResponse(call: Call<AppUpdate>, response: Response<AppUpdate>) {
                println("Response is ${response.body()}")
                val data = response.body()!!
                if(data.version>thisVersion){
                    println("Update available")
                    msg.text = msg.text.toString() + data.message
                    intent = setUpLink(data.link)
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
            startActivity(intent)
        }
        laterBtn.setOnClickListener{
            toggleUpdateDialog()
        }
        bottomNavigationView.setupWithNavController(findNavController(R.id.fragmentContainerView))
       getStartedBtn.setOnClickListener {
           val tutorial = createTapTargetSequence()!!
           tutorial.start()
           bottomNavigationView.visibility = VISIBLE
           welcomeScreen.visibility = GONE
       }


    }

    private fun setUpLink(link: String) : Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(link))
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

    private fun createTapTargetSequence(): TapTargetSequence? {
        return TapTargetSequence(this).targets(
            tapTargetBuilder.createTapTarget(findViewById(R.id.dashboardFragment), "Home "),
            tapTargetBuilder.createTapTarget(searchButton,"Search"),
            tapTargetBuilder.createTapTarget(findViewById(R.id.ttFavBtn),"addToFav"),
            tapTargetBuilder.createTapTarget(findViewById(R.id.favouritesFragment),"favourites"),
            tapTargetBuilder.createTapTarget(findViewById(R.id.shareFragment),"share"),
        ).listener(object : TapTargetSequence.Listener{
            override fun onSequenceFinish() {
            }
            override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {}
            override fun onSequenceCanceled(lastTarget: TapTarget?) {}
        }).continueOnCancel(true)
    }

}