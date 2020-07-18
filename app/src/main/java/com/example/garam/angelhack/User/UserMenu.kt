package com.example.garam.angelhack.User

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.garam.angelhack.MainActivity
import com.example.garam.angelhack.Point.PointQr
import com.example.garam.angelhack.Point.PointUse
import com.example.garam.angelhack.R
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.usermgmt.callback.UnLinkResponseCallback
import kotlinx.android.synthetic.main.activity_user_menu.*


class UserMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_menu)
        val uid = intent.getStringExtra("uid")
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }

        payment.setOnClickListener {
            val qrpay = Intent(this,KakaoPayment::class.java)
            qrpay.putExtra("uid",uid)
            startActivity(qrpay)
        }

        pointUse.setOnClickListener {
            val qrPoint = Intent(this,PointQr::class.java)
            qrPoint.putExtra("uid",uid)
            startActivity(qrPoint)
        }

        logout.setOnClickListener {
            /*UserManagement.getInstance()
                .requestLogout(object : LogoutResponseCallback() {
                    override fun onCompleteLogout() {
                        Log.i("KAKAO_API", "로그아웃 완료")
                        val intent = Intent(this@UserMenu,MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }
                }) */

            UserManagement.getInstance()
                .requestUnlink(object : UnLinkResponseCallback() {
                    override fun onSessionClosed(errorResult: ErrorResult) {
                        Log.e("KAKAO_API", "세션이 닫혀 있음: $errorResult")
                    }

                    override fun onFailure(errorResult: ErrorResult) {
                        Log.e("KAKAO_API", "연결 끊기 실패: $errorResult")
                    }

                    override fun onSuccess(result: Long) {
                        Log.i("KAKAO_API", "연결 끊기 성공. id: $result")
                        val intent = Intent(this@UserMenu,MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }

                    override fun onNotSignedUp() {
                        Log.e("kakao","가입안되있음")
                        super.onNotSignedUp()
                    }
                })
        }
    }
}