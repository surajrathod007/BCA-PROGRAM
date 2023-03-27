package com.surajrathod.bcaprogram.ui.activity

import alirezat775.lib.carouselview.Carousel
import alirezat775.lib.carouselview.CarouselModel
import alirezat775.lib.carouselview.CarouselView
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Animatable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.surajrathod.bcaprogram.R
import com.surajrathod.bcaprogram.adapter.ImageAdapter
import com.surajrathod.bcaprogram.databinding.ActivityBookDetailsBinding
import com.surajrathod.bcaprogram.model.Book
import com.surajrathod.bcaprogram.model.SampleModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.floor

class BookDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityBookDetailsBinding
    var book: Book? = null
    var isRead = false
    var isWrite = false
    lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                isRead = it[android.Manifest.permission.READ_EXTERNAL_STORAGE] ?: isRead
                isWrite = it[android.Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: isWrite
            }

        book = intent.getSerializableExtra("book") as Book?
        if (book != null) {
            setupBook(book!!)
        } else {
            Toast.makeText(this, "Book is null", Toast.LENGTH_SHORT).show()
        }
        setupClickListeners()


    }

    private fun setupClickListeners() {
        binding.btnBackBookDetails.setOnClickListener {
            finish()
        }
        binding.btnDownloadBook.setOnClickListener {
            requestPermission()
            if(isRead && isWrite){
                if(book != null){
                    downloadFile(book!!.downloadLink,book!!.title.trim()+".pdf")
                    Toast.makeText(this, "Downloading started...", Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(this, "Book is null ;(", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Please allow required read/write permissions !", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupBook(book: Book) {

        val adp = ImageAdapter()
        val carousel = Carousel(this, binding.imageCarousel, adp)
        carousel.setOrientation(CarouselView.HORIZONTAL, false)
        carousel.autoScroll(true, 5000, true)
        carousel.scaleView(true)
        val images = mutableListOf<SampleModel>()
        images.add(SampleModel(book.imageUrl))
        book.demoImages?.forEach {
            images.add(SampleModel(it))
        }
        carousel.addAll(images as MutableList<CarouselModel>)
        binding.txtBookTitle.text = book.title
        var size = ""
        CoroutineScope(Dispatchers.IO).launch {
            val s =
                getFileSize(book.downloadLink)
            withContext(Dispatchers.Main) {
                if (s < 1) {
                    size = String.format("%.2f", s * 1024) + " Kb"
                    binding.txtBookDescription.text = book.description + "\nFile size : " + size
                } else {
                    size = String.format("%.2f", s) + " Mb"
                    binding.txtBookDescription.text = book.description + "\nFile size : " + size
                }
            }
        }
        binding.chipTags.apply {
            book.tags?.forEach {
                val chip = Chip(this@BookDetailsActivity)
                chip.text = it
                chip.typeface = resources.getFont(R.font.hack_regular)

                addView(chip)
            }
        }
    }

    fun getFileSize(url: String): Double {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .head()
            .build()
        val response = client.newCall(request).execute()
        return response.header("Content-Length")?.toDouble()?.div(1000000) ?: 0.0
    }

    fun downloadFile(url: String, fileName: String) {
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(fileName)
            .setDescription("Downloading...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    fun requestPermission() {

        //check permission already granted or not
        isRead = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        isWrite = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        var permissionRequest: MutableList<String> = ArrayList()

        if (!isRead) {
            permissionRequest.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (!isWrite) {
            permissionRequest.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissionRequest.isNotEmpty()) {
            //request permission
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }


    }
}