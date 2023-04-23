package com.surajrathod.bcaprogram.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore

import com.surajrathod.bcaprogram.R
import com.surajrathod.bcaprogram.adapter.ProgramDescriptionViewPager
import com.surajrathod.bcaprogram.databinding.ActivityDescriptionBinding
import com.surajrathod.bcaprogram.model.ProgramEntity
import com.surajrathod.bcaprogram.viewmodel.FavouriteViewModel
import kotlinx.android.synthetic.main.activity_description.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DescriptionActivity : AppCompatActivity() {

    lateinit var favViewModel : FavouriteViewModel

    lateinit var binding: ActivityDescriptionBinding

    lateinit var app : Update

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //loadUpdate()
        val data = intent.getSerializableExtra("program") as com.surajrathod.bcaprogram.model.ProgramEntity
        val dataList = intent.getSerializableExtra("programs") as ArrayList<*>

        binding = ActivityDescriptionBinding.inflate(layoutInflater)

        setContentView(binding.root)
        //setContentView(R.layout.activity_description)
        favViewModel = ViewModelProvider(this@DescriptionActivity).get(FavouriteViewModel()::class.java)
        favViewModel.setUpDataBase(this)

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
                setFavIcon(data.id)
            }
        }

        setSupportActionBar(favToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)

        viewpager.adapter = ProgramDescriptionViewPager(
            this,
            data,
            dataList,
            supportFragmentManager,
            lifecycle
        )
        viewpager.setCurrentItem(dataList.indexOf(data))
    }

    fun setFavIcon(data : Int){
        val isFav = favViewModel.isFav(data)
       CoroutineScope(Dispatchers.Main).launch {  if(isFav) binding.btnFav.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_favorite_24))
       else binding.btnFav.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_favorite_border_24)) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun loadUpdate(){

        /*
        NetworkService.networkInstance.checkForUpdates().enqueue(object : Callback<AppUpdate?> {
            override fun onResponse(call: Call<AppUpdate?>, response: Response<AppUpdate?>) {

                var body = response.body()
                val u = Update(body!!.id, body.version, body.link, body.message)
                setUpdate(u)
            }

            override fun onFailure(call: Call<AppUpdate?>, t: Throwable) {
                println("RETROFIT ERROR WHILE CHECKING UPDATES IN DESCRIPTION ACTIVITY: $t")
            }
        })
         */
        val update = FirebaseFirestore.getInstance().collection("newPrograms").document("appUpdate").get()
        update.addOnSuccessListener {
            val data = it.data
            val update = Update(
                id = 1,
                link = it.get("link").toString(),
                version = it.get("version").toString().toFloat(),
                message = it.get("message").toString(),
            )
            setUpdate(update)
        }
            .addOnFailureListener {
                Toast.makeText(this@DescriptionActivity,"Update check failed !",Toast.LENGTH_LONG).show()
            }
    }

    private fun setUpdate(u: Update) {
        app = u
    }

    fun copyTextToClipboard(textToCopy : String) {

        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("code", textToCopy)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this@DescriptionActivity, "Code copied to clipboard", Toast.LENGTH_LONG).show()
    }

    fun sendReport(recipient : String,subject : String,message: String)
    {
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        mIntent.putExtra(Intent.EXTRA_SUBJECT,subject)
        mIntent.putExtra(Intent.EXTRA_TEXT,message)

        try{
            startActivity(Intent.createChooser(mIntent,"Choose Gmail To Send Report..."))
        }catch (e : Exception){

        }
    }

    data class Update(
        var id : Int,
        var version : Float,
        var link : String,
        var message : String = ""
    )
}