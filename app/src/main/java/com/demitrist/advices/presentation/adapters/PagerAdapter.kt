package com.demitrist.advices.presentation.adapters

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.demitrist.advices.presentation.MainActivity
import com.demitrist.advices.presentation.screens.FavouritesFragment
import com.demitrist.advices.presentation.screens.HistoryFragment
import com.demitrist.advices.presentation.screens.SearchFragment

/**
 * @author Demitrist on 09.02.2023
 **/

class PagerAdapter(private val activity: MainActivity, private val itemCount: Int) : FragmentStateAdapter(activity) {

    override fun getItemCount() = itemCount

    override fun createFragment(position: Int) =
        when(position){
            0 -> HistoryFragment()
            2 -> FavouritesFragment()
            else -> SearchFragment()
        }
}