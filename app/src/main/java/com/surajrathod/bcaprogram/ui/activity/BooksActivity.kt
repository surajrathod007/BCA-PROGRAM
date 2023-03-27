package com.surajrathod.bcaprogram.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.surajrathod.bcaprogram.R
import com.surajrathod.bcaprogram.adapter.MainBooksAdapter
import com.surajrathod.bcaprogram.databinding.ActivityBooksBinding
import com.surajrathod.bcaprogram.model.Book
import com.surajrathod.bcaprogram.model.MainBook

class BooksActivity : AppCompatActivity() {

    lateinit var binding: ActivityBooksBinding
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBooksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sem = intent.getStringExtra("sem")
        if (!sem.isNullOrEmpty()) {
            loadBooksBySem(sem)
        } else {
            Toast.makeText(this, "Sem is null", Toast.LENGTH_SHORT).show()
        }
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnBackBooks.setOnClickListener {
            finish()
        }
    }

    private fun loadBooksBySem(sem: String) {
        binding.txtBooksToolBar.text = sem + " Books"
        val c = db.collection("books")
        //TODO : use where equal to , to fetch books by sem
        c.get().addOnSuccessListener {

            val books = it.toObjects(Book::class.java)

            var mainBooks = mutableListOf<MainBook>()
            val sub = books.map {
                it.sub
            }.distinct()
            sub.forEach{sub ->
                val l = books.filter {
                    it.sub == sub
                }
                mainBooks.add(MainBook(sub,l))
            }

            binding.rvMainBooks.adapter = MainBooksAdapter(this,mainBooks)
        }
    }
}