package com.surajrathod.bcaprogram.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import com.surajrathod.bcaprogram.R
import com.surajrathod.bcaprogram.databinding.FragmentMaterialsBinding
import com.surajrathod.bcaprogram.ui.activity.BooksActivity

class MaterialsFragment : Fragment() {


    lateinit var binding: FragmentMaterialsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_materials, container, false)
        binding = FragmentMaterialsBinding.bind(view)

        setupClickListeners()

        return binding.root
    }

    private fun setupClickListeners() {
        with(binding) {

            cardSem1.setOnClickListener {
                val i = Intent(requireContext(), BooksActivity::class.java)
                i.putExtra("sem", "Sem 1")
                startActivity(i)
            }
            cardSem2.setOnClickListener {
                val i = Intent(requireContext(), BooksActivity::class.java)
                i.putExtra("sem", "Sem 2")
                startActivity(i)
            }
            cardSem3.setOnClickListener {
                val i = Intent(requireContext(), BooksActivity::class.java)
                i.putExtra("sem", "Sem 3")
                startActivity(i)
            }
            cardSem4.setOnClickListener {
                val i = Intent(requireContext(), BooksActivity::class.java)
                i.putExtra("sem", "Sem 4")
                startActivity(i)
            }
            cardSem5.setOnClickListener {
                val i = Intent(requireContext(), BooksActivity::class.java)
                i.putExtra("sem", "Sem 5")
                startActivity(i)
            }
            cardSem6.setOnClickListener {
                val i = Intent(requireContext(), BooksActivity::class.java)
                i.putExtra("sem", "Sem 6")
                startActivity(i)
            }
        }
    }


}