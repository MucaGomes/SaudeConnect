package com.saudeconnectapp.button_dialog_screens

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.saudeconnectapp.FullScreenImageCnsActivity
import com.saudeconnectapp.R
import com.saudeconnectapp.databinding.ScreenDialogCnsBinding
import jp.wasabeef.blurry.Blurry


class CnsBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: ScreenDialogCnsBinding
    private var uriPictureCns: Uri? = null
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private val usuarioId = FirebaseAuth.getInstance().currentUser!!.uid
    private lateinit var db: FirebaseFirestore

    private var imageUrl: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = ScreenDialogCnsBinding.inflate(layoutInflater, container, false)


        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.imgSend.setOnClickListener {
            openGallery()
        }

        binding.txtAlterar.setOnClickListener {
            openGallery()
        }

        binding.btnSave.setOnClickListener {
            savePhoto()
        }

        binding.imgSend.setOnClickListener {
            val intent = Intent(requireContext(), FullScreenImageCnsActivity::class.java)
            intent.putExtra("imageUrl", imageUrl) // Passe a URL da imagem que você carregou
            startActivity(intent)
        }

        loadUserAvatarPefil()
        return binding.root

    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun savePhoto() {
        val userId = auth.currentUser?.uid ?: return
        val storageRef = storage.reference

        val uriCns = getPictureCns()
        val uploadTasks = mutableListOf<Task<*>>()


        uriCns?.let { uri ->
            val perfilRef = storageRef.child("fotosSus/$userId/sus.jpg")
            val uploadTask = perfilRef.putFile(uri).addOnSuccessListener {
                perfilRef.downloadUrl.addOnSuccessListener { url ->
                    salvarUrlNoFirestore(url.toString(), "fotoSUSUrl")
                }.addOnFailureListener { exception ->
                    Log.e(
                        "Firebase",
                        "Erro ao obter URL da foto do cartão do SUS: ${exception.message}"
                    )
                }
            }.addOnFailureListener { exception ->
                Log.e(
                    "Firebase",
                    "Erro ao fazer upload da foto do cartão do SUS: ${exception.message}"
                )
            }
            uploadTasks.add(uploadTask)
        }

        // Atualizar o campo fotosEnviadas no Firestore
        Tasks.whenAllComplete(uploadTasks).addOnCompleteListener {
            db.collection("usuarios").document(userId).update("fotosEnviadas", true)
                .addOnSuccessListener {
                    updateProfileCompleteStatus()
                }.addOnFailureListener {
                    Log.e("Firestore Update Error", "Erro ao atualizar o status no Firestore", it)
                }
        }
    }


    private fun updateProfileCompleteStatus() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userRef = db.collection("usuarios").document(userId)
        userRef.update("hasCompletedPhotoUpload", true).addOnSuccessListener {
            Toast.makeText(context, "Aguarde, enquanto atualizamos...", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Log.e("PhotoUpload", "Error updating document", e)
        }
    }

    private fun salvarUrlNoFirestore(url: String, campo: String) {
        val userId = auth.currentUser?.uid ?: return
        val updates = mapOf(campo to url)
        db.collection("usuarios").document(userId).set(updates, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("Firestore", "$campo salvo com sucesso")
            }.addOnFailureListener { exception ->
                Log.e("Firestore", "Erro ao salvar $campo: ${exception.message}")
            }
    }

    private fun getPictureCns(): Uri? {
        return uriPictureCns
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // Remova o desfoque ao fechar o BottomSheet
        Blurry.delete(requireActivity().findViewById(R.id.background_view))
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            uriPictureCns = data?.data
            binding.imgSend.setImageURI(uriPictureCns)

            // Chama o método para salvar a nova imagem no Firebase Storage e Firestore
            savePhoto()
        }
    }

    private fun loadUserAvatarPefil() {
        val userId = auth.currentUser?.uid ?: return
        val userRef = db.collection("usuarios").document(userId)

        userRef.get().addOnSuccessListener { documentSnapshot ->
            imageUrl = documentSnapshot.getString("fotoSUSUrl")
            if (!imageUrl.isNullOrEmpty()) {
                if (isAdded && view != null) {
                    Glide.with(requireContext()).load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                        .into(binding.imgSend)
                }
            }
        }.addOnFailureListener {
            Log.e("Firestore", "Erro ao carregar a imagem do CNS: ${it.message}")
        }
    }


}
