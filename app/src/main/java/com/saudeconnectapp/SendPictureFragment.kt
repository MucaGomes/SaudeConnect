package com.saudeconnectapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.saudeconnectapp.databinding.FragmentSendPictureBinding

class SendPictureFragment : Fragment() {

    private lateinit var binding: FragmentSendPictureBinding
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null


    private val storageReference = FirebaseStorage.getInstance().reference
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSendPictureBinding.inflate(layoutInflater, container, false)

        binding.btnEditPicture.setOnClickListener { abrirGaleria() }

        // Configurar o botão para enviar a imagem
        binding.btnSendPicture.setOnClickListener {
            imageUri?.let {
                uploadImagemParaFirebase(it)
            } ?: Toast.makeText(context, "Selecione uma imagem primeiro", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    // Método para abrir a galeria
    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Recebe a imagem selecionada
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            binding.imgAvatar.setImageURI(imageUri)
        }
    }

    // Faz o upload da imagem para o Firebase Storage
    private fun uploadImagemParaFirebase(uri: Uri) {
        val userId = auth.currentUser?.uid ?: return
        val fileRef = storageReference.child("fotosPerfil/$userId.jpg")

        fileRef.putFile(uri)
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { url ->
                    salvarUrlNoFirestore(url.toString())
                }
            }
            .addOnFailureListener {
                Log.e("ErroS: ", it.toString())
                Toast.makeText(context, "Falha ao enviar a imagem", Toast.LENGTH_SHORT).show()
            }
    }


    private fun salvarUrlNoFirestore(url: String) {
        val userId = auth.currentUser?.uid

        userId?.let {
            val userDocRef = firestore.collection("Usuários").document(it)
            userDocRef.update("fotoUrl", url)
                .addOnSuccessListener {
                    Toast.makeText(context, "Imagem salva com sucesso!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Erro ao salvar a URL da imagem", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

}