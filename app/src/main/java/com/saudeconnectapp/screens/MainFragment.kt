package com.saudeconnectapp.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.saudeconnectapp.R
import com.saudeconnectapp.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        val btNavb = binding.bottomNavigation

        val navController = findNavController()
        binding.bottomNavigation.setupWithNavController(navController)

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

    private fun checkUserProfile() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("usuarios").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val isProfileComplete = document.getBoolean("isProfileComplete") ?: false

                    if (!isProfileComplete) {
                        // Verifique se o fragmento de envio de imagens não está já ativo para evitar redirecionamentos repetidos
                        val currentFragmentId = findNavController().currentDestination?.id
                        if (currentFragmentId != R.id.sendPictureFragment) {
                            findNavController().navigate(R.id.action_mainFragment_to_sendPictureFragment)
                        }
                    }
                } else {
                    // Documento não encontrado, talvez criar o documento com um perfil incompleto por padrão
                    db.collection("usuarios").document(userId)
                        .set(mapOf("isProfileComplete" to false))
                }
            }
            .addOnFailureListener { exception ->
                // Trate erros aqui, como exibir uma mensagem para o usuário
                Log.e("MainFragment", "Erro ao verificar o perfil: ${exception.message}")
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkUserProfile()
    }

}