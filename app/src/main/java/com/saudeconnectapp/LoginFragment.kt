package com.saudeconnectapp

import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.saudeconnectapp.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)


        val imageViewShowPassword = binding.imageViewShowPassword
        val textInputEditTextPassword = binding.txtEditPassword

        imageViewShowPassword.setOnClickListener {
            if (textInputEditTextPassword.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                textInputEditTextPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                imageViewShowPassword.setImageResource(R.drawable.show_password)
            } else {
                textInputEditTextPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                imageViewShowPassword.setImageResource(R.drawable.hide_password)
            }
            // Move o cursor para o final do texto
            textInputEditTextPassword.setSelection(textInputEditTextPassword.text?.length ?: 0)
        }


        val txtSingUp = binding.txtSingUp

        txtSingUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_singUpFragment)
        }


        return binding.root
    }

}