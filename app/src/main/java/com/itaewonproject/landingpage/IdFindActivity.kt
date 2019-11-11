package com.itaewonproject.landingpage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.itaewonproject.R

class IdFindActivity : AppCompatActivity() {

    private lateinit var buttonBack:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_id_find)

        buttonBack = findViewById(R.id.button_back) as ImageView
        buttonBack.setOnClickListener({ finish()})
    }
}
