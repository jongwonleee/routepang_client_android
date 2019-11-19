package com.itaewonproject.message

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.cloud.Timestamp
import com.itaewonproject.R
import com.itaewonproject.Routepang
import com.itaewonproject.adapter.AdapterMessengerList
import com.itaewonproject.model.receiver.Message
import com.itaewonproject.model.receiver.Messenger
import com.itaewonproject.model.sender.Customer

class MessageListActivity : AppCompatActivity() {

    private lateinit var buttonBack: ImageView
    private lateinit var recyclerview: RecyclerView
    private lateinit var buttonNewMessage: FloatingActionButton
    private lateinit var adapter :AdapterMessengerList
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)
        buttonBack=findViewById(R.id.button_back)
        recyclerview = findViewById(R.id.recyclerView)
        buttonNewMessage = findViewById(R.id.button_new_message)
        buttonNewMessage.setOnClickListener {
            val intent = Intent(this,MessagingActivity::class.java)
            startActivity(intent)
        }
        buttonBack.setOnClickListener { finish() }

        val m2 = Message()
        m2.regDate= Timestamp.now().seconds
        m2.customer = (application as Routepang).customer
        m2.isMe = false
        m2.text = "hi baby"
        val m = Messenger()
        m.customer = (application as Routepang).customer
        m.customer.reference="김덕춘삼"
        m.lastMessage = m2

        adapter = AdapterMessengerList(this, arrayListOf(m,m,m,m,m,m,m,m,m))
        recyclerview.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerview.layoutManager = linearLayoutManager
        recyclerview.setHasFixedSize(true)
        adapter.setOnItemClickClickListener(object : AdapterMessengerList.OnItemClickListener {
            override fun onItemClick(v: View, opposite: Customer) {
                val intent = Intent(this@MessageListActivity,MessagingActivity::class.java)
                intent.putExtra("customer",opposite)
                startActivity(intent)
            }
        })
    }
}
