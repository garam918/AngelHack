package com.example.garam.angelhack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kakao.auth.Session.getCurrentSession


class MainActivity : AppCompatActivity() {

    private var callback: SessionCallback = SessionCallback()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getCurrentSession().addCallback(callback)
    }

    override fun onDestroy() {
        super.onDestroy()
        getCurrentSession().removeCallback(callback);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.e("Log", "session get current session")
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}