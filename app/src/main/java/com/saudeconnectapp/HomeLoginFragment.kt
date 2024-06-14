package com.saudeconnectapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.saudeconnectapp.databinding.FragmentHomeLoginBinding

class HomeLoginFragment : Fragment() {

    private lateinit var binding: FragmentHomeLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeLoginBinding.inflate(layoutInflater, container, false)

        val btFromLogin = binding.btFromLogin
        val btFromSingUp = binding.btFromCadastro

        btFromLogin.setOnClickListener {
            findNavController().navigate(R.id.action_homeLoginFragment_to_loginFragment)
        }

        btFromSingUp.setOnClickListener {
            findNavController().navigate(R.id.action_homeLoginFragment_to_singUpFragment)
        }



        return binding.root
    }

}