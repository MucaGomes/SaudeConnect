package com.saudeconnectapp.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.saudeconnectapp.R
import com.saudeconnectapp.databinding.FragmentHomeBinding

@Suppress("UNUSED_EXPRESSION")
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)


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
                .replace(R.id.fragment_container, selectedFragment)
                .commit()

            true
        }

        return binding.root

    }

}