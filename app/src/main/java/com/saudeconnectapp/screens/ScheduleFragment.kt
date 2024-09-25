package com.saudeconnectapp.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.saudeconnectapp.button_dialog_screens.MessageDialogFragment
import com.saudeconnectapp.R
import com.saudeconnectapp.adapters.ProfessionalAdaptaer
import com.saudeconnectapp.databinding.FragmentScheduleBinding
import com.saudeconnectapp.model.PerfilProfessional
class ScheduleFragment : Fragment() {

    private lateinit var binding: FragmentScheduleBinding
    private lateinit var perfilProfessionalAdapter: ProfessionalAdaptaer
    private val listaProfessionalPerfil: MutableList<PerfilProfessional> = mutableListOf()


    private val usuarioId = FirebaseAuth.getInstance().currentUser!!.uid
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar o layout para este fragmento
        binding = FragmentScheduleBinding.inflate(inflater, container, false)


        // Exibe o DialogFragment quando o fragmento é aberto
        MessageDialogFragment().show(parentFragmentManager, "MessageDialog")

        // Configurar o RecyclerView
        setupRecyclerViewProfessionalList()

        // Configuração do BottomNavigationView
        val btNavb = binding.bottomNavigation
        btNavb.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment = HomeFragment()

            when (item.itemId) {
                R.id.bottom_home -> selectedFragment = HomeFragment()
                R.id.bottom_schedule -> selectedFragment = ScheduleFragment()
                R.id.bottom_message -> selectedFragment = MessageFragment()
                R.id.bottom_map -> selectedFragment = MapFragment()
                R.id.bottom_perfil -> selectedFragment = PerfilFragment()
            }

            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit()

            true
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadUserAvatarPefil() // Ensure the photo is updated when the fragment resumes
    }

    override fun onStart() {
        super.onStart()
        val documentRef = db.collection("usuarios").document(usuarioId)

        documentRef.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->
            if (snapshot != null) {
                binding.nameUserHome.text = snapshot.getString("nome")
            }
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

    private fun setupRecyclerViewProfessionalList() {
        val rvlPerfilProfessionals = binding.rvlProfessional
        rvlPerfilProfessionals.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvlPerfilProfessionals.setHasFixedSize(true)

        // Criação do adapter e configuração
        context?.let { ctx ->
            perfilProfessionalAdapter = ProfessionalAdaptaer(ctx, listaProfessionalPerfil)
            rvlPerfilProfessionals.adapter = perfilProfessionalAdapter
        }

        // Adiciona os dados
        getProfessionals()
        // Notifica o adapter sobre as mudanças nos dados
        perfilProfessionalAdapter.notifyDataSetChanged()
    }

    private fun getProfessionals() {
        val cardOne = PerfilProfessional(
            R.drawable.prof_one,
            "José da Silva",
            "Dentista",
            R.id.btnSchedule
        )
        listaProfessionalPerfil.add(cardOne)

        val cardTwo = PerfilProfessional(
            R.drawable.prof_two,
            "Maria Guilhermina",
            "Pediatra"
        )
        listaProfessionalPerfil.add(cardTwo)

    }
}
