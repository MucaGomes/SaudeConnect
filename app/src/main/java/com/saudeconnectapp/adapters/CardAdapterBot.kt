package com.saudeconnectapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.clonemercadolivre.adapter.com.saudeconnectapp.model.CarrosselBot
import com.saudeconnectapp.databinding.ItemCardCarroselBotOneBinding

class CardAdapterBot(
    private val context: Context,
    val listaCarrosselBot: MutableList<CarrosselBot>,
    private val onItemClick: (String) -> Unit // Função para tratar cliques
) : RecyclerView.Adapter<CardAdapterBot.ProdutoCarroselViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoCarroselViewHolder {
        val itemLista =
            ItemCardCarroselBotOneBinding.inflate(LayoutInflater.from(context), parent, false)
        return ProdutoCarroselViewHolder(itemLista)
    }

    override fun onBindViewHolder(holder: ProdutoCarroselViewHolder, position: Int) {
        holder.title.text = listaCarrosselBot[position].title.toString()
        holder.img.setImageResource(listaCarrosselBot[position].img!!)

        if (position == 1) {
            // Configurar o scaleType como FIT_CENTER
            holder.img.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            // Para outros cards, definir o scaleType como CENTER_CROP
            holder.img.scaleType = ImageView.ScaleType.CENTER_CROP
        }

        holder.itemView.setOnClickListener{
            when (position){
                0->
                    onItemClick("https://marjan.com.br/blog/5-exercicios-para-controlar-a-ansiedade/")
                1->
                    onItemClick("https://blog.sabin.com.br/autocuidado/exames-indicados-para-o-seu-check-up/")
            }
        }

    }

    override fun getItemCount() = listaCarrosselBot.size

    inner class ProdutoCarroselViewHolder(binding: ItemCardCarroselBotOneBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.txtCardBotOne
        val img = binding.imageView3


    }
}