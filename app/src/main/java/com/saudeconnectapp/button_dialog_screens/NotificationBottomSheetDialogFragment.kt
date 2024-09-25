package com.saudeconnectapp.button_dialog_screens

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.saudeconnectapp.R
import com.saudeconnectapp.databinding.ScreenNotificationListBinding
import jp.wasabeef.blurry.Blurry


class NotificationBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: ScreenNotificationListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ScreenNotificationListBinding.inflate(layoutInflater, container, false)


        return binding.root

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        // Remova o desfoque ao fechar o BottomSheet
        Blurry.delete(requireActivity().findViewById(R.id.background_view))
    }
}
