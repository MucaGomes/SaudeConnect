package com.saudeconnectapp.screens

import CardPerfilAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.clonemercadolivre.adapter.com.saudeconnectapp.model.CarrosselBot
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.saudeconnectapp.HomeLoginFragment
import com.saudeconnectapp.MainActivity
import com.saudeconnectapp.R
import com.saudeconnectapp.adapters.CardAdapter
import com.saudeconnectapp.adapters.CardAdapterBot
import com.saudeconnectapp.databinding.FragmentHomeBinding
import com.saudeconnectapp.databinding.FragmentPerfilBinding
import com.saudeconnectapp.model.CarrosselPerfil
import com.saudeconnectapp.model.CarrosselTop

class PerfilFragment : Fragment() {

    private lateinit var binding: FragmentPerfilBinding

    private lateinit var cardAdapterPerfil: CardPerfilAdapter

    private val listaCarrosselPerfil: MutableList<CarrosselPerfil> = mutableListOf()

    private val usuarioId = FirebaseAuth.getInstance().currentUser!!.uid
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPerfilBinding.inflate(layoutInflater, container, false)

        setupRecyclerViewCarroselPerfil()

        auth = FirebaseAuth.getInstance()

        binding.btnGoEditPerfil.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_mainFragment_to_editPerfilFragment)
        }

        binding.btnLogoff.setOnClickListener {
            logout()
            restartApp()
        }

        return binding.root
    }

    private fun restartApp() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish() // Opcional: fecha a atividade atual
    }

    private fun logout() {
        auth.signOut() // Verifica se o fragmento está anexado antes de tentar navegar
    }

    private fun navigationScreens(btNavb: BottomNavigationView) {
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
    }

    private fun setupRecyclerViewCarroselPerfil() {
        val recyclerViewProdutosCarrosel = binding.rvlCarrosselPerfil
        recyclerViewProdutosCarrosel.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewProdutosCarrosel.setHasFixedSize(true)

        cardAdapterPerfil = context?.let { CardPerfilAdapter(it, listaCarrosselPerfil) }!!
        recyclerViewProdutosCarrosel.adapter = cardAdapterPerfil
        getCardsCarroselPerfil()
    }


    private fun getCardsCarroselPerfil() {
        val cardOne = CarrosselPerfil(
            2,
            "Vacinas Salvas",
            "• Vazio",
            "• Vazio",
            "",
            R.drawable.background_fundo_perfil_card_one,
            R.id.btnAddPerfilCard,
            "Últimas Vacinas Salvas: "
        )

        listaCarrosselPerfil.add(cardOne)

        val cardTwo = CarrosselPerfil(
            2,
            "Exames Salvos",
            "• Vazio",
            "• Vazio",
            "",
            R.drawable.background_fundo_perfil_card_two,
            R.id.btnAddPerfilCard,
            "Útimos Exames salvos: "

        )
        listaCarrosselPerfil.add(cardTwo)
    }

    override fun onStart() {
        super.onStart()

        val documentRef = db.collection("usuarios").document(usuarioId)

        documentRef.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->
            if (snapshot != null) {
                binding.nameUser.text = snapshot.getString("nome")
                binding.emailUser.text = snapshot.getString("email")
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUserAvatarPefil()
    }

    private fun loadUserAvatarPefil() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userRef = FirebaseFirestore.getInstance().collection("usuarios").document(userId)

        userRef.get().addOnSuccessListener { documentSnapshot ->
            val imageUrl = documentSnapshot.getString("fotoPerfilUrl")
            if (!imageUrl.isNullOrEmpty()) {
                // Certifique-se de que o fragmento ainda está anexado e visível
                if (isAdded && activity != null) {
                    Glide.with(requireContext()).load(imageUrl)
                        .circleCrop() // Para deixar a imagem redonda
                        .into(binding.imgAvatar)
                }
            }
        }.addOnFailureListener {
            // Tratar possíveis erros na requisição do Firestore
            Log.e("PerfilFragment", "Erro ao carregar avatar: ${it.message}")
        }
    }

}
