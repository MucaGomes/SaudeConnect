package com.saudeconnectapp.onboarding_screen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.saudeconnectapp.R
import com.saudeconnectapp.databinding.FragmentOnboardingThirdBinding

class OnboardingThirdFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingThirdBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnboardingThirdBinding.inflate(layoutInflater, container, false)

        val finish = binding.txtFinished

        finish.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_onboardingFourthFragment)

        }

        return binding.root
    }

}