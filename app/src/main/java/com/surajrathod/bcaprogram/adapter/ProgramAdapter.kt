package com.surajrathod.bcaprogram.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.surajrathod.bcaprogram.databinding.ItemProgramBinding
import com.surajrathod.bcaprogram.model.Program
import com.surajrathod.bcaprogram.ui.DescriptionActivity

class ProgramAdapter(private val list: List<Program>): RecyclerView.Adapter<ProgramAdapter.ViewHolder>() {
    class ViewHolder(binding : ItemProgramBinding):RecyclerView.ViewHolder(binding.root){
            val index = binding.ProgramIndex
            val title = binding.programTitle
            val item = binding.programCard
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemProgramBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val program = list[position]
        with(holder){
            index.text = (position+1).toString()
            title.text = if(program.title.length < 75) program.title
            else program.title.substring(0,75)+"..."
            item.setOnClickListener {
                startActivity(it.context,Intent(it.context,DescriptionActivity::class.java), Bundle())
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}