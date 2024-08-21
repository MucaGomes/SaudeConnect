package com.saudeconnectapp.screens

import CardPerfilAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.clonemercadolivre.adapter.com.saudeconnectapp.model.CarrosselBot
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.saudeconnectapp.HomeLoginFragment
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPerfilBinding.inflate(layoutInflater, container, false)

        setupRecyclerViewCarroselPerfil()

        binding.btnGoEditPerfil.setOnClickListener{
            val navController = findNavController()
            navController.navigate(R.id.action_mainFragment_to_editPerfilFragment)
        }

        return binding.root
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
            "• Influenza",
            "• Vacina contra hepatite B",
            "",
            R.drawable.background_fundo_perfil_card_one,
            R.id.btnAddPerfilCard,
            "Últimas Vacinas Salvas: "
        )

        listaCarrosselPerfil.add(cardOne)

        val cardTwo = CarrosselPerfil(
            2,
            "Exames Salvos",
            "• Colesterol",
            "• Glicemia",
            "",
            R.drawable.background_fundo_perfil_card_two,
            R.id.btnAddPerfilCard,
            "Útimos Exames salvos: "

        )
        listaCarrosselPerfil.add(cardTwo)
    }

    override fun onStart() {
        super.onStart()

        val documentRef = db.collection("Usuários").document(usuarioId)

        documentRef.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->
            if (snapshot != null) {
                binding.nameUser.text = snapshot.getString("nome")
                binding.emailUser.text = snapshot.getString("email")
            }

        }
    }


}
