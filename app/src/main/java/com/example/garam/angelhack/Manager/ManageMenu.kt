package com.example.garam.angelhack.Manager

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import com.example.garam.angelhack.R
import com.google.gson.JsonObject
import com.google.gson.JsonParser

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import kotlinx.android.synthetic.main.activity_manage_menu.*
//import com.journeyapps.barcodescanner.BarcodeEncoder
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.*

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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_menu)
        val multiFormatWriter = MultiFormatWriter()
/*
        val obj = JSONObject()
        obj.put("uid",hostName.text.toString())
        obj.put("storename",hostAddress.text.toString())
        obj.put("introduceText",hostComment.text.toString())

        val json = obj.toString()
        val gsonObject = JsonParser().parse(json) as JsonObject
        val hints = Hashtable<EncodeHintType,String>()
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
        val bitMatrix = multiFormatWriter.encode(json,BarcodeFormat.QR_CODE,150,150,hints)
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.createBitmap(bitMatrix)
        saveBitmapToJpeg(bitmap,"")
        val path = cacheDir.path + "/" + ""
        val testBit = BitmapFactory.decodeFile(path)
        val byteArrayOutputStream = ByteArrayOutputStream()
        testBit.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
*/
        button3.setOnClickListener {

            val obj = JSONObject()
            obj.put("uid",hostName.text.toString())
            obj.put("storename",hostAddress.text.toString())
            obj.put("introduceText",hostComment.text.toString())

            val json = obj.toString()
            Log.e("큐알",json)

            val qrgEncoder = QRGEncoder(json,QRGContents.Type.TEXT,50*50)
            qrgEncoder.colorBlack = Color.BLACK
            qrgEncoder.colorWhite = Color.WHITE
            val bitmap = qrgEncoder.bitmap
            Log.e("비트맵","$bitmap")
            imageView5.setImageBitmap(bitmap)

        }



    }
}