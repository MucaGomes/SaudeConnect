package com.saudeconnectapp.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.saudeconnectapp.R
import com.saudeconnectapp.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        checkUserProfile()
        val btNavb = binding.bottomNavigation

        // Set the initial fragment
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment())
                .commit()

        }

        btNavb.setOnNavigationItemSelectedListener {
            var selectedFragment: Fragment = HomeFragment()

            when (it.itemId) {
                R.id.bottom_home -> selectedFragment = HomeFragment()
                R.id.bottom_schedule -> selectedFragment = ScheduleFragment()
                R.id.bottom_message -> selectedFragment = MessageFragment()
                R.id.bottom_map -> selectedFragment = MapFragment()
                R.id.bottom_perfil -> selectedFragment = PerfilFragment()
            }

            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment).commit()

            true
        }



        return binding.root
    }

    override fun onStart() {
        super.onStart()
        checkUserPhotoAndRedirect()
    }

    private fun checkUserPhotoAndRedirect() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        val userRef = db.collection("usuarios").document(userId)
        userRef.get().addOnSuccessListener { document ->
            if (document != null) {
                val hasCompletedPhotoUpload = document.getBoolean("hasCompletedPhotoUpload") ?: false
                if (!hasCompletedPhotoUpload) {
                    redirectToPageView()
                }
            } else {
                Log.d("UserCheck", "No such document")
            }
        }.addOnFailureListener { e ->
            Log.d("UserCheck", "Get failed with ", e)
        }
    }

    private fun redirectToPageView() {
        findNavController().navigate(R.id.action_mainFragment_to_sendPictureFragment)
    }


    private fun checkUserProfile() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("usuarios").document(userId).get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val isProfileComplete = document.getBoolean("isProfileComplete") ?: false

                    if (!isProfileComplete) {
                        // Se o perfil não estiver completo, redireciona para o fragmento de envio de imagens
                        findNavController().navigate(R.id.action_mainFragment_to_sendPictureFragment)
                    }
                }
            }.addOnFailureListener { exception ->
                // Trate erros aqui, como exibir uma mensagem para o usuário
                Log.e("HomeFragment", "Erro ao verificar o perfil: ${exception.message}")
            }

    }

}