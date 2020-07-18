package com.example.garam.angelhack.Manager

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.core.content.ContextCompat
import com.example.garam.angelhack.R
import com.example.garam.angelhack.network.NetworkService
import com.google.gson.JsonObject
import com.google.gson.JsonParser

import com.google.zxing.MultiFormatWriter
import kotlinx.android.synthetic.main.activity_manage_menu.*
import kotlinx.android.synthetic.main.activity_store_info.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
//import com.journeyapps.barcodescanner.BarcodeEncoder
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

class ManageMenu : AppCompatActivity() {

    private fun saveBitmapToJpeg(bitmap: Bitmap, name: String){
        val storage = cacheDir
        val fileName = "$name"
        val tempFile = File(storage,fileName)
        try{
            tempFile.createNewFile()
            val out = FileOutputStream(tempFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out)
            out.close()
        }
        catch (e: FileNotFoundException){
            Log.e("FileNot", "FileNotFoundException$e")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        val baseURL = "https://a961f35ba588.ngrok.io"
        val retrofit2: Retrofit = Retrofit.Builder().baseUrl(baseURL).addConverterFactory(
            GsonConverterFactory.create()).client(
            OkHttpClient.Builder().connectTimeout(1,
                TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES).writeTimeout(1, TimeUnit.MINUTES).addInterceptor(
                HttpLoggingInterceptor()
            ).build()).build()
        val networkService = retrofit2.create(NetworkService::class.java)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_menu)
        val intent = intent
        val uid = intent.getStringExtra("hid")

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }
        button3.setOnClickListener {

            val obj = JSONObject()
            obj.put("uid",uid)
            obj.put("storename",hostName.text.toString())
            obj.put("introduceText",hostComment.text.toString())

            val json = obj.toString()
            Log.e("큐알",json)
            val qrgEncoder = QRGEncoder(json,QRGContents.Type.TEXT,50*50)
            qrgEncoder.colorBlack = Color.BLACK
            qrgEncoder.colorWhite = Color.WHITE
            val bitmap = qrgEncoder.bitmap
            Log.e("비트맵","$bitmap")
            imageView5.setImageBitmap(bitmap)
            saveBitmapToJpeg(bitmap,"$uid.jpg")
            val path = cacheDir.path + "/" + "$uid.jpg"
            val testBit = BitmapFactory.decodeFile(path)
            val byteArrayOutputStream = ByteArrayOutputStream()
            testBit.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)

            val qrbody = RequestBody.create("image/jpg".toMediaTypeOrNull(),
                byteArrayOutputStream.toByteArray())
            val mImage : MultipartBody.Part = MultipartBody.Part.createFormData("qr","$uid.jpg",qrbody)
            val infoBody = RequestBody.create("text/plain".toMediaTypeOrNull(),json)
            val postQr : Call<String> = networkService.qrSend(infoBody,mImage)
            postQr.enqueue(object : Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.e("성공","${response.body()}")
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                }
            })
            val intent = Intent(this@ManageMenu,HelloManager::class.java)
            intent.putExtra("hid",uid)
            startActivity(intent)
            finish()
        }
    }
}