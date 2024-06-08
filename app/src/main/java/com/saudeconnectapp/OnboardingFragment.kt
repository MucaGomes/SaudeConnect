package com.saudeconnectapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saudeconnectapp.databinding.FragmentOnboardingBinding
import com.saudeconnectapp.onboarding_screen.OnboardingFirstFragment
import com.saudeconnectapp.onboarding_screen.OnboardingSecondFragment
import com.saudeconnectapp.onboarding_screen.OnboardingThirdFragment

class OnboardingFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnboardingBinding.inflate(layoutInflater, container, false)

        val fragmentList = arrayListOf<Fragment>(
            OnboardingFirstFragment(),
            OnboardingSecondFragment(),
            OnboardingThirdFragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        val viewPager = binding.viewPager

        viewPager.adapter = adapter

        // indicator dots
        val dots = binding.dotsIndicator

        dots.attachTo(viewPager)

        return binding.root

    }


}