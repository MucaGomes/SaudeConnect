package com.saudeconnectapp.button_dialog_screens

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class MessageDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Tela em produção")
            .setMessage("Está tela está em desenvolvimento, para entregar para vocês uma melhor usabilidade do nosso app, peço que tenham calma até desenvolvermos completamente, por gentileza teste nossas funcionalidades de salvar arquivos na tela de perfil.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }
}