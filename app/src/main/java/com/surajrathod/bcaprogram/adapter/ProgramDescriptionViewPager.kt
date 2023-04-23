package com.surajrathod.bcaprogram.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.surajrathod.bcaprogram.model.ProgramEntity
import com.surajrathod.bcaprogram.ui.DescriptionActivity
import com.surajrathod.bcaprogram.ui.fragments.DescriptionFragment

class ProgramDescriptionViewPager(
    val descriptionActivity: DescriptionActivity,
    init_program: ProgramEntity,
    val programs: ArrayList<*>,
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm,lifecycle) {
    val initIndex = programs.indexOf(init_program)
    override fun getItemCount(): Int {
        return programs.size
    }

    override fun createFragment(position: Int): Fragment {
        return DescriptionFragment.newInstance(
            descriptionActivity,
            programs[position] as ProgramEntity
        )
    }
}