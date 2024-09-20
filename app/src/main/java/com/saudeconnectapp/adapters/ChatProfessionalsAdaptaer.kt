package com.saudeconnectapp.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.saudeconnectapp.databinding.CardChatProffessionalsBinding
import com.saudeconnectapp.databinding.CardProfisionaisGendamentoBinding
import com.saudeconnectapp.databinding.ItemCardCarrosselTopOneBinding
import com.saudeconnectapp.model.ChatProfessional
import com.saudeconnectapp.model.PerfilProfessional

class ChatProfessionalsAdaptaer(
    private val context: Context,
    val listaPerfilProfessional: List<ChatProfessional>
) : RecyclerView.Adapter<ChatProfessionalsAdaptaer.CardPerfilViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardPerfilViewHolder {
        val itemLista =
            CardChatProffessionalsBinding.inflate(LayoutInflater.from(context), parent, false)
        return CardPerfilViewHolder(itemLista)
    }

    override fun onBindViewHolder(holder: CardPerfilViewHolder, position: Int) {
        holder.img.setImageResource(listaPerfilProfessional[position].img!!)
        holder.name.text = listaPerfilProfessional[position].name.toString()
        holder.desc.text = listaPerfilProfessional[position].desc.toString()
        holder.sendAgo.text = listaPerfilProfessional[position].sendAgo.toString()
        holder.btn.setOnClickListener {
            Toast.makeText(context, "Teste: Enviando...", Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount() = listaPerfilProfessional.size

    inner class CardPerfilViewHolder(binding: CardChatProffessionalsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val img = binding.imgPerfilProfessional
        val name = binding.txtNamePerfilProfessional
        val desc = binding.txtDescProf
        val btn = binding.btnQntMessage
        val sendAgo = binding.txtSendAgo
    }


}