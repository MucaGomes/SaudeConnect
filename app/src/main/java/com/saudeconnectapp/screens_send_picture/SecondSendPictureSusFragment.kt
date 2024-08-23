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
import com.saudeconnectapp.databinding.FragmentSecondSendPictureSusBinding

class SecondSendPictureSusFragment : Fragment() {

    private lateinit var binding: FragmentSecondSendPictureSusBinding
    private var uriFotoSus: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSecondSendPictureSusBinding.inflate(layoutInflater, container, false)

        binding.btnSendImage.setOnClickListener {
            openGallery()
        }

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        binding.txtSkip.setOnClickListener{
            viewPager?.currentItem = 2
        }

        return binding.root
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == 2) {
            uriFotoSus = data?.data
            // Atualizar a imagem na interface do usuário
            binding.imgAvatar.setImageURI(uriFotoSus)
        }
    }

    fun getFotoSusUri(): Uri? {
        return uriFotoSus
    }


}