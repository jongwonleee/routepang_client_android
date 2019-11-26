package com.itaewonproject.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.cloud.Timestamp
import com.itaewonproject.JsonParser
import com.itaewonproject.R
import com.itaewonproject.Routepang
import com.itaewonproject.adapter.AdapterMessagingList
import com.itaewonproject.model.receiver.ChatMessage
import com.itaewonproject.model.receiver.Route
import com.itaewonproject.model.sender.Customer
import com.itaewonproject.rests.get.GetChatConnector
import com.itaewonproject.rests.post.PostChatConnector

class MessagingActivity : AppCompatActivity() {

    private lateinit var buttonBack: ImageView
    private lateinit var editMessage: EditText
    private lateinit var buttonSend: ImageView
    private lateinit var recyclerview:RecyclerView
    private lateinit var adapter:AdapterMessagingList
    private lateinit var title:TextView

    private lateinit var customer:Customer
    private var list = arrayListOf<ChatMessage>()

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

        list = JsonParser().listJsonParsing(GetChatConnector().get((application as Routepang).customer.customerId,customer.customerId),ChatMessage::class.java)

        adapter = AdapterMessagingList(this, list,(application as Routepang).customer.customerId)
        recyclerview.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        linearLayoutManager.reverseLayout=true
        linearLayoutManager.stackFromEnd=true
        recyclerview.layoutManager = linearLayoutManager
        recyclerview.setHasFixedSize(true)

        buttonSend.setOnClickListener({
            if(!editMessage.text.trim().isEmpty())
            {
                val chat =ChatMessage()
                chat.senderId=(application as Routepang).customer.customerId
                chat.receiverId = customer.customerId
                chat.content=editMessage.text.trim().toString()
                val ret = PostChatConnector().post(chat)
                if(ret.responceCode==200){
                    editMessage.text = Editable.Factory.getInstance().newEditable("")
                    list = JsonParser().listJsonParsing(GetChatConnector().get((application as Routepang).customer.customerId,customer.customerId),ChatMessage::class.java)
                    adapter.refreshData(list)
                }
            }else
            {
                Toast.makeText(this,"메시지를 압력해주세요.",Toast.LENGTH_SHORT).show()
            }
        })


    }
}
