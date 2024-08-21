package com.saudeconnectapp.screens

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.saudeconnectapp.R
import com.saudeconnectapp.adapters.ProfessionalAdaptaer
import com.saudeconnectapp.databinding.FragmentScheduleBinding
import com.saudeconnectapp.model.CarrosselPerfil
import com.saudeconnectapp.model.PerfilProfessional
class ScheduleFragment : Fragment() {

    private lateinit var binding: FragmentScheduleBinding
    private lateinit var perfilProfessionalAdapter: ProfessionalAdaptaer
    private val listaProfessionalPerfil: MutableList<PerfilProfessional> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar o layout para este fragmento
        binding = FragmentScheduleBinding.inflate(inflater, container, false)

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
            R.drawable.avatar,
            "José da Silva",
            "Dentista",
            R.id.btnSchedule
        )
        listaProfessionalPerfil.add(cardOne)

        val cardTwo = PerfilProfessional(
            R.drawable.avatar,
            "Maria Guilhermina",
            "Pediatra"
        )
        listaProfessionalPerfil.add(cardTwo)

        val cardThree = PerfilProfessional(
            R.drawable.avatar,
            "Jonas J. Marson",
            "Dentista"
        )
        listaProfessionalPerfil.add(cardThree)

        val cardFour = PerfilProfessional(
            R.drawable.avatar,
            "João Pedro",
            "Clinico Geral"
        )
        listaProfessionalPerfil.add(cardFour)

        val cardFive = PerfilProfessional(
            R.drawable.avatar,
            "Deivid V. Lima",
            "Clinico Geral"
        )
        listaProfessionalPerfil.add(cardFive)
    }
}
