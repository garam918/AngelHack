package com.example.garam.angelhack.User

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.garam.angelhack.R
import com.example.garam.angelhack.network.NetworkService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_my_page.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MyPage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)
        val intent = intent
        val money = intent.getStringExtra("money")
        val name = intent.getStringExtra("name")
        val storename = intent.getStringExtra("storename")
        userNameInfo.text = name

        myPointInfo.setOnClickListener {
            val intent = Intent(this,PointList::class.java)
            intent.putExtra("name",name)
            intent.putExtra("storename",storename)
            intent.putExtra("money",money)
            startActivity(intent)
        }
    }
}