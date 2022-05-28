package com.surajrathod.bcaprogram.ui

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.ViewModelProvider

import com.surajrathod.bcaprogram.R
import com.surajrathod.bcaprogram.databinding.ActivityDescriptionBinding
import com.surajrathod.bcaprogram.model.ProgramEntity
import com.surajrathod.bcaprogram.viewmodel.FavouriteViewModel
import kotlinx.android.synthetic.main.activity_description.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL
import java.util.regex.Pattern

class DescriptionActivity : AppCompatActivity() {





    lateinit var favViewModel : FavouriteViewModel

    lateinit var binding: ActivityDescriptionBinding





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = intent.getSerializableExtra("program") as com.surajrathod.bcaprogram.model.ProgramEntity

        binding = ActivityDescriptionBinding.inflate(layoutInflater)



        setContentView(binding.root)
        //setContentView(R.layout.activity_description)


        val pish = "<html>\n" +
                "<head>\n" +
                "\n" +
                "<style>\n" +
                "\n" +
                "body{\n" +
                "font-family: 'Open Sans', sans-serif;\n" +
                "color : #202d5a;\n" +
                "}\n" +
                "\n" +
                "</style>\n" +
                "<link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\n" +
                "<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\n" +
                "<link href=\"https://fonts.googleapis.com/css2?family=Open+Sans&display=swap\" rel=\"stylesheet\">\n" +
                "\n" +
                "</head>\n" +
                "<body>"

        val pas = "</body>"









        val gk = data.content.intern()




        val s = data.content.intern()





        binding.marDown.setMarkDownText(data.content)


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
}