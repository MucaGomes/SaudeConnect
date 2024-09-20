package com.saudeconnectapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.saudeconnectapp.databinding.FragmentSingUpBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class SingUpFragment : Fragment() {

    private lateinit var binding: FragmentSingUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSingUpBinding.inflate(layoutInflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()

        val imageViewShowPassword = binding.imageViewShowPassword
        val textInputEditTextPassword = binding.txtEditPassword
        val dateOfBirthEditText = binding.boxEditNasc
        val btBack = binding.imgBack
        val buttonSingUp = binding.btSingUp

        // função que faz a imagem de ocultar senha ser trocada
        hideOrShowPassword(imageViewShowPassword, textInputEditTextPassword)

        // função que faz o dialog de data aparecer
        dateOfBirthDialog(dateOfBirthEditText)

        // função que faz o cadastro da pessoa no banco de dados quando o botão é clicado
        buttonRegisterClick(buttonSingUp)

        // função que faz voltar a tela de login
        btBack.setOnClickListener {
                findNavController().navigate(R.id.action_singUpFragment_to_loginFragment)
        }

        val spinnerUF: Spinner = binding.spinnerUF
        val ufs = listOf(
            "Acre (AC)",
            "Alagoas (AL)",
            "Amapá (AP)",
            "Amazonas (AM)",
            "Bahia (BA)",
            "Ceará (CE)",
            "Distrito Federal (DF)",
            "Espírito Santo (ES)",
            "Goiás (GO)",
            "Maranhão (MA)",
            "Mato Grosso (MT)",
            "Mato Grosso do Sul (MS)",
            "Minas Gerais (MG)",
            "Pará (PA)",
            "Paraíba (PB)",
            "Paraná (PR)",
            "Pernambuco (PE)",
            "Piauí (PI)",
            "Rio de Janeiro (RJ)",
            "Rio Grande do Norte (RN)",
            "Rio Grande do Sul (RS)",
            "Rondônia (RO)",
            "Roraima (RR)",
            "Santa Catarina (SC)",
            "São Paulo (SP)",
            "Sergipe (SE)",
            "Tocantins (TO)"
        )

        val adapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, ufs) }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUF.adapter = adapter

        return binding.root
    }

    private fun validarCampos(email: String, pass: String, cpf: String, city: String, nasc: String, name: String): String? {
        return when {
            email.isBlank() -> "O campo de email está vazio!"
            pass.isBlank() -> "O campo de senha está vazio!"
            cpf.isBlank() -> "O campo de CPF está vazio!"
            city.isBlank() -> "O campo de cidade está vazio!"
            nasc.isBlank() -> "O campo de data de nascimento está vazio!"
            name.isBlank() -> "O campo de nome está vazio!"
            else -> null
        }
    }

    private fun buttonRegisterClick(buttonSingUp: Button) {
        buttonSingUp.setOnClickListener {
            val email = binding.txtEditEmail.text.toString()
            val pass = binding.txtEditPassword.text.toString()
            val cpf = binding.txtEditCPF.text.toString()
            val city = binding.txtEditCity.text.toString()
            val uf = binding.spinnerUF.selectedItem?.toString() ?: ""
            val nasc = binding.txtEditNasc.text.toString()
            val name = binding.txtEditName.text.toString()

            // Verificar se os campos estão em branco
            val mensagemErroCampos = validarCampos(email, pass, cpf, city, nasc, name)

            if (mensagemErroCampos != null) {
                // Mostrar mensagem de erro se houver campos vazios
                Toast.makeText(context, mensagemErroCampos, Toast.LENGTH_SHORT).show()
            } else {
                // Proseguir com o cadastro
                firebaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Conta Criada Com Sucesso!", Toast.LENGTH_SHORT).show()

                            val usuariosMap = hashMapOf(
                                "nome" to name,
                                "city" to city,
                                "email" to email,
                                "nasc" to nasc,
                                "uf" to uf,
                            )

                            val userId = firebaseAuth.currentUser!!.uid

                            db.collection("usuarios").document(userId).set(usuariosMap)
                                .addOnCompleteListener {
                                    Log.d("db", "Sucesso ao salvar os dados no banco")
                                }.addOnFailureListener {
                                    Log.d("db", it.toString())
                                }

                            // Limpar os campos após cadastro
                            binding.txtEditEmail.setText("")
                            binding.txtEditCPF.setText("")
                            binding.txtEditCity.setText("")
                            binding.txtEditName.setText("")
                            binding.txtEditNasc.setText("")
                            binding.txtEditPassword.setText("")

                            findNavController().navigate(R.id.action_singUpFragment_to_loginFragment)
                        }
                    }.addOnFailureListener { exception ->
                        val mensagemErro = when (exception) {
                            is FirebaseAuthWeakPasswordException -> "Digite uma senha com 6 Caracteres no mínimo!"
                            is FirebaseAuthInvalidCredentialsException -> "Digite um email válido!"
                            is FirebaseAuthUserCollisionException -> "Essa conta já foi cadastrada!"
                            is FirebaseNetworkException -> "Verifique a conexão com a Internet e tente novamente"
                            else -> "Erro ao Cadastrar usuário!"
                        }
                        Toast.makeText(context, mensagemErro, Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun dateOfBirthDialog(dateOfBirthEditBox: LinearLayout) {

        val dateOfBirthEditText = binding.txtEditNasc
        dateOfBirthEditBox.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, month, dayOfMonth)
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    dateOfBirthEditText.text = dateFormat.format(selectedDate.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
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





