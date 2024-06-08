package com.saudeconnectapp.onboarding_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.saudeconnectapp.R
import com.saudeconnectapp.databinding.FragmentOnboardingFirstBinding

class OnboardingFirstFragment : Fragment() {

    private lateinit var binding : FragmentOnboardingFirstBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnboardingFirstBinding.inflate(layoutInflater, container, false)

        val next = binding.txtSkip

        val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager)

        next.setOnClickListener{
            viewPager?.currentItem = 1
        }

        return binding.root
    }


}