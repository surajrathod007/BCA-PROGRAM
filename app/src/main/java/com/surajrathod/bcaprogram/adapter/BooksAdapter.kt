package com.surajrathod.bcaprogram.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.surajrathod.bcaprogram.databinding.BookItemLayoutBinding
import com.surajrathod.bcaprogram.model.Book

class BooksAdapter(val c: Context, val l: List<Book>) : Adapter<BooksAdapter.BooksViewHolder>() {

    class BooksViewHolder(binding: BookItemLayoutBinding) : ViewHolder(binding.root) {
        val imgBookPoster = binding.imgBookPoster
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        return BooksViewHolder(BookItemLayoutBinding.inflate(LayoutInflater.from(c), parent, false))
    }

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        val book = l[position]
        with(holder){
            Glide.with(c).load(book.imageUrl).into(imgBookPoster)
            imgBookPoster.setOnClickListener {
                Toast.makeText(it.context, "You clicked : ${book.title}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return l.size
    }
}