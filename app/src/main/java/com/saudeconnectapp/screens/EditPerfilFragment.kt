package com.saudeconnectapp.screens

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.saudeconnectapp.MainActivity
import com.saudeconnectapp.R
import com.saudeconnectapp.button_dialog_screens.CardConvBottomSheetDialogFragment
import com.saudeconnectapp.button_dialog_screens.CnsBottomSheetDialogFragment
import com.saudeconnectapp.databinding.FragmentEditPerfilBinding
import jp.wasabeef.blurry.Blurry

class EditPerfilFragment : Fragment() {

    private lateinit var binding: FragmentEditPerfilBinding
    private var uriFotoPerfil: Uri? = null
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private val usuarioId = FirebaseAuth.getInstance().currentUser!!.uid
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditPerfilBinding.inflate(layoutInflater, container, false)

        // Inicializar FirebaseAuth, FirebaseStorage e FirebaseFirestore
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.imgEditPhotoPefil.setOnClickListener {
            openGallery()
        }

        binding.viewLogout.setOnClickListener {
            logout()
            restartApp()
        }

        binding.backToPerfil.setOnClickListener {
            findNavController().navigate(R.id.action_editPerfilFragment_to_mainFragment)
        }

        binding.cardCns.setOnClickListener {
            Blurry.with(context).radius(10) // Define a intensidade do desfoque
                .sampling(2) // Ajuste para performance e qualidade
                .async() // Executa a operação de desfoque em uma thread separada
                .onto(binding.backgroundView)

            val bottomSheet = CnsBottomSheetDialogFragment()
            bottomSheet.show(parentFragmentManager, "NotificationBottomSheet")
        }

        binding.cardCarteiraConvenio.setOnClickListener {
            Blurry.with(context).radius(10) // Define a intensidade do desfoque
                .sampling(2) // Ajuste para performance e qualidade
                .async() // Executa a operação de desfoque em uma thread separada
                .onto(binding.backgroundView)

            val bottomSheet = CardConvBottomSheetDialogFragment()
            bottomSheet.show(parentFragmentManager, "NotificationBottomSheet")
        }

        binding.cardAlterarSenha.setOnClickListener {
            Toast.makeText(context, "Em desenvolvimento", Toast.LENGTH_SHORT).show()
        }
        binding.cardEditarDados.setOnClickListener {
            Toast.makeText(context, "Em desenvolvimento", Toast.LENGTH_SHORT).show()
        }
        binding.cardInfosClinicas.setOnClickListener {
            Toast.makeText(context, "Em desenvolvimento", Toast.LENGTH_SHORT).show()
        }

        binding.btnSalvar.setOnClickListener {
            Toast.makeText(context, "Salvando informações...", Toast.LENGTH_SHORT).show()
            savePhoto()
        }

        return binding.root
    }

    private fun restartApp() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish() // fecha a atividade atual
    }

    private fun logout() {
        auth.signOut()

    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onStart() {
        super.onStart()
        val documentRef = db.collection("usuarios").document(usuarioId)

        documentRef.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->
            if (snapshot != null) {
                binding.txtNameUser.text = snapshot.getString("nome")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            uriFotoPerfil = data.data
            // Atualizar a imagem no ImageView com Glide
            uriFotoPerfil?.let { uri ->
                Glide.with(this).load(uri).into(binding.imgAvatar)
                // Após a seleção da imagem, já inicia o upload
                savePhoto()  // Salva a foto logo após a seleção da galeria
            }
        }
    }

    private fun savePhoto() {
        val userId = auth.currentUser?.uid ?: return
        val storageRef = storage.reference

        val uriPerfil = getFotoPerfil()
        val uploadTasks = mutableListOf<Task<*>>()

        uriPerfil?.let { uri ->
            val perfilRef = storageRef.child("fotosPerfil/$userId/perfil.jpg")
            val uploadTask = perfilRef.putFile(uri).addOnSuccessListener {
                perfilRef.downloadUrl.addOnSuccessListener { url ->
                    salvarUrlNoFirestore(url.toString(), "fotoPerfilUrl")

                    // Atualizar a imagem localmente após o upload
                    context?.let {
                        Glide.with(it).load(url).circleCrop() // Para manter a imagem redonda
                            .into(binding.imgAvatar)
                    }
                }.addOnFailureListener { exception ->
                    Log.e("Firebase", "Erro ao obter URL da foto de perfil: ${exception.message}")
                }
            }.addOnFailureListener { exception ->
                Log.e("Firebase", "Erro ao fazer upload da foto de perfil: ${exception.message}")
            }
            uploadTasks.add(uploadTask)
        }

        Tasks.whenAllComplete(uploadTasks).addOnCompleteListener {
            db.collection("usuarios").document(userId).update("fotosEnviadas", true)
                .addOnSuccessListener {
                    Toast.makeText(context, "Informações atualizadas com sucesso!", Toast.LENGTH_SHORT)
                        .show()
                }.addOnFailureListener {
                    Log.e("Firestore Update Error", "Erro ao atualizar o status no Firestore", it)
                }
        }
    }

    override fun onResume() {
        super.onResume()
        loadUserAvatarPefil()
    }


    private fun updateProfileCompleteStatus() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userRef = db.collection("usuarios").document(userId)
        userRef.update("hasCompletedPhotoUpload", true).addOnSuccessListener {
            Toast.makeText(context, "Aguarde, enquanto atualizamos...", Toast.LENGTH_SHORT).show()

            // Envia um sinal para a tela anterior atualizar a foto de perfil
            val result = Bundle().apply {
                putBoolean("profileUpdated", true)
            }
            setFragmentResult("profileUpdateKey", result) // Envia o resultado

            findNavController().navigate(R.id.action_editPerfilFragment_to_mainFragment)
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

    private fun loadUserAvatarPefil() {
        val userRef = db.collection("usuarios").document(usuarioId)

        userRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val fotoPerfilUrl = documentSnapshot.getString("fotoPerfilUrl")
                val fotoSusUrl = documentSnapshot.getString("fotoSusUrl")

                if (fotoPerfilUrl != null) {
                    Log.d("Firestore", "URL da foto de perfil: $fotoPerfilUrl")
                    // Carregar a imagem usando uma biblioteca como Glide ou Picasso

                    if (isAdded && view != null) {
                        context?.let {
                            Glide.with(it).load(fotoPerfilUrl)
                                .circleCrop() // Para deixar a imagem redonda
                                .into(binding.imgAvatar)
                        }
                    }
                } else {
                    Log.e("Firestore", "Campo 'fotoPerfilUrl' está null")
                }

                if (fotoSusUrl != null) {
                    Log.d("Firestore", "URL da foto SUS: $fotoSusUrl")
                } else {
                    Log.e("Firestore", "Campo 'fotoSusUrl' está null")
                }
            } else {
                Log.e("Firestore", "Documento não encontrado!")
            }
        }.addOnFailureListener { exception ->
            Log.e("Firestore", "Erro ao acessar o documento: ${exception.message}")
        }
    }

    private fun getFotoPerfil(): Uri? {
        return uriFotoPerfil
    }
}
