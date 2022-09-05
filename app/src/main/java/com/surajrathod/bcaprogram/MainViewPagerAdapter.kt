package com.surajrathod.bcaprogram

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.surajrathod.bcaprogram.ui.fragments.DashboardFragment
import com.surajrathod.bcaprogram.ui.fragments.FavouritesFragment
import com.surajrathod.bcaprogram.ui.fragments.ShareFragment

class MainViewPagerAdapter( val fragments : List<Fragment>,activity : AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}