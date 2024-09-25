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
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.saudeconnectapp.R
import com.saudeconnectapp.databinding.FragmentSaveMedicalBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SaveMedicalFragment : Fragment() {


    private lateinit var binding: FragmentSaveMedicalBinding
    private var uriImage: Uri? = null
    private lateinit var firestore: FirebaseFirestore
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSaveMedicalBinding.inflate(layoutInflater, container, false)

        firestore = FirebaseFirestore.getInstance()

        binding.buttonChooseImage.setOnClickListener {
            openGallery()
        }

        binding.buttonSaveMedical.setOnClickListener {
            if (saveVaccine()) { // Chama a função de salvar
                // A navegação agora ocorre apenas após o salvamento bem-sucedido
            }

        }

        binding.imgBack.setOnClickListener{
            findNavController().popBackStack()
        }

        val dateOfBirthEditText = binding.boxEditDate
        dateOfBirthDialog(dateOfBirthEditText)


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

    private fun saveVaccine(): Boolean {

        val title = binding.txtEditTitleMedical.text.toString()
        val dose = binding.txtEditDose.text.toString()
        val date = binding.txtEditDate.text.toString()
        val freq = binding.txtEditFreq.text.toString()
        val duration = binding.txtEditDuration.text.toString()


        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return false

        if (title.isEmpty() || date.isEmpty() || freq.isEmpty() || duration.isEmpty() || dose.isEmpty()) {
            Toast.makeText(context, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return false
        }

        val noteData = hashMapOf(
            "title" to title,
            "date" to date,
            "dose" to dose,
            "freq" to freq,
            "duration" to duration,
            "userId" to userId,
            "imageUri" to (uriImage?.toString() ?: "") // Verificação adicional
        )

        Log.d("SaveVaccineFragment", "Salvando Medicamento: $noteData")

        firestore.collection("medical").add(noteData).addOnSuccessListener {
            Toast.makeText(context, "Medicamento salva com sucesso!", Toast.LENGTH_SHORT).show()
            val result = Bundle()
            result.putString("lastMedicalName", title)
            setFragmentResult("requestKey", result)
            findNavController().popBackStack()

        }.addOnFailureListener { e ->
            Toast.makeText(context, "Erro ao salvar a Medicamento: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("SaveMedicalFragment", "Erro ao salvar: ${e.message}")
        }

        return true
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
}