package com.demitrist.advices.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.demitrist.advices.R
import com.demitrist.advices.databinding.ActivityMainBinding
import com.demitrist.advices.presentation.adapters.PagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private val titles = arrayListOf(
        "History",
        "Search",
        "Favourites",
    )
    private val icons = arrayListOf(
        R.drawable.ic_baseline_subject_24,
        R.drawable.ic_baseline_search_24,
        R.drawable.ic_baseline_star_24,
    )
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        val pager = binding.pager
        pager.adapter = PagerAdapter(this, 3)
        pager.currentItem = 1
        binding.tabLayout.setSelectedTabIndicatorColor(getColor(R.color.orange))
        TabLayoutMediator(binding.tabLayout, pager, TabLayoutMediator.TabConfigurationStrategy
        { tab, position ->
            tab.text = titles[position]
            tab.icon = getDrawable(icons[position])
        }).attach()

        setContentView(binding.root)
    }

}