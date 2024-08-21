package com.saudeconnectapp

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth


class SplashFragment : Fragment() {

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val onboardingCompleted = onBoardingFinished()

        // Verificar se o usuário está logado
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        Handler(Looper.getMainLooper()).postDelayed({
            if (onboardingCompleted) {
                // Usuário finalizou o onboarding, então decidir para onde navegar
                if (currentUser != null) {
                    // Usuário está logado, navega para HomeFragment
                    findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
                } else {
                    // Usuário não está logado, navegar para LoginFragment
                    findNavController().navigate(R.id.action_splashFragment_to_homeLoginFragment)
                }
            } else {
                // Usuário não finalizou o onboarding, navegar para OnboardingFragment
                findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
            }
        }, 2000) // 2 segundos de espera para mostrar a splash screen

        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        return view
    }

    // caso a pessoa ja fez o onboarding
    private fun onBoardingFinished(): Boolean {

        val sharedPreferences = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("finished", false)
    }

}