package com.itaewonproject.message

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.cloud.Timestamp
import com.itaewonproject.R
import com.itaewonproject.Routepang
import com.itaewonproject.adapter.AdapterFollowList
import com.itaewonproject.adapter.AdapterMessengerList
import com.itaewonproject.model.receiver.Message
import com.itaewonproject.model.receiver.Messenger
import com.itaewonproject.model.sender.Customer
import kotlinx.android.synthetic.main.activity_follow.view.*

class FollowActivity : AppCompatActivity() {

    private lateinit var recyclerview:RecyclerView
    private lateinit var adapter:AdapterFollowList


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow)

        recyclerview = findViewById(R.id.recyclerView)


        val customer = Customer()
        customer.reference="멋쟁이 사용자"
        customer.customerId=1
        adapter = AdapterFollowList(this, arrayListOf(customer,customer,customer,customer,customer,customer,customer,customer,customer,customer,customer))
        recyclerview.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerview.layoutManager = linearLayoutManager
        recyclerview.setHasFixedSize(true)
        adapter.setOnItemClickClickListener(object : AdapterFollowList.OnItemClickListener {
            override fun onItemClick(opposite: Customer) {
                val intent = Intent()
                intent.putExtra("customer",opposite)
                setResult(RESULT_OK,intent)
                finish()
            }
        })

    }
}