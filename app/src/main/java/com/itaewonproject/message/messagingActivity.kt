package com.itaewonproject.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.itaewonproject.R

class messagingActivity : AppCompatActivity() {

    private lateinit var buttonBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messaging)


        buttonBack=findViewById(R.id.button_back) as ImageView
        buttonBack.setOnClickListener({ finish() })
    }
}
