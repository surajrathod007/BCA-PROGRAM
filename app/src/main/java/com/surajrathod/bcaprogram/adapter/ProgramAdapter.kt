package com.surajrathod.bcaprogram.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.surajrathod.bcaprogram.R
import com.surajrathod.bcaprogram.databinding.ItemProgramBinding
import com.surajrathod.bcaprogram.model.ProgramEntity
import com.surajrathod.bcaprogram.ui.DescriptionActivity
import com.surajrathod.bcaprogram.ui.fragments.FavouritesFragment
import com.surajrathod.bcaprogram.viewmodel.FavouriteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ProgramAdapter(private val list: List<ProgramEntity>,val vm : FavouriteViewModel,val activity: Fragment): RecyclerView.Adapter<ProgramAdapter.ViewHolder>() {
    class ViewHolder(binding : ItemProgramBinding):RecyclerView.ViewHolder(binding.root){
            val index = binding.ProgramIndex
            val title = binding.programTitle
            val item = binding.programCard
            val fav = binding.isFav
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
               val intent = Intent(it.context,DescriptionActivity::class.java).apply {
                   putExtra("program",program)
                   putExtra("programs",list as java.io.Serializable)
               }
                val options = ActivityOptions.makeSceneTransitionAnimation(it.context as Activity,holder.title,"title_transition")
                it.context.startActivity(intent,options.toBundle())
            }
            CoroutineScope(Dispatchers.IO).launch {
                setIcon(fav,program.id)
            }
            fav.setOnClickListener{
                CoroutineScope(Dispatchers.IO).launch{
                    vm.toggleFavourite(program)
                    when(activity){
                        is FavouritesFragment -> showSnakeBar()
                    }
                   setIcon(fav,program.id)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    private fun setIcon(it : ImageView, id : Int){
        val isFav = vm.isFav(id)
        CoroutineScope(Dispatchers.Main).launch {
            if(isFav) it.setImageDrawable(it.resources.getDrawable(R.drawable.ic_baseline_favorite_24))
            else it.setImageDrawable(it.resources.getDrawable(R.drawable.ic_baseline_favorite_border_24))
        }
    }
    fun showSnakeBar(){
        val snackBar = activity.view?.let {
            Snackbar.make(it.findViewById(R.id.FavouriteLinearLayoutCompat),"Program removed",Snackbar.LENGTH_SHORT).setAction("Undo",
                View.OnClickListener {
                    CoroutineScope(Dispatchers.IO).launch { vm.undoDelete() }
                }).show()
        }
    }
}