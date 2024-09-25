package com.saudeconnectapp.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.saudeconnectapp.button_dialog_screens.MessageDialogFragment
import com.saudeconnectapp.R
import com.saudeconnectapp.adapters.ChatProfessionalsAdaptaer
import com.saudeconnectapp.databinding.FragmentMessageBinding
import com.saudeconnectapp.model.ChatProfessional

class MessageFragment : Fragment() {

    private lateinit var binding: FragmentMessageBinding

    private lateinit var perfilProfessionalAdapter: ChatProfessionalsAdaptaer
    private val listaProfessionalPerfil: MutableList<ChatProfessional> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMessageBinding.inflate(layoutInflater, container, false)

        // Exibe o DialogFragment quando o fragmento é aberto
        MessageDialogFragment().show(parentFragmentManager, "MessageDialog")

        setupRecyclerViewProfessionalList()

        val btNavb = binding.bottomNavigation

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


    private fun setupRecyclerViewProfessionalList() {
        val rvlPerfilProfessionals = binding.rvlProfessional
        rvlPerfilProfessionals.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvlPerfilProfessionals.setHasFixedSize(true)

        // Criação do adapter e configuração
        context?.let { ctx ->
            perfilProfessionalAdapter = ChatProfessionalsAdaptaer(ctx, listaProfessionalPerfil)
            rvlPerfilProfessionals.adapter = perfilProfessionalAdapter
        }

        // Adiciona os dados
        getProfessionals()
        // Notifica o adapter sobre as mudanças nos dados
        perfilProfessionalAdapter.notifyDataSetChanged()
    }

    private fun getProfessionals() {
        val cardOne = ChatProfessional(
            R.drawable.prof_one,
            "José da Silva",
            "A sua consulta está confirmada. Nos vemos amanhã!",
            R.id.btnQntMessage,
            "Há 3 min"
        )
        listaProfessionalPerfil.add(cardOne)

        val cardTwo = ChatProfessional(
            R.drawable.prof_two,
            "Maria Guilhermina",
            "Verifique seu e-mail para informações adicionais.",
            R.id.btnQntMessage,
            "Há 50 min"
        )
        listaProfessionalPerfil.add(cardTwo)
    }
}