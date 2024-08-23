package com.saudeconnectapp.edit_perfil_screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saudeconnectapp.R
import com.saudeconnectapp.databinding.FragmentSendImagesEditBinding


class SendImagesEditFragment : Fragment() {

    private lateinit var binding: FragmentSendImagesEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSendImagesEditBinding.inflate(layoutInflater, container, false)



        return binding.root
    }

}