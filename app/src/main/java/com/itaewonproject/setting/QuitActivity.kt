package com.itaewonproject.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.itaewonproject.R

class QuitActivity : AppCompatActivity() {

    private lateinit var buttonBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quit)
        buttonBack= findViewById<ImageView>(R.id.button_back)
        buttonBack.setOnClickListener { finish() }
    }
}
