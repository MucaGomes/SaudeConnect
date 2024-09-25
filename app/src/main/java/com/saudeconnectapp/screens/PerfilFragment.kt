package com.saudeconnectapp.screens

import CardPerfilAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.saudeconnectapp.MainActivity
import com.saudeconnectapp.R
import com.saudeconnectapp.databinding.FragmentPerfilBinding
import com.saudeconnectapp.model.CarrosselPerfil

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

        setFragmentResultListener("requestKey") { key, bundle ->
            val lastVaccineName = bundle.getString("lastVaccineName")
            updateVaccineInfo()
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
        auth.signOut()

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
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewProdutosCarrosel.setHasFixedSize(true)

        cardAdapterPerfil = context?.let { CardPerfilAdapter(it, listaCarrosselPerfil) }!!
        recyclerViewProdutosCarrosel.adapter = cardAdapterPerfil

        cardAdapterPerfil.setOnItemClickListener { position ->

            when (position) {
                0 -> {
                    // Navega para o fragmento de salvar vacinas
                    val navController = findNavController()
                    navController.navigate(R.id.action_mainFragment_to_saveVaccineFragment)
                }

                 1 -> {
                    // Navega para o fragmento de salvar exames
                     val navController = findNavController()
                    navController.navigate(R.id.action_mainFragment_to_saveExamFragment)
                }

                2 -> {
                    // Navega para o fragmento de salvar medicamentos
                    val navController = findNavController()
                    navController.navigate(R.id.action_mainFragment_to_saveMedicalFragment)
                }

                else -> {}
            }
        }

        getCardsCarroselPerfil()
    }


    private fun getCardsCarroselPerfil() {
        val cardOne = CarrosselPerfil(
            0, "Vacinas Salvas", "• Teste",
            R.drawable.background_fundo_perfil_card_one,
            R.id.btnAddPerfilCard,
            "Últimas Vacinas Salvas: ",
            count = 0 // Inicializa com 0, será atualizado depois
        )

        listaCarrosselPerfil.add(cardOne)

        // Outros cards
        val cardTwo = CarrosselPerfil(
            0, "Exames Salvos",
            "• Vazio",
            R.drawable.img_card_two_perfil,
            R.id.btnAddPerfilCard,
            "Útimos Exames salvos:",
            count = 0 // Inicializa com 0
        )
        listaCarrosselPerfil.add(cardTwo)

        val cardThree = CarrosselPerfil(
            0, "Medicamentos Salvos", "• Vazio",
            R.drawable.img_card_three_perfil,
            R.id.btnAddPerfilCard,
            "Útimos Medicamentos salvos:",
            count = 0 // Inicializa com 0
        )
        listaCarrosselPerfil.add(cardThree)

        // Chama a função para buscar as vacinas salvas
        fetchSavedVaccines(cardOne)
        fetchSavedExams(cardTwo)
        fetchSavedMedical(cardThree)
    }

    private fun fetchSavedVaccines(cardOne: CarrosselPerfil) {
        val vaccinesRef = db.collection("vaccine") // Supondo que suas vacinas estão na coleção "notes"

        vaccinesRef.whereEqualTo("userId", usuarioId).get()
            .addOnSuccessListener { documents ->
                val vaccineCount = documents.size()
                var lastVaccineDate = "Nenhuma vacina salva"

                if (vaccineCount > 0) {
                    // Obtém a última vacina salva
                    val lastVaccine = documents.documents.last()
                    lastVaccineDate = lastVaccine.getString("title") ?: "Data desconhecida"
                }

                // Atualiza o card com a quantidade e a última vacina
                cardOne.count = vaccineCount
                cardOne.item1 = lastVaccineDate

                // Atualiza o RecyclerView
                cardAdapterPerfil.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("PerfilFragment", "Erro ao buscar vacinas: ${e.message}")
            }
    }

    private fun fetchSavedExams(cardTwo: CarrosselPerfil) {
        val vaccinesRef = db.collection("exam") // Supondo que suas vacinas estão na coleção "notes"

        vaccinesRef.whereEqualTo("userId", usuarioId).get()
            .addOnSuccessListener { documents ->
                val examCount = documents.size()
                var lastExamDate = "Nenhuma Exame salva"

                if (examCount > 0) {
                    // Obtém a última vacina salva
                    val lastVaccine = documents.documents.last()
                    lastExamDate = lastVaccine.getString("title") ?: "Data desconhecida"
                }

                // Atualiza o card com a quantidade e a última vacina
                cardTwo.count = examCount
                cardTwo.item1 = lastExamDate

                // Atualiza o RecyclerView
                cardAdapterPerfil.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("PerfilFragment", "Erro ao buscar Exames: ${e.message}")
            }
    }

    private fun fetchSavedMedical(cardThree: CarrosselPerfil) {
        val vaccinesRef = db.collection("medical") // Supondo que suas vacinas estão na coleção "notes"

        vaccinesRef.whereEqualTo("userId", usuarioId).get()
            .addOnSuccessListener { documents ->
                val medicalCount = documents.size()
                var lastMedicalDate = "Nenhum Medicamento salvo"

                if (medicalCount > 0) {
                    // Obtém a última vacina salva
                    val lastVaccine = documents.documents.last()
                    lastMedicalDate = lastVaccine.getString("title") ?: "Data desconhecida"
                }

                // Atualiza o card com a quantidade e a última vacina
                cardThree.count = medicalCount
                cardThree.item1 = lastMedicalDate

                // Atualiza o RecyclerView
                cardAdapterPerfil.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("PerfilFragment", "Erro ao buscar Medicamentos: ${e.message}")
            }
    }

    private fun updateVaccineInfo() {
        val vaccinesRef = db.collection("vaccine") // Coleção de vacinas
        vaccinesRef.whereEqualTo("userId", usuarioId).get()
            .addOnSuccessListener { documents ->
                val vaccineCount = documents.size()
                var lastVaccineName = "Nenhuma vacina salva"

                if (vaccineCount > 0) {
                    // Obtém a última vacina salva
                    val lastVaccine = documents.documents.last()
                    lastVaccineName = lastVaccine.getString("title") ?: "Nome desconhecido"
                }

                // Atualiza o card de vacinas salvas
                listaCarrosselPerfil[0].count = vaccineCount
                listaCarrosselPerfil[0].ultimas = "Última vacina salva: $lastVaccineName"

                // Notifica o adapter sobre a mudança
                cardAdapterPerfil.notifyItemChanged(0)
            }
            .addOnFailureListener { e ->
                Log.e("PerfilFragment", "Erro ao buscar vacinas: ${e.message}")
            }
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
