package com.itaewonproject.message

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.JsonParser
import com.itaewonproject.R
import com.itaewonproject.Routepang
import com.itaewonproject.adapter.AdapterFollowList
import com.itaewonproject.model.sender.Customer
import com.itaewonproject.rests.get.GetFollowerConnector
import com.itaewonproject.rests.get.GetFollowingConnector

class FollowActivity : AppCompatActivity() {

    private lateinit var recyclerview:RecyclerView
    private lateinit var adapter:AdapterFollowList
    private var list = arrayListOf<Customer>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow)

        recyclerview = findViewById(R.id.recyclerView)


        list = JsonParser().listJsonParsing(GetFollowingConnector().get((application as Routepang).customer.customerId),Customer::class.java)
        adapter = AdapterFollowList(this,list)
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