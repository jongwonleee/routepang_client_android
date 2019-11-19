package com.itaewonproject.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.cloud.Timestamp
import com.itaewonproject.R
import com.itaewonproject.Routepang
import com.itaewonproject.adapter.AdapterMessagingList
import com.itaewonproject.model.receiver.Message
import com.itaewonproject.model.sender.Customer

class MessagingActivity : AppCompatActivity() {

    private lateinit var buttonBack: ImageView
    private lateinit var editMessage: EditText
    private lateinit var buttonSend: ImageView
    private lateinit var recyclerview:RecyclerView
    private lateinit var adapter:AdapterMessagingList
    private lateinit var title:TextView

    private lateinit var customer:Customer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messaging)

        customer = intent.getSerializableExtra("customer") as Customer

        editMessage =findViewById(R.id.edit_message)
        buttonSend=findViewById(R.id.button_send)
        recyclerview = findViewById(R.id.recyclerView)
        buttonBack=findViewById(R.id.button_back)
        title = findViewById(R.id.title)
        buttonBack.setOnClickListener { finish() }

        title.text=customer.reference

        ///TODO 차후에 지워주기
        val m1 = Message()
        m1.regDate= Timestamp.now().seconds
        m1.customer = (application as Routepang).customer
        m1.isMe = true
        m1.text = "text11"
        val m2 = Message()
        m2.regDate= Timestamp.now().seconds
        m2.customer = (application as Routepang).customer
        m2.isMe = false
        m2.text = "hi baby"



        adapter = AdapterMessagingList(this, arrayListOf(m2,m1,m2,m2,m2,m1,m2,m2,m1))
        recyclerview.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerview.layoutManager = linearLayoutManager
        recyclerview.setHasFixedSize(true)


    }
}
