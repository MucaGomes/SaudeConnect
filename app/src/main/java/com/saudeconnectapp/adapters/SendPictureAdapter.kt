package com.saudeconnectapp.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.saudeconnectapp.screens_send_picture.FirstSendPicturePerfilFragment
import com.saudeconnectapp.screens_send_picture.SecondSendPictureSusFragment
import com.saudeconnectapp.screens_send_picture.ThirdSendPictureVaccinationCard

class SendPictureAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val fragments = listOf(
        FirstSendPicturePerfilFragment(),
        SecondSendPictureSusFragment(),
                ThirdSendPictureVaccinationCard()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun getFragment(position: Int): Fragment = fragments[position]
}