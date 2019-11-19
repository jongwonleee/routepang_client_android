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
import com.itaewonproject.model.receiver.Article
import com.itaewonproject.rests.get.GetReviewConnector

class ReviewFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var list: ArrayList<Article>

    private fun setListViewOption(view: View) {
        list = JsonParser().listJsonParsing(GetReviewConnector().get((activity!!.application as Routepang).customer.customerId), Article::class.java)
        recyclerView = view.findViewById(R.id.review_RecyclerView) as RecyclerView
        val adapter = AdapterArticleList(view.context, list)

        adapter.setOnItemClickClickListener(object : AdapterArticleList.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(list[position].link.linkUrl))
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
        return inflater.inflate(R.layout.fragment_review, container, false)
    }
}
