package com.example.garam.angelhack.User

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.garam.angelhack.R
import kotlinx.android.synthetic.main.activity_kakao_web.*

class KakaoWeb : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kakao_web)
        val intent = intent
        val url = intent.getStringExtra("url")
        Log.e("주소", url)
        val kakaoweb = findViewById<WebView>(R.id.webview)
        kakaoweb.webViewClient = WebViewClient()
        val mWebset = kakaoweb.settings
        mWebset.javaScriptEnabled =true
        mWebset.javaScriptCanOpenWindowsAutomatically = false
        mWebset.loadWithOverviewMode = true
        mWebset.cacheMode = WebSettings.LOAD_NO_CACHE
        kakaoweb.loadUrl(url)

    }
}