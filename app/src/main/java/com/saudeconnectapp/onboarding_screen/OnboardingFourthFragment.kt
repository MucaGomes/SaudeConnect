package com.saudeconnectapp.onboarding_screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.saudeconnectapp.R
import com.saudeconnectapp.databinding.FragmentOnboardingFourthBinding

class OnboardingFourthFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingFourthBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnboardingFourthBinding.inflate(layoutInflater, container, false)

        binding.btnFinished.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFourthFragment_to_homeLoginFragment)
            onBoardingFinished()
        }

        return binding.root

    }

    private fun onBoardingFinished() {

        val sharedPreferences =
            requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putBoolean("finished", true)
        editor.apply()

    }
}