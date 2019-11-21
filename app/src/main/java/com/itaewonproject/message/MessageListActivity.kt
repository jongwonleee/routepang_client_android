package com.itaewonproject.message

import android.app.Activity
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

    private val REQUEST_CODE=9999

    private lateinit var buttonBack: ImageView
    private lateinit var recyclerview: RecyclerView
    private lateinit var buttonNewMessage: FloatingActionButton
    private lateinit var adapter :AdapterMessengerList
    private val list = arrayListOf<Messenger>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)
        buttonBack=findViewById(R.id.button_back)
        recyclerview = findViewById(R.id.recyclerView)
        buttonNewMessage = findViewById(R.id.button_new_message)
        buttonNewMessage.setOnClickListener {
            val intent = Intent(this,FollowActivity::class.java)
            startActivityForResult(intent,REQUEST_CODE)
        }
        buttonBack.setOnClickListener { finish() }

        adapter = AdapterMessengerList(this, list)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val customer = data!!.getSerializableExtra("customer") as Customer
            var messenger = Messenger()
            var contained=false
            for(l in list){
                if(l.customer.equals(customer)){
                    messenger = l
                    contained=true
                }
            }
            if(!contained){
                messenger.customer=customer
                list.add(messenger)
            }
            val intent = Intent(this@MessageListActivity,MessagingActivity::class.java)
            intent.putExtra("customer",customer)
            startActivity(intent)
            adapter.notifyDataSetChanged()
        }
    }
}
