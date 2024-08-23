package com.saudeconnectapp.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.saudeconnectapp.R
import com.saudeconnectapp.databinding.FragmentEditPerfilBinding

class EditPerfilFragment : Fragment() {


    private lateinit var binding: FragmentEditPerfilBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditPerfilBinding.inflate(layoutInflater, container, false)

        binding.txtAlterarFoto.setOnClickListener {

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backToPerfil.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}