package com.surajrathod.bcaprogram.ui.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.surajrathod.bcaprogram.R
import com.surajrathod.bcaprogram.adapter.ProgramAdapter
import com.surajrathod.bcaprogram.databinding.FragmentFavouritesBinding
import com.surajrathod.bcaprogram.viewmodel.FavouriteViewModel
import kotlinx.android.synthetic.main.activity_main.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper.ORIENTATION_VERTICAL


class FavouritesFragment : Fragment() {

    lateinit var favModel : FavouriteViewModel

    lateinit var binding: FragmentFavouritesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)
        var hide = false
        binding = FragmentFavouritesBinding.bind(view)

        favModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)
        favModel.setUpDataBase(requireContext())

        binding.rvFav.layoutManager = LinearLayoutManager(activity)
        OverScrollDecoratorHelper.setUpOverScroll(binding.rvFav,ORIENTATION_VERTICAL)
        toggleLoadingScreen()
        favModel.getAllPrograms(viewLifecycleOwner)
        Handler().postDelayed({
            toggleLoadingScreen()
        },250)
        favModel.favProgramsList.observe(viewLifecycleOwner, Observer {

            if(it.isEmpty() && hide){

                binding.noFavoriteScreen.root.visibility = View.VISIBLE
                binding.noFavoriteScreen.btnToDashboard.setOnClickListener {
                    activity?.onBackPressed()
                }

            } else {
                hide = true
                binding.rvFav.adapter = ProgramAdapter(it,favModel, this)
                binding.noFavoriteScreen.root.visibility = View.GONE
            }

        })

        return view
    }
    fun toggleLoadingScreen(){
        if(binding.loadingScreen.root.visibility == View.VISIBLE) binding.loadingScreen.root.visibility =
            View.GONE
        else binding.loadingScreen.root.visibility = View.VISIBLE
    }

}