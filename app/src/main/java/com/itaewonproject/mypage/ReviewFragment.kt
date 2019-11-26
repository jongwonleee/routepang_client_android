package com.itaewonproject.mypage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.JsonParser
import com.itaewonproject.R
import com.itaewonproject.Routepang
import com.itaewonproject.adapter.AdapterArticleList
import com.itaewonproject.linkshare.LinkShareActivity
import com.itaewonproject.mainservice.UserInfoActivity
import com.itaewonproject.model.receiver.Article
import com.itaewonproject.model.sender.Customer
import com.itaewonproject.rests.get.GetReviewConnector

class ReviewFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var list: ArrayList<Article>
    lateinit var  customer:Customer
    lateinit var adapter:AdapterArticleList
    override fun onResume() {
        super.onResume()
        list = JsonParser().listJsonParsing(GetReviewConnector().get(customer.customerId), Article::class.java)
        adapter.notifyDataSetChanged()

    }
    private fun setListViewOption(view: View) {
        list = JsonParser().listJsonParsing(GetReviewConnector().get(customer.customerId), Article::class.java)
        recyclerView = view.findViewById(R.id.review_RecyclerView) as RecyclerView
        adapter = AdapterArticleList(view.context, list)

        adapter.setOnItemClickClickListener(object : AdapterArticleList.OnItemClickListener {
            override fun onReferenceClick(position: Int) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(list[position].link.linkUrl))
                startActivity(intent)            }

            override fun onProfileClick(customer: Customer) {
                val intent = Intent(context, UserInfoActivity::class.java)
                intent.putExtra("customer",customer)
                startActivity(intent)
            }
        })
        recyclerView.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val button = view.findViewById(R.id.button_link_share) as CardView
        button.setOnClickListener {
            val intent = Intent(context, LinkShareActivity::class.java)
            startActivity(intent)
        }
        setListViewOption(view)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.customer = arguments!!.getSerializable("customer") as Customer

        return inflater.inflate(R.layout.fragment_review, container, false)
    }
}
