package com.saudeconnectapp.screens_send_picture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.saudeconnectapp.R
import com.saudeconnectapp.databinding.FragmentFirstSendPicturePerfilBinding

class FirstSendPicturePerfilFragment : Fragment() {

    private lateinit var binding : FragmentFirstSendPicturePerfilBinding
    private var uriFotoPerfil: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFirstSendPicturePerfilBinding.inflate(layoutInflater, container, false)

        // acão de abrir a galeria
        binding.btnSendImage.setOnClickListener{
            openGallery()
        }

        // navegação entre as pages
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.txtSkip.setOnClickListener{
            viewPager?.currentItem = 1
        }


        return binding.root
    }

    private fun openGallery() {
        val itent = Intent(Intent.ACTION_PICK)
        itent.type = "image/"

        startActivityForResult(itent,1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == 1) {
            uriFotoPerfil = data?.data
            binding.imgAvatar.setImageURI(uriFotoPerfil)
        }
    }

    fun getFotoPerfil(): Uri? {
        return uriFotoPerfil
    }
}