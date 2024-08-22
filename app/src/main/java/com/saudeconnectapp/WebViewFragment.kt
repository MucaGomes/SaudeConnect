package com.saudeconnectapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.saudeconnectapp.databinding.FragmentWebViewBinding


class WebViewFragment : Fragment() {

    private lateinit var webView: WebView

    private lateinit var binding: FragmentWebViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWebViewBinding.inflate(layoutInflater, container, false)

        webView = binding.webView

        webView.webViewClient = WebViewClient()

        webView.settings.javaScriptEnabled = true

        val url = arguments?.getString("url")

        url?.let {
            webView.loadUrl(it)
        }


        return binding.root
    }


}