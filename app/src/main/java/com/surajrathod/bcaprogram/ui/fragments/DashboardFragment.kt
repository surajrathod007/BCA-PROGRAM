package com.surajrathod.bcaprogram.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.surajrathod.bcaprogram.R
import com.surajrathod.bcaprogram.databinding.FragmentDashboardBinding
import com.surajrathod.bcaprogram.utils.SpinnerAdapter
import com.surajrathod.bcaprogram.viewmodel.ProgramViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var programViewModel: ProgramViewModel
    lateinit var binding: FragmentDashboardBinding
    lateinit var filterer : LinearLayoutCompat
    val spinnerAdapter = SpinnerAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        binding = FragmentDashboardBinding.bind(view)
        filterer = binding.Filterer
        programViewModel = ViewModelProvider(this).get(ProgramViewModel::class.java)
        programViewModel.addToSubjectMutableMap(1, arrayOf("c#","c","c++"))
        with(binding) {
            activity?.let {
                semSpinner.adapter = spinnerAdapter.createAdapter(it, programViewModel.semList)
                unitSpinner.adapter = spinnerAdapter.createAdapter(it, programViewModel.unitList)
                semSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        adt: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        subjectSpinner.adapter = spinnerAdapter.createAdapter(it,
                            programViewModel.subjectmutableMap[position+1]!!
                        )
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        semSpinner.adapter = spinnerAdapter.createAdapter(it,
                            programViewModel.subjectmutableMap[0]!!
                        )
                    }
                }
            }
        }
        binding.searchButton.setOnClickListener{
            enableSearch()
        }
        return view
    }

   private fun enableSearch(){
        if(filterer.visibility==VISIBLE) filterer.visibility = GONE
        else filterer.visibility = VISIBLE
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}