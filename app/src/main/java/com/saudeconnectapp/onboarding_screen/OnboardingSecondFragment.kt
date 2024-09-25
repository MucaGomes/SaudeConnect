package com.saudeconnectapp.onboarding_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.saudeconnectapp.R
import com.saudeconnectapp.databinding.FragmentOnboardingSecondBinding


class OnboardingSecondFragment : Fragment() {


    private lateinit var binding: FragmentOnboardingSecondBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnboardingSecondBinding.inflate(layoutInflater, container, false)

        val next = binding.txtSkip
        val nextFab = binding.floatingActionButton
        
        val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager)

        next.setOnClickListener {
            viewPager?.currentItem = 2
        }

        nextFab.setOnClickListener {
            viewPager?.currentItem = 2
        }

        return binding.root
    }


}