package com.saudeconnectapp.screens_send_picture

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.saudeconnectapp.R
import com.saudeconnectapp.adapters.SendPictureAdapter
import com.saudeconnectapp.databinding.FragmentSendPictureBinding

class SendPictureFragment : Fragment() {

    private lateinit var binding: FragmentSendPictureBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSendPictureBinding.inflate(layoutInflater, container, false)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        db = FirebaseFirestore.getInstance()

        val adapter = SendPictureAdapter(this)
        binding.viewPager.adapter = adapter

        binding.btnFinished.setOnClickListener {
            savePhoto()
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Foto de Perfil"
                1 -> tab.text = "Cartão SUS"
                2 -> tab.text = "Carteira de Vacinação"
            }
        }.attach()

        return binding.root
    }

    private fun savePhoto() {
        val userId = auth.currentUser?.uid ?: return
        val storageRef = storage.reference

        val adapter = binding.viewPager.adapter as SendPictureAdapter
        val fotoPerfilFragment = adapter.getFragment(0) as? FirstSendPicturePerfilFragment
        val fotoSusFragment = adapter.getFragment(1) as? SecondSendPictureSusFragment
        val fotoCardVaccinationFragment = adapter.getFragment(2) as? ThirdSendPictureVaccinationCard

        val uriPerfil = fotoPerfilFragment?.getFotoPerfil()
        val uriSus = fotoSusFragment?.getFotoSusUri()
        val uriCardVac = fotoCardVaccinationFragment?.getFotoSusUri()

        val uploadTasks = mutableListOf<Task<*>>()

        uriPerfil?.let { uri ->
            val perfilRef = storageRef.child("fotosPerfil/$userId/perfil.jpg")
            val uploadTask = perfilRef.putFile(uri).addOnSuccessListener {
                    perfilRef.downloadUrl.addOnSuccessListener { url ->
                        salvarUrlNoFirestore(url.toString(), "fotoPerfilUrl")
                    }.addOnFailureListener { exception ->
                        Log.e(
                            "Firebase", "Erro ao obter URL da foto de perfil: ${exception.message}"
                        )
                    }
                }.addOnFailureListener { exception ->
                    Log.e(
                        "Firebase", "Erro ao fazer upload da foto de perfil: ${exception.message}"
                    )
                }
            uploadTasks.add(uploadTask)
        }

        uriSus?.let { uri ->
            val susRef = storageRef.child("fotosSus/$userId/sus.jpg")
            val uploadTask = susRef.putFile(uri).addOnSuccessListener {
                    susRef.downloadUrl.addOnSuccessListener { url ->
                        salvarUrlNoFirestore(url.toString(), "fotoSusUrl")
                    }
                }.addOnFailureListener {
                    Log.e("Upload Error", "Erro ao fazer upload do cartão SUS", it)
                }
            uploadTasks.add(uploadTask)
        }

        uriCardVac?.let { uri ->
            val cardVacRef = storageRef.child("fotosCardVaccination/$userId/vaccination.jpg")
            val uploadTask = cardVacRef.putFile(uri).addOnSuccessListener {
                    cardVacRef.downloadUrl.addOnSuccessListener { url ->
                        salvarUrlNoFirestore(url.toString(), "fotoCardVacUrl")
                    }
                }.addOnFailureListener {
                    Log.e("Upload Error", "Erro ao fazer upload do cartão de vacinação", it)
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
        val db = FirebaseFirestore.getInstance()

        val userRef = db.collection("usuarios").document(userId)
        userRef.update("isProfileComplete", true)
            .addOnSuccessListener {
                // Navegar para o HomeFragment após completar o upload
                findNavController().navigate(R.id.action_sendPictureFragment_to_mainFragment)
            }
            .addOnFailureListener { e ->
                Log.e("PhotoUpload", "Erro ao atualizar o perfil", e)
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

    private fun navigateTo(fragment: Fragment) {
        childFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }

}