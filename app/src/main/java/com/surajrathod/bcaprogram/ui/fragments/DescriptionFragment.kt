package com.surajrathod.bcaprogram.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.surajrathod.bcaprogram.R
import com.surajrathod.bcaprogram.databinding.ActivityDescriptionBinding
import com.surajrathod.bcaprogram.databinding.FragmentDescriptionBinding
import com.surajrathod.bcaprogram.model.ProgramEntity
import com.surajrathod.bcaprogram.ui.DescriptionActivity
import com.surajrathod.bcaprogram.ui.shareLink
import com.surajrathod.bcaprogram.viewmodel.FavouriteViewModel
import kotlinx.android.synthetic.main.activity_description.*
import kotlinx.android.synthetic.main.fragment_description.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

class DescriptionFragment : Fragment() {

    lateinit var activity : DescriptionActivity

    lateinit var binding: FragmentDescriptionBinding

    lateinit var app : DescriptionActivity.Update
    lateinit var data : ProgramEntity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_description, container, false)
        binding = FragmentDescriptionBinding.bind(view)

        activity.binding.btnCopyCode.setOnClickListener {
            activity.copyTextToClipboard(data.content)
        }

        OverScrollDecoratorHelper.setUpOverScroll(binding.scrollDescription)

        binding.marDown.setMarkDownText(data.content)



        //favViewModel.favDb.programDao().isFav(data.id).toString()


        binding.reportProgram.setOnClickListener {
            val send = "bcazone007@gmail.com"
            val subject = "Report ProgramEntity ${data.id}"
            val message = "Hello, BCA Hub team , I Found an Error On ProgramEntity Number ${data.id}"

            activity.sendReport(send,subject,message)
        }
        activity.binding.btnShareProgram.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT,data.content + "\nHey i found an app where you can get all BCA Practical programs for free!! \n Download Now : $shareLink")
            startActivity(Intent.createChooser(intent,"Share With Friends"))

        }




        with(binding){
            txtTitle.text = data.title.toString()
            txtSem.text = data.sem.toString()
            txtSub.text = data.sub.toString()
            txtUnit.text = data.unit.toString()
        }

        println(data.toString())


        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(descriptionActivity: DescriptionActivity, program: ProgramEntity) =
            DescriptionFragment().apply {
                activity = descriptionActivity
                data = program
            }
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {  activity.setFavIcon(data.id) }
    }
}