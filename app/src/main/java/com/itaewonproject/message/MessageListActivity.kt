package com.itaewonproject.message

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.itaewonproject.JsonParser
import com.itaewonproject.R
import com.itaewonproject.adapter.AdapterMessengerList
import com.itaewonproject.model.receiver.Messenger
import com.itaewonproject.model.sender.Customer
import com.itaewonproject.rests.WebResponce

class MessageListActivity : AppCompatActivity() {

    private val REQUEST_CODE=9999

    private lateinit var buttonBack: ImageView
    private lateinit var recyclerview: RecyclerView
    private lateinit var buttonNewMessage: FloatingActionButton
    private lateinit var adapter :AdapterMessengerList
    private lateinit var sharedPreferences:SharedPreferences
    private var list = arrayListOf<Customer>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)


        sharedPreferences = getSharedPreferences("messageList", Context.MODE_PRIVATE)
        list = JsonParser().listJsonParsing(WebResponce(sharedPreferences.getString("list","")!!,200),Customer::class.java)

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

    override fun onDestroy() {
        Log.i("!!","!@!@")
        val editor = sharedPreferences.edit()
        editor.putString("list", Gson().toJson(list))
        editor.apply()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val customer = data!!.getSerializableExtra("customer") as Customer
            var contained=false
            for(l in list){
                if(l.equals(customer)){
                    contained=true
                }
            }
            if(!contained){
                list.add(customer)
            }
            val intent = Intent(this@MessageListActivity,MessagingActivity::class.java)
            intent.putExtra("customer",customer)
            startActivity(intent)
            adapter.notifyDataSetChanged()
        }
    }
}
