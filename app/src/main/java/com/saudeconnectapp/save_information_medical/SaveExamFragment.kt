package com.saudeconnectapp.save_information_medical

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.saudeconnectapp.R
import com.saudeconnectapp.databinding.FragmentSaveExamBinding
import com.saudeconnectapp.databinding.FragmentSaveaccineBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class SaveExamFragment : Fragment() {

    private lateinit var binding: FragmentSaveExamBinding
    private var uriImage: Uri? = null
    private lateinit var firestore: FirebaseFirestore
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentSaveExamBinding.inflate(layoutInflater, container, false)

        firestore = FirebaseFirestore.getInstance()

        binding.buttonChooseImage.setOnClickListener {
            openGallery()
        }

        binding.buttonSaveExam.setOnClickListener {
            if (saveVaccine()) { // Chama a função de salvar
                // A navegação agora ocorre apenas após o salvamento bem-sucedido
            }

        }

        binding.imgBack.setOnClickListener{
            findNavController().popBackStack()
        }


        val dateOfBirthEditText = binding.boxEditDate
        dateOfBirthDialog(dateOfBirthEditText)

        setupSpinners()

        return binding.root
    }

    private fun setupSpinners() {
        spinnerUf()
    }

    private fun spinnerUf() {
        val spinnerUF: Spinner = binding.spinnerUf
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

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ufs)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUF.adapter = adapter
    }

    private fun dateOfBirthDialog(dateOfBirthEditBox: LinearLayout) {
        val dateOfBirthEditText = binding.txtEditDate
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

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun saveVaccine(): Boolean {

        val title = binding.txtEditTitleExam.text.toString()
        val date = binding.txtEditDate.text.toString()
        val institution = binding.txtEditInstitution.text.toString()
        val city = binding.txtEditCity.text.toString()
        val uf = binding.spinnerUf.selectedItem?.toString() ?: ""
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return false

        if (title.isEmpty() || date.isEmpty() || institution.isEmpty() || city.isEmpty() || uf.isEmpty()) {
            Toast.makeText(context, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return false
        }

        val noteData = hashMapOf(
            "title" to title,
            "date" to date,
            "manufacturer" to institution,
            "city" to city,
            "uf" to uf,
            "userId" to userId,
            "imageUri" to (uriImage?.toString() ?: "") // Verificação adicional
        )

        Log.d("SaveVaccineFragment", "Salvando exame: $noteData")

        firestore.collection("exam").add(noteData).addOnSuccessListener {
            Toast.makeText(context, "Exame salva com sucesso!", Toast.LENGTH_SHORT).show()
            val result = Bundle()
            result.putString("lastExamineName", title)
            setFragmentResult("requestKey", result)
            findNavController().popBackStack()

        }.addOnFailureListener { e ->
            Toast.makeText(context, "Erro ao salvar a Exame: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("SaveExamFragment", "Erro ao salvar: ${e.message}")
        }

        return true
    }
}