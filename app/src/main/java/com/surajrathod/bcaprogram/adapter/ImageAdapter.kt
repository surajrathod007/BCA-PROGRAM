package com.surajrathod.bcaprogram.adapter

import alirezat775.lib.carouselview.CarouselAdapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.surajrathod.bcaprogram.R
import com.surajrathod.bcaprogram.databinding.BookItemLayoutBinding
import com.surajrathod.bcaprogram.model.SampleModel
import kotlinx.android.synthetic.main.book_item_layout.view.*
import kotlinx.android.synthetic.main.book_poster_layout.view.*

class ImageAdapter() : CarouselAdapter() {

    private val EMPTY_ITEM = 0
    private val NORMAL_ITEM = 1

    private var vh: CarouselViewHolder? = null
    var onClick: OnClick? = null

    fun setOnClickListener(onClick: OnClick?) {
        this.onClick = onClick
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val v = inflater.inflate(R.layout.book_poster_layout, parent, false)
        vh = MyViewHolder(v)
        return vh as MyViewHolder
    }


    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        when (holder) {
            is MyViewHolder -> {
                vh = holder
                val model = getItems()[position] as SampleModel
                Glide.with(holder.itemView.context).load(model.getId()).into((vh as MyViewHolder).imgPoster)
            }
        }
    }

    inner class MyViewHolder(itemView: View) : CarouselViewHolder(itemView) {

        var imgPoster: ImageView = itemView.imgBookPosterDetails

        init {
            imgPoster.setOnClickListener { onClick?.click(getItems()[adapterPosition] as SampleModel) }
        }

    }
    interface OnClick {
        fun click(model: SampleModel)
    }
}