package com.saudeconnectapp.edit_perfil_screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saudeconnectapp.R
import com.saudeconnectapp.databinding.FragmentEditPerfilBinding
import com.saudeconnectapp.databinding.FragmentEditPictureAvatarBinding

class EditPictureAvatarFragment : Fragment() {

    private lateinit var binding: FragmentEditPictureAvatarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=  FragmentEditPictureAvatarBinding.inflate(layoutInflater, container, false)

        return binding.root
    }


}