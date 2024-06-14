package com.saudeconnectapp

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.saudeconnectapp.databinding.FragmentLoginBinding
import com.saudeconnectapp.databinding.FragmentSingUpBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        val imageViewShowPassword = binding.imageViewShowPassword
        val textInputEditTextPassword = binding.txtEditPassword
        val txtSingUp = binding.txtSingUp

        auth = FirebaseAuth.getInstance()

        // função que faz a imagem alterar ao mostrar a senha
        hideOrShowPassword(imageViewShowPassword, textInputEditTextPassword)

        // função que faz a auntenticação do login do usuario no banco
        loginAutenticado()

        txtSingUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_singUpFragment)
        }


        return binding.root
    }

    private fun loginAutenticado() {
        binding.btLogin.setOnClickListener { view ->
            val email = binding.txtEditEmail.text.toString()
            val senha = binding.txtEditPassword.text.toString()

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(
                    context, "Preencha todos os campos!", Toast.LENGTH_SHORT
                ).show()
            } else {
                auth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { autenticacao ->
                        if (autenticacao.isSuccessful) {
                            Toast.makeText(context, "Login feito com Sucesso!", Toast.LENGTH_SHORT)
                                .show()
                            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                        }
                    }.addOnFailureListener {
                        Toast.makeText(
                            context, "Erro ao Fazer Login", Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }

    private fun hideOrShowPassword(
        imageViewShowPassword: ImageView,
        textInputEditTextPassword: TextInputEditText
    ) {
        imageViewShowPassword.setOnClickListener {
            if (textInputEditTextPassword.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                textInputEditTextPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                imageViewShowPassword.setImageResource(R.drawable.show_password)
            } else {
                textInputEditTextPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                imageViewShowPassword.setImageResource(R.drawable.hide_password)
            }
            // Move o cursor para o final do texto
            textInputEditTextPassword.setSelection(textInputEditTextPassword.text?.length ?: 0)
        }
    }

}