package com.saudeconnectapp.screens


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.saudeconnectapp.adapters.CardAdapter
import com.saudeconnectapp.adapters.CardAdapterBot
import com.app.clonemercadolivre.adapter.com.saudeconnectapp.model.CarrosselBot
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.storage.FirebaseStorage
import com.saudeconnectapp.NotificationBottomSheetDialogFragment
import com.saudeconnectapp.R
import com.saudeconnectapp.WebViewFragment
import com.saudeconnectapp.databinding.FragmentHomeBinding
import com.saudeconnectapp.model.CarrosselTop
import jp.wasabeef.blurry.Blurry


@Suppress("UNUSED_EXPRESSION")
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var carroselAdapter: CardAdapter
    private lateinit var carroselBotAdapter: CardAdapterBot

    private val listaCarrosselTop: MutableList<CarrosselTop> = mutableListOf()
    private val listaCarrosselBot: MutableList<CarrosselBot> = mutableListOf()

    private val usuarioId = FirebaseAuth.getInstance().currentUser!!.uid
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        val btNavb = binding.bottomNavigation

        setupRecyclerViewCarrosel()
        navigationScreens(btNavb)
        setupRecyclerViewCarroselBot()


        val notificationIcon = binding.notificationIcon


        notificationIcon.setOnClickListener {
            Blurry.with(context).radius(10) // Define a intensidade do desfoque
                .sampling(2) // Ajuste para performance e qualidade
                .async() // Executa a operação de desfoque em uma thread separada
                .onto(binding.backgroundView)

            val bottomSheet = NotificationBottomSheetDialogFragment()
            bottomSheet.show(parentFragmentManager, "NotificationBottomSheet")
        }


        return binding.root

    }

    override fun onResume() {
        super.onResume()
        loadUserAvatarPefil() // Ensure the photo is updated when the fragment resumes
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

        carroselBotAdapter = context?.let {
            CardAdapterBot(it, listaCarrosselBot) { url ->
                // Navegar para o WebViewFragment ao clicar no item
                val fragment = WebViewFragment()
                val bundle = Bundle()
                bundle.putString("url", url)
                fragment.arguments = bundle

                parentFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                    .addToBackStack(null).commit()
            }
        }!!

        recyclerViewProdutosCarrosel.adapter = carroselBotAdapter
        getCardsCarroselBot()
    }


    private fun getCardsCarrosel() {
        if (listaCarrosselTop.isEmpty()) {
            val cardOne = CarrosselTop(
                "Encontre Médicos para Telemedicina de Forma Rápida e Fácil",
                R.id.btnGoOne,
                R.drawable.image_top_info
            )
            listaCarrosselTop.add(cardOne)

            val cardTwo = CarrosselTop(
                "Econtre farmácias próximas de você com os melhores preços",
                R.id.btnGoTwo,
                R.drawable.farmacia_image
            )
            listaCarrosselTop.add(cardTwo)
            carroselAdapter.notifyDataSetChanged()
        }
    }

    private fun getCardsCarroselBot() {
        if (listaCarrosselBot.isEmpty()) {
            val cardOne = CarrosselBot(
                "MPOX:Saiba quais são os sintomas", R.drawable.img_card3_bot
            )
            listaCarrosselBot.add(cardOne)

            val cardTwo = CarrosselBot(
                "Check-ups Médicos: Quando e Por Que", R.drawable.img_carrossel_bot_2
            )
            listaCarrosselBot.add(cardTwo)

            val cardThree = CarrosselBot(
                "Exercícios para Aliviar a Ansiedade", R.drawable.img_carrossel_bot_1
            )
            listaCarrosselBot.add(cardThree)
            carroselBotAdapter.notifyDataSetChanged()
        }
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
}
