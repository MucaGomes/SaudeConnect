package com.saudeconnectapp.screens


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.app.clonemercadolivre.adapter.CardAdapter
import com.app.clonemercadolivre.adapter.com.saudeconnectapp.CardAdapterBot
import com.app.clonemercadolivre.adapter.com.saudeconnectapp.model.CarrosselBot
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.saudeconnectapp.R
import com.saudeconnectapp.databinding.FragmentHomeBinding
import com.saudeconnectapp.model.CarroselTop


@Suppress("UNUSED_EXPRESSION")
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var carroselAdapter: CardAdapter
    private lateinit var carroselBotAdapter: CardAdapterBot

    private val listaCarrosselTop: MutableList<CarroselTop> = mutableListOf()
    private val listaCarrosselBot: MutableList<CarrosselBot> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        val btNavb = binding.bottomNavigation

        setupRecyclerViewCarrosel()
        navigationScreens(btNavb)

        setupRecyclerViewCarroselBot()



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

    private fun setupRecyclerViewCarrosel() {
        val recyclerViewProdutosCarrosel = binding.rvlCarrossel
        recyclerViewProdutosCarrosel.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewProdutosCarrosel.setHasFixedSize(true)

        carroselAdapter = context?.let { CardAdapter(it, listaCarrosselTop) }!!
        recyclerViewProdutosCarrosel.adapter = carroselAdapter
        getCardsCarrosel()
    }

    private fun setupRecyclerViewCarroselBot() {
        val recyclerViewProdutosCarrosel = binding.rvlCarrosselBot

        recyclerViewProdutosCarrosel.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewProdutosCarrosel.setHasFixedSize(true)

        carroselBotAdapter = context?.let { CardAdapterBot(it, listaCarrosselBot) }!!
        recyclerViewProdutosCarrosel.adapter = carroselBotAdapter
        getCardsCarroselBot()
    }


    private fun getCardsCarrosel() {
        val cardOne = CarroselTop(
            "Encontre Médicos para Telemedicina de Forma Rápida e Fácil", R.id.btnGoOne, R.drawable.image_top_info
        )

        listaCarrosselTop.add(cardOne)

        val cardTwo = CarroselTop(
            "Econtre farmácias próximas de você com os melhores preços", R.id.btnGoTwo, R.drawable.farmacia_image
        )
        listaCarrosselTop.add(cardTwo)
    }

    private fun getCardsCarroselBot() {
        val cardOne = CarrosselBot(
            "Exercícios para Aliviar a Ansiedade", R.drawable.img_carrossel_bot_1
        )

        listaCarrosselBot.add(cardOne)

        val cardTwo = CarrosselBot(
            "Check-ups Médicos: Quando e Por Que", R.drawable.img_carrossel_bot_2
        )

        listaCarrosselBot.add(cardTwo)
    }

}