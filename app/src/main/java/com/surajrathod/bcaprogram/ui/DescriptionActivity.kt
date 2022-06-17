package com.surajrathod.bcaprogram.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.ViewModelProvider

import com.surajrathod.bcaprogram.R
import com.surajrathod.bcaprogram.databinding.ActivityDescriptionBinding
import com.surajrathod.bcaprogram.model.AppUpdate
import com.surajrathod.bcaprogram.model.ProgramEntity
import com.surajrathod.bcaprogram.network.NetworkService
import com.surajrathod.bcaprogram.ui.fragments.ShareFragment
import com.surajrathod.bcaprogram.viewmodel.FavouriteViewModel
import kotlinx.android.synthetic.main.activity_description.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL
import java.util.regex.Pattern

class DescriptionActivity : AppCompatActivity() {





    lateinit var favViewModel : FavouriteViewModel

    lateinit var binding: ActivityDescriptionBinding

    lateinit var app : Update




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadUpdate()
        val data = intent.getSerializableExtra("program") as com.surajrathod.bcaprogram.model.ProgramEntity

        binding = ActivityDescriptionBinding.inflate(layoutInflater)

        setContentView(binding.root)
        //setContentView(R.layout.activity_description)


        binding.btnCopyCode.setOnClickListener {
            copyTextToClipboard(data.content)
        }

        OverScrollDecoratorHelper.setUpOverScroll(binding.scrollDescription)

        binding.marDown.setMarkDownText(data.content)

        favViewModel = ViewModelProvider(this@DescriptionActivity).get(FavouriteViewModel()::class.java)
        favViewModel.setUpDataBase(this)
       GlobalScope.launch {  setFavIcon(data.id) }
        //favViewModel.favDb.programDao().isFav(data.id).toString()


        binding.reportProgram.setOnClickListener {
            val send = "bcazone007@gmail.com"
            val subject = "Report Program ${data.id}"
            val message = "Hello, BCA Hub team , I Found an Error On Program Number ${data.id}"

            sendReport(send,subject,message)
        }
        binding.btnShareProgram.setOnClickListener {

            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT,data.content + "\nHey i found an app where you can get all BCA Practical programs for free!! \n Download Now : $shareLink")
            startActivity(Intent.createChooser(intent,"Share With Friends"))

        }


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

        txtTitle.text = data.title.toString()
        txtSem.text = data.sem.toString()
        txtSub.text = data.sub.toString()
        txtUnit.text = data.unit.toString()
        //txtProgram.text = gk










       // txtProgram.text = g
        println(data.toString())

        setSupportActionBar(favToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
    }

//
//    fun setupCodeView()
//    {
//
//
//        val pairCompleteMap: MutableMap<Char, Char> = HashMap()
//        pairCompleteMap['{'] = '}'
//        pairCompleteMap['['] = ']'
//        pairCompleteMap['('] = ')'
//        pairCompleteMap['<'] = '>'
//        pairCompleteMap['"'] = '"'
//        pairCompleteMap['\''] = '\''
//
//        val characterSet: MutableSet<Char> = HashSet()
//        characterSet.add('{')
//
//        val characterSet2: MutableSet<Char> = java.util.HashSet()
//        characterSet.add('{')
//
//        binding.codeView.apply {
//            setEnableAutoIndentation(true)
//            setEnableLineNumber(true)
//            setPairCompleteMap(pairCompleteMap)
//            enablePairComplete(true)
//            enablePairCompleteCenterCursor(true)
//            setTabLength(4)
//            setIndentationStarts(characterSet)
//            setIndentationEnds(characterSet2)
//            setLineNumberTextColor(Color.GRAY)
//            setLineNumberTextSize(25f)
//            setText(data.content)
//
//
//        }
//
//        binding.codeView.resetHighlighter()
//
//        binding.codeView.addSyntaxPattern(PATTERN_HEX,resources.getColor(R.color.white));
//        binding.codeView.addSyntaxPattern(PATTERN_CHAR,resources.getColor(R.color.white));
//        binding.codeView.addSyntaxPattern(PATTERN_STRING, resources.getColor(R.color.white));
//        binding.codeView.addSyntaxPattern(PATTERN_NUMBERS, resources.getColor(R.color.white));
//        binding.codeView.addSyntaxPattern(PATTERN_KEYWORDS, resources.getColor(R.color.white));
//        binding.codeView.addSyntaxPattern(PATTERN_BUILTINS, resources.getColor(R.color.white));
//        binding.codeView.addSyntaxPattern(PATTERN_SINGLE_LINE_COMMENT, resources.getColor(R.color.white));
//        binding.codeView.addSyntaxPattern(PATTERN_MULTI_LINE_COMMENT,resources.getColor(R.color.white));
//        binding.codeView.addSyntaxPattern(PATTERN_ANNOTATION, resources.getColor(R.color.white));
//        binding.codeView.addSyntaxPattern(PATTERN_ATTRIBUTE, resources.getColor(R.color.white));
//        binding.codeView.addSyntaxPattern(PATTERN_GENERIC, resources.getColor(R.color.white));
//        binding.codeView.addSyntaxPattern(PATTERN_OPERATION, resources.getColor(R.color.white));
//
//        binding.codeView.reHighlightSyntax()
//    }


    private fun setFavIcon(data : Int){
        val isFav = favViewModel.isFav(data)
       CoroutineScope(Dispatchers.Main).launch {  if(isFav) binding.btnFav.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_favorite_24))
       else binding.btnFav.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_favorite_border_24)) }
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


    fun loadUpdate(){
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
    }

    private fun setUpdate(u: Update) {

        app = u
    }

    private fun copyTextToClipboard(textToCopy : String) {

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