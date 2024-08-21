package com.saudeconnectapp.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.saudeconnectapp.databinding.CardProfisionaisGendamentoBinding
import com.saudeconnectapp.databinding.ItemCardCarrosselTopOneBinding
import com.saudeconnectapp.model.PerfilProfessional

class ProfessionalAdaptaer(
    private val context: Context,
    val listaPerfilProfessional: List<PerfilProfessional>
) : RecyclerView.Adapter<ProfessionalAdaptaer.CardPerfilViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardPerfilViewHolder {
        val itemLista =
            CardProfisionaisGendamentoBinding.inflate(LayoutInflater.from(context), parent, false)
        return CardPerfilViewHolder(itemLista)
    }

    override fun onBindViewHolder(holder: CardPerfilViewHolder, position: Int) {
        holder.img.setImageResource(listaPerfilProfessional[position].img!!)
        holder.name.text = listaPerfilProfessional[position].name.toString()
        holder.specialty.text = listaPerfilProfessional[position].specialty.toString()
        holder.btn.setOnClickListener {
            Toast.makeText(context, "Teste: Enviando...", Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount() = listaPerfilProfessional.size

    inner class CardPerfilViewHolder(binding: CardProfisionaisGendamentoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val img = binding.imgPerfilProfessional
        val name = binding.txtNamePerfilProfessional
        val specialty = binding.txtSpecialtyProfessional
        val btn = binding.btnSchedule
    }


}