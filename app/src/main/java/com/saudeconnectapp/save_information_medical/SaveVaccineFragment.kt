package com.saudeconnectapp.save_information_medical

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.saudeconnectapp.databinding.FragmentSaveaccineBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SaveVaccineFragment : Fragment() {

    private lateinit var binding: FragmentSaveaccineBinding
    private var uriImage: Uri? = null
    private lateinit var firestore: FirebaseFirestore
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSaveaccineBinding.inflate(layoutInflater, container, false)

        firestore = FirebaseFirestore.getInstance()

        binding.buttonChooseImage.setOnClickListener {
            openGallery()
        }

        binding.buttonSaveVaccine.setOnClickListener {
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

    private fun setupSpinners() {
        spinnerUf()
        spinnerDose()
        spinnerVaccine()
    }

    private fun spinnerVaccine() {
        val spinnerUF: Spinner = binding.spinnerNameVaccine
        val ufs = listOf(
            "BCG",
            "Hepatite A",
            "Hepatite B",
            "Penta (DTP/Hib/Hep. B)",
            "Pneumocócica 10 valente",
            "Vacina Inativada Poliomielite (VIP)",
            "Vacina Oral Poliomielite (VOP)",
            "Vacina Rotavírus Humano (VRH)",
            "Meningocócica C (conjugada)",
            "Febre amarela",
            "Tríplice viral",
            "Tetraviral",
            "DTP (tríplice bacteriana)",
            "Varicela",
            "HPV quadrivalente",
            "dT (dupla adulto)",
            "dTpa (DTP adulto)",
            "Menigocócica ACWY"
        )

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ufs)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUF.adapter = adapter
    }

    private fun spinnerDose() {
        val spinnerUF: Spinner = binding.spinnerDose
        val ufs = listOf("1ª Dose", "2ª Dose", "3ª Dose")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ufs)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUF.adapter = adapter
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

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            uriImage = data.data
            binding.imageView.setImageURI(uriImage) // Atualiza a ImageView com a imagem escolhida
        }
    }

    private fun saveVaccine(): Boolean {
        val title = binding.spinnerNameVaccine.selectedItem?.toString() ?: ""
        val dose = binding.spinnerDose.selectedItem?.toString() ?: ""
        val date = binding.txtEditDate.text.toString()
        val manufacturer = binding.txtEditManufacturer.text.toString()
        val city = binding.txtEditCity.text.toString()
        val uf = binding.spinnerUf.selectedItem?.toString() ?: ""
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return false

        if (title.isEmpty() || dose.isEmpty() || date.isEmpty() || manufacturer.isEmpty() || city.isEmpty() || uf.isEmpty()) {
            Toast.makeText(context, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return false
        }

        val noteData = hashMapOf(
            "title" to title,
            "dose" to dose,
            "date" to date,
            "manufacturer" to manufacturer,
            "city" to city,
            "uf" to uf,
            "userId" to userId,
            "imageUri" to (uriImage?.toString() ?: "") // Verificação adicional
        )

        Log.d("SaveVaccineFragment", "Salvando vacina: $noteData")

        firestore.collection("vaccine").add(noteData).addOnSuccessListener {
            Toast.makeText(context, "Vacina salva com sucesso!", Toast.LENGTH_SHORT).show()
            val result = Bundle()
            result.putString("lastVaccineName", title) // Substitua 'vaccineName' pelo nome da vacina salva
            setFragmentResult("requestKey", result)
            findNavController().popBackStack()

        }.addOnFailureListener { e ->
            Toast.makeText(context, "Erro ao salvar a vacina: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("SaveVaccineFragment", "Erro ao salvar: ${e.message}")
        }

        return true
    }

}
