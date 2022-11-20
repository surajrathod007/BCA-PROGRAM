package com.surajrathod.bcaprogram.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.OvershootInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.surajrathod.bcaprogram.R
import com.surajrathod.bcaprogram.adapter.ProgramAdapter
import com.surajrathod.bcaprogram.databinding.FragmentDashboardBinding
import com.surajrathod.bcaprogram.network.NetworkService
import com.surajrathod.bcaprogram.utils.SpinnerAdapter
import com.surajrathod.bcaprogram.viewmodel.FavouriteViewModel
import com.surajrathod.bcaprogram.viewmodel.ProgramViewModel
//import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper.ORIENTATION_VERTICAL


class DashboardFragment : Fragment() {

    lateinit var programViewModel: ProgramViewModel
    lateinit var favViewModel : FavouriteViewModel
    lateinit var binding: FragmentDashboardBinding
//    lateinit var filterer : LinearLayoutCompat
    val spinnerAdapter = SpinnerAdapter()
    lateinit var offlinePage : ConstraintLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var hide = false
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        binding = FragmentDashboardBinding.bind(view)
        offlinePage = binding.offlineScreen.root
//      filterer = binding.Filterer
        binding.programRV.layoutManager = LinearLayoutManager(activity)

        OverScrollDecoratorHelper.setUpOverScroll(binding.programRV,ORIENTATION_VERTICAL)
        //binding.programRV.itemAnimator = SlideInUpAnimator(OvershootInterpolator(2f))
        programViewModel = ViewModelProvider(this).get(ProgramViewModel::class.java)
        favViewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)
        favViewModel.setUpDataBase(activity as Context)

        programViewModel.isloading.observe(viewLifecycleOwner) {
            if (!it) {
                binding.loadingAnimation.root.visibility = GONE
            } else {
                binding.loadingAnimation.root.visibility = VISIBLE
            }
        }

        with(programViewModel){
           setObserver(curSemester)
            setObserver(curSubject)
            setObserver(curUnit)
            programsList.observe(viewLifecycleOwner,Observer{
                if(it.isEmpty() && hide){

                   binding.noProgramsScreen.root.visibility = VISIBLE
                    binding.programRV.visibility = GONE
                } else {
                    hide = true
                    refresh()
                }
            })
        }

        programViewModel.msg.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(),"Msg :  $it",Toast.LENGTH_LONG).show()
        }
        //programViewModel.getAllPrograms()
        with(binding){
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
                        toggleLoadingScreen()
                        Handler().postDelayed({
                            toggleLoadingScreen()
                        }, 500)
                        subjectSpinner.adapter = spinnerAdapter.createAdapter(it,
                            programViewModel.subjectmutableMap[position+1]!!
                        )
                        unitSpinner.setSelection(0)
                        hide = false
                       programViewModel.curSemester.value = semSpinner.selectedItem.toString()
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        semSpinner.adapter = spinnerAdapter.createAdapter(it,
                            programViewModel.subjectmutableMap[0]!!
                        )
                    }
                }
            }
        }
        setOnSpinnerItemSelected()
        return view
    }

    private fun setObserver(curSelection: MutableLiveData<String>) {
        curSelection.observe(viewLifecycleOwner, Observer {
            retrivePrograms()
        })
    }
   /* private fun enableSearch(){
        if(filterer.visibility==VISIBLE) filterer.visibility = GONE
        else filterer.visibility = VISIBLE
    }*/
    private fun retrivePrograms(){
        if(NetworkService.checkForInternet(activity as Context)){
            if(offlinePage.isVisible){
                offlinePage.visibility = GONE
            }
                toggleLoadingScreen()
                with(programViewModel) {
                    //TODO : Change Date Source Api/Firestore
                    //getRemotePrograms(curSemester.value!!, curSubject.value!!, curUnit.value!!)
                    getFirestorePrograms(curSemester.value!!, curSubject.value!!, curUnit.value!!)
                }
                Handler().postDelayed({
                    toggleLoadingScreen()
                }, 500)
        }else{
            Toast.makeText(requireContext(),"No internet connection !",Toast.LENGTH_LONG).show()
               if(offlinePage.isVisible){

               }else{
                   offlinePage.visibility = VISIBLE
                   Glide.with(this).load(resources.getDrawable(R.drawable.vector_offline_screen))
                       .circleCrop()
                       .into(offlinePage.findViewById(R.id.ivVector))
                   offlinePage.findViewById<AppCompatButton>(R.id.btnRetry).setOnClickListener {
                       retrivePrograms()
                   }
               }
        }

    }
    private fun setOnSpinnerItemSelected(){
        with(binding){
            subjectSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adt: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    programViewModel.curSubject.value = subjectSpinner.selectedItem.toString()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    programViewModel.curSubject.value = subjectSpinner.getItemAtPosition(0).toString()
                }
            }

            unitSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adt: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    programViewModel.curUnit.value = unitSpinner.selectedItem.toString()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    programViewModel.curUnit.value = unitSpinner.getItemAtPosition(0).toString()
                }
            }
        }
    }
    fun refresh(){
        binding.programRV.adapter =
            programViewModel.programsList.value?.let { it -> ProgramAdapter(it.sortedBy { it.id },favViewModel,this)
            }
        binding.noProgramsScreen.root.visibility = GONE
        binding.programRV.visibility = VISIBLE
    }
    fun toggleLoadingScreen(){
        if(binding.progressLayout.root.visibility == VISIBLE) binding.progressLayout.root.visibility = GONE
        else binding.progressLayout.root.visibility = VISIBLE
    }


}