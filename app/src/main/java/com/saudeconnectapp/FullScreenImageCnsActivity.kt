package com.saudeconnectapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.saudeconnectapp.databinding.ActivityFullScreenImageCnsBinding

class FullScreenImageCnsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFullScreenImageCnsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenImageCnsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtém a URL da imagem passada pela Intent
        val imageUrl = intent.getStringExtra("imageUrl")

        // Carrega a imagem usando o Glide
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this).load(imageUrl).into(binding.fullscreenImageView)
        }
        // Fecha a Activity ao clicar na imagem
        binding.fullscreenImageView.setOnClickListener {
            finish()
        }
    }
}