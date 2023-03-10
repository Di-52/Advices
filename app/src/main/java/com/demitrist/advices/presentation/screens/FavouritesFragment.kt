package com.demitrist.advices.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.demitrist.advices.databinding.FragmentFavouritesBinding

class FavouritesFragment : Fragment() {

    private lateinit var binding:FragmentFavouritesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFavouritesBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
}