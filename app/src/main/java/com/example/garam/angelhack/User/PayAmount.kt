package com.example.garam.angelhack.User

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doBeforeTextChanged
import com.example.garam.angelhack.R
import kotlinx.android.synthetic.main.activity_pay_amount.*

class PayAmount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_amount)

        val payedit = findViewById<EditText>(R.id.payEdit)
        val paybutton = findViewById<Button>(R.id.payButton)
        paybutton.isEnabled = false
        payedit.doBeforeTextChanged { text, start, count, after ->
              paybutton.isEnabled = true
        }
        paybutton.setOnClickListener {
              val data = intent
              data.putExtra("amount","${payedit.text}")
              setResult(100,data)
              finish()
        }

    }
}