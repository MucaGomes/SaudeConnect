package com.saudeconnectapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saudeconnectapp.databinding.FragmentSingUpBinding


class SingUpFragment : Fragment() {

    private lateinit var binding: FragmentSingUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSingUpBinding.inflate(layoutInflater, container, false)




        return binding.root
    }


}