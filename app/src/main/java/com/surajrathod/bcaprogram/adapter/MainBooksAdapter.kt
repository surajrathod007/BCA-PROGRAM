package com.surajrathod.bcaprogram.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.surajrathod.bcaprogram.databinding.MainBookLayoutBinding
import com.surajrathod.bcaprogram.model.MainBook

class MainBooksAdapter(val c: Context, val l: List<MainBook>) :
    RecyclerView.Adapter<MainBooksAdapter.MainBooksViewHolder>() {

    class MainBooksViewHolder(binding: MainBookLayoutBinding) : ViewHolder(binding.root) {
        val txtBookSubject = binding.txtBookSubjectRv
        val btnViewMore = binding.btnViewMoreBooks
        val rvBooks = binding.rvBooks
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainBooksViewHolder {
        return MainBooksViewHolder(
            MainBookLayoutBinding.inflate(
                LayoutInflater.from(c),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainBooksViewHolder, position: Int) {
        val b = l[position]
        with(holder){
            txtBookSubject.text = b.subject
            btnViewMore.setOnClickListener {
                Toast.makeText(it.context, "Coming soon...", Toast.LENGTH_SHORT).show()
            }
            rvBooks.adapter = BooksAdapter(c,b.books)
        }
    }

    override fun getItemCount(): Int {
        return l.size
    }
}