package com.itaewonproject.message

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.itaewonproject.R

class MessageListActivity : AppCompatActivity() {

    private lateinit var buttonBack: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var buttonNewMessage: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)
        buttonBack=findViewById(R.id.button_back)
        recyclerView = findViewById(R.id.recyclerView)
        buttonNewMessage = findViewById(R.id.button_new_message)
        buttonNewMessage.setOnClickListener({
            val intent = Intent(this,messagingActivity::class.java)
            startActivity(intent)
        })
        buttonBack.setOnClickListener({ finish() })
    }
}
