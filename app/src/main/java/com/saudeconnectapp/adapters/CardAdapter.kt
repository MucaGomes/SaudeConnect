package com.saudeconnectapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.saudeconnectapp.databinding.ItemCardCarrosselTopOneBinding
import com.saudeconnectapp.model.CarrosselTop




class CardAdapter(
    private val context: Context, val listaCarrosselTop: MutableList<CarrosselTop>
) : RecyclerView.Adapter<CardAdapter.ProdutoCarroselViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoCarroselViewHolder {
        val itemLista =
            ItemCardCarrosselTopOneBinding.inflate(LayoutInflater.from(context), parent, false)
        return ProdutoCarroselViewHolder(itemLista)
    }

    override fun onBindViewHolder(holder: ProdutoCarroselViewHolder, position: Int) {
        holder.title.text = listaCarrosselTop[position].title.toString()
        holder.img.setImageResource(listaCarrosselTop[position].img!!)
        holder.btn.setOnClickListener {
            Toast.makeText(context,"Em desenvolvimento...", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = listaCarrosselTop.size

    inner class ProdutoCarroselViewHolder(binding: ItemCardCarrosselTopOneBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.txtCardOne
        val img = binding.imgCardOne
        val btn = binding.btnGoOne
    }
}