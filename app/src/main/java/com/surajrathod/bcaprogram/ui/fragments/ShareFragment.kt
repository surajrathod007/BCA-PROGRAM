package com.surajrathod.bcaprogram.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.surajrathod.bcaprogram.R
import com.surajrathod.bcaprogram.databinding.FragmentShareBinding
import com.surajrathod.bcaprogram.model.AppUpdate
import com.surajrathod.bcaprogram.model.Quote
import com.surajrathod.bcaprogram.network.NetworkService
import com.surajrathod.bcaprogram.ui.DescriptionActivity
import com.surajrathod.bcaprogram.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_share.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ShareFragment : Fragment() {


    //val update : AppUpdate = NetworkService.networkInstance.checkForUpdate()

    lateinit var btnShare: Button
    lateinit var app: Update
    lateinit var binding: FragmentShareBinding


    override fun onCreate(savedInstanceState: Bundle?) {


        //
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_share, container, false)

        binding = FragmentShareBinding.inflate(inflater, container, false)

        //loadQuote()
        var appLink = ""
        if (checkInternet(requireContext())) {
            loadUpdate()
            //appLink = app.link.toString()
        } else {
            binding.txtMessage.text = "Pls turn on internet"
            appLink = " "
        }



        binding.txtVersion.text = "app version : ${MainActivity().thisVersion}"

        btnShare = view.findViewById(R.id.btnShare)





        binding.btnShare.setOnClickListener {

            if (checkInternet(it.context)) {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.type = "text/plain"
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Hey i found an app where you can get all BCA Practical programs for free!! \n Download Now : ${app.link}"
                )
                startActivity(Intent.createChooser(intent, "Share With Friends"))
            } else {
                Toast.makeText(requireContext(), "No internet", Toast.LENGTH_SHORT).show()
            }


        }


        return binding.root


    }

    /*
    private fun loadQuote() {
        val q = NetworkService.networkInstance.getQuote()
        q.enqueue(object : Callback<Quote?> {
            override fun onResponse(call: Call<Quote?>, response: Response<Quote?>) {
                binding.txtQuote.text =
                    response.body()!!.en.toString() + "\n" + "- " + response.body()!!.author.toString()
            }
            override fun onFailure(call: Call<Quote?>, t: Throwable) {
                binding.txtQuote.text = "Please check your network !"
            }
        })
    }
     */

    fun setUpdate(body: Update) {
        app = body
    }


    fun loadUpdate() {
        /*
        val response = NetworkService.networkInstance.checkForUpdates()
        // ctrl+shift+space
        response.enqueue(object : Callback<AppUpdate?> {
            override fun onResponse(call: Call<AppUpdate?>, response: Response<AppUpdate?>) {
               val body = response.body()

                if(body == null){
                    Toast.makeText(requireContext(),"failed", Toast.LENGTH_LONG).show()
                }else{
                    //Toast.makeText(requireContext(),"Success ${body.message}", Toast.LENGTH_LONG).show()
                }

                txtMessage.text = body?.message.toString()

                val u = Update(body!!.id,body.version,body.link,body.message)

                setUpdate(u)

            }

            override fun onFailure(call: Call<AppUpdate?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

         */

        val update =
            FirebaseFirestore.getInstance().collection("newPrograms").document("appUpdate").get()
        update.addOnSuccessListener {
            if (it.exists()) {
                var msg = it.get("message").toString()
                var not = it.get("notice").toString()
                txtMessage.text =
                    "If you find any error in code , then please report that program !"
                txtNotice.text = not.replace("\\n", "\n")
                setUpdate(
                    Update(
                        id = 1,
                        link = it.get("link").toString(),
                        version = it.get("version").toString().toFloat(),
                        message = it.get("message").toString(),
                        notice = it.get("notice").toString()
                    )
                )
            }
        }


    }

    fun checkInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {

                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                else -> false

            }
        } else {

            val netInfo = connectivityManager.activeNetworkInfo ?: return false

            return netInfo.isConnected
        }

    }

    data class Update(
        var id: Int,
        var version: Float,
        var link: String,
        var message: String = "",
        var notice: String = ""
    )

}