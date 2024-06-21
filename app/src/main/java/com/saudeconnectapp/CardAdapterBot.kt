package com.app.clonemercadolivre.adapter.com.saudeconnectapp

import android.content.Context
import android.graphics.Matrix
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.clonemercadolivre.adapter.com.saudeconnectapp.model.CarrosselBot
import com.saudeconnectapp.databinding.ItemCardCarroselBotOneBinding
import com.saudeconnectapp.databinding.ItemCardCarrosselTopOneBinding

class CardAdapterBot(
    private val context: Context, val listaCarrosselBot: MutableList<CarrosselBot>
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

    }

    override fun getItemCount() = listaCarrosselBot.size

    inner class ProdutoCarroselViewHolder(binding: ItemCardCarroselBotOneBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.txtCardBotOne
        val img = binding.imageView3
    }
}