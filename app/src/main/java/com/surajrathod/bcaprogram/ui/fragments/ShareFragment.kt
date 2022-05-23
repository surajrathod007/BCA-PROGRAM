package com.surajrathod.bcaprogram.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.surajrathod.bcaprogram.R
import com.surajrathod.bcaprogram.databinding.FragmentShareBinding
import com.surajrathod.bcaprogram.model.AppUpdate
import com.surajrathod.bcaprogram.network.NetworkService
import com.surajrathod.bcaprogram.ui.DescriptionActivity
import kotlinx.android.synthetic.main.fragment_share.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ShareFragment : Fragment() {


    //val update : AppUpdate = NetworkService.networkInstance.checkForUpdate()

    lateinit var btnShare : Button
    lateinit var app : Update
    lateinit var binding: FragmentShareBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        loadUpdate()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_share, container, false)

        binding = FragmentShareBinding.inflate(inflater,container,false)
        btnShare = view.findViewById(R.id.btnShare)


        binding.updateCardView.setOnClickListener {
            startActivity(Intent(activity,DescriptionActivity::class.java))
        }


        btnShare.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT,"Hey i found an app where you can get all BCA Practical programs for free!! \n Download Now : ${app.link}")
            startActivity(Intent.createChooser(intent,"Share With Friends"))

        }







        return binding.root


    }

    fun setUpdate(body : Update){
        app = body
    }



    fun loadUpdate()
    {
        val response = NetworkService.networkInstance.checkForUpdates()



        // ctrl+shift+space
        response.enqueue(object : Callback<AppUpdate?> {
            override fun onResponse(call: Call<AppUpdate?>, response: Response<AppUpdate?>) {
               val body = response.body()

                if(body == null){
                    Toast.makeText(requireContext(),"failed", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(requireContext(),"Success ${body.message}", Toast.LENGTH_LONG).show()
                }

                txtMessage.text = body?.message.toString()

                val u = Update(body!!.id,body.version,body.link,body.message)

                setUpdate(u)

            }

            override fun onFailure(call: Call<AppUpdate?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })



    }

    data class Update(
        var id : Int,
        var version : Float,
        var link : String,
        var message : String = ""
    )

}